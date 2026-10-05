package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.OperadorLogisticoEntity;
import com.proyecto.integrador.model.request.OperadorLogisticoRegistroRequest;
import com.proyecto.integrador.model.response.OperadorLogisticoResponse;
import com.proyecto.integrador.repository.OperadorLogisticoRepository;
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
@DisplayName("OperadorLogisticoServiceImpl - pruebas unitarias")
class OperadorLogisticoServiceImplTest {

    @Mock
    private OperadorLogisticoRepository operadorRepository;

    @InjectMocks
    private OperadorLogisticoServiceImpl operadorService;

    private static OperadorLogisticoRegistroRequest request(Integer id, String ruc, String razonSocial) {
        return new OperadorLogisticoRegistroRequest(id, ruc, razonSocial, null, null, null, null, null);
    }

    private static OperadorLogisticoEntity entidad(Integer id, String ruc, String razonSocial, Boolean activo) {
        OperadorLogisticoEntity e = new OperadorLogisticoEntity();
        e.setOperadorLogisticoId(id); e.setRuc(ruc); e.setRazonSocial(razonSocial); e.setActivo(activo);
        return e;
    }

    @Nested
    @DisplayName("Registrar")
    class Registrar {

        @Test
        @DisplayName("OPE-01 crea operador válido normalizando datos y lo deja activo")
        void creaOperadorValido() {
            OperadorLogisticoRegistroRequest req = new OperadorLogisticoRegistroRequest(0, " 20555555551 ", " ransa comercial sa ",
                    " ransa ", " Pedro ", " Contacto@RANSA.pe ", " (01) 555-1234 ", " Av. Argentina 123 ");

            ResponseEntity<Object> res = operadorService.registrar(req);

            ArgumentCaptor<OperadorLogisticoEntity> captor = ArgumentCaptor.forClass(OperadorLogisticoEntity.class);
            verify(operadorRepository).save(captor.capture());
            OperadorLogisticoEntity guardado = captor.getValue();
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(mensaje(res)).isEqualTo("Operador logístico registrado correctamente");
            assertThat(guardado.getRuc()).isEqualTo("20555555551");
            assertThat(guardado.getRazonSocial()).isEqualTo("RANSA COMERCIAL SA");
            assertThat(guardado.getNombreComercial()).isEqualTo("RANSA");
            assertThat(guardado.getContacto()).isEqualTo("Pedro");
            assertThat(guardado.getCorreo()).isEqualTo("contacto@ransa.pe");
            assertThat(guardado.getTelefono()).isEqualTo("(01) 555-1234");
            assertThat(guardado.getDireccion()).isEqualTo("Av. Argentina 123");
            assertThat(guardado.getActivo()).isTrue();
        }

        @Test
        @DisplayName("OPE-02 rechaza RUC duplicado")
        void rechazaRucDuplicado() {
            when(operadorRepository.existsByRucIgnoreCase("20555555551")).thenReturn(true);

            ResponseEntity<Object> res = operadorService.registrar(request(0, "20555555551", "Otra"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(mensaje(res)).isEqualTo("El RUC ya está registrado");
            verify(operadorRepository, never()).save(any());
        }

        @Test
        @DisplayName("OPE-03 rechaza razón social duplicada sin importar mayúsculas")
        void rechazaRazonSocialDuplicada() {
            when(operadorRepository.existsByRucIgnoreCase("20666666661")).thenReturn(false);
            when(operadorRepository.existsByRazonSocialIgnoreCase("RANSA COMERCIAL SA")).thenReturn(true);

            ResponseEntity<Object> res = operadorService.registrar(request(0, "20666666661", "ransa comercial sa"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(mensaje(res)).isEqualTo("La razón social ya está registrada");
        }

        @Test
        @DisplayName("OPE-04 edita sin chocar consigo mismo y usa saveAndFlush")
        void editaOperadorExistente() {
            OperadorLogisticoEntity existente = entidad(1, "20555555551", "RANSA", true);
            when(operadorRepository.findById(1)).thenReturn(Optional.of(existente));
            when(operadorRepository.existsByRucIgnoreCaseAndOperadorLogisticoIdNot("20555555551", 1)).thenReturn(false);
            when(operadorRepository.existsByRazonSocialIgnoreCaseAndOperadorLogisticoIdNot("RANSA EDITADO", 1)).thenReturn(false);

            ResponseEntity<Object> res = operadorService.registrar(request(1, "20555555551", "Ransa editado"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(mensaje(res)).isEqualTo("Operador logístico actualizado correctamente");
            assertThat(existente.getRazonSocial()).isEqualTo("RANSA EDITADO");
            verify(operadorRepository).saveAndFlush(existente);
        }

        @Test
        @DisplayName("OPE-05 rechaza editar usando la razón social de otro operador")
        void editaConRazonSocialDeOtro() {
            when(operadorRepository.findById(1)).thenReturn(Optional.of(entidad(1, "20555555551", "RANSA", true)));
            when(operadorRepository.existsByRucIgnoreCaseAndOperadorLogisticoIdNot("20555555551", 1)).thenReturn(false);
            when(operadorRepository.existsByRazonSocialIgnoreCaseAndOperadorLogisticoIdNot("NEPTUNIA", 1)).thenReturn(true);

            ResponseEntity<Object> res = operadorService.registrar(request(1, "20555555551", "Neptunia"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            verify(operadorRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("OPE-06 retorna 404 al editar un operador inexistente")
        void editaInexistente() {
            when(operadorRepository.findById(999)).thenReturn(Optional.empty());

            ResponseEntity<Object> res = operadorService.registrar(request(999, "20555555551", "X"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(mensaje(res)).isEqualTo("No se encontró el operador logístico");
        }
    }

    @Nested
    @DisplayName("Eliminación lógica")
    class CambiarEstado {

        @Test
        @DisplayName("OPE-07 desactiva el operador sin borrarlo")
        void desactiva() {
            OperadorLogisticoEntity existente = entidad(1, "20555555551", "RANSA", true);
            when(operadorRepository.findById(1)).thenReturn(Optional.of(existente));

            ResponseEntity<Object> res = operadorService.cambiarEstado(1, false);

            assertThat(mensaje(res)).isEqualTo("Operador logístico desactivado correctamente");
            assertThat(existente.getActivo()).isFalse();
            verify(operadorRepository, never()).delete(any());
        }

        @Test
        @DisplayName("OPE-08 reactiva el operador")
        void reactiva() {
            OperadorLogisticoEntity existente = entidad(1, "20555555551", "RANSA", false);
            when(operadorRepository.findById(1)).thenReturn(Optional.of(existente));

            assertThat(mensaje(operadorService.cambiarEstado(1, true))).isEqualTo("Operador logístico activado correctamente");
            assertThat(existente.getActivo()).isTrue();
        }

        @Test
        @DisplayName("OPE-09 retorna 404 al cambiar estado de un operador inexistente")
        void inexistente() {
            when(operadorRepository.findById(999)).thenReturn(Optional.empty());

            assertThat(operadorService.cambiarEstado(999, false).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("OPE-10 validar duplicados excluye el propio id")
        void validarDuplicadosExcluyendoId() {
            when(operadorRepository.existsByRucIgnoreCaseAndOperadorLogisticoIdNot("20555555551", 3)).thenReturn(false);
            when(operadorRepository.existsByRazonSocialIgnoreCaseAndOperadorLogisticoIdNot("RANSA", 3)).thenReturn(true);

            Map<String, Boolean> resultado = data(operadorService.validarDuplicados("20555555551", "ransa", 3));

            assertThat(resultado).containsEntry("existeRuc", false).containsEntry("existeRazonSocial", true);
        }

        @Test
        @DisplayName("OPE-11 listar activos mapea las entidades a respuesta")
        void listarActivos() {
            when(operadorRepository.findAllByActivoTrueOrderByRazonSocialAsc())
                    .thenReturn(List.of(entidad(1, "20555555551", "RANSA", true)));

            assertThat(operadorService.listarActivos()).extracting(OperadorLogisticoResponse::ruc).containsExactly("20555555551");
        }

        @Test
        @DisplayName("OPE-12 obtener por id retorna el operador o 404")
        void obtenerPorId() {
            when(operadorRepository.findById(1)).thenReturn(Optional.of(entidad(1, "20555555551", "RANSA", true)));
            when(operadorRepository.findById(2)).thenReturn(Optional.empty());

            OperadorLogisticoResponse operador = data(operadorService.obtenerPorId(1));

            assertThat(operador.razonSocial()).isEqualTo("RANSA");
            assertThat(operadorService.obtenerPorId(2).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
