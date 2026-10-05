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

@DisplayName("API /api/navieras - pruebas de integración")
class NavieraControllerIT extends IntegracionBaseTest {

    private static final String URL = "/api/navieras";

    private Map<String, Object> naviera(int id, String codigo, String nombre, String pais) {
        Map<String, Object> body = new HashMap<>();
        body.put("navieraId", id); body.put("codigo", codigo); body.put("nombre", nombre); body.put("pais", pais);
        return body;
    }

    private Integer crear(String codigo, String nombre, String pais) throws Exception {
        MvcResult res = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(naviera(0, codigo, nombre, pais))))
                .andExpect(status().isCreated()).andReturn();
        return idCreado(res, "navieraId");
    }

    @Test
    @DisplayName("NAV-01 crear naviera válida persiste los datos normalizados")
    void crearValida() throws Exception {
        Map<String, Object> body = naviera(0, "msc", "Mediterranean Shipping Company", "suiza");
        body.put("sitioWeb", "https://www.msc.com");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.codigo").value("MSC"))
                .andExpect(jsonPath("$.data.nombre").value("MEDITERRANEAN SHIPPING COMPANY"))
                .andExpect(jsonPath("$.data.sitioWeb").value("https://www.msc.com"))
                .andExpect(jsonPath("$.data.activo").value(true));
    }

    @Test
    @DisplayName("NAV-02 / NAV-03 rechaza código y nombre duplicados con 409")
    void rechazaDuplicados() throws Exception {
        crear("MSC", "Mediterranean Shipping Company", "Suiza");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(naviera(0, " msc ", "Otra", "Suiza"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El código ya está registrado"));
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(naviera(0, "MSC2", "mediterranean shipping company", "Suiza"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El nombre ya está registrado"));
    }

    @Test
    @DisplayName("NAV-13 valida código, correo, teléfono, sitio web y obligatorios")
    void validaCampos() throws Exception {
        Map<String, Object> body = naviera(0, "M S$C", null, null);
        body.put("correo", "x@");
        body.put("telefono", "abc");
        body.put("sitioWeb", "www.sin-protocolo.com");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores[*].campo", containsInAnyOrder("codigo", "nombre", "pais", "correo", "telefono", "sitioWeb")));
    }

    @Test
    @DisplayName("NAV-04 / NAV-06 edita una naviera existente y responde 404 si no existe")
    void editar() throws Exception {
        Integer id = crear("MSC", "MSC", "Suiza");

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(naviera(id, "MSC", "MSC Mediterranean", "Suiza"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("MSC MEDITERRANEAN"))
                .andExpect(jsonPath("$.data.fechaModificacion").isNotEmpty());
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json(naviera(999999, "X", "X", "X"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("NAV-07 / NAV-08 / NAV-11 eliminación lógica y listado de activas")
    void eliminacionLogica() throws Exception {
        Integer id = crear("MSC", "MSC", "Suiza");

        mockMvc.perform(delete(URL).param("navieraId", id.toString()).param("activo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Naviera desactivada correctamente"));
        assertThat(activoEnBd("naviera", "naviera_id", id)).isFalse();
        mockMvc.perform(get(URL + "/activas")).andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(delete(URL).param("navieraId", id.toString()).param("activo", "true")).andExpect(status().isOk());
        mockMvc.perform(get(URL + "/activas")).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("NAV-14 filtra por texto, país y estado, ordena por nombre y pagina")
    void filtrosYPaginacion() throws Exception {
        crear("MSC", "Mediterranean Shipping", "Suiza");
        crear("CMA", "CMA CGM", "Francia");
        Integer inactiva = crear("HAP", "Hapag-Lloyd", "Alemania");
        mockMvc.perform(delete(URL).param("navieraId", inactiva.toString()).param("activo", "false"));

        mockMvc.perform(get(URL)).andExpect(jsonPath("$.datos[*].nombre", contains("CMA CGM", "HAPAG-LLOYD", "MEDITERRANEAN SHIPPING")));
        mockMvc.perform(get(URL).param("pais", "francia")).andExpect(jsonPath("$.datos[*].codigo", contains("CMA")));
        mockMvc.perform(get(URL).param("texto", "llo")).andExpect(jsonPath("$.paginacion.totalElementos").value(1));
        mockMvc.perform(get(URL).param("activo", "true")).andExpect(jsonPath("$.paginacion.totalElementos").value(2));
        mockMvc.perform(get(URL).param("pagina", "2").param("tamPagina", "2")).andExpect(jsonPath("$.datos", hasSize(1)));
    }

    @Test
    @DisplayName("NAV-10 / NAV-12 validar duplicados y obtener por id")
    void validarYObtenerPorId() throws Exception {
        Integer id = crear("MSC", "MSC", "Suiza");

        mockMvc.perform(get(URL + "/validar").param("codigo", "msc").param("nombre", "Nueva"))
                .andExpect(jsonPath("$.data.existeCodigo").value(true))
                .andExpect(jsonPath("$.data.existeNombre").value(false));
        mockMvc.perform(get(URL + "/validar").param("codigo", "MSC").param("navieraId", id.toString()))
                .andExpect(jsonPath("$.data.existeCodigo").value(false));
        mockMvc.perform(get(URL + "/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.data.codigo").value("MSC"));
        mockMvc.perform(get(URL + "/999999")).andExpect(status().isNotFound());
    }
}
