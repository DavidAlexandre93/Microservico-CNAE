package com.porto.testecnae.unit.adapters.in.controller;

import com.porto.testecnae.adapters.in.controller.RestExceptionHandler;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void deveRetornar404PadronizadoQuandoCnaeNaoExistir() {
        var response = handler.handleCnaeNaoEncontrado(new CnaeNaoEncontradoException("0000-0/00"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertAll(
                () -> assertEquals(404, response.getBody().getStatus()),
                () -> assertEquals("Not Found", response.getBody().getErro()),
                () -> assertEquals("CNAE nao encontrado para o codigo: 0000-0/00", response.getBody().getMensagem())
        );
    }

    @Test
    void deveRetornar404PadronizadoQuandoRecursoNaoExistir() {
        var response = handler.handleRecursoNaoEncontrado();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertAll(
                () -> assertEquals(404, response.getBody().getStatus()),
                () -> assertEquals("Not Found", response.getBody().getErro()),
                () -> assertEquals("Recurso nao encontrado", response.getBody().getMensagem())
        );
    }

    @Test
    void deveRetornar400PadronizadoQuandoRequisicaoForInvalida() {
        var response = handler.handleRequisicaoInvalida();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertAll(
                () -> assertEquals(400, response.getBody().getStatus()),
                () -> assertEquals("Bad Request", response.getBody().getErro()),
                () -> assertEquals("Parametros ou corpo da requisicao invalidos", response.getBody().getMensagem())
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
