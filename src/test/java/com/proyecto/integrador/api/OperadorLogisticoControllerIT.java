package com.proyecto.integrador.api;

import com.proyecto.integrador.support.IntegracionBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API /api/operadores-logisticos - pruebas de integración")
class OperadorLogisticoControllerIT extends IntegracionBaseTest {

    private static final String URL = "/api/operadores-logisticos";

    private Map<String, Object> operador(int id, String ruc, String razonSocial) {
        Map<String, Object> body = new HashMap<>();
        body.put("operadorLogisticoId", id); body.put("ruc", ruc); body.put("razonSocial", razonSocial);
        return body;
    }

    private Integer crear(String ruc, String razonSocial) throws Exception {
        MvcResult res = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(operador(0, ruc, razonSocial))))
                .andExpect(status().isCreated()).andReturn();
        return idCreado(res, "operadorLogisticoId");
    }

    @Test
    @DisplayName("OPE-01 crear operador válido persiste los datos normalizados")
    void crearValido() throws Exception {
        Map<String, Object> body = operador(0, "20555555551", "ransa comercial sa");
        body.put("telefono", "(01) 555-1234");
        body.put("correo", "Contacto@RANSA.pe");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.razonSocial").value("RANSA COMERCIAL SA"))
                .andExpect(jsonPath("$.data.correo").value("contacto@ransa.pe"))
                .andExpect(jsonPath("$.data.activo").value(true));
    }

    @Test
    @DisplayName("OPE-02 / OPE-03 rechaza RUC y razón social duplicados con 409")
    void rechazaDuplicados() throws Exception {
        crear("20555555551", "Ransa Comercial SA");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(operador(0, "20555555551", "Otra"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El RUC ya está registrado"));
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(operador(0, "20666666661", "ransa comercial sa"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("La razón social ya está registrada"));
    }

    @Test
    @DisplayName("OPE-13 valida RUC de 11 dígitos, correo, teléfono y obligatorios")
    void validaCampos() throws Exception {
        Map<String, Object> body = operador(0, "2055", null);
        body.put("correo", "no-es-correo");
        body.put("telefono", "abc");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores[*].campo", containsInAnyOrder("ruc", "razonSocial", "correo", "telefono")));
    }

    @Test
    @DisplayName("OPE-04 / OPE-06 edita un operador existente y responde 404 si no existe")
    void editar() throws Exception {
        Integer id = crear("20555555551", "Ransa");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(operador(id, "20555555551", "Ransa editado"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.razonSocial").value("RANSA EDITADO"))
                .andExpect(jsonPath("$.data.fechaModificacion").isNotEmpty());
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(operador(999999, "20555555551", "X"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("OPE-07 / OPE-08 / OPE-11 eliminación lógica y listado de activos")
    void eliminacionLogica() throws Exception {
        Integer id = crear("20555555551", "Ransa");

        mockMvc.perform(delete(URL).param("operadorLogisticoId", id.toString()).param("activo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operador logístico desactivado correctamente"));
        assertThat(activoEnBd("operador_logistico", "operador_logistico_id", id)).isFalse();
        mockMvc.perform(get(URL + "/activos")).andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(delete(URL).param("operadorLogisticoId", id.toString()).param("activo", "true")).andExpect(status().isOk());
        mockMvc.perform(get(URL + "/activos")).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("OPE-14 filtra por texto y estado, ordena por razón social y pagina")
    void filtrosYPaginacion() throws Exception {
        crear("20555555551", "Ransa Comercial");
        crear("20666666661", "Neptunia");
        Integer inactivo = crear("20777777771", "Alma Logistics");
        mockMvc.perform(delete(URL).param("operadorLogisticoId", inactivo.toString()).param("activo", "false"));

        mockMvc.perform(get(URL)).andExpect(jsonPath("$.datos[*].razonSocial", contains("ALMA LOGISTICS", "NEPTUNIA", "RANSA COMERCIAL")));
        mockMvc.perform(get(URL).param("texto", "2066")).andExpect(jsonPath("$.datos[*].ruc", contains("20666666661")));
        mockMvc.perform(get(URL).param("activo", "false")).andExpect(jsonPath("$.paginacion.totalElementos").value(1));
        mockMvc.perform(get(URL).param("pagina", "1").param("tamPagina", "2"))
                .andExpect(jsonPath("$.datos", hasSize(2)))
                .andExpect(jsonPath("$.paginacion.totalElementos").value(3));
    }

    @Test
    @DisplayName("OPE-10 / OPE-12 validar duplicados y obtener por id")
    void validarYObtenerPorId() throws Exception {
        Integer id = crear("20555555551", "Ransa");

        mockMvc.perform(get(URL + "/validar").param("ruc", "20555555551").param("razonSocial", "Nueva"))
                .andExpect(jsonPath("$.data.existeRuc").value(true))
                .andExpect(jsonPath("$.data.existeRazonSocial").value(false));
        mockMvc.perform(get(URL + "/validar").param("ruc", "20555555551").param("operadorLogisticoId", id.toString()))
                .andExpect(jsonPath("$.data.existeRuc").value(false));
        mockMvc.perform(get(URL + "/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.data.ruc").value("20555555551"));
        mockMvc.perform(get(URL + "/999999")).andExpect(status().isNotFound());
    }
}
