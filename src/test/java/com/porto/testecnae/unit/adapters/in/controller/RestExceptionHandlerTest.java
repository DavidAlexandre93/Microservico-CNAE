package com.porto.testecnae.unit.adapters.in.controller;

import com.porto.testecnae.adapters.in.controller.RestExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void deveOcultarDetalhesDeErroInesperadoERetornar500Padronizado() {
        var response = handler.handleErroInesperado(new IllegalStateException("detalhe sensivel"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertAll(
                () -> assertEquals(500, response.getBody().getStatus()),
                () -> assertEquals("Internal Server Error", response.getBody().getErro()),
                () -> assertEquals("Erro interno do servidor", response.getBody().getMensagem())
        );
    }
}
