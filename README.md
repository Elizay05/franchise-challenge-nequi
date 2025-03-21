# Proyecto: Franquicias

Este es un proyecto basado en **Spring Boot** y **R2DBC**, diseñado para gestionar franquicias con sus respectivas sucursales y productos asociados.

## 📌 Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalados los siguientes componentes:

- **Java 17 o superior** → [Descargar](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- **Gradle** → [Descargar](https://gradle.org/install/)
- **MySQL 8** → [Descargar](https://dev.mysql.com/downloads/mysql/)

## 🛠 Configuración de la Base de Datos

1. Inicia MySQL y crea la base de datos con el siguiente comando:

    ```sql
    CREATE DATABASE franchises;
    ```

2. Configura las credenciales en `application.properties` o `application.yml`, asegurándote de que sean correctas para tu entorno local:

    ```properties
    spring.r2dbc.url=${DATABASE_URL:r2dbc:mysql://localhost:3306/franchises}
    spring.r2dbc.username=${DATABASE_USER:root}
    spring.r2dbc.password=${DATABASE_PASSWORD:12345}

    spring.sql.init.mode=always
    spring.sql.init.data-locations=classpath:/database/data.sql

    logging.level.org.springframework.r2dbc=DEBUG
    ```
    
## 🚀 Ejecución en Entorno Local

Para iniciar la aplicación sin Docker, sigue estos pasos:

1. Clona el repositorio y accede a la carpeta del proyecto:

    ```sh
    git clone https://github.com/Elizay05/franchise-challenge-nequi.git
    cd franchises_challenge
    ```

2. Compila el proyecto con Gradle:

    ```sh
    ./gradlew build
    ```

3. Inicia la aplicación:

    ```sh
    ./gradlew bootRun
    ```

4. Una vez en ejecución, accede a la API en:

    - Aplicación: [http://localhost:9090](http://localhost:9090)
    - Documentación Swagger: [http://localhost:9090/swagger-ui/index.html](http://localhost:9090/swagger-ui/index.html)

## 🐳 Despliegue con Docker

Si prefieres usar Docker para ejecutar la aplicación, sigue estos pasos:

1. Asegúrate de tener **Docker** y **Docker Compose** instalados.
2. Clona el repositorio y accede a la carpeta:

    ```sh
    git clone https://github.com/Elizay05/franchise-challenge-nequi.git
    cd franchises_challenge
    ```

3. Construye la imagen de la aplicación:

    ```sh
    ./gradlew build
    ```

4. Inicia los contenedores con:

    ```sh
    docker-compose up --build
    ```

5. Accede a la aplicación en:

    - Aplicación: [http://localhost:9090](http://localhost:9090)
    - Documentación Swagger: [http://localhost:9090/swagger-ui/index.html](http://localhost:9090/swagger-ui/index.html)

## 📜 Notas Adicionales

- Si experimentas errores de conexión a la base de datos, verifica que MySQL esté corriendo y que las credenciales sean correctas.
- Puedes modificar el puerto de la aplicación en `application.properties` si es necesario.

---
