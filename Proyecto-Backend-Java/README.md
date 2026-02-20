# 🛒 Prueba Técnica – Supermercado API

API REST para la **gestión de un supermercado**, incluyendo **productos, sucursales y ventas**.  
Desarrollada con **Spring Boot 4** y **Java 25**.

---

## 📑 Contenido

- [Requisitos](#-requisitos)
- [Tecnologías](#-tecnologías)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
  - [Con Maven](#con-maven-sin-docker)
  - [Con Docker Compose](#con-docker-compose)
  - [Solo la app en Docker](#solo-la-aplicación-en-docker)
- [API REST](#-api-rest)
- [Tests](#-tests)
- [Licencia](#-licencia)

---

## 📦 Requisitos

- **Java 25**
- **Maven 3.6+**
- **MySQL 8.x**  
  _(opcional: Docker con `docker-compose`)_

---

## 🧰 Tecnologías

- Spring Boot 4.0.2
- Spring Data JPA
- Spring Web MVC
- MySQL (producción)
- H2 (tests)
- Lombok
- Maven

---

## 🗂️ Estructura del proyecto

src/main/java/com/example/demo/
├── PruebaTecSupermercadoApplication.java # Punto de entrada
├── controller/ # Controladores REST
├── dto/ # DTOs request/response
├── exception/ # Manejo de excepciones
├── mapper/ # Entity ↔ DTO
├── model/ # Entidades JPA
├── repository/ # Repositorios JPA
└── service/ # Lógica de negocio


---

## ⚙️ Configuración

La aplicación se configura mediante **variables de entorno**:

| Variable        | Descripción             | Ejemplo |
|-----------------|-------------------------|---------|
| `DB_URL`        | URL JDBC                | `jdbc:mysql://localhost:3306/pruebatecsuper` |
| `DB_USER_NAME`  | Usuario DB              | `root` |
| `DB_PASSWORD`   | Contraseña DB           | `1234` |
| `DB_PLATFORM`   | Dialecto Hibernate      | `org.hibernate.dialect.MySQLDialect` |

### 🔧 Ejemplo local (MySQL)

```bash
export DB_URL="jdbc:mysql://localhost:3306/pruebatecsuper?createDatabaseIfNotExist=true&serverTimezone=UTC"
export DB_USER_NAME=root
export DB_PASSWORD=1234
export DB_PLATFORM=org.hibernate.dialect.MySQLDialect
📌 Puerto por defecto: 8080

▶️ Ejecución
Con Maven (sin Docker)
Asegúrate de tener MySQL corriendo.

Configura las variables de entorno.

Ejecuta:

cd Proyecto-Backend-Java
mvn clean install
mvn spring-boot:run
Con Docker Compose
Desde la raíz del proyecto:

docker-compose up -d
API → http://localhost:8080

MySQL → puerto 3307 (host)

ℹ️ La aplicación espera a que MySQL esté listo antes de iniciar.

Solo la aplicación en Docker
mvn clean package -DskipTests
docker build -t pruebatecsupermercado .

docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/pruebatecsuper \
  -e DB_USER_NAME=root \
  -e DB_PASSWORD=1234 \
  -e DB_PLATFORM=org.hibernate.dialect.MySQLDialect \
  pruebatecsupermercado
🌐 API REST
Base URL: http://localhost:8080

📦 Productos — /api/productos
Método	Endpoint	Descripción
GET	/api/productos	Listar productos
POST	/api/productos	Crear producto
PUT	/api/productos/{id}	Actualizar producto
DELETE	/api/productos/{id}	Eliminar producto
🏪 Sucursales — /api/sucursales
Método	Endpoint	Descripción
GET	/api/sucursales	Listar sucursales
POST	/api/sucursales	Crear sucursal
PUT	/api/sucursales/{id}	Actualizar sucursal
DELETE	/api/sucursales/{id}	Eliminar sucursal
💰 Ventas — /api/ventas
Método	Endpoint	Descripción
GET	/api/ventas	Listar ventas
POST	/api/ventas	Crear venta
PUT	/api/ventas/{id}	Actualizar venta
DELETE	/api/ventas/{id}	Eliminar venta
📌 Los cuerpos de las peticiones son JSON y usan DTOs.

🧪 Tests
mvn test
Spring Boot Test

Base de datos H2 en memoria

📄 Licencia
Proyecto de demostración con fines técnicos y educativos.


---

Si quieres, en el próximo mensaje puedo:
- Ajustarlo a **README para entrevista técnica**
- Agregar **ejemplos de requests JSON**
- Hacer una versión **más corta tipo portfolio**
- O meterle badges y screenshots 😎