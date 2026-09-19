# Progreso del proyecto

## 2026-09-18

- Se conecto la autenticacion de login y registro con el backend.
- Se corrigio el envio del JWT con el esquema `Bearer`.
- Se agrego persistencia local del token y del identificador del usuario.
- Se valido correo, longitud, mayuscula y caracter especial en contrasenas.
- Se conectaron catalogo, perfil, pre-evaluacion y solicitudes a sus repositorios.
- Se redujo el logging HTTP a nivel `BASIC` para no registrar cuerpos sensibles.
- Se corrigio el package esperado por el test instrumentado.
- Se agrego README y documentacion KDoc para los contratos principales.

## Estado actual

La aplicacion compila y las pruebas unitarias pasan. El flujo principal es utilizable como MVP siempre que el backend este disponible y la cuenta tenga los permisos requeridos. Las funcionalidades que no cuentan con endpoint backend permanecen como pantallas de interfaz o modelos preparados para una integracion posterior.

## Proximos pasos del backend

1. Publicar endpoints para concesionarias, inventario por concesionaria, mensajes y test drives.
2. Definir el contrato de roles y registro de concesionarias.
3. Exponer IDs de entidades financieras para simulaciones.
4. Agregar pruebas de contrato o un entorno staging para probar la app sin datos de produccion.
