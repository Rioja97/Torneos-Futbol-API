## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 17 (o la versión que uses).
* **Framework:** Spring Boot 3.x.
* **Seguridad:** Spring Security + **JWT** (Autenticación basada en tokens).
* **Base de Datos:** MySQL + Spring Data JPA (Hibernate).
* **Build Tool:** Maven.
* **Gestión de Proyectos:** Jira (Metodologías Ágiles/Scrum).

## 🚀 Cómo ejecutar el proyecto localmente

### Requisitos previos
* Java 17+ (o tu versión)
* Maven
* MySQL Server (puerto 3306)

### Instalación
1. Clonar el repositorio:
   `git clone [TU_LINK_DE_GITHUB]`
2. Crear la base de datos en MySQL:
   `CREATE DATABASE torneofutbol;`
3. (Opcional) Configurar las variables de entorno `DB_USER` y `DB_PASSWORD` si tus credenciales no son `root/root`.
4. Levantar la aplicación con Maven:
   `mvn spring-boot:run`

*Nota: La estructura de tablas se generará automáticamente al iniciar el servidor gracias a Hibernate.*
