# 🛒 Prueba Técnica – Supermercado API

API REST para la **gestión de un supermercado**, incluyendo **productos, sucursales y ventas**.  
Desarrollada con **Spring Boot 4** y **Java 25**.

---

## 🚀 Inicio rápido

```bash
mvn spring-boot:run
```

La API estará disponible en http://localhost:8080

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
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── Exception/
│   │   │   ├── mapper/
│   │   │   ├── model/
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

| Controlador | Base path | Operaciones |
|-------------|-----------|-------------|
| `ProductoController` | `/api/productos` | GET, POST, PUT, DELETE |
| `SucursalController` | `/api/sucursales` | GET, POST, PUT, DELETE |
| `VentaController` | `/api/ventas` | GET, POST, PUT, DELETE |

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

### Productos — `/api/productos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar productos |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |

### Sucursales — `/api/sucursales`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/sucursales` | Listar sucursales |
| POST | `/api/sucursales` | Crear sucursal |
| PUT | `/api/sucursales/{id}` | Actualizar sucursal |
| DELETE | `/api/sucursales/{id}` | Eliminar sucursal |

### Ventas — `/api/ventas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/ventas` | Listar ventas |
| POST | `/api/ventas` | Crear venta |
| PUT | `/api/ventas/{id}` | Actualizar venta |
| DELETE | `/api/ventas/{id}` | Eliminar venta |

Los cuerpos de las peticiones son JSON y utilizan los DTOs del proyecto.

### Ejemplo de producto (POST `/api/productos`)

```json
{
  "nombre": "Leche",
  "categoria": "Lácteos",
  "precio": 75.25,
  "cantidad": 100
}
```

### Ejemplo de sucursal (POST `/api/sucursales`)

```json
{
  "nombre": "Sucursal Centro",
  "direccion": "Av. Principal 123"
}
```

### Ejemplo de venta (POST `/api/ventas`)

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
