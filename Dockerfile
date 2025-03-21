FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 9090

ENV DATABASE_USER_NAME=root
ENV DATABASE_PASSWORD=admin
ENV DATABASE_URL=r2dbc:mysql://franchise_mysql:3306/franchise

ENTRYPOINT ["java", "-jar", "app.jar"]