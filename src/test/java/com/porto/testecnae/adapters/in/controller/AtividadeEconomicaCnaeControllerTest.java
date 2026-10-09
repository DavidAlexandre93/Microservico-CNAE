package com.porto.testecnae.adapters.in.controller;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.ports.in.AtividadeEconomicaCnaeInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtividadeEconomicaCnaeControllerTest {

    private static final AtividadeEconomicaCnae CNAE = new AtividadeEconomicaCnae(
            5L,
            "6201-5/01",
            "Desenvolvimento de programas de computador sob encomenda",
            "Tecnologia"
    );

    @Mock
    private AtividadeEconomicaCnaeInputPort inputPort;

    private AtividadeEconomicaCnaeController controller;

    @BeforeEach
    void setUp() {
        controller = new AtividadeEconomicaCnaeController(inputPort);
    }

    @Test
    void deveListarCnaesComoRespostasHttp() {
        when(inputPort.listarTodas()).thenReturn(List.of(CNAE));

        var response = controller.listarCnaes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(CNAE.codigo(), response.getBody().getFirst().getCodigo());
        verify(inputPort).listarTodas();
    }

    @Test
    void deveBuscarCnaesPorDescricaoComoRespostasHttp() {
        when(inputPort.buscarPorDescricao("programas")).thenReturn(List.of(CNAE));

        var response = controller.buscarCnaesPorDescricao("programas");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CNAE.codigo(), response.getBody().getFirst().getCodigo());
        verify(inputPort).buscarPorDescricao("programas");
    }

    @Test
    void deveBuscarCnaePorCodigoComoRespostaHttp() {
        when(inputPort.buscarPorCodigo(CNAE.codigo())).thenReturn(CNAE);

        var response = controller.buscarCnaePorCodigo(CNAE.codigo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CNAE.codigo(), response.getBody().getCodigo());
        verify(inputPort).buscarPorCodigo(CNAE.codigo());
    }
}
