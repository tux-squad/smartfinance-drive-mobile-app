# Guía del backend — SmartFinance Drive Platform

Documento de referencia para construir un frontend web o una aplicación mobile contra el backend principal del repositorio.

> **Fuente de verdad:** el código Java de `src/main/java` y las migraciones Flyway de `src/main/resources/db/migration`.  
> El directorio `reference-code/` contiene implementaciones de referencia y no debe usarse como contrato de la API principal.

## 1. Resumen ejecutivo

El backend es un monolito modular construido con:

- Java y Spring Boot.
- Arquitectura hexagonal/DDD, organizada por bounded contexts.
- PostgreSQL como base de datos.
- JPA/Hibernate y migraciones Flyway.
- API REST JSON bajo `/api/v1`.
- JWT access token y refresh token, con sesiones stateless.
- Spring Security con autorización por roles y ownership del recurso.
- OpenAPI/Swagger.
- Cloudinary para imágenes.
- Stripe para suscripciones, checkout, facturas y webhooks.
- Google OAuth2, Firebase Phone Auth, Brevo/SMTP, EmailVerify.io, Chequea.pe y Factiliza.

La plataforma cubre cinco grandes experiencias:

1. Registro, login y verificación de identidad.
2. Catálogo de vehículos y publicación de inventario.
3. Simulación financiera, scoring y solicitudes de crédito.
4. Operación B2B de concesionarios y entidades financieras.
5. Suscripciones, facturación y dashboards analíticos.

## 2. Ejecución y URLs

### Desarrollo

- API local: `http://localhost:8080`
- Base URL: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Producción configurada/documentada

- API: `https://smartfinance-drive-platform.onrender.com`
- Base URL: `https://smartfinance-drive-platform.onrender.com/api/v1`
- Swagger UI: `https://smartfinance-drive-platform.onrender.com/swagger-ui/index.html`
- OpenAPI JSON: `https://smartfinance-drive-platform.onrender.com/v3/api-docs`

La URL efectiva debe ser configurable en el frontend mediante una variable de entorno. No hardcodear la URL de producción.

## 3. Seguridad y autenticación

### Cabecera

Todas las rutas protegidas usan:

```http
Authorization: Bearer <access-token>
Content-Type: application/json
```

Para cargas de imágenes se usa `multipart/form-data`; no se debe forzar manualmente el `Content-Type` porque el navegador agrega el boundary.

### Reglas globales

| Regla | Comportamiento |
|---|---|
| `/api/v1/auth/**` | Público |
| `GET /api/v1/vehicles` y `GET /api/v1/vehicles/**` | Público |
| Swagger/OpenAPI | Público si `SWAGGER_ENABLED=true`; solo ADMIN si está desactivado |
| Cualquier otra ruta | Requiere usuario autenticado |
| Sesión | Stateless; no depende de cookies de sesión |
| CSRF | Deshabilitado para la API |
| CORS | Orígenes definidos por `CORS_ALLOWED_ORIGINS` |
| Límite de archivos | 10 MB por archivo y por request |

Que un método no tenga `@PreAuthorize` no lo convierte en público: la regla global sigue exigiendo JWT.

### Tokens

- Access token: JWT firmado, duración por defecto de 15 minutos.
- Refresh token: duración por defecto de 7 días.
- El backend incluye roles en el resultado de autenticación.
- El logout agrega el token y su JTI a una blacklist hasta su expiración.
- El frontend debe guardar el access token en memoria cuando sea posible y el refresh token en almacenamiento seguro (Keychain/Keystore en mobile).
- Ante `401`, intentar una sola renovación con `POST /api/v1/auth/tokens`; si falla, limpiar sesión y enviar al login.
- No reintentar infinitamente una petición que devuelve `401`.

### Roles efectivos

Los únicos valores del enum `Roles` son:

| Rol | Uso |
|---|---|
| `ROLE_USER` | Comprador/cliente final; perfiles, simulaciones, scoring, solicitudes, consultas, test drives |
| `ROLE_DEALER` | Concesionario; inventario, perfil comercial, asesores, CRM, métricas y suscripción |
| `ROLE_FINANCIAL_INSTITUTION` | Banco/financiera; entidades, benchmarks y evaluación de solicitudes |
| `ROLE_FINANCIAL_ANALYST` | Analista; lectura de entidades y benchmarks |
| `ROLE_ADMIN` | Administración global y operaciones privilegiadas |

**No existe `ROLE_SALES_AGENT`.** `SalesAgent` es un registro interno del concesionario. No diseñar una sesión ni guards de seguridad para ese rol.

Los roles pueden ser acumulativos. Por ejemplo, una cuenta puede tener `ROLE_USER` y `ROLE_DEALER`. Después de una elevación de rol hay que renovar el token para recibir los authorities actualizados.

### Ownership

Además del rol, varios endpoints comprueban que el recurso pertenece al usuario autenticado:

- Perfil: propietario o ADMIN.
- Vehículo: propietario del vehículo.
- Simulación: propietario o ADMIN para lectura; propietario para eliminación.
- Score y proyección: propietario o ADMIN.
- Entidad financiera/dealership: propietario de la entidad o ADMIN.
- Métricas: usuario dueño del concesionario o entidad financiera relacionada.

El frontend no debe permitir editar un recurso solo porque conoce su UUID. El backend es la autoridad final.

## 4. Convenciones HTTP y respuestas

- JSON por defecto.
- `201 Created` para altas.
- `200 OK` para lecturas y actualizaciones.
- `204 No Content` para eliminaciones u operaciones sin cuerpo.
- `400 Bad Request` para payload inválido o errores de dominio expuestos por los controladores.
- `401 Unauthorized` para JWT ausente, inválido, expirado o revocado.
- `403 Forbidden` para rol insuficiente o ownership fallido.
- `404 Not Found` para recursos inexistentes.
- `409 Conflict` puede aparecer en conflictos de unicidad/estado.
- `429 Too Many Requests` para rate limits, especialmente OTP.
- `503 Service Unavailable` cuando una integración externa obligatoria no está configurada/disponible.

Las colecciones paginadas usan el `Page<T>` directo de Spring Data:

```json
{
  "content": [],
  "number": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true,
  "empty": true
}
```

El frontend debe consumir `content`, `totalPages`, `totalElements`, `number` y `size`, y tratar `number` como índice base cero.

## 5. Contextos y funcionalidades

### IAM

Usuarios, roles, passwords, JWT, Google login, recuperación de contraseña, verificación de email, verificación de teléfono y verificación corporativa.

### Profiles

Perfil personal/socioeconómico del comprador y consulta de DNI mediante RENIEC.

### Catalog

Inventario de vehículos, filtros, estados, imágenes y almacenamiento Cloudinary.

### Partners

Concesionarios, entidades financieras, tasas benchmark, logos/banners y verificación de RUC.

### Financing

Simulaciones financieras, cronogramas de pagos y solicitudes de crédito.

### Scoring

Creación, consulta y eliminación de evaluaciones de score crediticio.

### Projections

Proyecciones de depreciación y valor futuro del vehículo.

### Consultations

Historial de consultas a la IA y recomendaciones de vehículos.

### CRM

Prospectos, notas, timeline, estados y test drives.

### Billing

Planes, suscripciones, checkout Stripe, facturas PDF, pagos y webhooks.

### Analytics

Dashboards para dealer, entidad financiera y administrador.

## 6. Catálogo completo de endpoints

La siguiente tabla resume los 109 endpoints lógicos documentados por el backend. Las rutas con “alias” aceptan más de una URL equivalente.

### 6.1 Autenticación y usuarios

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| POST | `/auth/registrations` | Público | Registrar cuenta; se asigna USER por defecto |
| POST | `/auth/sessions` | Público | Login y emisión de tokens |
| POST | `/auth/tokens` | Público | Renovar access/refresh token |
| DELETE | `/auth/sessions/current` | Público | Revocar sesión/token actual |
| POST | `/auth/password-recoveries` | Público | Solicitar recuperación sin enumerar usuarios |
| POST | `/auth/password-resets` | Público | Cambiar password con reset token |
| POST | `/auth/google` | Público | Login/registro con Google ID token |
| POST | `/auth/email-verification/send` | Público | Enviar OTP de email |
| POST | `/auth/email-verification/verify` | Público | Validar OTP de email |
| POST | `/auth/phone-verification` | Público | Validar token de teléfono Firebase |
| POST | `/auth/phone-verification/firebase` | Público | Alias de validación Firebase |
| GET | `/users` | ADMIN | Listar usuarios |
| GET | `/users/{userId}` | Propietario/ADMIN | Ver usuario |
| PUT | `/users/{userId}/roles` | ADMIN | Cambiar/agregar rol |
| POST | `/users/{userId}/dealer-role-requests` | Propietario/ADMIN | Solicitar elevación a dealer |
| POST | `/users/{userId}/financial-institution-role-requests` | Propietario/ADMIN | Solicitar elevación a financiera |
| POST | `/users/me/corporate-verification/initiate` | Autenticado | Iniciar verificación corporativa |
| POST | `/users/me/corporate-verification/confirm` | Autenticado | Confirmar verificación corporativa |
| POST | `/users/{userId}/corporate-verification/initiate` | Propietario/ADMIN | Iniciar verificación para usuario |
| POST | `/users/{userId}/corporate-verification/confirm` | Propietario/ADMIN | Confirmar verificación para usuario |

Todos los paths anteriores tienen el prefijo `/api/v1`.

### 6.2 Vehículos

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| GET | `/vehicles` | Público | Catálogo paginado y filtrable |
| GET | `/vehicles/brands` | Público | Marcas disponibles |
| GET | `/vehicles/{vehicleId}` | Público | Detalle de vehículo |
| GET | `/vehicles/my-listings` | Autenticado | Inventario propio |
| GET | `/vehicles/users/{userId}` | Propietario/ADMIN | Vehículos de un usuario |
| POST | `/vehicles` | DEALER/ADMIN | Publicar vehículo |
| PUT | `/vehicles/{vehicleId}` | Propietario | Actualizar vehículo |
| PATCH | `/vehicles/{vehicleId}/status` | Propietario | Cambiar a ACTIVE, RESERVED o SOLD |
| DELETE | `/vehicles/{vehicleId}` | Propietario | Eliminar vehículo |
| POST | `/vehicles/{vehicleId}/image` | Propietario | Subir imagen principal |
| POST | `/vehicles/{vehicleId}/images` | Propietario | Agregar imagen a galería |
| DELETE | `/vehicles/{vehicleId}/images/{imageIndex}` | Propietario | Quitar imagen por índice base cero |

Filtros de `GET /vehicles`: `brand`, `model`, `minPrice`, `maxPrice`, `minYear`, `maxYear`, `condition`, `page`, `size` y `sort`. El tamaño máximo documentado es 50.

### 6.3 Perfiles y verificaciones

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| POST | `/profiles` | Autenticado | Crear perfil |
| GET | `/profiles/{profileId}` | Propietario/ADMIN | Obtener perfil |
| GET | `/profiles/users/{userId}` | Propietario/ADMIN | Obtener perfil por usuario |
| PUT | `/profiles/{profileId}` | Propietario | Actualizar perfil |
| DELETE | `/profiles/{profileId}` | Propietario | Eliminar perfil |
| GET | `/profiles/reniec/dni/{dni}` | Autenticado | Consultar DNI |
| GET | `/partners/sunat/ruc/{ruc}` | Autenticado | Consultar RUC |
| GET | `/partners/corporate-verification/lookup/{ruc}` | Autenticado | Consultar verificación corporativa |

### 6.4 Concesionarios y asesores

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| GET | `/dealerships` | Autenticado | Directorio paginado |
| GET | `/dealerships/me` | DEALER/ADMIN | Mi concesionario |
| PUT | `/dealerships/me` | DEALER/ADMIN | Crear/actualizar concesionario |
| POST | `/dealerships/me/logo` | DEALER/ADMIN | Subir logo |
| POST | `/dealerships/me/banner` | DEALER/ADMIN | Subir banner |
| GET | `/dealerships/{id}` | Autenticado | Detalle público/privado |
| GET | `/dealerships/{id}/vehicles` | Autenticado | Vehículos del dealer |
| GET | `/dealers/me/sales-agents` | DEALER/ADMIN | Listar asesores internos |
| POST | `/dealers/me/sales-agents` | DEALER/ADMIN | Crear asesor |
| PUT | `/dealers/me/sales-agents/{id}` | DEALER/ADMIN | Actualizar asesor |
| POST | `/dealers/me/sales-agents/{id}/reassign-leads` | DEALER/ADMIN | Reasignar prospectos |
| GET | `/dealers/me/metrics` | DEALER/ADMIN | Métricas de billing del dealer |

Las cargas de logo/banner usan `multipart/form-data` y el campo `file`.

### 6.5 Entidades financieras

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| GET | `/financial-entities` | USER/DEALER/FI/ANALYST/ADMIN | Listado |
| GET | `/financial-entities/me` | FI/ADMIN | Entidad propia |
| GET | `/financial-entities/{id}` | Autorizado | Detalle |
| POST | `/financial-entities` | FI/ADMIN | Crear entidad |
| PUT | `/financial-entities/{id}` | Dueño/ADMIN | Actualizar entidad |
| DELETE | `/financial-entities/{id}` | ADMIN | Eliminar entidad |
| POST | `/financial-entities/{id}/rate-benchmarks` | Dueño/ADMIN | Registrar benchmark de tasas |
| POST | `/financial-entities/me/logo` | FI/ADMIN | Subir logo propio |
| POST | `/financial-entities/me/banner` | FI/ADMIN | Subir banner propio |
| POST | `/financial-entities/{id}/logo` | Dueño/ADMIN | Subir logo |
| POST | `/financial-entities/{id}/banner` | Dueño/ADMIN | Subir banner |

### 6.6 Simulación, scoring y crédito

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| POST | `/simulations` | Autenticado | Crear simulación |
| GET | `/simulations` | Autenticado | Listar simulaciones propias |
| GET | `/simulations/{id}` | Propietario/ADMIN | Ver simulación y cronograma |
| POST | `/simulations/{id}/apply` | Autenticado | Convertir simulación en solicitud |
| DELETE | `/simulations/{id}` | Propietario | Eliminar simulación |
| POST | `/credit-applications` | USER/ADMIN | Crear solicitud de crédito |
| GET | `/credit-applications/me` | USER/ADMIN | Solicitudes propias |
| GET | `/credit-applications/{id}` | USER/FI/DEALER/ADMIN | Ver solicitud autorizada |
| PATCH | `/credit-applications/{id}/status` | FI/ADMIN | Cambiar estado de solicitud |
| POST | `/credit-scores` | Autenticado | Crear evaluación |
| GET | `/credit-scores` | Autenticado | Listar scores propios |
| GET | `/credit-scores/{id}` | Propietario/ADMIN | Ver score |
| GET | `/credit-scores/profile/{profileId}` | Propietario/ADMIN | Score por perfil |
| DELETE | `/credit-scores/{id}` | Propietario/ADMIN | Eliminar score |
| POST | `/depreciation-projections` | Autenticado | Crear proyección |
| GET | `/depreciation-projections` | Autenticado | Listar proyecciones |
| GET | `/depreciation-projections/{id}` | Propietario/ADMIN | Ver proyección |
| GET | `/depreciation-projections/vehicle/{vehicleId}` | Propietario/ADMIN | Proyección por vehículo |
| DELETE | `/depreciation-projections/{id}` | Propietario/ADMIN | Eliminar proyección |

### 6.7 IA, CRM y test drives

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| POST | `/consultations`, `/consultations/chat` | Autenticado | Consultar IA |
| POST | `/ai/consultations`, `/ai/consultations/chat` | Autenticado | Alias de consulta IA |
| GET | `/consultations/history` | Autenticado | Historial IA |
| GET | `/consultations/recommendations` | Autenticado | Recomendaciones |
| GET | `/ai/consultations/history` | Autenticado | Alias de historial |
| GET | `/ai/consultations/recommendations` | Autenticado | Alias de recomendaciones |
| POST | `/prospects`, `/dealers/me/prospects` | Autenticado | Crear prospecto |
| GET | `/dealers/me/prospects` | DEALER/ADMIN | Listar prospectos |
| GET | `/dealers/me/prospects/{id}` | DEALER/ADMIN | Detalle de prospecto |
| POST | `/prospects/{id}/notes` | DEALER/ADMIN | Agregar nota |
| GET | `/prospects/{id}/timeline` | DEALER/ADMIN | Timeline CRM |
| PATCH | `/prospects/{id}/status` | DEALER/ADMIN | Cambiar estado |
| POST | `/test-drives` | Autenticado | Agendar prueba |
| GET | `/test-drives/me` | Autenticado | Mis pruebas |
| GET | `/test-drives/{id}` | Autenticado | Detalle de prueba |
| PATCH | `/test-drives/{id}/status` | Autenticado | Cambiar estado |
| DELETE | `/test-drives/{id}` | Autenticado | Cancelar/eliminar prueba |

### 6.8 Billing y Stripe

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| GET | `/billing/plans` | Autenticado | Planes activos |
| GET | `/billing/plans/{planId}` | Autenticado | Detalle de plan |
| POST | `/billing/plans` | ADMIN | Crear plan |
| GET | `/billing/subscriptions/me` | Autenticado | Suscripción actual |
| POST | `/billing/subscriptions` | Autenticado | Suscribirse |
| DELETE | `/billing/subscriptions/{subscriptionId}` | Autenticado | Cancelar suscripción |
| POST | `/billing/subscriptions/checkout-session` | Autenticado | Crear checkout Stripe |
| GET | `/billing/invoices/me` | Autenticado | Facturas propias |
| GET | `/billing/invoices/{invoiceId}/pdf` | Autenticado | Descargar factura PDF |
| PATCH | `/billing/invoices/{invoiceId}` | Autenticado | Pagar factura |
| POST | `/billing/webhooks/stripe` | Stripe signature | Recibir eventos de Stripe |

El webhook no se consume desde la UI. Stripe llama al endpoint y envía `Stripe-Signature`; el backend valida la firma.

### 6.9 Analytics

| Método | Ruta | Acceso | Función |
|---|---|---|---|
| GET | `/analytics/dealer` | DEALER/ADMIN | Dashboard comercial |
| GET | `/analytics/financial-institution` | FI/ADMIN | Dashboard financiero |
| GET | `/analytics/admin` | ADMIN | Dashboard global |

`/analytics/dealer` acepta `dealerUserId` y `period`; `/analytics/financial-institution` acepta `financialEntityId`.

## 7. Flujos recomendados para frontend/mobile

### 7.1 Registro y login

1. Opcionalmente verificar email con `email-verification/send` y `verify`.
2. Registrar con `POST /auth/registrations`.
3. Iniciar sesión con `POST /auth/sessions`.
4. Guardar tokens y roles.
5. Cargar perfil con `/profiles/users/{userId}` si ya existe.
6. Redirigir según roles:
   - USER: catálogo, simulaciones y solicitudes.
   - DEALER: dashboard, inventario y CRM.
   - FINANCIAL_INSTITUTION: solicitudes y benchmarks.
   - ADMIN: administración.

### 7.2 Elevación a dealer o financiera

1. Usuario autenticado envía solicitud de rol con RUC.
2. El backend verifica datos corporativos mediante Chequea.pe/SUNAT.
3. Si la verificación es aprobada, el usuario obtiene el nuevo rol.
4. Renovar tokens con `/auth/tokens`.
5. Crear/actualizar el perfil de dealership o entidad financiera.

### 7.3 Compra y financiamiento

1. Explorar `/vehicles` sin login.
2. Ver detalle del vehículo.
3. Autenticarse y completar `/profiles`.
4. Crear simulación en `/simulations`.
5. Mostrar cronograma, cuota y métricas retornadas.
6. Opcionalmente crear score.
7. Aplicar la simulación con `/simulations/{id}/apply` o crear una solicitud.
8. Consultar el estado de `/credit-applications/me`.

### 7.4 Dealer

1. Obtener `/dealerships/me`.
2. Registrar vehículos con `POST /vehicles`.
3. Subir portada/galería a Cloudinary mediante el backend.
4. Administrar asesores y prospectos.
5. Consultar métricas y gestionar el plan de billing.

### 7.5 Mobile

- Usar secure storage para refresh token.
- Enviar siempre `Authorization` en llamadas protegidas.
- Implementar interceptor para `401` con mutex para evitar múltiples refresh simultáneos.
- Para imágenes usar `multipart/form-data` y compresión previa razonable, sin superar 10 MB.
- Manejar offline solo como cache de UI; las operaciones financieras deben confirmarse contra el backend.
- Nunca calcular permisos únicamente en el cliente: ocultar pantallas mejora UX, pero el backend decide.

## 8. Modelo de datos funcional

Las migraciones principales crean o amplían:

- Usuarios, roles y tokens revocados.
- Perfiles y verificaciones de email, teléfono y corporativas.
- Vehículos y entidades financieras.
- Conversaciones y mensajes.
- Consultas IA.
- Solicitudes de crédito.
- Concesionarios, asesores, prospectos, notas y test drives.
- Planes, suscripciones y facturas.
- Benchmarks de tasas y dominios corporativos permitidos.

Los identificadores de dominio suelen ser UUID; usuarios y referencias externas usan `String`; billing usa IDs `Long`. El frontend debe conservar los IDs como string para evitar pérdida de precisión o conversiones implícitas.

## 9. Integraciones y variables de entorno

El backend requiere configuración externa para algunas funciones:

| Integración | Variables principales | Funcionalidad |
|---|---|---|
| PostgreSQL | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | Persistencia |
| JWT | `JWT_SECRET`, expiraciones | Autenticación |
| Google | `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET` | Login Google |
| Cloudinary | `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET` | Imágenes |
| Chequea.pe | `CHEQUEA_API_KEY`, `CHEQUEA_BASE_URL` | RUC/SUNAT |
| Factiliza | `FACTILIZA_API_KEY`, `FACTILIZA_BASE_URL` | DNI/RENIEC |
| Firebase | `FIREBASE_PROJECT_ID`, credenciales | Verificación telefónica |
| Mail/Brevo | variables SMTP y `BREVO_API_KEY` | OTP y correos |
| EmailVerify | `EMAILVERIFY_API_KEY`, `EMAILVERIFY_BASE_URL` | Validación email |
| Stripe | `STRIPE_API_KEY`, `STRIPE_WEBHOOK_SECRET`, URLs | Billing |
| CORS | `CORS_ALLOWED_ORIGINS` | Orígenes autorizados |

En desarrollo puede haber respuestas de integración emuladas según la configuración, pero el frontend debe tratar errores de proveedor y `503` explícitamente.

## 10. Checklist de implementación del cliente

- [ ] Centralizar `BASE_URL`.
- [ ] Crear cliente HTTP con timeout y parsing de errores.
- [ ] Agregar interceptor de access token.
- [ ] Implementar refresh con protección contra carreras.
- [ ] Persistir y limpiar sesión correctamente.
- [ ] Crear guards por rol y por estado de autenticación.
- [ ] Modelar respuestas paginadas de Spring.
- [ ] Modelar UUID como string.
- [ ] Implementar estados de carga, vacío, error, 401, 403, 404 y 429.
- [ ] No enviar `userId` del formulario cuando el backend lo toma del JWT.
- [ ] Usar `FormData` para imágenes y mostrar progreso de subida.
- [ ] No exponer secrets de Stripe, Cloudinary, Chequea, Brevo ni JWT en la app.
- [ ] Mantener secretos únicamente en el backend.
- [ ] Probar flujos con USER, DEALER, FINANCIAL_INSTITUTION y ADMIN.

## 11. Archivos clave del backend

- `src/main/java/com/smartfinance/smartfinancedriveplatform/iam/interfaces/rest/`: autenticación y usuarios.
- `src/main/java/com/smartfinance/smartfinancedriveplatform/catalog/interfaces/rest/`: vehículos.
- `src/main/java/com/smartfinance/smartfinancedriveplatform/financing/interfaces/rest/`: simulaciones y crédito.
- `src/main/java/com/smartfinance/smartfinancedriveplatform/partners/interfaces/rest/`: dealers y entidades financieras.
- `src/main/java/com/smartfinance/smartfinancedriveplatform/billing/interfaces/rest/`: planes, Stripe y facturas.
- `src/main/java/com/smartfinance/smartfinancedriveplatform/analytics/interfaces/rest/`: dashboards.
- `src/main/resources/db/migration/`: esquema y evolución de PostgreSQL.
- `docs/api-documentation.md`: documentación detallada existente de payloads.
- `docs/authentication-flow.md`: secuencias de autenticación existentes.

