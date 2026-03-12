# 🛒 Prueba Técnica – Supermercado API (monolito por contexto)

API REST para la **gestión de un supermercado**, incluyendo **productos, sucursales y ventas**.  
Desarrollada con **Spring Boot 4** y **Java 25**.

El proyecto está organizado como **un solo despliegue (monolito)** con **APIs separadas por contexto**:
- **Supermercado (actual):** todo lo existente bajo `/api/supermercado/*`
- **Otros / futuras APIs:** mejoras o APIs de otros proyectos bajo `/api/otro/*`

Puedes distinguir el contexto por **URL** o por **grupos en Swagger UI**.

---

## 🚀 Inicio rápido

```bash
mvn spring-boot:run
```

- **API:** http://localhost:8080  
- **Swagger UI:** http://localhost:8080/swagger-ui.html (selector de grupo: *supermercado* | *otros*)

---

## 📑 Índice

- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Archivos de Configuración](#-archivos-de-configuración)
- [Dependencias](#-dependencias)
- [Código Fuente](#-código-fuente)
- [Tests](#-tests)
- [Scripts de Build y Ejecución](#-scripts-de-build-y-ejecución)
- [Configuraciones Especiales](#-configuraciones-especiales)
- [Requisitos](#-requisitos)
- [Instrucciones de Uso](#-instrucciones-de-uso)
- [API REST](#-api-rest)
- [Separación por contexto (monolito)](#-separación-por-contexto-monolito)

---

## 🔀 Separación por contexto (monolito)

Un solo despliegue expone varias “APIs” diferenciadas por **prefijo de URL** y por **grupo en Swagger**:

| Contexto      | Prefijo URL            | Grupo en Swagger | Uso                          |
|---------------|------------------------|------------------|------------------------------|
| **Supermercado** | `/api/supermercado/*` | `supermercado`   | Lo que ya existe (ventas, productos, sucursales) |
| **Otros**     | `/api/otro/*`          | `otros`          | Futuras mejoras o APIs de otros proyectos       |

- **Por URL:** las peticiones van a `http://localhost:8080/api/supermercado/...` o `http://localhost:8080/api/otro/...`.
- **Por Swagger:** en http://localhost:8080/swagger-ui.html el desplegable de grupo permite elegir *supermercado* o *otros*.

Para **añadir APIs de otro proyecto o mejoras futuras**: crea controladores bajo el paquete `com.example.demo.otro` (o un subpaquete como `otro.xxx`) y usa `@RequestMapping("/api/otro/...")` para que queden en el mismo contexto y en el grupo *otros* en Swagger.

---

## 📁 Estructura del Proyecto

El proyecto es la raíz del repositorio (no hay subcarpeta intermedia):

```
Proyecto-Backend-Java/            # Raíz del repo (este proyecto)
├── README.md
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
├── .mvn/
│   └── wrapper/
│       ├── maven-wrapper.properties
│       └── maven-wrapper.jar
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── PruebaTecSupermercadoApplication.java
│   │   │   ├── config/           # OpenAPI (Swagger) por contexto
│   │   │   ├── controller/       # Supermercado: ventas, productos, sucursales
│   │   │   ├── dto/
│   │   │   ├── Exception/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── otro/             # Futuras APIs (ej. otro.controller)
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── schema-fix-drop-tables.sql
│   └── test/
│       └── java/com/example/demo/
│           └── PruebaTecSupermercadoApplicationTests.java
└── target/                        # Generado por Maven
```

---

## ⚙️ Archivos de Configuración

### 1. `pom.xml` (Maven)

| Propiedad | Valor |
|-----------|-------|
| GroupId | `com.example` |
| ArtifactId | `demo` |
| Versión | `0.0.1` |
| Nombre | PruebaTecSupermercado |
| Parent | Spring Boot 4.0.2 |
| Java | 25 |

### 2. `application.yaml`

Configuración de la aplicación Spring Boot:

| Propiedad | Descripción |
|-----------|-------------|
| `spring.application.name` | pruebatecsupermercado |
| `spring.datasource.url` | URL JDBC PostgreSQL (o variable de entorno) |
| `spring.datasource.username` | Usuario de BD |
| `spring.datasource.password` | Contraseña de BD |
| `spring.jpa.hibernate.ddl-auto` | update |
| `spring.jpa.show-sql` | true |
| `spring.jpa.properties.hibernate.dialect` | org.hibernate.dialect.PostgreSQLDialect |
| `server.port` | 8080 |

### 3. Maven Wrapper (`.mvn/wrapper/maven-wrapper.properties`)

- Wrapper: 3.3.4
- Maven: 3.9.12

---

## 📦 Dependencias

### Producción

| Dependencia | Propósito |
|-------------|-----------|
| `spring-boot-starter-data-jpa` | Persistencia JPA/Hibernate |
| `spring-boot-starter-webmvc` | API REST (Spring MVC) |
| `postgresql` | Driver PostgreSQL (scope: runtime) |
| `lombok` | Reducción de boilerplate (getters, builders, etc.) |
| `h2` | Base de datos en memoria para tests (scope: runtime) |
| `springdoc-openapi-starter-webmvc-ui` | Swagger UI y OpenAPI 3; grupos por contexto (supermercado / otros) |

### Tests

| Dependencia | Propósito |
|-------------|-----------|
| `spring-boot-starter-data-jpa-test` | Tests de repositorios JPA (`@DataJpaTest`) |
| `spring-boot-starter-webmvc-test` | Tests de controladores (`@WebMvcTest`, MockMvc) |

---

## 💻 Código Fuente

### Paquete principal: `com.example.demo`

#### 1. Punto de entrada

- **`PruebaTecSupermercadoApplication.java`**: Clase principal con `@SpringBootApplication`, arranca el contexto Spring.

#### 2. Modelo (`model/`)

| Clase | Descripción |
|-------|-------------|
| `Producto` | Entidad: id, nombre, categoria, precio, cantidad |
| `Sucursal` | Entidad: id, nombre, direccion |
| `Venta` | Entidad: id, fecha, estado, total, sucursal (ManyToOne), detalle (OneToMany) |
| `DetalleVenta` | Entidad: id, cantProd, precio, venta (ManyToOne), producto (ManyToOne) |

#### 3. DTOs (`dto/`)

| Clase | Descripción |
|-------|-------------|
| `ProductoDTO` | id, nombre, categoria, precio, cantidad |
| `SucursalDTO` | id, nombre, direccion |
| `VentaDTO` | id, fecha, estado, IdSucursal, detalle, total |
| `DetalleVentaDTO` | id, nombreProd, cantProd, precio, subtotal |

#### 4. Repositorios (`repository/`)

| Interfaz | Entidad | Métodos adicionales |
|----------|---------|---------------------|
| `ProductoRepository` | Producto | `findByNombre(String)` |
| `SucursalRepository` | Sucursal | — |
| `VentaRepository` | Venta | — |

#### 5. Servicios (`service/`)

| Interfaz | Implementación | Funcionalidad |
|----------|----------------|---------------|
| `IProductoService` | `ProductoService` | CRUD de productos |
| `ISucursalService` | `SucursalService` | CRUD de sucursales |
| `IVentaService` | `VentaService` | CRUD de ventas; creación con detalle y validaciones |

**Validaciones en `VentaService`:**
- Venta no nula
- Al menos un producto en el detalle
- Sucursal y productos existentes
- Lanza `NotFoundException` cuando no se encuentran recursos

#### 6. Controladores (`controller/`)

**Contexto Supermercado** (base path `/api/supermercado`):

| Controlador | Base path | Operaciones |
|-------------|-----------|-------------|
| `ProductoController` | `/api/supermercado/productos` | GET, POST, PUT, DELETE |
| `SucursalController` | `/api/supermercado/sucursales` | GET, POST, PUT, DELETE |
| `VentaController` | `/api/supermercado/ventas` | GET, POST, PUT, DELETE |

**Contexto Otros / futuras APIs** (base path `/api/otro`):

| Controlador | Base path | Descripción |
|-------------|-----------|-------------|
| `otro.controller.OtroApiController` | `/api/otro` | Placeholder; agregar aquí nuevas APIs de otros proyectos |

#### 6.1. Configuración OpenAPI (`config/`)

- **`OpenApiConfig`**: Define grupos de Swagger **supermercado** y **otros** para filtrar por contexto en la UI.

#### 7. Mapper (`mapper/`)

- **`Mapper`**: Métodos estáticos `toDTO()` para convertir `Producto`, `Sucursal` y `Venta` a sus DTOs.

#### 8. Excepciones (`Exception/`)

- **`NotFoundException`**: `RuntimeException` para recursos no encontrados (productos, sucursales, ventas).

**Nota:** No existe un `@ControllerAdvice` global; las excepciones pueden propagarse como 500. Se podría añadir un manejador para devolver 404.

---

## 🧪 Tests

### Archivo de test

- **`PruebaTecSupermercadoApplicationTests`**: Test de contexto (`@SpringBootTest`) que verifica que la aplicación arranca correctamente.

### Ejecución

```bash
mvn test
```

- **Base de datos en tests:** H2 en memoria (configurado por los starters de test).

---

---

## 🔧 Scripts de Build y Ejecución

### Maven Wrapper

- **`mvnw`** / **`mvnw.cmd`**: Maven Wrapper para ejecutar Maven sin instalación previa.

### Comandos principales

| Comando | Descripción |
|---------|-------------|
| `mvn clean install` | Compila, ejecuta tests y empaqueta |
| `mvn spring-boot:run` | Ejecuta la aplicación |
| `mvn clean package -DskipTests` | Empaqueta sin ejecutar tests |
| `mvn test` | Ejecuta solo los tests |

---

## 🔒 Configuraciones Especiales

### Variables de entorno requeridas

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | URL JDBC | `jdbc:postgresql://localhost:5432/mydb` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de BD | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de BD | `1234` |

**Nota:** En `application.yaml` la base de datos puede configurarse también con variables de entorno. El dialecto Hibernate para PostgreSQL es `org.hibernate.dialect.PostgreSQLDialect` (ya configurado por defecto en la aplicación).

### Lombok

- Procesador de anotaciones configurado en `maven-compiler-plugin` para Lombok.
- Lombok excluido del JAR final en `spring-boot-maven-plugin`.

### JPA/Hibernate

- `ddl-auto: update` para crear/actualizar esquema automáticamente.
- `show-sql: true` para depuración (log de SQL).

**Si ves errores "there is no primary key for referenced table":** la base ya tenía tablas sin clave primaria (por un arranque anterior u otro origen). Ejecuta en tu base de datos el script `src/main/resources/schema-fix-drop-tables.sql` (en Supabase: SQL Editor) y reinicia la aplicación; Hibernate creará las tablas correctamente.

---

## 📦 Requisitos

- **Java 25**
- **Maven 3.6+** (o Maven Wrapper)
- **PostgreSQL 12+** para producción
---

## ▶️ Instrucciones de Uso

### Con Maven

1. Arrancar PostgreSQL (o usar la URL de Supabase configurada en `application.yaml`).
2. Configurar variables de entorno (opcional si usas los valores por defecto de `application.yaml`):

**Linux / macOS (bash):**
```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/mydb"
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=1234
```

**Windows (PowerShell):**
```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/mydb"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="1234"
```

3. Compilar y ejecutar (desde la raíz del repositorio):

```bash
mvn clean install
mvn spring-boot:run
```

Puerto por defecto: **8080**.

---

## 🌐 API REST

Base URL: **http://localhost:8080**

Todas las APIs del dominio actual están bajo el prefijo **`/api/supermercado`**. Las futuras o de otros proyectos, bajo **`/api/otro`**. En **Swagger UI** (http://localhost:8080/swagger-ui.html) puedes elegir el grupo *supermercado* o *otros* para ver solo ese contexto.

### Productos — `/api/supermercado/productos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/supermercado/productos` | Listar productos |
| POST | `/api/supermercado/productos` | Crear producto |
| PUT | `/api/supermercado/productos/{id}` | Actualizar producto |
| DELETE | `/api/supermercado/productos/{id}` | Eliminar producto |

### Sucursales — `/api/supermercado/sucursales`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/supermercado/sucursales` | Listar sucursales |
| POST | `/api/supermercado/sucursales` | Crear sucursal |
| PUT | `/api/supermercado/sucursales/{id}` | Actualizar sucursal |
| DELETE | `/api/supermercado/sucursales/{id}` | Eliminar sucursal |

### Ventas — `/api/supermercado/ventas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/supermercado/ventas` | Listar ventas |
| POST | `/api/supermercado/ventas` | Crear venta |
| PUT | `/api/supermercado/ventas/{id}` | Actualizar venta |
| DELETE | `/api/supermercado/ventas/{id}` | Eliminar venta |

### Otros / futuras APIs — `/api/otro`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/otro/info` | Info del contexto "otros" (ejemplo para nuevas APIs) |

Los cuerpos de las peticiones son JSON y utilizan los DTOs del proyecto.

### Ejemplo de producto (POST `/api/supermercado/productos`)

```json
{
  "nombre": "Leche",
  "categoria": "Lácteos",
  "precio": 75.25,
  "cantidad": 100
}
```

### Ejemplo de sucursal (POST `/api/supermercado/sucursales`)

```json
{
  "nombre": "Sucursal Centro",
  "direccion": "Av. Principal 123"
}
```

### Ejemplo de venta (POST `/api/supermercado/ventas`)

```json
{
  "fecha": "2025-02-20",
  "estado": "COMPLETADA",
  "IdSucursal": 1,
  "total": 150.50,
  "detalle": [
    {
      "nombreProd": "Leche",
      "cantProd": 2,
      "precio": 75.25
    }
  ]
}
```

**Nota:** Los productos se identifican por `nombreProd` en el detalle; deben existir previamente en la base de datos.

---

## 📄 Licencia

Proyecto de demostración con fines técnicos y educativos.
