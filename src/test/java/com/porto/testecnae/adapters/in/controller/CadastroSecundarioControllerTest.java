package com.porto.testecnae.adapters.in.controller;

import com.porto.testecnae.adapters.in.api.model.CadastroSecundarioRequest;
import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.ports.in.CadastrarCadastroSecundarioCommand;
import com.porto.testecnae.application.ports.in.CadastroSecundarioInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastroSecundarioControllerTest {

    private static final AtividadeEconomicaCnae CNAE = new AtividadeEconomicaCnae(
            5L,
            "6201-5/01",
            "Desenvolvimento de programas de computador sob encomenda",
            "Tecnologia"
    );
    private static final CadastroSecundario CADASTRO = new CadastroSecundario(
            10L,
            "Tech Porto",
            "12345678000199",
            CNAE
    );

    @Mock
    private CadastroSecundarioInputPort inputPort;

    private CadastroSecundarioController controller;

    @BeforeEach
    void setUp() {
        controller = new CadastroSecundarioController(inputPort);
    }

    @Test
    void deveCadastrarEDevolverStatusCriado() {
        var request = new CadastroSecundarioRequest(
                CADASTRO.nomeFantasia(),
                CADASTRO.documento(),
                CNAE.codigo()
        );
        when(inputPort.cadastrar(any(CadastrarCadastroSecundarioCommand.class))).thenReturn(CADASTRO);

        var response = controller.cadastrarCadastroSecundario(request);

        var captor = ArgumentCaptor.forClass(CadastrarCadastroSecundarioCommand.class);
        verify(inputPort).cadastrar(captor.capture());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertAll(
                () -> assertEquals(request.getNomeFantasia(), captor.getValue().nomeFantasia()),
                () -> assertEquals(request.getDocumento(), captor.getValue().documento()),
                () -> assertEquals(request.getCodigoCnae(), captor.getValue().codigoCnae()),
                () -> assertEquals(CADASTRO.id(), response.getBody().getId()),
                () -> assertEquals(CADASTRO.nomeFantasia(), response.getBody().getNomeFantasia()),
                () -> assertEquals(CNAE.codigo(), response.getBody().getCnae().getCodigo())
        );
    }

    @Test
    void deveListarCadastrosComoRespostasHttp() {
        when(inputPort.listarTodos()).thenReturn(List.of(CADASTRO));

        var response = controller.listarCadastrosSecundarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CADASTRO.id(), response.getBody().getFirst().getId());
        assertEquals(CNAE.codigo(), response.getBody().getFirst().getCnae().getCodigo());
        verify(inputPort).listarTodos();
    }

    @Test
    void deveValidarCnaeComoRespostaHttp() {
        when(inputPort.validarCnae(CNAE.codigo())).thenReturn(CNAE);

        var response = controller.validarCnae(CNAE.codigo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CNAE.codigo(), response.getBody().getCodigo());
        verify(inputPort).validarCnae(CNAE.codigo());
    }
}
