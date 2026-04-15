# Supermercado API

API REST para la gestión de un supermercado (productos, sucursales y ventas), construida con **Spring Boot 4** y **Java 25**. Proyecto de portfolio orientado a demostrar buenas prácticas, principios SOLID, clean code y un stack moderno de desarrollo backend.

---

## Finalidad del proyecto

Este proyecto fue desarrollado con fines de **portfolio profesional** y tiene como objetivo demostrar dominio en:

- Arquitectura por capas con separación clara de responsabilidades
- Principios **SOLID** y buenas prácticas de **clean code**
- Testing completo en todos los niveles (unitarios, integración, controladores, repositorios)
- Configuración profesional con perfiles de entorno, variables externalizadas y CI/CD
- API REST bien diseñada con validación, paginación y manejo global de errores

---

## Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 25 | Lenguaje principal |
| **Spring Boot** | 4.0.2 | Framework backend |
| **Spring Data JPA** | (BOM) | Persistencia con Hibernate |
| **Spring Validation** | (BOM) | Bean Validation (Jakarta) en DTOs |
| **Spring Actuator** | (BOM) | Health checks y métricas |
| **PostgreSQL** | (BOM) | Base de datos en producción |
| **H2** | (BOM) | Base de datos en memoria para desarrollo y tests |
| **MapStruct** | 1.6.3 | Mapeo entidad-DTO con generación en compilación |
| **Lombok** | (BOM) | Reducción de boilerplate (`@RequiredArgsConstructor`, `@Slf4j`, `@Builder`) |
| **SpringDoc OpenAPI** | 3.0.2 | Swagger UI y documentación OpenAPI 3 |
| **JUnit 5** | (BOM) | Framework de testing |
| **Mockito** | (BOM) | Mocking en tests unitarios |
| **JaCoCo** | 0.8.14 | Cobertura de código |
| **GitHub Actions** | - | CI/CD (build + tests automáticos) |
| **Maven** | 3.9.12 | Gestión de dependencias y build |

---

## Buenas prácticas aplicadas

### Principios SOLID

- **SRP**: cada clase tiene una única responsabilidad (controlador solo rutea, servicio contiene la lógica, repositorio accede a datos)
- **OCP**: jerarquía de excepciones extensible (`BusinessException` -> `ResourceNotFoundException`, `BadRequestException`)
- **LSP**: servicios implementan interfaces (`IProductoService`, `ISucursalService`, `IVentaService`)
- **ISP**: interfaces de servicio con contratos específicos por dominio
- **DIP**: inyección por constructor con `@RequiredArgsConstructor`; controladores dependen de interfaces, no de implementaciones

### Clean Code

- Convención de paquetes en minúsculas (`exception` en vez de `Exception`)
- Nombres descriptivos y consistentes en castellano
- `BigDecimal` para todos los valores monetarios (precios, totales, subtotales)
- Sin `@Autowired` en campos: constructor injection en todas las clases
- `@Transactional` en todos los métodos de servicio (`readOnly = true` para lecturas)
- Logging estructurado con `@Slf4j` en operaciones críticas

### Validación y manejo de errores

- **Bean Validation** con anotaciones Jakarta (`@NotBlank`, `@NotNull`, `@Positive`, `@Min`) en todos los DTOs
- `@Valid` en todos los endpoints que reciben `@RequestBody`
- **`@RestControllerAdvice`** global (`GlobalExceptionHandler`) que captura:
  - `ResourceNotFoundException` -> `404 Not Found`
  - `BadRequestException` / `IllegalArgumentException` -> `400 Bad Request`
  - `MethodArgumentNotValidException` -> `400` con detalle de errores por campo
  - `Exception` genérica -> `500 Internal Server Error`
- Respuestas de error estandarizadas con DTO `ApiError` (timestamp, status, error, message, path, details)

### Mapeo con MapStruct

- Interfaces `ProductoMapper`, `SucursalMapper`, `VentaMapper` con `@Mapper(componentModel = "spring")`
- Integración Lombok + MapStruct configurada en `maven-compiler-plugin`
- Cálculo automático de subtotales y totales en `VentaMapper`

### Paginación

- Todos los endpoints de listado aceptan paginación: `?page=0&size=20&sort=nombre,asc`
- Retornan `Page<T>` con metadata (totalElements, totalPages, etc.)
- Endpoints `GET /{id}` disponibles para cada recurso

---

## Estructura del proyecto

```
Proyecto-Backend-Java/
├── .github/workflows/ci.yml          # Pipeline CI (GitHub Actions)
├── .env.example                       # Variables de entorno documentadas
├── pom.xml
├── mvnw / mvnw.cmd
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── PruebaTecSupermercadoApplication.java
│   │   │   ├── config/
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── ProductoController.java
│   │   │   │   ├── SucursalController.java
│   │   │   │   └── VentaController.java
│   │   │   ├── dto/
│   │   │   │   ├── ApiError.java
│   │   │   │   ├── ProductoDTO.java
│   │   │   │   ├── SucursalDTO.java
│   │   │   │   ├── VentaDTO.java
│   │   │   │   └── DetalleVentaDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BadRequestException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── mapper/
│   │   │   │   ├── ProductoMapper.java
│   │   │   │   ├── SucursalMapper.java
│   │   │   │   └── VentaMapper.java
│   │   │   ├── model/
│   │   │   │   ├── Producto.java
│   │   │   │   ├── Sucursal.java
│   │   │   │   ├── Venta.java
│   │   │   │   └── DetalleVenta.java
│   │   │   ├── otro/controller/
│   │   │   │   └── OtroApiController.java
│   │   │   ├── repository/
│   │   │   │   ├── ProductoRepository.java
│   │   │   │   ├── SucursalRepository.java
│   │   │   │   └── VentaRepository.java
│   │   │   └── service/
│   │   │       ├── IProductoService.java
│   │   │       ├── ISucursalService.java
│   │   │       ├── IVentaService.java
│   │   │       ├── ProductoService.java
│   │   │       ├── SucursalService.java
│   │   │       └── VentaService.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-dev.yaml
│   │       └── application-prod.yaml
│   └── test/
│       ├── java/com/example/demo/
│       │   ├── PruebaTecSupermercadoApplicationTests.java
│       │   ├── controller/
│       │   │   ├── ProductoControllerTest.java
│       │   │   ├── SucursalControllerTest.java
│       │   │   └── VentaControllerTest.java
│       │   ├── integration/
│       │   │   └── SupermercadoIntegrationTest.java
│       │   ├── mapper/
│       │   │   ├── ProductoMapperTest.java
│       │   │   └── VentaMapperTest.java
│       │   ├── repository/
│       │   │   ├── ProductoRepositoryTest.java
│       │   │   ├── SucursalRepositoryTest.java
│       │   │   └── VentaRepositoryTest.java
│       │   └── service/
│       │       ├── ProductoServiceTest.java
│       │       ├── SucursalServiceTest.java
│       │       └── VentaServiceTest.java
│       └── resources/
│           └── application-test.yaml
└── target/
```

---

## Perfiles de configuración

| Perfil | Base de datos | DDL | Uso |
|--------|---------------|-----|-----|
| **dev** (default) | H2 en memoria | `create-drop` | Desarrollo local, sin instalar nada |
| **prod** | PostgreSQL | `validate` | Producción, requiere variables de entorno |
| **test** | H2 en memoria | `create-drop` | Ejecución de tests |

Las credenciales están externalizadas con variables de entorno y valores por defecto para desarrollo local. Ver `.env.example` para la lista completa.

---

## Tests

El proyecto cuenta con **68 tests** distribuidos en 4 niveles:

| Tipo | Anotación | Cantidad | Qué verifica |
|------|-----------|----------|--------------|
| **Unitarios (servicios)** | `@ExtendWith(MockitoExtension)` | 27 | Lógica de negocio con mocks de repositorios y mappers |
| **Repositorios** | `@DataJpaTest` | 9 | Persistencia JPA, queries custom, relaciones en cascada |
| **Controladores** | `@WebMvcTest` | 21 | Endpoints HTTP, status codes, validación de requests |
| **Integración** | `@SpringBootTest` | 6 | Flujo end-to-end completo con H2 |
| **Mappers** | `@SpringBootTest` | 5 | Conversión entidad-DTO, cálculo de totales |

---

## API REST

Base URL: `http://localhost:8080`

Documentación interactiva: `http://localhost:8080/swagger-ui.html`

### Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/supermercado/productos` | Listar productos (paginado) |
| GET | `/api/supermercado/productos/{id}` | Obtener producto por ID |
| POST | `/api/supermercado/productos` | Crear producto |
| PUT | `/api/supermercado/productos/{id}` | Actualizar producto |
| DELETE | `/api/supermercado/productos/{id}` | Eliminar producto |

### Sucursales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/supermercado/sucursales` | Listar sucursales (paginado) |
| GET | `/api/supermercado/sucursales/{id}` | Obtener sucursal por ID |
| POST | `/api/supermercado/sucursales` | Crear sucursal |
| PUT | `/api/supermercado/sucursales/{id}` | Actualizar sucursal |
| DELETE | `/api/supermercado/sucursales/{id}` | Eliminar sucursal |

### Ventas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/supermercado/ventas` | Listar ventas (paginado) |
| GET | `/api/supermercado/ventas/{id}` | Obtener venta por ID |
| POST | `/api/supermercado/ventas` | Crear venta con detalle |
| PUT | `/api/supermercado/ventas/{id}` | Actualizar venta |
| DELETE | `/api/supermercado/ventas/{id}` | Eliminar venta |

### Ejemplos de request

**Crear producto:**

```json
POST /api/supermercado/productos
{
  "nombre": "Leche",
  "categoria": "Lácteos",
  "precio": 75.50,
  "cantidad": 100
}
```

**Crear sucursal:**

```json
POST /api/supermercado/sucursales
{
  "nombre": "Sucursal Centro",
  "direccion": "Av. Principal 123"
}
```

**Crear venta (el total se calcula automáticamente):**

```json
POST /api/supermercado/ventas
{
  "fecha": "2025-02-20",
  "estado": "COMPLETADA",
  "idSucursal": 1,
  "detalle": [
    {
      "nombreProd": "Leche",
      "cantProd": 2,
      "precio": 75.50
    }
  ]
}
```

**Respuesta de error (validación):**

```json
{
  "timestamp": "2025-02-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validación",
  "path": "/api/supermercado/productos",
  "details": [
    "nombre: El nombre es obligatorio",
    "precio: El precio debe ser mayor a cero"
  ]
}
```

**Paginación:**

```
GET /api/supermercado/productos?page=0&size=10&sort=nombre,asc
```

---

## Inicio rápido

### Requisitos

- **Java 25**
- **Maven 3.6+** (o usar el wrapper incluido `mvnw` / `mvnw.cmd`)

### Ejecutar en modo desarrollo (H2 en memoria)

```bash
./mvnw spring-boot:run
```

No necesita base de datos externa. El perfil `dev` se activa por defecto y usa H2 en memoria.

### Ejecutar con PostgreSQL (producción)

**Linux / macOS:**

```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/supermercadodb"
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=tu_password
./mvnw spring-boot:run
```

**Windows (PowerShell):**

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/supermercadodb"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="tu_password"
.\mvnw.cmd spring-boot:run
```

---

## Comandos útiles

| Comando | Descripción |
|---------|-------------|
| `./mvnw spring-boot:run` | Ejecutar la aplicación (perfil dev) |
| `./mvnw test` | Ejecutar los 68 tests |
| `./mvnw clean verify` | Build completo + tests + reporte JaCoCo |
| `./mvnw clean package -DskipTests` | Empaquetar JAR sin ejecutar tests |

### Links locales (con la aplicación corriendo)

| Recurso | URL |
|---------|-----|
| API base | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| Actuator Health | http://localhost:8080/actuator/health |
| Actuator Info | http://localhost:8080/actuator/info |
| Actuator Metrics | http://localhost:8080/actuator/metrics |
| H2 Console (dev) | http://localhost:8080/h2-console |
| JaCoCo Report | `target/site/jacoco/index.html` (después de `mvn verify`) |

---

## CI/CD

El proyecto incluye un pipeline de GitHub Actions (`.github/workflows/ci.yml`) que se ejecuta en cada push y PR a `main`:

1. Checkout del código
2. Setup de Java 25 (Temurin)
3. Cache de dependencias Maven
4. Build + ejecución de todos los tests con perfil `test`
5. Generación y upload del reporte de cobertura JaCoCo

---

## Variables de entorno

| Variable | Descripción | Default (dev) |
|----------|-------------|---------------|
| `SPRING_PROFILES_ACTIVE` | Perfil activo | `dev` |
| `SPRING_DATASOURCE_URL` | URL JDBC | `jdbc:h2:mem:supermercadodb` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de BD | `sa` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de BD | *(vacío)* |
| `SERVER_PORT` | Puerto del servidor | `8080` |

---

## Licencia

Proyecto de demostración con fines técnicos y de portfolio.
