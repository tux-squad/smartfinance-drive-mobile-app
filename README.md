# SmartFinance Drive Mobile App

Aplicación móvil Android para explorar vehículos, comparar opciones y gestionar procesos relacionados con la compra y el financiamiento de automóviles. El proyecto presenta dos experiencias principales: **cliente/comprador** y **concesionaria**.

## Presentación

SmartFinance Drive conecta a personas interesadas en comprar un vehículo con concesionarias y servicios de financiamiento desde una sola aplicación. El cliente puede consultar el catálogo, revisar el detalle de un vehículo, comparar alternativas, solicitar una prueba de manejo y realizar una preevaluación crediticia. Las concesionarias cuentan con herramientas para administrar inventario, prospectos, solicitudes y mensajes.

La aplicación incluye una pantalla de acceso con diseño orientado a seguridad, registro de usuarios y un acceso demo para entrar directamente al flujo de cliente comprador durante las demostraciones.

## Funcionalidades

### Cliente o comprador

- Inicio de sesión y registro de cuenta.
- Acceso demo como usuario comprador.
- Catálogo de vehículos.
- Búsqueda y consulta de vehículos.
- Detalle de vehículo.
- Comparación de vehículos.
- Consulta de concesionarias.
- Solicitud de prueba de manejo.
- Consulta asistida por IA.
- Preevaluación de financiamiento.
- Seguimiento de solicitudes.
- Mensajería.
- Perfil y configuración de usuario.

### Concesionaria

- Dashboard de concesionaria.
- Administración de inventario.
- Registro de nuevos vehículos.
- Gestión de prospectos.
- Consulta del detalle de prospectos.
- Gestión de solicitudes de financiamiento.
- Mensajería con clientes.
- Configuración de concesionaria.
- Gestión de membresía.

## Tecnologías utilizadas

- **Kotlin 2.2.10**
- **Android Gradle Plugin 9.4.0**
- **Jetpack Compose** y Material 3
- **Android SDK 37**
- **Min SDK 24**
- **Retrofit 2.11.0** para comunicación HTTP
- **OkHttp 4.12.0** para cliente de red e interceptores
- **Gson 2.10.1** para serialización JSON
- **Coil** para carga de imágenes
- **Gradle Version Catalog** para administrar versiones y dependencias

## Arquitectura

El código está organizado por contextos funcionales y utiliza una separación por capas:

```text
app/src/main/java/com/smartfinance/mobile/
├── catalog/          # Vehículos, inventario, concesionarias y pruebas de manejo
├── financing/        # Preevaluación y solicitudes de financiamiento
├── iam/              # Login, registro y autenticación
├── messaging/        # Mensajería de clientes y concesionarias
├── profiles/         # Perfil del usuario
├── settings/         # Configuración y membresía
├── shared/           # Componentes, navegación y layouts compartidos
├── core/
│   ├── network/      # Retrofit, configuración API e interceptor de autorización
│   ├── storage/      # Almacenamiento local de sesión
│   └── ui/theme/     # Colores y tema visual
├── di/               # Contenedor manual de dependencias
├── MainActivity.kt   # Entrada y navegación principal
└── SmartFinanceApplication.kt
```

La aplicación utiliza repositorios para separar las pantallas de las fuentes de datos y un contenedor manual de dependencias para compartir el cliente Retrofit, las APIs y el almacenamiento de sesión.

## Seguridad

- Las contraseñas se envían únicamente al endpoint de autenticación y no se almacenan localmente.
- El token de acceso se guarda cifrado mediante **Android Keystore**.
- Las peticiones protegidas agregan el encabezado `Authorization: Bearer <token>`.
- Las rutas de login y registro permanecen públicas para permitir la autenticación inicial.
- La interfaz muestra mensajes de error generales sin exponer respuestas internas del servidor.
- El campo de contraseña permite ocultar y mostrar el valor introducido.
- El acceso demo no utiliza credenciales falsas ni guarda un token; entra directamente al flujo de comprador para fines de presentación.

## Internacionalización

Los flujos de inicio de sesión y registro están disponibles en español e inglés mediante recursos nativos de Android:

```text
app/src/main/res/values/strings.xml       # Inglés, idioma base
app/src/main/res/values-es/strings.xml    # Español
```

El selector `ES / EN` aparece en la pantalla de inicio de sesión. La selección se guarda localmente y se conserva para las siguientes aperturas de la aplicación. El español es el idioma inicial para conservar la experiencia actual.

## Requisitos

- Android Studio actualizado.
- JDK compatible con la configuración de Gradle del proyecto.
- Android SDK con API 37.
- Conexión a internet para consumir el backend.

## Configuración del backend

La URL base se encuentra centralizada en:

```text
app/src/main/java/com/smartfinance/mobile/core/network/ApiConfig.kt
```

Actualmente apunta a:

```text
https://smartfinance-drive-platform.onrender.com/
```

Para utilizar otro backend, modifica `ApiConfig.BASE_URL` y conserva la barra final requerida por Retrofit.

## Ejecución local

Clona el repositorio y abre la carpeta en Android Studio:

```bash
git clone https://github.com/tux-squad/smartfinance-drive-mobile-app.git
cd smartfinance-drive-mobile-app
```

Después, sincroniza Gradle y ejecuta la configuración `app` en un emulador o dispositivo Android.

También se puede compilar desde la terminal:

```bash
bash ./gradlew assembleDebug
```

El APK de depuración se genera en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Instalación del APK

1. Genera el APK con `bash ./gradlew assembleDebug`.
2. Copia `app-debug.apk` al dispositivo Android o usa `adb install`.
3. Instala el archivo y abre **SmartFinance Drive**.

El instalador y el launcher utilizan un ícono vectorial de automóvil definido en:

```text
app/src/main/res/drawable/ic_car_logo.xml
```

## Flujo de demostración

1. Abre la aplicación.
2. Selecciona **Ingresar como usuario demo** para entrar directamente como cliente comprador.
3. Explora el catálogo y las opciones de vehículos.
4. Para probar el flujo real, utiliza el formulario de inicio de sesión con una cuenta válida del backend.
5. Para crear una cuenta, selecciona **Regístrate aquí** y completa las validaciones de correo y contraseña.

## Estado del proyecto

El proyecto se encuentra preparado como aplicación Android funcional para demostración académica y desarrollo incremental. La navegación, las pantallas de cliente y concesionaria, la integración HTTP y el flujo de autenticación están implementados dentro del módulo `app`.

## Licencia

Este repositorio corresponde a un proyecto académico de SmartFinance Drive. No se incluye una licencia de distribución independiente.
