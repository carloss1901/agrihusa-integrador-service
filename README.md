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
- MySQL configurado mediante `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`.
- Puerto de ejecución: `8085`.

La configuración de la BD está preparada, pero la auto-configuración de datasource y JPA está desactivada temporalmente porque todavía no se está utilizando una base de datos.

## Ejecución

Windows:

```bash
gradlew.bat bootRun
```

Linux/macOS:

```bash
./gradlew bootRun
```

Actualmente está implementada la clase principal, el endpoint de prueba `GET /api/hola-mundo` y la estructura de paquetes; aún no hay servicios, repositorios ni modelos de negocio.

## Despliegue con Render

El `Dockerfile` compila la aplicación con Gradle y ejecuta el JAR generado. En local usa el puerto `8085`; en Render toma automáticamente el puerto definido por la variable `PORT`.
