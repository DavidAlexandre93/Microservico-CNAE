package com.porto.testecnae.adapters.in.controller;

import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void deveConverterCnaeNaoEncontradoEmErro404() {
        var exception = new CnaeNaoEncontradoException("0000-0/00");

        var response = handler.handleCnaeNaoEncontrado(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertAll(
                () -> assertEquals(404, response.getBody().getStatus()),
                () -> assertEquals("Not Found", response.getBody().getErro()),
                () -> assertEquals(exception.getMessage(), response.getBody().getMensagem())
        );
    }

    @Test
    void deveConverterFalhaDeValidacaoEmErro400Padronizado() {
        var exception = new ConstraintViolationException(Set.of());

        var response = handler.handleRequisicaoInvalida(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertAll(
                () -> assertEquals(400, response.getBody().getStatus()),
                () -> assertEquals("Bad Request", response.getBody().getErro()),
                () -> assertEquals(
                        "Parametros ou corpo da requisicao invalidos",
                        response.getBody().getMensagem()
                )
        );
    }

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
