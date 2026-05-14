# dss-pades-signer

REST API para firma digital de documentos PDF en formato **PAdES** (PDF Advanced Electronic Signatures) utilizando la librería [EU DSS](https://ec.europa.eu/digital-building-blocks/DSS/webapp-demo/home) y certificados PKCS#12 (.p12 / .pfx).

---

## Requisitos

- Java 17+
- Maven 3.8+
- Certificado digital en formato PKCS#12 (.p12 o .pfx)

## Arrancar el servicio

```bash
mvn spring-boot:run
```

El servicio escucha en `http://localhost:8080`.

## Docker

### Construir la imagen localmente

```bash
docker build -t dss-pades-signer:local .
```

### Ejecutar la imagen localmente

```bash
docker run --rm -p 8080:8080 dss-pades-signer:local
```

Si necesitas acceder a certificados PKCS#12 desde el contenedor, monta un directorio de solo lectura y usa esa ruta en `keystorePath`:

```bash
docker run --rm -p 8080:8080 \
  -v "$(pwd)/certs:/certs:ro" \
  dss-pades-signer:local
```

Ejemplo de `keystorePath` dentro del contenedor: `/certs/certificado.p12`.

### Publicar automáticamente en GHCR

El repositorio incluye el workflow [c:\Raul\Java\Workspace\.github\workflows\publish-ghcr.yml](c:\Raul\Java\Workspace\.github\workflows\publish-ghcr.yml), que publica la imagen en GitHub Container Registry en cada push a `main` o `master`, y también en tags `v*`.

Si tu repositorio en GitHub es `usuario/mi-servicio`, la imagen publicada quedará disponible como:

```bash
docker pull ghcr.io/usuario/mi-servicio:latest
docker run --rm -p 8080:8080 ghcr.io/usuario/mi-servicio:latest
```

`latest` se publica desde la rama por defecto. Además, el workflow genera tags por rama, tag Git y SHA del commit.

## Codificar un PDF en Base64 (PowerShell)

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("C:\ruta\al\documento.pdf"))
```

---

## Endpoints

### `GET /health`

Comprueba que el servicio está activo.

**Respuesta exitosa (200 OK):**
```json
{ "status": "UP" }
```

---

### `POST /sign-pades-b`

Firma el PDF con nivel **PAdES Baseline B** (firma básica sin sello de tiempo). Antes de firmar, el servicio valida la autorización contra `https://api.firma-one.com/api/signing/authorize-sign` enviando el header `X-API-KEY` y el campo `CurrentUsers`.

**Header requerido:**
```http
X-API-KEY: <tu-api-key>
```

**Request body:**
```json
{
  "pdfBase64": "<PDF codificado en Base64>",
  "currentUsers": 12,
  "keystorePath": "C:\\Users\\usuario\\certificado.p12",
  "keystorePassword": "contraseña",
  "reason": "Firmado por ...",
  "location": "Barcelona"
}
```

**Ejemplo funcional verificado:**
```json
{
  "pdfBase64": "JVBERi0x...",
  "currentUsers": 12,
  "keystorePath": "C:\\Users\\rbonito\\Desktop\\Certificados\\HEBO_46132064K_RICARDO_NOVEL__R__B65554842_.p12",
  "keystorePassword": "1774",
  "reason": "Signed by Raúl Bonito",
  "location": "Barcelona"
}
```

**Respuesta exitosa (200 OK):**
```json
{
  "status": "SUCCESS",
  "message": "Document signed successfully",
  "signedPdfBase64": "<PDF firmado en Base64>"
}
```

**Respuesta de error (400 Bad Request):**
```json
{
  "status": "ERROR",
  "message": "Descripción del error",
  "signedPdfBase64": null
}
```

**Respuesta si la autorización externa rechaza la firma (403 Forbidden):**
```json
{
  "status": "ERROR",
  "message": "Authorization rejected",
  "signedPdfBase64": null
}
```

---

### `POST /sign-pades-t`

Firma el PDF con nivel **PAdES Baseline T** (firma con sello de tiempo). Requiere una URL de servidor TSP (Timestamp Authority) accesible.

**Request body:**
```json
{
  "pdfBase64": "<PDF codificado en Base64>",
  "keystorePath": "C:\\Users\\usuario\\certificado.p12",
  "keystorePassword": "contraseña",
  "tspUrl": "http://timestamp.sectigo.com",
  "reason": "Firmado por ...",
  "location": "Barcelona"
}
```

> Con sello de tiempo la firma incluye la fecha y hora certificada de la firma.

---

### `POST /sign-pades-lt`

Firma el PDF con nivel **PAdES Baseline LT** (firma con información de validación a largo plazo). Incluye el sello de tiempo más los datos de validación (CRL/OCSP) incrustados en el PDF. Requiere TSP y acceso a los servicios de validación del certificado.

**Request body:**
```json
{
  "pdfBase64": "<PDF codificado en Base64>",
  "keystorePath": "C:\\Users\\usuario\\certificado.p12",
  "keystorePassword": "contraseña",
  "tspUrl": "http://timestamp.sectigo.com",
  "reason": "Firmado por ...",
  "location": "Barcelona"
}
```

---

### `POST /sign-pades-lta`

Firma el PDF con nivel **PAdES Baseline LTA** (firma con archivado a largo plazo). Añade un sello de tiempo de archivo adicional sobre los datos de validación para garantizar la integridad a largo plazo. Es el nivel más alto de conformidad PAdES.

**Request body:**
```json
{
  "pdfBase64": "<PDF codificado en Base64>",
  "keystorePath": "C:\\Users\\usuario\\certificado.p12",
  "keystorePassword": "contraseña",
  "tspUrl": "http://timestamp.sectigo.com",
  "reason": "Firmado por ...",
  "location": "Barcelona"
}
```

---

## Campos del Request

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `pdfBase64` | String | ✅ | PDF a firmar codificado en Base64 |
| `currentUsers` / `CurrentUsers` | Integer | ✅ | Número actual de usuarios que se envía al endpoint externo de autorización |
| `keystorePath` | String | ✅ | Ruta absoluta al fichero .p12/.pfx |
| `keystorePassword` | String | ✅ | Contraseña del keystore |
| `keyPassword` | String | ❌ | Contraseña de la clave privada (si es distinta a `keystorePassword`) |
| `tspUrl` | String | ⚠️ | URL del servidor de sello de tiempo. Requerido para niveles T, LT y LTA |
| `tspUsername` | String | ❌ | Usuario para autenticación en el TSP (si lo requiere) |
| `tspPassword` | String | ❌ | Contraseña para autenticación en el TSP (si lo requiere) |
| `reason` | String | ❌ | Razón de la firma (aparece en los metadatos del PDF) |
| `location` | String | ❌ | Localización del firmante (aparece en los metadatos del PDF) |
| `addVisualSignature` | boolean | ❌ | Si `true`, añade una firma visual al PDF (por defecto `false`) |
| `visualText` | String | ❌ | Texto a mostrar en la firma visual |
| `visualX` | int | ❌ | Posición X de la firma visual (puntos PDF) |
| `visualY` | int | ❌ | Posición Y de la firma visual (puntos PDF) |
| `visualWidth` | int | ❌ | Anchura del bloque de firma visual (puntos PDF) |
| `visualHeight` | int | ❌ | Altura del bloque de firma visual (puntos PDF) |

### Notas sobre los campos

- **Rutas en Windows**: Los separadores de directorio deben ir escapados (`\\`). Ejemplo: `"C:\\Users\\usuario\\cert.p12"`.
- **Autorización obligatoria**: Todos los endpoints de firma requieren el header `X-API-KEY` y el campo numérico `currentUsers` o `CurrentUsers` en el body.
- **keyPassword**: Si no se proporciona, se usa `keystorePassword` como contraseña de la clave privada.
- **Clave utilizada**: Se selecciona automáticamente la primera clave disponible en el keystore.

---

## Niveles de firma PAdES

| Nivel | Endpoint | Sello de tiempo | Datos de validación | Archivado LTA |
|---|---|---|---|---|
| Baseline B | `/sign-pades-b` | ❌ | ❌ | ❌ |
| Baseline T | `/sign-pades-t` | ✅ | ❌ | ❌ |
| Baseline LT | `/sign-pades-lt` | ✅ | ✅ | ❌ |
| Baseline LTA | `/sign-pades-lta` | ✅ | ✅ | ✅ |

---

## Servidores TSP gratuitos

Para pruebas con los niveles T, LT y LTA puedes usar:

| Proveedor | URL |
|---|---|
| Sectigo | `http://timestamp.sectigo.com` |
| DigiCert | `http://timestamp.digicert.com` |
| GlobalSign | `http://timestamp.globalsign.com/scripts/timstamp.dll` |
| Freetsa | `https://freetsa.org/tsr` |

---

## Ejemplo con curl

```bash
curl -X POST http://localhost:8080/sign-pades-b \
  -H "X-API-KEY: <tu-api-key>" \
  -H "Content-Type: application/json" \
  -d '{
    "pdfBase64": "<base64>",
    "currentUsers": 12,
    "keystorePath": "C:\\ruta\\certificado.p12",
    "keystorePassword": "contraseña",
    "reason": "Aprobado",
    "location": "Madrid"
  }'
```

---

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.0 |
| EU DSS (dss-pades) | 6.1 |
| EU DSS (dss-pades-pdfbox) | 6.1 |
| EU DSS (dss-service) | 6.1 |
| EU DSS (dss-token) | 6.1 |
| EU DSS (dss-validation) | 6.1 |