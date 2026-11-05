package com.proyecto.integrador.service;

import com.proyecto.integrador.model.response.ClimaResponse;
import com.proyecto.integrador.service.impl.ClimaServiceImpl;
import com.proyecto.integrador.utils.ApiException;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
class ClimaServiceTest {

    @Test
    void debeConsultarYMapearClima(WireMockRuntimeInfo wireMock) {
        configurarUbicacionValida();
        configurarPronosticoValido();

        ClimaResponse response = crearServicio(wireMock)
                .consultar("Huaral");

        assertAll(
                () -> assertEquals("Huaral", response.destino()),
                () -> assertEquals("Perú", response.pais()),
                () -> assertEquals(-11.495, response.latitud()),
                () -> assertEquals(-77.2078, response.longitud()),
                () -> assertEquals(22.5, response.temperaturaCelsius()),
                () -> assertEquals(0.0, response.precipitacionMm()),
                () -> assertEquals(12.4, response.velocidadVientoKmh()),
                () -> assertEquals(1, response.codigoTiempo()),
                () -> assertEquals(
                        "Parcialmente nublado",
                        response.descripcion()
                ),
                () -> assertEquals(
                        "America/Lima",
                        response.zonaHoraria()
                ),
                () -> assertFalse(response.desdeCache())
        );

        verify(1, getRequestedFor(
                urlPathEqualTo("/geocoding")
        ));

        verify(1, getRequestedFor(
                urlPathEqualTo("/forecast")
        ));
    }

    @Test
    void debeUsarCacheEnConsultaRepetida(
            WireMockRuntimeInfo wireMock
    ) {
        configurarUbicacionValida();
        configurarPronosticoValido();

        ClimaServiceImpl service = crearServicio(wireMock);

        ClimaResponse primera = service.consultar("Huaral");
        ClimaResponse segunda = service.consultar("Huaral");

        assertFalse(primera.desdeCache());
        assertTrue(segunda.desdeCache());

        verify(1, getRequestedFor(
                urlPathEqualTo("/geocoding")
        ));

        verify(1, getRequestedFor(
                urlPathEqualTo("/forecast")
        ));
    }

    @Test
    void debeResponder404CuandoDestinoNoExiste(
            WireMockRuntimeInfo wireMock
    ) {
        stubFor(get(urlPathEqualTo("/geocoding"))
                .willReturn(okJson("""
                        {
                          "results": []
                        }
                        """)));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> crearServicio(wireMock)
                        .consultar("Destino inexistente")
        );

        assertEquals(
                "404",
                exception.getErrorGenerico().getCodigo()
        );

        verify(1, getRequestedFor(
                urlPathEqualTo("/geocoding")
        ));

        verify(0, getRequestedFor(
                urlPathEqualTo("/forecast")
        ));
    }

    @Test
    void debeResponder502CuandoRespuestaEsIncompleta(
            WireMockRuntimeInfo wireMock
    ) {
        configurarUbicacionValida();

        stubFor(get(urlPathEqualTo("/forecast"))
                .willReturn(okJson("""
                        {
                          "timezone": "America/Lima",
                          "current": {
                            "time": "2026-11-04T10:00",
                            "temperature_2m": 22.5
                          }
                        }
                        """)));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> crearServicio(wireMock)
                        .consultar("Huaral")
        );

        assertEquals(
                "502",
                exception.getErrorGenerico().getCodigo()
        );
    }

    @Test
    void debeReintentarYResponder503AnteError5xx(
            WireMockRuntimeInfo wireMock
    ) {
        configurarUbicacionValida();

        stubFor(get(urlPathEqualTo("/forecast"))
                .willReturn(serverError()));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> crearServicio(wireMock)
                        .consultar("Huaral")
        );

        assertEquals(
                "503",
                exception.getErrorGenerico().getCodigo()
        );

        verify(2, getRequestedFor(
                urlPathEqualTo("/forecast")
        ));
    }

    @Test
    void debeReintentarYResponder503AnteError429(
            WireMockRuntimeInfo wireMock
    ) {
        configurarUbicacionValida();

        stubFor(get(urlPathEqualTo("/forecast"))
                .willReturn(aResponse().withStatus(429)));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> crearServicio(wireMock)
                        .consultar("Huaral")
        );

        assertEquals(
                "503",
                exception.getErrorGenerico().getCodigo()
        );

        verify(2, getRequestedFor(
                urlPathEqualTo("/forecast")
        ));
    }

    private ClimaServiceImpl crearServicio(
            WireMockRuntimeInfo wireMock
    ) {
        return new ClimaServiceImpl(
                wireMock.getHttpBaseUrl() + "/geocoding",
                wireMock.getHttpBaseUrl() + "/forecast",
                1000,
                1000,
                300
        );
    }

    private void configurarUbicacionValida() {
        stubFor(get(urlPathEqualTo("/geocoding"))
                .willReturn(okJson("""
                        {
                          "results": [
                            {
                              "name": "Huaral",
                              "latitude": -11.495,
                              "longitude": -77.2078,
                              "country": "Perú",
                              "timezone": "America/Lima"
                            }
                          ]
                        }
                        """)));
    }

    private void configurarPronosticoValido() {
        stubFor(get(urlPathEqualTo("/forecast"))
                .willReturn(okJson("""
                        {
                          "latitude": -11.495,
                          "longitude": -77.2078,
                          "timezone": "America/Lima",
                          "current": {
                            "time": "2026-11-04T10:00",
                            "temperature_2m": 22.5,
                            "precipitation": 0.0,
                            "wind_speed_10m": 12.4,
                            "weather_code": 1
                          }
                        }
                        """)));
    }
}