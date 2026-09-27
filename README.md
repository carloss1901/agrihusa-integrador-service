# agrihusa-integrador-service

Esqueleto inicial del servicio integrador, organizado con Gradle y Spring Boot.

## Estructura actual

```text
agrihusa-integrador-service/
├── build.gradle
├── settings.gradle
├── Dockerfile
├── gradlew
├── gradlew.bat
├── gradle/
├── src/
│   └── main/
│       ├── java/com/proyecto/integrador/
│       │   ├── ProyectoIntegradorApplication.java
│       │   ├── api/
│       │   ├── config/
│       │   ├── model/
│       │   │   ├── dto/
│       │   │   ├── entity/
│       │   │   ├── projection/
│       │   │   ├── request/
│       │   │   └── response/
│       │   ├── repository/
│       │   ├── service/
│       │   │   └── impl/
│       │   └── util/
│       └── resources/
│           └── application.yml
└── README.md
```

## Configuración actual

- Java 17.
- Spring Boot 3.0.6.
- Gradle.
- Spring Web, JPA, validación y Swagger/OpenAPI.
- PostgreSQL de Supabase configurado mediante `SUPABASE_DB_URL`, `SUPABASE_DB_USER` y `SUPABASE_DB_PASSWORD`.
- Puerto de ejecución: `8085`.

La aplicación utiliza JPA/Hibernate con el esquema `public` y `ddl-auto=validate`; no crea ni elimina tablas al iniciar.

## Ejecución

Windows:

```bash
gradlew.bat bootRun
```

Linux/macOS:

```bash
./gradlew bootRun
```

Actualmente están implementados el endpoint de prueba `GET /api/hola-mundo` y el flujo de consulta de personas `GET /api/personas`. El resto de módulos de negocio aún está pendiente.

## Despliegue con Render

El `Dockerfile` compila la aplicación con Gradle y ejecuta el JAR generado. En local usa el puerto `8085`; en Render toma automáticamente el puerto definido por la variable `PORT`.
