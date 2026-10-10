# Guía funcional de SmartFinance Drive

## 1. Objetivo del producto

SmartFinance Drive es un portal para compradores de vehículos que centraliza:

- La búsqueda de vehículos recomendados.
- La exploración de concesionarias aliadas.
- El seguimiento de solicitudes de financiamiento.
- La consulta de un asistente de inteligencia artificial.
- La administración del perfil personal y financiero.
- Las preferencias de notificaciones y seguridad de la cuenta.

Esta guía resume el frontend web actual y sirve como contrato funcional para construir la versión mobile.

> **Importante:** el frontend actual es principalmente una base visual. Las pantallas no tienen integraciones con backend, autenticación, persistencia, formularios funcionales ni consultas reales. En mobile deben conservarse las funcionalidades descritas, pero también deben conectarse a servicios reales cuando estén disponibles.

## 2. Estructura de navegación

La aplicación usa Vue 3, Vue Router, Pinia, PrimeIcons y Tailwind CSS.

| Ruta | Nombre | Opción visible | Propósito |
|---|---|---|---|
| `/search-vehicles` | `search-vehicles` | Buscar Vehículos | Inicio del comprador, búsquedas y recomendaciones de vehículos. |
| `/concessionaries` | `concessionaries` | Buscar Concesionarias | Inventario y oferta de concesionarias aliadas. |
| `/consultations` | `consultations` | Consulta IA | Seguimiento de solicitudes de financiamiento según el texto de la vista. |
| `/reports` | `reports` | Reporte | Asistente inteligente para encontrar vehículo y plan de pagos. |
| `/users` | `users` | Perfil | Información personal y perfil financiero. |
| `/configuration` | `configuration` | Configuración | Notificaciones, seguridad y acciones de cuenta. |

La ruta `/` redirige a `/search-vehicles`. Cualquier ruta desconocida también redirige a esa pantalla.

## 3. Elementos globales

### 3.1 Barra lateral y menú

En desktop se muestra una barra lateral azul con:

- Marca SmartFinance Drive.
- Acceso a Buscar Vehículos.
- Acceso a Buscar Concesionarias.
- Acceso a Consulta IA.
- Acceso a Reporte.
- Acceso a Perfil.
- Acceso a Configuración.
- Bloque de ayuda con la opción **Llamar Soporte**.

### 3.2 Encabezado

El encabezado muestra:

- Contexto de navegación: **Panel de Comprador / Portal Aliados**.
- Nombre del usuario: actualmente se muestra “Carlos Mendoza”.
- Estado del usuario: actualmente se muestra “Comprador Pre-aprobado”.

### 3.3 Comportamiento requerido en mobile

La barra lateral no debe ocupar permanentemente el ancho de la pantalla. Debe convertirse en:

- Un encabezado fijo o superior con marca y botón de menú.
- Un drawer o menú modal con las seis rutas.
- Un área táctil mínima de 44 px para enlaces, botones, toggles e iconos.
- Cierre del drawer al seleccionar una ruta o tocar fuera del menú.
- Indicador visual claro de la ruta activa.
- Acceso a soporte visible sin ocultar el contenido principal.

La navegación debe usar el router de la aplicación, no enlaces que recarguen toda la página. En el web actual se usan enlaces HTML directos; mobile debe evitar esa limitación.

## 4. Funcionalidades por pantalla

## 4.1 Buscar Vehículos

**Ruta:** `/search-vehicles`

**Contenido actual:**

- Saludo personalizado: “Bienvenido, Carlos”.
- Descripción del portal para gestionar búsquedas y solicitudes de financiamiento automotriz.
- Sección “Recomendados para ti”.

**Funcionalidad esperada para mobile:**

- Mostrar vehículos recomendados de acuerdo con el perfil financiero y las preferencias del usuario.
- Permitir buscar y filtrar por marca, modelo, precio, año, tipo de vehículo, ubicación y condiciones de financiamiento.
- Mostrar cada vehículo en una tarjeta mobile con imagen, precio, cuota estimada, concesionaria y estado de disponibilidad.
- Abrir el detalle del vehículo.
- Guardar y quitar vehículos favoritos.
- Iniciar una solicitud de financiamiento desde un vehículo.
- Mostrar estados de carga, vacío, error y ausencia de resultados.

**Estado actual:** solo existe el título de la sección; no hay buscador, tarjetas, filtros, favoritos ni solicitudes implementadas.

## 4.2 Concesionarias Aliadas

**Ruta:** `/concessionaries`

**Contenido actual:**

- Título “Concesionarias Aliadas”.
- Descripción de inventarios certificados y planes de financiamiento integrados.

**Funcionalidad esperada para mobile:**

- Listar concesionarias aliadas.
- Buscar y filtrar por nombre, ubicación, marca, distancia y disponibilidad.
- Mostrar logo o imagen, nombre, dirección, calificación, horarios y datos de contacto.
- Consultar el inventario de una concesionaria.
- Ver ubicación en mapa o abrir indicaciones.
- Contactar a la concesionaria por llamada, mensaje o canal definido.
- Mostrar concesionarias sin resultados, errores y disponibilidad limitada.

**Estado actual:** solo existe el encabezado descriptivo; no hay listado ni interacción.

## 4.3 Consulta IA / Mis Solicitudes

**Ruta:** `/consultations`

**Contenido actual:**

- Título “Mis Solicitudes”.
- Descripción del estado en tiempo real de solicitudes de financiamiento automotriz.

**Funcionalidad esperada para mobile:**

- Listar solicitudes del usuario.
- Mostrar identificador, vehículo, concesionaria, fecha, monto y estado.
- Representar estados como enviada, en revisión, pre-aprobada, aprobada, rechazada o requiere información.
- Abrir el detalle de una solicitud.
- Mostrar la línea de tiempo de cambios.
- Permitir adjuntar o completar información pendiente.
- Actualizar el estado manualmente y recibir actualizaciones automáticas cuando el backend lo soporte.
- Enlazar la solicitud con el asistente IA para resolver dudas sobre requisitos, cuotas y próximos pasos.

**Estado actual:** la ruta y el texto corresponden al seguimiento de solicitudes; no hay listado, chat IA, estado ni conexión en tiempo real.

## 4.4 Reporte / Asistente Inteligente

**Ruta:** `/reports`

**Contenido actual:**

- Título “Asistente Inteligente”.
- Descripción de una conversación con IA para encontrar un auto y un plan de pagos.

**Funcionalidad esperada para mobile:**

- Mostrar una interfaz de conversación optimizada para teclado móvil.
- Permitir escribir preguntas y enviar mensajes.
- Mostrar mensajes del usuario y respuestas de la IA con estados de envío, carga y error.
- Sugerir preguntas iniciales, por ejemplo presupuesto, cuota máxima, tipo de auto y ubicación.
- Usar el perfil financiero y las preferencias del usuario para contextualizar respuestas.
- Presentar recomendaciones de vehículos y planes de pago como tarjetas accionables.
- Permitir abrir el detalle de una recomendación, guardarla o iniciar una solicitud.
- Mantener el historial de conversaciones y permitir iniciar una conversación nueva.
- Informar claramente cuando la IA no pueda responder o cuando deba intervenir un asesor.

**Estado actual:** solo existe la descripción; no hay chat, historial, IA ni generación de reportes.

## 4.5 Mi Perfil

**Ruta:** `/users`

### Información personal

La pantalla muestra campos para:

- Nombre completo.
- Correo electrónico.
- Teléfono de contacto.

### Perfil financiero

La pantalla muestra campos para:

- Ingreso mensual estimado en USD.
- Situación laboral.
- Antigüedad laboral.

### Acciones

- Editar la información personal.
- Editar el perfil financiero.
- Guardar cambios y mostrar confirmación o error.
- Actualizar contraseña (el botón aparece también en esta pantalla).

**Estado actual:** los valores están escritos directamente en inputs (`Carlos Mendoza`, `carlos.mendoza@gmail.com`, `+51 12345678`, `$ 3,500 USD`, etc.). No usan `v-model`, no validan datos, no guardan cambios y el botón no ejecuta una acción.

**Requisitos mobile:**

- Usar un formulario de una sola columna.
- Separar visualmente datos personales y financieros.
- Aplicar teclado y tipo de input adecuado para correo, teléfono y moneda.
- Validar campos obligatorios, formato de correo, teléfono e ingreso no negativo.
- Indicar campos editables y modo lectura si corresponde.
- Evitar perder cambios al navegar; solicitar confirmación si hay cambios sin guardar.

## 4.6 Configuración

**Ruta:** `/configuration`

### Notificaciones

La pantalla incluye:

- Alertas inmediatas cuando una concesionaria actualiza el estado de crédito.
- Resumen semanal de vehículos recomendados por correo.

Mobile debe persistir cada preferencia, mostrar su estado actual y confirmar errores de actualización.

### Seguridad de la cuenta

La pantalla incluye:

- Contraseña actual.
- Nueva contraseña.
- Botón “Actualizar contraseña”.

Mobile debe agregar confirmación de nueva contraseña, reglas de complejidad, mostrar/ocultar contraseña, mensajes de validación y cierre o invalidación de sesiones cuando el backend lo requiera.

### Zona de peligro

La pantalla presenta:

- Acción para eliminar la cuenta.
- Advertencia de que se eliminarán búsquedas guardadas, perfiles de crédito y solicitudes.

Mobile debe proteger esta acción con:

- Diálogo de confirmación explícito.
- Explicación de consecuencias irreversibles.
- Reautenticación o contraseña, si el backend lo exige.
- Estado de proceso, error y confirmación final.

**Estado actual:** los toggles no están enlazados a estado, los inputs de contraseña tienen valores de ejemplo visibles en el código, el botón no ejecuta acciones y la eliminación de cuenta solo se muestra como texto.

## 5. Modelo de datos mínimo para mobile

Para conservar la paridad funcional, mobile debería manejar al menos estas entidades:

- **Usuario:** id, nombre, correo, teléfono, estado de comprador.
- **Perfil financiero:** ingreso mensual, situación laboral, antigüedad, preferencias y capacidad de cuota.
- **Vehículo:** id, marca, modelo, año, precio, cuota estimada, imágenes, ubicación, disponibilidad y concesionaria.
- **Concesionaria:** id, nombre, logo, dirección, coordenadas, horario, contacto y calificación.
- **Solicitud:** id, vehículo, concesionaria, monto, fecha, estado, documentos pendientes y línea de tiempo.
- **Preferencias:** alertas de solicitud, resumen semanal y demás canales habilitados.
- **Conversación IA:** id, mensajes, fecha, estado y recomendaciones asociadas.

## 6. Estados y errores que deben existir

Cada pantalla con datos remotos debe contemplar:

- Cargando: skeleton o indicador no bloqueante.
- Vacío: explicación y acción para comenzar o ajustar filtros.
- Error de red: mensaje entendible y botón para reintentar.
- Sesión expirada: redirección al inicio de sesión.
- Permiso insuficiente: mensaje sin revelar información sensible.
- Éxito: confirmación visible después de guardar, enviar o actualizar.
- Sin conexión: conservar datos locales seguros y sincronizar cuando sea posible.

## 7. Reglas de experiencia mobile

- Priorizar una columna y desplazamiento vertical.
- No replicar la barra lateral de 256 px del desktop.
- Mantener jerarquía de títulos, descripciones y acciones.
- Usar botones de ancho completo en formularios y acciones primarias.
- Mantener contraste suficiente y estados de foco, presión, error y deshabilitado.
- No depender solo del color para comunicar estados.
- Proteger datos personales, financieros y credenciales.
- No mostrar contraseñas de ejemplo ni valores sensibles embebidos en la interfaz.
- Confirmar acciones destructivas.
- Mantener la navegación atrás del dispositivo coherente con el historial de rutas.

## 8. Brechas prioritarias del frontend web actual

Estas brechas deben considerarse al implementar mobile y, de ser posible, corregirse también en web:

1. No existe autenticación ni protección de rutas.
2. No hay cliente API ni modelos de datos remotos.
3. No hay store Pinia para usuario, vehículos, solicitudes, preferencias o conversación IA.
4. Las vistas de vehículos y concesionarias no muestran datos.
5. La ruta `/consultations` tiene contenido de solicitudes, mientras `/reports` tiene contenido de IA; los nombres y textos deben confirmarse con producto para evitar confusión.
6. Los formularios no usan estado reactivo ni validación.
7. Los botones, toggles y enlaces de soporte no tienen acciones funcionales.
8. No hay estados de carga, vacío, error ni confirmaciones.
9. No hay diseño responsive implementado para la barra lateral ni las grillas.
10. La información de usuario está hardcodeada y debe provenir de sesión/perfil.

## 9. Criterio de aceptación de paridad mobile

La versión mobile se considera funcionalmente equivalente cuando un usuario puede:

- Navegar a las seis áreas desde un menú mobile.
- Consultar, filtrar, guardar y abrir vehículos.
- Explorar concesionarias y contactar o ubicar una.
- Consultar solicitudes y su estado.
- Conversar con IA y actuar sobre una recomendación.
- Editar y guardar sus datos personales y financieros.
- Configurar notificaciones.
- Cambiar su contraseña de forma segura.
- Solicitar la eliminación de su cuenta mediante confirmación protegida.
- Recibir feedback claro para carga, éxito, vacío, error y sesión expirada.

