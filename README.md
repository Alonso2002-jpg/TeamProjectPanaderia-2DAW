# Bienvenido a la Panaderia MIGA DE ORO
## Video de Presentacion
- https://www.youtube.com/watch?v=4KwWlfTI-3c
***
## Integrantes

* Joselyn Obando
* Laura Garrido
* Kevin Bermudez
* Jorge Cruz
* Miguel Zanotto
***

### Puntos claves

- [Gestión de la API](#la-api-se-organiza-en-módulos-clave-para-promover-una-arquitectura-limpia-y-una-separación-clara-de-responsabilidades)

- [Funcionalidades](#funcionalidades-clave)

- [Tecnologias Usadas](#tecnologías-utilizadas)

- [Ventajas](#ventajas)

- [Casos de uso](#casos-de-uso)

- [Requisitos del sistema](#requisitos-del-sistema)

- [Documentacion de la API](#documentación-de-la-api)

- [Pruebas](#pruebas-tecnologias-usadas)

- [Dependencias](#dependencias-usadas-en-el-proyecto)

- [Usuarios](#usuarios)

- [Pedidos](#pedidos)

# La API se organiza en módulos clave para promover una arquitectura limpia y una separación clara de responsabilidades

### Controllers
Controladores REST que facilitan las interacciones entre el cliente y el servidor, manejando las solicitudes HTTP y delegando las operaciones a los servicios correspondientes.
##### Esto se implementa en los principales endpoint de nuestra panaderia:

-Categoria
-Cliente
-Personal
-Producto
-Proveedores

### Services
Capas de servicio que contienen la lógica de negocio principal, interactuando con los repositorios para realizar operaciones de base de datos.

### Repositories
Interfaces JPA que abstraen la capa de persistencia de datos, permitiendo una gestión de datos eficiente y simplificada.

### DTOs (Data Transfer Objects)
Objetos utilizados para encapsular los datos y enviarlos desde y hacia la API, optimizando el intercambio de información entre cliente y servidor.

### Mappers
Clases de utilidad que transforman los modelos de dominio en DTOs y viceversa, asegurando una transferencia de datos precisa y eficiente.

### Models
Entidades que representan las tablas de la base de datos, definiendo la estructura del dominio de la aplicación.

### Exceptions
Manejadores de excepciones personalizados para proporcionar respuestas claras y detalladas en caso de errores durante las solicitudes HTTP.

### Configuraciones
Configuraciones para almacenamiento, websockets y otros componentes cruciales para el funcionamiento de la API.

### WebSocket
Configuraciones y manejadores para establecer comunicaciones en tiempo real entre el cliente y el servidor, en este proyecto lo usaremos para transmitir notificaciones.

### Funcionalidades Clave

- **Gestión de Productos**: Permite a los usuarios crear, actualizar, eliminar y consultar información detallada de los productos disponibles, incluyendo precios y disponibilidad con los stock .
- **Manejo de Personal**: Administra los datos del personal, sus roles, asegurando que la asignación de tareas sea óptima y eficiente.
- **Relación con Proveedores**: Facilita la comunicación y gestión de pedidos con proveedores, optimizando el proceso de compra y mantenimiento de inventario.
- **Seguimiento en Tiempo Real**: A través de WebSockets, ofrece a los usuarios la capacidad de recibir actualizaciones en tiempo real sobre el estado de los pedidos y las transacciones. 

### Tecnologías Utilizadas
Este proyecto ha sido construido utilizando un stack tecnológico moderno y eficiente, que garantiza rendimiento, seguridad y escalabilidad:

- **Java Spring Boot**: Como columna vertebral de nuestra API, Spring Boot proporciona un marco de trabajo poderoso y flexible para desarrollar aplicaciones basadas en Java, facilitando la configuración y el despliegue.
- **MySQL**: Usamos MySQL para gestionar la información estructural de la panadería, aprovechando su confiabilidad y amplio uso en la industria para manejar datos relacionales.
- **MongoDB**: Para el almacenamiento de pedidos, optamos por MongoDB, una base de datos NoSQL que nos permite manejar grandes volúmenes de datos con esquemas flexibles, ideal para las operaciones dinámicas de pedidos que requieren escalabilidad y velocidad.
- **JPA**: Para una integración fluida con nuestras bases de datos y una fácil manipulación de los objetos Java en el contexto de persistencia.
- **WebSockets**: Para proporcionar comunicaciones en tiempo real, lo cual es esencial para una interacción instantánea entre clientes y el servidor, mejorando así la experiencia del usuario al proporcionar actualizaciones de estado y notificaciones en vivo.

La combinación de estas tecnologías nos permite ofrecer una API robusta que se adapta a las necesidades cambiantes de las panaderías y sus clientes, asegurando que la gestión de datos sea tanto eficiente como efectiva.

### Ventajas

- **Escalabilidad**: Diseñada para crecer con tu negocio, nuestra API maneja cargas de trabajo crecientes con facilidad.
- **Seguridad**: Implementamos las mejores prácticas de seguridad para proteger la información sensible de los usuarios y transacciones.
- **Documentación Clara**: Proporcionamos una documentación completa que facilita la integración y el uso de la API.

### Casos de Uso

- **Panaderías Digitales**: Perfecta para panaderías que buscan digitalizar su operación y proporcionar servicios en línea.

## Requisitos del Sistema

Para una experiencia de desarrollo y ejecución óptima de la Panadería API, asegúrate de que tu sistema cumple con los siguientes requisitos:

- **Java Development Kit (JDK)**: Deberías tener instalado JDK versión 11 o superior. Esto es necesario para ejecutar el servidor de aplicaciones Spring Boot y Kotlin.
- **Kotlin**: Se requiere Kotlin versión 1.4 o superior para compilar y ejecutar el código fuente de Kotlin.
- **Gradle**: Utilizamos Gradle como nuestra herramienta de automatización y gestión de dependencias. Deberías tener instalada la versión 6.3 o superior.
- **MySQL**: Una instancia de MySQL versión 5.7 o superior es necesaria para manejar la persistencia de datos relacionales.
- **MongoDB**: MongoDB versión 4.0 o superior debe estar instalado y corriendo para el almacenamiento de datos NoSQL de pedidos.
- **Sistema Operativo**: Aunque nuestra API es agnóstica al sistema operativo y debería funcionar en cualquier entorno que soporte Java, recomendamos el uso de sistemas basados en Unix, como Linux o macOS, para entornos de desarrollo o producción.
- **Conectividad de Red**: Es esencial tener una conexión a internet estable para la integración con bases de datos remotas y otros servicios web.

Antes de comenzar la instalación, verifica que todos los requisitos estén satisfechos para garantizar una configuración sin problemas y una ejecución eficiente de la API.
## Documentación de la API
-[Productos](#endpoints-de-productos)
-[Personal](#documentación-de-la-api---endpoints-de-personal)
-[Proveedores](#documentación-de-la-api---endpoints-de-proveedores)
-[Categoria](#documentación-de-la-api---endpoints-de-categoría)
-[Cliente](#documentación-de-la-api---endpoints-de-clientes)

La Panadería API proporciona una serie de endpoints RESTful para la gestión de recursos de una panadería, incluyendo productos, personal, y pedidos. A continuación se presentan los endpoints disponibles con sus respectivos métodos, parámetros y ejemplos de respuestas.

### Endpoints de Productos

### Obtener todos los productos

- **GET** `/producto`
- **Descripción**: Retorna una lista paginada de todos los productos, con opciones de filtrado por nombre, stock mínimo, precio máximo, estado activo, categoría y proveedor.
- **Parámetros**:
  - `nombre` (string, opcional): Filtro por nombre del producto.
  - `stockMin` (integer, opcional): Filtro por stock mínimo.
  - `precioMax` (double, opcional): Filtro por precio máximo.
  - `isActivo` (boolean, opcional): Filtro por estado activo/inactivo del producto.
  - `categoria` (string, opcional): Filtro por categoría del producto.
  - `proveedor` (string, opcional): Filtro por proveedor del producto.
  - `page` (integer, opcional): Número de página para la paginación.
  - `size` (integer, opcional): Tamaño de página para la paginación.
  - `sortBy` (string, opcional): Campo por el cual ordenar.
  - `direction` (string, opcional): Dirección de la ordenación (ascendente/descendente).
- **Respuesta de éxito**: `200 OK`

**Response:**

```json
{
  "content": [
    {
    "nombre":"Pan Bimbo",
    "stock":100,
    "precio":1.87,
    "categoria":"Pan",
    "proveedor":"12345678A"
}
  ],
    "totalPages": 1,
    "totalElements": 4,
    "pageSize": 10,
    "pageNumber": 0,
    "totalPageElements": 4,
    "empty": false,
    "first": true,
    "last": true,
    "sortBy": "id",
    "direction": "asc"
}

```
### post de un producto 




```json
{
    "nombre":"Pan Bimbo",
    "stock":100,
    "precio":1.87,
    "categoria":"Pan",
    "proveedor":"12345678A"
}

```
## Uso de UUID para Identificadores de Productos

En nuestra API, elegimos utilizar UUID (Universally Unique Identifier) para los identificadores de productos por varias razones clave:

1. **Unicidad Garantizada**: Los UUID son globalmente únicos, eliminando cualquier riesgo de duplicación incluso en sistemas distribuidos o escalables.

2. **Seguridad Mejorada**: A diferencia de los identificadores secuenciales, los UUID no revelan información sobre la estructura de datos subyacente, lo que mejora la seguridad y la privacidad.

3. **Independencia de la Base de Datos**: Los UUID se pueden generar antes de interactuar con la base de datos, facilitando operaciones asíncronas y transacciones distribuidas.

4. **Facilita la Integración y Sincronización**: Su unicidad global simplifica la sincronización de datos entre diferentes sistemas o bases de datos.

5. **Rendimiento Optimizado en Grandes Bases de Datos**: Los UUID pueden ofrecer un mejor rendimiento en bases de datos grandes o distribuidas, especialmente en inserciones.

El uso de UUID en nuestra API representa una elección estratégica para asegurar la escalabilidad, seguridad y flexibilidad del sistema.

## Documentación de la API - Endpoints de Personal

#### Tabla del controlador

| Endpoint                                      | URL                                      | HTTP Verbo | AUTH                          | Descripción                                   | HTTP Status Code | Otras Salidas                                          |
|-----------------------------------------------|------------------------------------------|------------|-------------------------------|-----------------------------------------------|------------------|---------------------------------------------------------|
| Obtiene todo el personal                      | `GET /api/version/personal`              | GET        | Requiere autenticación        | Obtiene una lista de todo el personal         | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Obtiene un miembro del personal por su UUID   | `GET /api/version/personal/{uuid}`       | GET        | Requiere autenticación        | Obtiene los detalles de un miembro del personal por su UUID | 200 OK | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Crea un nuevo miembro del personal            | `POST /api/version/personal`             | POST       | Requiere autenticación        | Registra un nuevo miembro del personal        | 201 Created      | 400 Bad Request, 401 Unauthorized, 403 Forbidden       |
| Actualiza un miembro del personal             | `PUT /api/version/personal/{uuid}`       | PUT        | Requiere autenticación        | Actualiza la información de un miembro del personal por su UUID | 200 OK | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Actualiza parcialmente un miembro del personal| `PATCH /api/version/personal/{uuid}`     | PATCH      | Requiere autenticación        | Actualiza parcialmente la información de un miembro del personal por su UUID | 200 OK | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Elimina un miembro del personal               | `DELETE /api/version/personal/{uuid}`    | DELETE     | Requiere autenticación        | Elimina un miembro del personal por su UUID  | 204 No Content   | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |


### Obtener todo el personal

- **GET** `/personal`
- **Descripción**: Retorna una lista paginada de todo el personal, con opciones de filtrado por nombre, DNI, categoría y estado activo.
- **Parámetros**:
  - `nombre` (string, opcional): Filtro por nombre del empleado.
  - `dni` (string, opcional): Filtro por DNI del empleado.
  - `categoria` (string, opcional): Filtro por categoría del empleado.
  - `isActivo` (boolean, opcional): Filtro por estado activo/inactivo del empleado.
  - `page` (integer, opcional): Número de página para la paginación.
  - `size` (integer, opcional): Tamaño de página para la paginación.
  - `sortBy` (string, opcional): Campo por el cual ordenar.
  - `direction` (string, opcional): Dirección de la ordenación (ascendente/descendente).
- **Respuesta de éxito**: `200 OK`
- **Ejemplo de respuesta**:
  ```json
  {
    "content": [
    {
            "id": "0c88d4da-9bc7-441f-a262-b589208a424b",
            "dni": "45678912D",
            "nombre": "Laura García",
            "seccion": "Limpieza",
            "fechaAlta": "2023-12-03",
            "isActive": true
        }
    ],
     "totalPages": 1,
    "totalElements": 5,
    "pageSize": 10,
    "pageNumber": 0,
    "totalPageElements": 5,
    "empty": false,
    "first": true,
    "last": true,
    "sortBy": "id",
    "direction": "asc"
  }
  

## Uso de UUID para Identificadores de Personal

En la Panadería API, hemos adoptado el uso de UUID (Universally Unique Identifier) para los identificadores del personal por motivos similares a los productos, enfocándonos en las siguientes ventajas:

1. **Consistencia en la Aplicación**: Utilizando el mismo sistema de identificación tanto para productos como para personal, mantenemos una consistencia en todo el proyecto. Esto simplifica el desarrollo y mantiene la uniformidad en las prácticas de codificación.

2. **Seguridad en la Gestión de Datos**: Al igual que con los productos, los UUID para el personal aumentan la seguridad. No revelan información sobre la cantidad de empleados o la frecuencia de nuevas incorporaciones, evitando así posibles inferencias o ataques basados en la estructura de IDs.

3. **Flexibilidad en la Escalabilidad**: Los UUID son ideales para sistemas que pueden expandirse o integrarse con otros en el futuro. Por ejemplo, si la aplicación necesita sincronizarse con sistemas externos de recursos humanos o plataformas de gestión de personal, los UUID facilitan este proceso sin preocuparse por conflictos de identificación.

4. **Optimización para Sistemas Distribuidos**: En un escenario de crecimiento donde se pueden incorporar múltiples servidores o bases de datos, los UUID evitan complicaciones con la generación de identificadores únicos, garantizando la integridad de los datos en un entorno distribuido.

El uso de UUID para el personal, así como para los productos, refleja nuestro compromiso con la seguridad, la escalabilidad y la consistencia en toda nuestra API, asegurando una base sólida para el crecimiento y la expansión futura del sistema.

#### Tabla del controlador

| Endpoint                                    | URL                                        | HTTP Verbo | AUTH                          | Descripción                                | HTTP Status Code | Otras Salidas                                          |
|---------------------------------------------|--------------------------------------------|------------|-------------------------------|--------------------------------------------|------------------|---------------------------------------------------------|
| Obtiene todos los clientes                  | `GET /api/version/cliente`                 | GET        | Requiere autenticación        | Obtiene una lista de todos los clientes    | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Obtiene un cliente por su ID                | `GET /api/version/cliente/{id}`            | GET        | Requiere autenticación        | Obtiene los detalles de un cliente específico por ID | 200 OK | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Crea un nuevo cliente                       | `POST /api/version/cliente`                | POST       | Requiere autenticación        | Registra un nuevo cliente                  | 201 Created      | 400 Bad Request, 401 Unauthorized, 403 Forbidden       |
| Actualiza un cliente                        | `PUT /api/version/cliente/{id}`            | PUT        | Requiere autenticación        | Actualiza la información de un cliente existente | 200 OK | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Actualiza parcialmente un cliente           | `PATCH /api/version/cliente/{id}`          | PATCH      | Requiere autenticación        | Actualiza parcialmente la información de un cliente | 200 OK | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Elimina un cliente                          | `DELETE /api/version/cliente/{id}`         | DELETE     | Requiere autenticación        | Elimina un cliente existente               | 204 No Content   | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Actualiza la imagen de un cliente           | `PATCH /api/version/cliente/imagen/{id}`   | PATCH      | Requiere autenticación        | Actualiza la imagen de un cliente          | 200 OK           | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |


## Documentación de la API - Endpoints de Proveedores

La API de proveedores permite gestionar la información de los proveedores de la panadería, incluyendo la creación, consulta, actualización y eliminación de registros de proveedores.

### Obtener todos los proveedores

- **GET** `/proveedores`
- **Descripción**: Retorna una lista paginada de todos los proveedores, con opciones de filtrado por NIF, nombre, estado activo y tipo.
- **Parámetros**:
  - `nif` (string, opcional): Filtro por NIF del proveedor.
  - `name` (string, opcional): Filtro por nombre del proveedor.
  - `isActive` (boolean, opcional): Filtro por estado activo/inactivo del proveedor.
  - `tipo` (string, opcional): Filtro por tipo de proveedor.
  - `page` (integer, opcional): Número de página para la paginación.
  - `size` (integer, opcional): Tamaño de página para la paginación.
  - `sortBy` (string, opcional): Campo por el cual ordenar.
  - `direction` (string, opcional): Dirección de la ordenación (ascendente/descendente).
- **Respuesta de éxito**: `200 OK`
- **Ejemplo de respuesta**:
  ```json
  {
    "content": [
     {
            "nif": "12345678A",
            "tipo": {
                "id": 10,
                "nameCategory": "Distribuidor",
                "createdAt": "2023-12-03",
                "updatedAt": "2023-12-03",
                "active": true
            },
            "numero": "123456789",
            "nombre": "Distribuciones Dulces",
            "isActive": true,
            "fechaCreacion": "2023-12-03",
            "fechaUpdate": "2023-12-03"
        }
    ],
   "totalPages": 1,
    "totalElements": 4,
    "pageSize": 10,
    "pageNumber": 0,
    "totalPageElements": 4,
    "empty": false,
    "first": true,
    "last": true,
    "sortBy": "id",
    "direction": "asc"
  }

## Documentación de la API - Endpoints de Categoría

La API de categoría facilita la gestión de categorías de productos y servicios en la panadería, permitiendo crear, leer, actualizar y eliminar categorías.

#### tabla de gestion del controlador 

| Endpoint                                    | URL                                | HTTP Verbo | AUTH                          | Descripción                              | HTTP Status Code | Otras Salidas                                          |
|---------------------------------------------|------------------------------------|------------|-------------------------------|------------------------------------------|------------------|---------------------------------------------------------|
| Obtiene todas las categorías                | `GET /api/version/categorias`      | GET        | Requiere autenticación        | Obtiene todas las categorías disponibles | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Obtiene una categoría por su id             | `GET /api/version/categorias/{id}` | GET        | Requiere autenticación        | Obtiene detalles de una categoría específica por ID | 200 OK | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Crea una nueva categoría                    | `POST /api/version/categorias`     | POST       | Requiere autenticación de admin | Crea una nueva categoría                  | 201 Created      | 400 Bad Request, 401 Unauthorized, 403 Forbidden       |
| Actualiza una categoría existente           | `PUT /api/version/categorias/{id}` | PUT        | Requiere autenticación de admin | Actualiza una categoría existente        | 200 OK           | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Elimina una categoría                       | `DELETE /api/version/categorias/{id}` | DELETE  | Requiere autenticación de admin | Elimina una categoría existente          | 204 No Content   | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |


### Obtener todas las categorías

- **GET** `/categoria`
- **Descripción**: Retorna una lista paginada de todas las categorías, con opciones de filtrado por nombre y estado activo.
- **Parámetros**:
  - `isActive` (boolean, opcional): Filtro por estado activo/inactivo de la categoría.
  - `name` (string, opcional): Filtro por nombre de la categoría.
  - `page` (integer, opcional): Número de página para la paginación.
  - `size` (integer, opcional): Tamaño de página para la paginación.
  - `sortBy` (string, opcional): Campo por el cual ordenar.
  - `direction` (string, opcional): Dirección de la ordenación (ascendente/descendente).
- **Respuesta de éxito**: `200 OK`
- **Ejemplo de respuesta**:
  ```json
  {
    "content": [
       {
            "nameCategory": "Dulces",
            "createdAt": "2023-12-03",
            "updatedAt": "2023-12-03",
            "isActive": true
        }
    ],
  "totalPages": 2,
    "totalElements": 12,
    "pageSize": 10,
    "pageNumber": 0,
    "totalPageElements": 10,
    "empty": false,
    "first": true,
    "last": false,
    "sortBy": "id",
    "direction": "asc"
  }

## Documentación de la API - Endpoints de Clientes

La API de clientes permite gestionar la información de los clientes de la panadería, incluyendo la creación, consulta, actualización y eliminación de registros de clientes.

#### Tabla de gestion l controlador

| Endpoint                                    | URL                                  | HTTP Verbo | AUTH                          | Descripción                              | HTTP Status Code | Otras Salidas                                          |
|---------------------------------------------|--------------------------------------|------------|-------------------------------|------------------------------------------|------------------|---------------------------------------------------------|
| Obtiene todos los clientes                  | `GET /api/version/cliente`           | GET        | Requiere autenticación        | Obtiene una lista de todos los clientes  | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Obtiene un cliente por su ID                | `GET /api/version/cliente/{id}`      | GET        | Requiere autenticación        | Obtiene los detalles de un cliente específico por ID | 200 OK | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Crea un nuevo cliente                       | `POST /api/version/cliente`          | POST       | Requiere autenticación        | Registra un nuevo cliente                | 201 Created      | 400 Bad Request, 401 Unauthorized, 403 Forbidden       |
| Actualiza un cliente                        | `PUT /api/version/cliente/{id}`      | PUT        | Requiere autenticación        | Actualiza la información de un cliente existente | 200 OK | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Actualiza parcialmente un cliente           | `PATCH /api/version/cliente/{id}`    | PATCH      | Requiere autenticación        | Actualiza parcialmente la información de un cliente | 200 OK | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Elimina un cliente                          | `DELETE /api/version/cliente/{id}`   | DELETE     | Requiere autenticación        | Elimina un cliente existente             | 204 No Content   | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Actualiza la imagen de un cliente           | `PATCH /api/version/cliente/imagen/{id}` | PATCH  | Requiere autenticación        | Actualiza la imagen de un cliente        | 200 OK           | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |


### Obtener todos los clientes

- **GET** `/cliente`
- **Descripción**: Retorna una lista paginada de todos los clientes, con opciones de filtrado por nombre completo y categoría.
- **Parámetros**:
  - `nombreCompleto` (string, opcional): Filtro por nombre completo del cliente.
  - `categoria` (string, opcional): Filtro por categoría del cliente.
  - `page` (integer, opcional): Número de página para la paginación.
  - `size` (integer, opcional): Tamaño de página para la paginación.
  - `sortBy` (string, opcional): Campo por el cual ordenar.
  - `direction` (string, opcional): Dirección de la ordenación (ascendente/descendente).
- **Respuesta de éxito**: `200 OK`
- **Ejemplo de respuesta**:
  ```json
  {
    "content": [
      {
            "id": 1,
            "nombreCompleto": "Laura González",
            "correo": "laura@example.com",
            "dni": "12345678A",
            "telefono": "612345678",
            "imagen": "imagen1.jpg",
            "fechaCreacion": "2023-12-03T11:52:28.744334",
            "fechaActualizacion": "2023-12-03T11:52:28.744334",
            "isActive": true,
            "categoria": {
                "id": 9,
                "nameCategory": "Normal",
                "createdAt": "2023-12-03",
                "updatedAt": "2023-12-03",
                "active": true
            }
        }
    ],
    "totalPages": 1,
    "totalElements": 5,
    "pageSize": 10,
    "pageNumber": 0,
    "totalPageElements": 5,
    "empty": false,
    "first": true,
    "last": true,
    "sortBy": "id",
    "direction": "asc"
  }

### Usuarios

La sección de usuarios de la API ofrece funcionalidades para la gestión de los usuarios de la Panadería, incluyendo operaciones para crear, leer, actualizar y eliminar registros de usuarios, así como la gestión de pedidos por parte de los usuarios.

#### Endpoints de Gestión de Usuarios

| Endpoint                                     | URL                                  | HTTP Verbo | AUTH                            | Descripción                                    | HTTP Status Code | Otras Salidas                                        |
|----------------------------------------------|--------------------------------------|------------|---------------------------------|------------------------------------------------|------------------|------------------------------------------------------|
| Obtiene todos los usuarios                   | `GET /api/version/users`             | GET        | Admin                           | Lista todos los usuarios con filtrado opcional | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found       |
| Obtiene un usuario por su ID                 | `GET /api/version/users/{id}`        | GET        | Admin                           | Detalles de un usuario específico              | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found       |
| Crea un nuevo usuario                        | `POST /api/version/users`            | POST       | Admin                           | Registra un nuevo usuario                      | 201 Created      | 400 Bad Request, 401 Unauthorized, 403 Forbidden     |
| Actualiza un usuario                         | `PUT /api/version/users/{id}`        | PUT        | Admin                           | Actualiza un usuario existente                 | 200 OK           | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Elimina un usuario                           | `DELETE /api/version/users/{id}`     | DELETE     | Admin                           | Elimina un usuario existente                   | 204 No Content   | 401 Unauthorized, 403 Forbidden, 404 Not Found       |

#### Endpoints de Gestión de Perfil y Pedidos de Usuario

| Endpoint                                      | URL                                      | HTTP Verbo | AUTH                            | Descripción                                    | HTTP Status Code | Otras Salidas                                        |
|-----------------------------------------------|------------------------------------------|------------|---------------------------------|------------------------------------------------|------------------|------------------------------------------------------|
| Obtiene perfil del usuario autenticado        | `GET /api/version/users/me/profile`      | GET        | User                            | Detalles del perfil del usuario autenticado    | 200 OK           | 401 Unauthorized, 403 Forbidden                       |
| Actualiza perfil del usuario autenticado      | `PUT /api/version/users/me/profile`      | PUT        | User                            | Actualiza el perfil del usuario autenticado    | 200 OK           | 400 Bad Request, 401 Unauthorized, 403 Forbidden      |
| Elimina perfil del usuario autenticado        | `DELETE /api/version/users/me/profile`   | DELETE     | User                            | Elimina el perfil del usuario autenticado      | 204 No Content   | 401 Unauthorized, 403 Forbidden                       |
| Obtiene pedidos del usuario autenticado       | `GET /api/version/users/me/pedidos`      | GET        | User                            | Lista pedidos del usuario autenticado          | 200 OK           | 401 Unauthorized, 403 Forbidden                       |
| Obtiene un pedido del usuario por ID          | `GET /api/version/users/me/pedidos/{id}` | GET        | User                            | Detalles de un pedido específico               | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found       |
| Crea un pedido para el usuario autenticado    | `POST /api/version/users/me/pedidos`     | POST       | User                            | Crea un nuevo pedido para el usuario           | 201 Created      | 400 Bad Request, 401 Unauthorized, 403 Forbidden     |
| Actualiza un pedido del usuario               | `PUT /api/version/users/me/pedidos/{id}` | PUT        | User                            | Actualiza un pedido existente del usuario      | 200 OK           | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Elimina un pedido del usuario                 | `DELETE /api/version/users/me/pedidos/{id}` | DELETE | User                            | Elimina un pedido existente del usuario        | 204 No Content   | 401 Unauthorized, 403 Forbidden, 404 Not Found       |

### Pedidos

La sección de pedidos de la API proporciona funcionalidades para gestionar los pedidos de la Panadería, permitiendo operaciones como la creación, consulta, actualización y eliminación de pedidos.

#### Endpoints de Gestión de Pedidos

| Endpoint                                    | URL                                    | HTTP Verbo | AUTH                            | Descripción                                        | HTTP Status Code | Otras Salidas                                          |
|---------------------------------------------|----------------------------------------|------------|---------------------------------|----------------------------------------------------|------------------|--------------------------------------------------------|
| Obtiene todos los pedidos                   | `GET /api/version/pedidos`             | GET        | Admin                           | Lista todos los pedidos con paginación y ordenación| 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Obtiene un pedido por su ID                 | `GET /api/version/pedidos/{id}`        | GET        | Admin                           | Detalles de un pedido específico por su ID         | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Crea un nuevo pedido                        | `POST /api/version/pedidos`            | POST       | Admin                           | Registra un nuevo pedido                           | 201 Created      | 400 Bad Request, 401 Unauthorized, 403 Forbidden       |
| Actualiza un pedido                         | `PUT /api/version/pedidos/{id}`        | PUT        | Admin                           | Actualiza un pedido existente                      | 200 OK           | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Elimina un pedido                           | `DELETE /api/version/pedidos/{id}`     | DELETE     | Admin                           | Elimina un pedido existente                        | 204 No Content   | 401 Unauthorized, 403 Forbidden, 404 Not Found         |

#### Endpoints de Gestión de Pedidos por Usuario

| Endpoint                                    | URL                                        | HTTP Verbo | AUTH                            | Descripción                                        | HTTP Status Code | Otras Salidas                                          |
|---------------------------------------------|--------------------------------------------|------------|---------------------------------|----------------------------------------------------|------------------|--------------------------------------------------------|
| Obtiene pedidos de un usuario específico    | `GET /api/version/pedidos/user/{id}`       | GET        | Admin/User                      | Lista los pedidos de un usuario específico         | 200 OK           | 401 Unauthorized, 403 Forbidden, 404 Not Found         |
| Crea un pedido para un usuario              | `POST /api/version/pedidos/user/{id}`      | POST       | Admin/User                      | Crea un pedido para un usuario específico          | 201 Created      | 400 Bad Request, 401 Unauthorized, 403 Forbidden       |
| Actualiza un pedido de un usuario           | `PUT /api/version/pedidos/user/{id}/{idPedido}` | PUT    | Admin/User                      | Actualiza un pedido de un usuario específico       | 200 OK           | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| Elimina un pedido de un usuario             | `DELETE /api/version/pedidos/user/{id}/{idPedido}` | DELETE | Admin/User                      | Elimina un pedido de un usuario específico         | 204 No Content   | 401 Unauthorized, 403 Forbidden, 404 Not Found         |



## Pruebas Tecnologias usadas

Para garantizar la calidad y el correcto funcionamiento de nuestra API, hemos implementado una serie de pruebas utilizando JUnit y Mockito. Estas herramientas nos permiten realizar pruebas unitarias y de integración de manera eficiente y efectiva.

## Dependencias Usadas en el proyecto 

Nuestro proyecto utiliza una serie de dependencias para facilitar diversas funcionalidades, desde la persistencia de datos hasta la comunicación en tiempo real. A continuación, se detallan las principales dependencias utilizadas:

### Spring Boot Starters

- **spring-boot-starter-data-jpa**: Proporciona soporte para la persistencia de datos utilizando Java Persistence API (JPA).
- **spring-boot-starter-web**: Ofrece todas las dependencias necesarias para construir aplicaciones web, incluyendo RESTful aplicaciones usando Spring MVC.
- **spring-boot-starter-validation**: Permite la validación de datos de entrada con el framework de validación de Spring.
- **spring-boot-starter-cache**: Facilita la implementación de caché en aplicaciones Spring Boot.

### Negociación de Contenido

- **jackson-dataformat-xml**: Permite que la API soporte tanto JSON como XML en las respuestas, facilitando la negociación de contenido.

### WebSockets

- **spring-boot-starter-websocket**: Proporciona soporte para características de comunicación en tiempo real a través de WebSockets.

### Otras Dependencias

- **lombok**: Utilizada para minimizar el código repetitivo en Java mediante anotaciones. Se incluye como dependencia `compileOnly` y `annotationProcessor`.
- **h2**: Una base de datos en memoria utilizada para pruebas. Se incluye como dependencia `runtimeOnly`.
- **spring-boot-starter-test**: Proporciona herramientas para realizar pruebas unitarias y de integración en aplicaciones Spring Boot.


##  Agradecimientos

Queremos expresar nuestro agradecimiento a todos los integrantes del equipo de este proyecto:

- Joselyn Obando
- Laura Garrido
- Kevin Bermudez
- Jorge Cruz
- Miguel Zanotto

## Contacto

Para cualquier consulta o soporte relacionado con este proyecto, por favor contacta a:

- GitHub: [Panaderia repo]( https://github.com/Alonso2002-jpg/TeamProjectPanaderia-2DAW.git)
