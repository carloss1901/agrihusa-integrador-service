package com.proyecto.integrador.service.impl;

import com.proyecto.integrador.model.entity.ClienteEntity;
import com.proyecto.integrador.model.request.ClienteRegistroRequest;
import com.proyecto.integrador.model.response.ClienteResponse;
import com.proyecto.integrador.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteServiceImpl - pruebas unitarias")
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private static ClienteRegistroRequest request(Integer id, String tipo, String numero, String razon) {
        return new ClienteRegistroRequest(id, tipo, numero, razon, null, null, null, null, null, "Peru");
    }

    private static ClienteEntity entidad(Integer id, String numero, String razon, Boolean activo) {
        ClienteEntity e = new ClienteEntity();
        e.setClienteId(id); e.setTipoDocumento("RUC"); e.setNumeroDocumento(numero); e.setRazonSocial(razon);
        e.setPais("PERU"); e.setActivo(activo);
        return e;
    }

    @Nested
    @DisplayName("Registrar")
    class Registrar {

        @Test
        @DisplayName("CLI-01 crea cliente válido normalizando datos y lo deja activo")
        void creaClienteValido() {
            ClienteRegistroRequest req = new ClienteRegistroRequest(0, "RUC", " 2012 3456 789 ", "  Frutas del Norte sac ",
                    "frutas norte", "  Ana  ", " Ventas@FRUTAS.pe ", " 999 888 777 ", "  ", " peru ");

            ResponseEntity<Object> res = clienteService.registrar(req);

            ArgumentCaptor<ClienteEntity> captor = ArgumentCaptor.forClass(ClienteEntity.class);
            verify(clienteRepository).save(captor.capture());
            ClienteEntity guardado = captor.getValue();
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(mensaje(res)).isEqualTo("Cliente registrado correctamente");
            assertThat(guardado.getNumeroDocumento()).isEqualTo("20123456789");
            assertThat(guardado.getRazonSocial()).isEqualTo("FRUTAS DEL NORTE SAC");
            assertThat(guardado.getNombreComercial()).isEqualTo("FRUTAS NORTE");
            assertThat(guardado.getContacto()).isEqualTo("Ana");
            assertThat(guardado.getCorreo()).isEqualTo("ventas@frutas.pe");
            assertThat(guardado.getTelefono()).isEqualTo("999 888 777");
            assertThat(guardado.getDireccion()).isNull();
            assertThat(guardado.getPais()).isEqualTo("PERU");
            assertThat(guardado.getActivo()).isTrue();
            Object respuesta = data(res);
            assertThat(respuesta).isInstanceOf(ClienteResponse.class);
        }

        @Test
        @DisplayName("CLI-02 rechaza número de documento duplicado (aunque venga con espacios)")
        void rechazaDocumentoDuplicado() {
            when(clienteRepository.existsByNumeroDocumentoIgnoreCase("20123456789")).thenReturn(true);

            ResponseEntity<Object> res = clienteService.registrar(request(0, "RUC", "201 2345 6789", "Otra"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(mensaje(res)).isEqualTo("El número de documento ya está registrado");
            verify(clienteRepository, never()).save(any());
        }

        @Test
        @DisplayName("CLI-03 rechaza razón social duplicada sin importar mayúsculas ni espacios")
        void rechazaRazonSocialDuplicada() {
            when(clienteRepository.existsByNumeroDocumentoIgnoreCase("12345678")).thenReturn(false);
            when(clienteRepository.existsByRazonSocialIgnoreCase("FRUTAS DEL NORTE SAC")).thenReturn(true);

            ResponseEntity<Object> res = clienteService.registrar(request(0, "DNI", "12345678", "  frutas del norte sac "));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(mensaje(res)).isEqualTo("La razón social ya está registrada");
            verify(clienteRepository, never()).save(any());
        }

        @ParameterizedTest(name = "CLI-05 {0} ''{1}'' es inválido")
        @CsvSource({
                "RUC, 2012345678",
                "RUC, 201234567890",
                "RUC, 2012345678A",
                "DNI, 1234567",
                "DNI, 123456789",
                "CARNET_EXTRANJERIA, 12345678",
                "PASAPORTE, 12345",
                "PASAPORTE, AB12345678901",
                "OTRO, AB",
                "OTRO, ABC_123"
        })
        @DisplayName("CLI-05 rechaza documentos con formato inválido según su tipo")
        void rechazaFormatoDocumentoInvalido(String tipo, String numero) {
            ResponseEntity<Object> res = clienteService.registrar(request(0, tipo, numero, "Cliente"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verifyNoInteractions(clienteRepository);
        }

        @ParameterizedTest(name = "CLI-05 {0} ''{1}'' es válido")
        @CsvSource({
                "RUC, 20123456789",
                "DNI, 12345678",
                "CARNET_EXTRANJERIA, 001234567",
                "PASAPORTE, ab123456",
                "OTRO, DOC-001"
        })
        @DisplayName("CLI-05 acepta documentos con formato válido según su tipo")
        void aceptaFormatoDocumentoValido(String tipo, String numero) {
            ResponseEntity<Object> res = clienteService.registrar(request(0, tipo, numero, "Cliente"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        @Test
        @DisplayName("CLI-04 edita sin chocar consigo mismo y usa saveAndFlush")
        void editaClienteExistente() {
            ClienteEntity existente = entidad(1, "20123456789", "FRUTAS SAC", true);
            when(clienteRepository.findById(1)).thenReturn(Optional.of(existente));
            when(clienteRepository.existsByNumeroDocumentoIgnoreCaseAndClienteIdNot("20123456789", 1)).thenReturn(false);
            when(clienteRepository.existsByRazonSocialIgnoreCaseAndClienteIdNot("FRUTAS SAC EDITADO", 1)).thenReturn(false);

            ResponseEntity<Object> res = clienteService.registrar(request(1, "RUC", "20123456789", "Frutas SAC editado"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(mensaje(res)).isEqualTo("Cliente actualizado correctamente");
            assertThat(existente.getRazonSocial()).isEqualTo("FRUTAS SAC EDITADO");
            assertThat(existente.getActivo()).isTrue();
            verify(clienteRepository).saveAndFlush(existente);
        }

        @Test
        @DisplayName("CLI-09 retorna 404 al editar un cliente inexistente")
        void editaClienteInexistente() {
            when(clienteRepository.findById(999)).thenReturn(Optional.empty());

            ResponseEntity<Object> res = clienteService.registrar(request(999, "RUC", "20123456789", "X"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(mensaje(res)).isEqualTo("No se encontró el cliente");
        }

        @Test
        @DisplayName("CLI-17 rechaza editar usando el documento de otro cliente")
        void editaConDocumentoDeOtro() {
            when(clienteRepository.findById(1)).thenReturn(Optional.of(entidad(1, "20111111111", "A", true)));
            when(clienteRepository.existsByNumeroDocumentoIgnoreCaseAndClienteIdNot("20222222222", 1)).thenReturn(true);

            ResponseEntity<Object> res = clienteService.registrar(request(1, "RUC", "20222222222", "A"));

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            verify(clienteRepository, never()).saveAndFlush(any());
        }
    }

    @Nested
    @DisplayName("Eliminación lógica")
    class CambiarEstado {

        @Test
        @DisplayName("CLI-10 desactiva el cliente sin borrarlo")
        void desactiva() {
            ClienteEntity existente = entidad(1, "20123456789", "A", true);
            when(clienteRepository.findById(1)).thenReturn(Optional.of(existente));

            ResponseEntity<Object> res = clienteService.cambiarEstado(1, false);

            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(mensaje(res)).isEqualTo("Cliente desactivado correctamente");
            assertThat(existente.getActivo()).isFalse();
            verify(clienteRepository).save(existente);
            verify(clienteRepository, never()).delete(any());
        }

        @Test
        @DisplayName("CLI-12 reactiva el cliente")
        void reactiva() {
            ClienteEntity existente = entidad(1, "20123456789", "A", false);
            when(clienteRepository.findById(1)).thenReturn(Optional.of(existente));

            ResponseEntity<Object> res = clienteService.cambiarEstado(1, true);

            assertThat(mensaje(res)).isEqualTo("Cliente activado correctamente");
            assertThat(existente.getActivo()).isTrue();
        }

        @Test
        @DisplayName("CLI-18 retorna 404 al cambiar estado de un cliente inexistente")
        void inexistente() {
            when(clienteRepository.findById(999)).thenReturn(Optional.empty());

            assertThat(clienteService.cambiarEstado(999, false).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("CLI-16 validar duplicados normaliza y excluye el propio id")
        void validarDuplicadosExcluyendoId() {
            when(clienteRepository.existsByNumeroDocumentoIgnoreCaseAndClienteIdNot("20123456789", 5)).thenReturn(true);
            when(clienteRepository.existsByRazonSocialIgnoreCaseAndClienteIdNot("FRUTAS SAC", 5)).thenReturn(false);

            ResponseEntity<Object> res = clienteService.validarDuplicados("2012 3456 789", " frutas sac ", 5);

            Map<String, Boolean> resultado = data(res);
            assertThat(resultado).containsEntry("existeNumeroDocumento", true).containsEntry("existeRazonSocial", false);
        }

        @Test
        @DisplayName("CLI-16 validar duplicados sin parámetros no consulta la base")
        void validarDuplicadosSinParametros() {
            ResponseEntity<Object> res = clienteService.validarDuplicados(null, "  ", null);

            Map<String, Boolean> resultado = data(res);
            assertThat(resultado).containsEntry("existeNumeroDocumento", false).containsEntry("existeRazonSocial", false);
            verify(clienteRepository, never()).existsByNumeroDocumentoIgnoreCase(anyString());
            verify(clienteRepository, never()).existsByRazonSocialIgnoreCase(anyString());
        }

        @Test
        @DisplayName("CLI-11 listar activos mapea las entidades a respuesta")
        void listarActivos() {
            when(clienteRepository.findAllByActivoTrueOrderByRazonSocialAsc())
                    .thenReturn(List.of(entidad(1, "20111111111", "A", true), entidad(2, "20222222222", "B", true)));

            List<ClienteResponse> activos = clienteService.listarActivos();

            assertThat(activos).extracting(ClienteResponse::clienteId).containsExactly(1, 2);
            assertThat(activos).allSatisfy(c -> assertThat(c.estadoDsc()).isEqualTo("Activo"));
        }

        @Test
        @DisplayName("CLI-19 obtener por id retorna el cliente o 404")
        void obtenerPorId() {
            when(clienteRepository.findById(1)).thenReturn(Optional.of(entidad(1, "20111111111", "A", false)));
            when(clienteRepository.findById(2)).thenReturn(Optional.empty());

            ResponseEntity<Object> encontrado = clienteService.obtenerPorId(1);
            ClienteResponse cliente = data(encontrado);

            assertThat(encontrado.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(cliente.estadoDsc()).isEqualTo("Inactivo");
            assertThat(clienteService.obtenerPorId(2).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
