# 🛒 Prueba Técnica – Supermercado API

API REST para la **gestión de un supermercado**, incluyendo **productos, sucursales y ventas**.  
Desarrollada con **Spring Boot 4** y **Java 25**.

---

## 🚀 Inicio rápido

```bash
# Con Docker (recomendado)
docker-compose up -d

# La API estará disponible en http://localhost:8080
```

---

## 📑 Índice

- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Archivos de Configuración](#-archivos-de-configuración)
- [Dependencias](#-dependencias)
- [Código Fuente](#-código-fuente)
- [Tests](#-tests)
- [Documentación Existente](#-documentación-existente)
- [Scripts de Build y Ejecución](#-scripts-de-build-y-ejecución)
- [Configuraciones Especiales](#-configuraciones-especiales)
- [Requisitos](#-requisitos)
- [Instrucciones de Uso](#-instrucciones-de-uso)
- [API REST](#-api-rest)

---

## 📁 Estructura del Proyecto

```
c:\Proyectos\Java\
├── docker-compose.yml              # Orquestación Docker (app + MySQL)
├── README.md                       # Este archivo
└── Proyecto-Backend-Java/         # Aplicación Spring Boot
    ├── pom.xml                    # Configuración Maven
    ├── Dockerfile                 # Imagen Docker de la aplicación
    ├── README.md                  # Documentación del submódulo
    ├── mvnw                       # Maven Wrapper (Unix)
    ├── mvnw.cmd                   # Maven Wrapper (Windows)
    ├── .gitignore
    ├── .mvn/
    │   └── wrapper/
    │       ├── maven-wrapper.properties
    │       └── maven-wrapper.jar   # (excluido en .gitignore)
    ├── .idea/                     # Configuración IntelliJ IDEA
    ├── src/
    │   ├── main/
    │   │   ├── java/com/example/demo/
    │   │   │   ├── PruebaTecSupermercadoApplication.java  # Punto de entrada
    │   │   │   ├── controller/    # Controladores REST
    │   │   │   ├── dto/           # DTOs request/response
    │   │   │   ├── Exception/     # Excepciones personalizadas
    │   │   │   ├── mapper/        # Entity ↔ DTO
    │   │   │   ├── model/         # Entidades JPA
    │   │   │   ├── repository/    # Repositorios Spring Data JPA
    │   │   │   └── service/       # Lógica de negocio
    │   │   └── resources/
    │   │       ├── application.yaml
    │   │       └── utils/
    │   │           └── docker-compose(copia).txt
    │   └── test/
    │       └── java/com/example/demo/
    │           └── PruebaTecSupermercadoApplicationTests.java
    └── target/                    # Artefactos de compilación (generado)
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
| `spring.datasource.url` | `${DB_URL}` (variable de entorno) |
| `spring.datasource.username` | `${DB_USER_NAME}` |
| `spring.datasource.password` | `${DB_PASSWORD}` |
| `spring.jpa.hibernate.ddl-auto` | update |
| `spring.jpa.show-sql` | true |
| `spring.jpa.properties.hibernate.dialect` | `${DB_PLATFORM}` |
| `server.port` | 8080 |

### 3. `docker-compose.yml` (raíz del proyecto)

- **Servicio `pruebatecsupermercado`**: Aplicación Spring Boot
  - Puerto: 8080
  - Memoria: 512MB
  - Depende de MySQL con healthcheck

- **Servicio `pruebatecsuper`**: MySQL 9.6.0
  - Puerto expuesto: 3307 (host) → 3306 (contenedor)
  - Base de datos: `mydb`
  - Host interno Docker: `pruebatecsuper` (usado en `DB_URL` por la app)
  - Healthcheck para esperar a que MySQL esté listo

### 4. `Dockerfile`

- Imagen base: `eclipse-temurin:25-jdk-alpine`
- JAR: `target/demo-0.0.1.jar`
- Puerto: 8080

### 5. Maven Wrapper (`.mvn/wrapper/maven-wrapper.properties`)

- Wrapper: 3.3.4
- Maven: 3.9.12

---

## 📦 Dependencias

### Producción

| Dependencia | Propósito |
|-------------|-----------|
| `spring-boot-starter-data-jpa` | Persistencia JPA/Hibernate |
| `spring-boot-starter-webmvc` | API REST (Spring MVC) |
| `mysql-connector-j` (9.6.0) | Driver MySQL (scope: runtime) |
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
cd Proyecto-Backend-Java
mvn test
```

- **Base de datos en tests:** H2 en memoria (configurado por los starters de test).

---

## 📚 Documentación Existente

- **`Proyecto-Backend-Java/README.md`**: Documentación básica del submódulo con requisitos, tecnologías, configuración, ejecución, API y tests. Incluye algunas líneas pegadas que conviene formatear.
- **`Proyecto-Backend-Java/src/main/resources/utils/docker-compose(copia).txt`**: Copia del `docker-compose.yml` de la raíz.

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
| `DB_URL` | URL JDBC | `jdbc:mysql://localhost:3306/pruebatecsuper?createDatabaseIfNotExist=true&serverTimezone=UTC` |
| `DB_USER_NAME` | Usuario de BD | `root` |
| `DB_PASSWORD` | Contraseña de BD | `1234` |
| `DB_PLATFORM` | Dialecto Hibernate | `org.hibernate.dialect.MySQLDialect` |

**Nota sobre Docker Compose:** El `docker-compose.yml` de la raíz no incluye `DB_PLATFORM` porque Hibernate puede inferir el dialecto en algunos entornos. Si al levantar con `docker-compose up` aparece un error relacionado con el dialecto, añade `DB_PLATFORM: org.hibernate.dialect.MySQLDialect` al bloque `environment` del servicio `pruebatecsupermercado`.

### Lombok

- Procesador de anotaciones configurado en `maven-compiler-plugin` para Lombok.
- Lombok excluido del JAR final en `spring-boot-maven-plugin`.

### JPA/Hibernate

- `ddl-auto: update` para crear/actualizar esquema automáticamente.
- `show-sql: true` para depuración (log de SQL).

---

## 📦 Requisitos

- **Java 25**
- **Maven 3.6+** (o Maven Wrapper)
- **MySQL 8.x** para producción
- **Docker** y **Docker Compose** (opcional, para ejecución con contenedores)

---

## ▶️ Instrucciones de Uso

### Con Maven (sin Docker)

1. Arrancar MySQL.
2. Configurar variables de entorno:

**Linux / macOS (bash):**
```bash
export DB_URL="jdbc:mysql://localhost:3306/pruebatecsuper?createDatabaseIfNotExist=true&serverTimezone=UTC"
export DB_USER_NAME=root
export DB_PASSWORD=1234
export DB_PLATFORM=org.hibernate.dialect.MySQLDialect
```

**Windows (PowerShell):**
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/pruebatecsuper?createDatabaseIfNotExist=true&serverTimezone=UTC"
$env:DB_USER_NAME="root"
$env:DB_PASSWORD="1234"
$env:DB_PLATFORM="org.hibernate.dialect.MySQLDialect"
```

3. Compilar y ejecutar:

```bash
cd Proyecto-Backend-Java
mvn clean install
mvn spring-boot:run
```

Puerto por defecto: **8080**.

### Con Docker Compose

Desde la raíz del proyecto:

```bash
docker-compose up -d
```

- API: http://localhost:8080  
- MySQL (host): puerto **3307**

La aplicación espera a que MySQL pase el healthcheck antes de iniciar.

### Solo la aplicación en Docker

```bash
cd Proyecto-Backend-Java
mvn clean package -DskipTests
docker build -t pruebatecsupermercado .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/pruebatecsuper \
  -e DB_USER_NAME=root \
  -e DB_PASSWORD=1234 \
  -e DB_PLATFORM=org.hibernate.dialect.MySQLDialect \
  pruebatecsupermercado
```

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
