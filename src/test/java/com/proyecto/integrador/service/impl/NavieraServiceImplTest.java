package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.NavieraEntity;
import com.proyecto.integrador.model.request.NavieraRegistroRequest;
import com.proyecto.integrador.model.response.NavieraResponse;
import com.proyecto.integrador.repository.NavieraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.proyecto.integrador.support.RespuestaTestUtils.data;
import static com.proyecto.integrador.support.RespuestaTestUtils.mensaje;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NavieraServiceImpl - pruebas unitarias")
class NavieraServiceImplTest {

    @Mock
    private NavieraRepository navieraRepository;

    @InjectMocks
    private NavieraServiceImpl navieraService;

    private static NavieraRegistroRequest request(Integer id, String codigo, String nombre) {
        return new NavieraRegistroRequest(id, codigo, nombre, "Suiza", null, null, null, null);
    }

    private static NavieraEntity entidad(Integer id, String codigo, String nombre, Boolean activo) {
        NavieraEntity e = new NavieraEntity();
        e.setNavieraId(id); e.setCodigo(codigo); e.setNombre(nombre); e.setPais("SUIZA"); e.setActivo(activo);
        return e;
    }

    @Nested
    @DisplayName("Registrar")
    class Registrar {

        @Test
        @DisplayName("NAV-01 crea naviera válida normalizando datos y la deja activa")
        void creaNavieraValida() {
            NavieraRegistroRequest req = new NavieraRegistroRequest(0, " m s c ", " Mediterranean Shipping ", " suiza ",
                    " Juan ", " Info@MSC.com ", "+41 22 703 8888", " https://www.msc.com ");

            ResponseEntity<Object> res = navieraService.registrar(req);

            ArgumentCaptor<NavieraEntity> captor = ArgumentCaptor.forClass(NavieraEntity.class);
            verify(navieraRepository).save(captor.capture());
            NavieraEntity guardada = captor.getValue();
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(mensaje(res)).isEqualTo("Naviera registrada correctamente");
            assertThat(guardada.getCodigo()).isEqualTo("MSC");
            assertThat(guardada.getNombre()).isEqualTo("MEDITERRANEAN SHIPPING");
            assertThat(guardada.getPais()).isEqualTo("SUIZA");
            assertThat(guardada.getContacto()).isEqualTo("Juan");
            assertThat(guardada.getCorreo()).isEqualTo("info@msc.com");
            assertThat(guardada.getSitioWeb()).isEqualTo("https://www.msc.com");
            assertThat(guardada.getActivo()).isTrue();
        }

        @Test
        @DisplayName("NAV-02 rechaza código duplicado sin importar mayúsculas ni espacios")
        void rechazaCodigoDuplicado() {
            when(navieraRepository.existsByCodigoIgnoreCase("MSC")).thenReturn(true);

            ResponseEntity<Object> res = navieraService.registrar(request(0, " msc ", "Otra"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(mensaje(res)).isEqualTo("El código ya está registrado");
            verify(navieraRepository, never()).save(any());
        }

        @Test
        @DisplayName("NAV-03 rechaza nombre duplicado")
        void rechazaNombreDuplicado() {
            when(navieraRepository.existsByCodigoIgnoreCase("MAERSK")).thenReturn(false);
            when(navieraRepository.existsByNombreIgnoreCase("MAERSK LINE")).thenReturn(true);

            ResponseEntity<Object> res = navieraService.registrar(request(0, "maersk", "maersk line"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(mensaje(res)).isEqualTo("El nombre ya está registrado");
        }

        @Test
        @DisplayName("NAV-04 edita sin chocar consigo misma y limpia campos opcionales vacíos")
        void editaNavieraExistente() {
            NavieraEntity existente = entidad(1, "MSC", "MSC", true);
            existente.setSitioWeb("https://antes.com");
            when(navieraRepository.findById(1)).thenReturn(Optional.of(existente));
            when(navieraRepository.existsByCodigoIgnoreCaseAndNavieraIdNot("MSC", 1)).thenReturn(false);
            when(navieraRepository.existsByNombreIgnoreCaseAndNavieraIdNot("MSC MEDITERRANEAN", 1)).thenReturn(false);
            NavieraRegistroRequest req = new NavieraRegistroRequest(1, "MSC", "MSC Mediterranean", "Suiza", null, null, null, "  ");

            ResponseEntity<Object> res = navieraService.registrar(req);

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(existente.getNombre()).isEqualTo("MSC MEDITERRANEAN");
            assertThat(existente.getSitioWeb()).isNull();
            verify(navieraRepository).saveAndFlush(existente);
        }

        @Test
        @DisplayName("NAV-05 rechaza editar usando el código de otra naviera")
        void editaConCodigoDeOtra() {
            when(navieraRepository.findById(1)).thenReturn(Optional.of(entidad(1, "MSC", "MSC", true)));
            when(navieraRepository.existsByCodigoIgnoreCaseAndNavieraIdNot("CMA", 1)).thenReturn(true);

            ResponseEntity<Object> res = navieraService.registrar(request(1, "CMA", "MSC"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            verify(navieraRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("NAV-06 retorna 404 al editar una naviera inexistente")
        void editaInexistente() {
            when(navieraRepository.findById(999)).thenReturn(Optional.empty());

            ResponseEntity<Object> res = navieraService.registrar(request(999, "X", "X"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(mensaje(res)).isEqualTo("No se encontró la naviera");
        }
    }

    @Nested
    @DisplayName("Eliminación lógica")
    class CambiarEstado {

        @Test
        @DisplayName("NAV-07 desactiva la naviera sin borrarla")
        void desactiva() {
            NavieraEntity existente = entidad(1, "MSC", "MSC", true);
            when(navieraRepository.findById(1)).thenReturn(Optional.of(existente));

            ResponseEntity<Object> res = navieraService.cambiarEstado(1, false);

            assertThat(mensaje(res)).isEqualTo("Naviera desactivada correctamente");
            assertThat(existente.getActivo()).isFalse();
            verify(navieraRepository, never()).delete(any());
        }

        @Test
        @DisplayName("NAV-08 reactiva la naviera")
        void reactiva() {
            NavieraEntity existente = entidad(1, "MSC", "MSC", false);
            when(navieraRepository.findById(1)).thenReturn(Optional.of(existente));

            assertThat(mensaje(navieraService.cambiarEstado(1, true))).isEqualTo("Naviera activada correctamente");
            assertThat(existente.getActivo()).isTrue();
        }

        @Test
        @DisplayName("NAV-09 retorna 404 al cambiar estado de una naviera inexistente")
        void inexistente() {
            when(navieraRepository.findById(999)).thenReturn(Optional.empty());

            assertThat(navieraService.cambiarEstado(999, true).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("NAV-10 validar duplicados para un registro nuevo")
        void validarDuplicadosNuevo() {
            when(navieraRepository.existsByCodigoIgnoreCase("MSC")).thenReturn(true);
            when(navieraRepository.existsByNombreIgnoreCase("NUEVA")).thenReturn(false);

            Map<String, Boolean> resultado = data(navieraService.validarDuplicados(" m sc", "nueva", null));

            assertThat(resultado).containsEntry("existeCodigo", true).containsEntry("existeNombre", false);
        }

        @Test
        @DisplayName("NAV-11 listar activas mapea las entidades a respuesta")
        void listarActivas() {
            when(navieraRepository.findAllByActivoTrueOrderByNombreAsc()).thenReturn(List.of(entidad(1, "MSC", "MSC", true)));

            assertThat(navieraService.listarActivas()).extracting(NavieraResponse::codigo).containsExactly("MSC");
        }

        @Test
        @DisplayName("NAV-12 obtener por id retorna la naviera o 404")
        void obtenerPorId() {
            when(navieraRepository.findById(1)).thenReturn(Optional.of(entidad(1, "MSC", "MSC", true)));
            when(navieraRepository.findById(2)).thenReturn(Optional.empty());

            NavieraResponse naviera = data(navieraService.obtenerPorId(1));

            assertThat(naviera.navieraId()).isEqualTo(1);
            assertThat(navieraService.obtenerPorId(2).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
