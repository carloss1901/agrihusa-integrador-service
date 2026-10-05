package com.proyecto.integrador.api;

import com.proyecto.integrador.support.IntegracionBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API /api/clientes - pruebas de integración")
class ClienteControllerIT extends IntegracionBaseTest {

    private static final String URL = "/api/clientes";

    private Map<String, Object> cliente(int id, String tipo, String numero, String razon) {
        Map<String, Object> body = new HashMap<>();
        body.put("clienteId", id); body.put("tipoDocumento", tipo); body.put("numeroDocumento", numero);
        body.put("razonSocial", razon); body.put("pais", "Peru");
        return body;
    }

    private Integer crear(String tipo, String numero, String razon) throws Exception {
        MvcResult res = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(cliente(0, tipo, numero, razon))))
                .andExpect(status().isCreated()).andReturn();
        return idCreado(res, "clienteId");
    }

    @Test
    @DisplayName("CLI-01 crear cliente válido persiste los datos normalizados")
    void crearValido() throws Exception {
        Map<String, Object> body = cliente(0, "RUC", "20123456789", " Frutas del Norte sac ");
        body.put("correo", " Ventas@FRUTAS.pe ");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.razonSocial").value("FRUTAS DEL NORTE SAC"))
                .andExpect(jsonPath("$.data.correo").value("ventas@frutas.pe"))
                .andExpect(jsonPath("$.data.pais").value("PERU"))
                .andExpect(jsonPath("$.data.activo").value(true))
                .andExpect(jsonPath("$.data.fechaCreacion").isNotEmpty());

        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cliente", Integer.class)).isEqualTo(1);
    }

    @Test
    @DisplayName("CLI-02 / CLI-03 rechaza documento y razón social duplicados con 409")
    void rechazaDuplicados() throws Exception {
        crear("RUC", "20123456789", "Frutas del Norte SAC");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(cliente(0, "RUC", "201 2345 6789", "Otra"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El número de documento ya está registrado"));
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(cliente(0, "DNI", "12345678", "frutas del norte sac"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("La razón social ya está registrada"));
    }

    @Test
    @DisplayName("CLI-06 / CLI-07 / CLI-08 valida campos y responde la lista de errores")
    void validaCampos() throws Exception {
        Map<String, Object> body = cliente(0, "XYZ", "abc", null);
        body.put("pais", null);
        body.put("correo", "no-es-correo");
        body.put("telefono", "abc");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Errores de validación"))
                .andExpect(jsonPath("$.errores[*].campo", containsInAnyOrder("tipoDocumento", "razonSocial", "pais", "correo", "telefono")));
    }

    @Test
    @DisplayName("CLI-05 rechaza DNI con longitud incorrecta")
    void rechazaDniInvalido() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(cliente(0, "DNI", "1234567", "Persona"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El DNI debe contener exactamente 8 dígitos numéricos"));
    }

    @Test
    @DisplayName("CLI-04 / CLI-09 edita un cliente existente y responde 404 si no existe")
    void editar() throws Exception {
        Integer id = crear("RUC", "20123456789", "Frutas SAC");
        Map<String, Object> body = cliente(id, "RUC", "20123456789", "Frutas SAC");
        body.put("contacto", "Ana");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contacto").value("Ana"))
                .andExpect(jsonPath("$.data.fechaModificacion").isNotEmpty());
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(cliente(999999, "RUC", "20123456789", "X"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("CLI-10 / CLI-11 / CLI-12 eliminación lógica: desactiva, sale de activos y se puede reactivar")
    void eliminacionLogica() throws Exception {
        Integer id = crear("RUC", "20123456789", "Frutas SAC");

        mockMvc.perform(delete(URL).param("clienteId", id.toString()).param("activo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cliente desactivado correctamente"));
        assertThat(activoEnBd("cliente", "cliente_id", id)).isFalse();
        mockMvc.perform(get(URL + "/activos")).andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(delete(URL).param("clienteId", id.toString()).param("activo", "true")).andExpect(status().isOk());
        mockMvc.perform(get(URL + "/activos")).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("CLI-13 / CLI-14 / CLI-15 filtra por texto, tipo y estado, ordena y pagina")
    void filtrosYPaginacion() throws Exception {
        crear("RUC", "20111111111", "Zeta Export SAC");
        crear("RUC", "20222222222", "Alfa Frutas SAC");
        Integer inactivo = crear("DNI", "12345678", "Beta Frutas");
        mockMvc.perform(delete(URL).param("clienteId", inactivo.toString()).param("activo", "false"));

        mockMvc.perform(get(URL).param("texto", "frutas"))
                .andExpect(jsonPath("$.datos[*].razonSocial", org.hamcrest.Matchers.contains("ALFA FRUTAS SAC", "BETA FRUTAS")));
        mockMvc.perform(get(URL).param("tipoDocumento", "RUC").param("activo", "true"))
                .andExpect(jsonPath("$.paginacion.totalElementos").value(2));
        mockMvc.perform(get(URL).param("activo", "false"))
                .andExpect(jsonPath("$.datos[0].estadoDsc").value("Inactivo"));
        mockMvc.perform(get(URL).param("pagina", "2").param("tamPagina", "2"))
                .andExpect(jsonPath("$.datos", hasSize(1)))
                .andExpect(jsonPath("$.paginacion.numeroPagina").value(2))
                .andExpect(jsonPath("$.paginacion.totalElementos").value(3));
    }

    @Test
    @DisplayName("CLI-16 / CLI-19 validar duplicados y obtener por id")
    void validarYObtenerPorId() throws Exception {
        Integer id = crear("RUC", "20123456789", "Frutas SAC");

        mockMvc.perform(get(URL + "/validar").param("numeroDocumento", "20123456789").param("razonSocial", "Nueva"))
                .andExpect(jsonPath("$.data.existeNumeroDocumento").value(true))
                .andExpect(jsonPath("$.data.existeRazonSocial").value(false));
        mockMvc.perform(get(URL + "/validar").param("numeroDocumento", "20123456789").param("clienteId", id.toString()))
                .andExpect(jsonPath("$.data.existeNumeroDocumento").value(false));
        mockMvc.perform(get(URL + "/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.data.clienteId").value(id));
        mockMvc.perform(get(URL + "/999999")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("SEG-01 CORS permite el origen del front y bloquea otros")
    void cors() throws Exception {
        mockMvc.perform(options(URL).header("Origin", "http://localhost:4200").header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
        mockMvc.perform(options(URL).header("Origin", "http://otro-sitio.com").header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
