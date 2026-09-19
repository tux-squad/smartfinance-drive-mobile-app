# SmartFinance Drive Mobile

Aplicacion Android para compradores de vehiculos y concesionarias. El proyecto usa Kotlin, Jetpack Compose, Clean Architecture y separacion por bounded contexts.

**Autor:** Abraam Acosta  
**Repositorio previsto:** `tux-squad/smartfinance-drive-mobile-app`

## Estado del proyecto

La aplicacion es un MVP funcional. La autenticacion, el catalogo, el perfil y las solicitudes de financiamiento estan preparados para consumir el backend REST configurado en `ApiConfig`. Algunas funciones de negocio (mensajeria, test drive, membresias, prospectos y operaciones completas de concesionaria) todavia requieren endpoints equivalentes en el backend.

## Funcionalidades

- Registro de compradores usando correo electronico.
- Inicio de sesion con JWT y deteccion de rol.
- Navegacion separada para comprador y concesionaria.
- Catalogo de vehiculos desde `GET /api/v1/vehicles`.
- Perfil desde `GET /api/v1/profiles/users/{userId}` y actualizacion mediante `PUT`.
- Simulaciones de financiamiento mediante `/api/v1/simulations`.
- Interfaz responsive construida con Jetpack Compose.

## Arquitectura

Los paquetes se organizan por contexto:

| Contexto | Responsabilidad |
| --- | --- |
| `iam` | Registro, autenticacion, usuario y roles |
| `catalog` | Vehiculos, concesionarias e inventario |
| `financing` | Simulaciones, pre-evaluaciones y solicitudes |
| `profiles` | Datos personales del usuario |
| `messaging` | Modelos y pantallas de conversaciones |
| `settings` | Preferencias y configuracion |
| `shared` | Shells, drawer, headers y navegacion reutilizable |
| `core` | Red, almacenamiento de token y tema |

La inyeccion de dependencias es manual: `SmartFinanceApplication` crea un `AppContainer` que comparte `Retrofit`, `TokenStorage`, APIs y repositorios.

## Backend y autenticacion

La URL base se define en:

```text
app/src/main/java/com/smartfinance/mobile/core/network/ApiConfig.kt
```

El backend exige que `username` sea un correo electronico y que la contrasena cumpla sus reglas de complejidad. La pantalla de registro valida esas reglas antes de enviar la peticion. Al iniciar sesion se guarda el token y el interceptor agrega:

```text
Authorization: Bearer <token>
```

No se incluyen credenciales de prueba ni secretos en el repositorio. Para probar la aplicacion se debe usar una cuenta existente en el backend o crear una desde la pantalla de registro.

## Requisitos

- Android Studio reciente.
- JDK 11.
- Android SDK con API 37.
- Dispositivo o emulador con Android 7.0 (API 24) o superior.
- Acceso de red al backend configurado.

## Ejecutar

En Windows:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
```

Luego abre el proyecto en Android Studio y ejecuta la variante `debug` en un emulador o dispositivo.

## Limitaciones conocidas

- El backend debe proporcionar IDs reales de entidades financieras para que una simulacion sea aceptada.
- Mensajeria, test drives, membresias, prospectos y configuracion avanzada de concesionarias necesitan endpoints y DTOs del backend.
- El backend debe estar disponible para validar el flujo completo; los tests actuales cubren compilacion y pruebas unitarias basicas.

## Documentacion Kotlin

La documentacion de APIs y clases publicas se escribe con **KDoc**, el equivalente de Javadoc para Kotlin. Los comentarios KDoc deben describir responsabilidad, parametros, valores devueltos y errores relevantes, sin duplicar detalles obvios de implementacion.
