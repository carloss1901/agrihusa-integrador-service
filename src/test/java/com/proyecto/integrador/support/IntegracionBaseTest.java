package com.proyecto.integrador.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.MountableFile;

/**
 * Levanta un SQL Server real (Testcontainers) con el script de db/init, el mismo que usa docker-compose.
 * El contenedor se comparte entre todas las clases de integración y se limpia la data antes de cada prueba.
 */
@Tag("integracion")
@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegracionBaseTest {

    private static final String SCRIPT = "db/init/01_AGRIHUSA_SQLSERVER.sql";

    @SuppressWarnings("resource")
    protected static final MSSQLServerContainer<?> SQL_SERVER =
            new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
                    .acceptLicense()
                    .withCopyFileToContainer(MountableFile.forHostPath(SCRIPT), "/tmp/init.sql");

    static {
        SQL_SERVER.start();
        try {
            Container.ExecResult resultado = SQL_SERVER.execInContainer(
                    "/opt/mssql-tools18/bin/sqlcmd", "-S", "localhost", "-U", SQL_SERVER.getUsername(),
                    "-P", SQL_SERVER.getPassword(), "-C", "-b", "-i", "/tmp/init.sql");
            if (resultado.getExitCode() != 0) {
                throw new IllegalStateException("Falló el script de BD: " + resultado.getStdout() + resultado.getStderr());
            }
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo inicializar la base de datos de pruebas", e);
        }
    }

    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> SQL_SERVER.getJdbcUrl() + ";databaseName=BD_AGRIHUSA");
        registry.add("spring.datasource.username", SQL_SERVER::getUsername);
        registry.add("spring.datasource.password", SQL_SERVER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    // La app usa auto-commit=false, por eso la limpieza va dentro de una transacción
    @BeforeEach
    void limpiarDatos() {
        transactionTemplate.executeWithoutResult(status -> {
            jdbcTemplate.execute("DELETE FROM despacho");
            jdbcTemplate.execute("DELETE FROM cliente");
            jdbcTemplate.execute("DELETE FROM naviera");
            jdbcTemplate.execute("DELETE FROM operador_logistico");
        });
    }

    protected Integer idCreado(MvcResult resultado, String campoId) throws Exception {
        return objectMapper.readTree(resultado.getResponse().getContentAsString()).path("data").path(campoId).asInt();
    }

    protected Boolean activoEnBd(String tabla, String columnaId, Integer id) {
        return jdbcTemplate.queryForObject("SELECT activo FROM " + tabla + " WHERE " + columnaId + " = ?", Boolean.class, id);
    }

    protected String json(Object objeto) throws Exception {
        return objectMapper.writeValueAsString(objeto);
    }
}
