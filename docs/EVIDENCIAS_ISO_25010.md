# Evidencias ISO/IEC 25010:2023

## Idoneidad y corrección funcional

- `model/request/DespachoRegistroRequest.java`: obligatoriedad, IDs positivos y límites de campos.
- `service/impl/DespachoServiceImpl.java`: fechas coherentes, referencias activas y relación producto-variedad.
- `repository/*Repository.java`: consultas de existencia activa que sostienen la integridad de referencias.
- `api/DespachoController.java`: límites de paginación.
- `utils/GlobalExceptionHandler.java`: contrato de error para validación, integridad y fallos no controlados.
- `src/test/java/com/proyecto/integrador/service/ServiceConditionalBranchesTest.java`: evidencia automatizada de rutas de error y registro de despacho.

## Interoperabilidad

- `config/CorsConfig.java` y `config/SecurityConfig.java`: CORS, JWT y autorización.
- `api/ClimaController.java`, `service/impl/ClimaServiceImpl.java`, `model/response/ClimaResponse.java`: contrato Open-Meteo, validación de coordenadas, timeout y caché.
- `application.yml`: URL externa y timeouts configurables por variables de entorno.

## Coexistencia

- `build.gradle`: Actuator para salud y métricas.
- `application.yml`: HikariCP con tamaño máximo configurable y publicación de `health`/`metrics`.
- Preparar un plan de prueba JMeter con operaciones simult\u00e1neas de consulta de despachos y reportes.

Ejecutar el plan JMeter contra un entorno de pruebas autenticado y registrar P95, tasa de errores, CPU y RAM. No se deben inventar resultados.
