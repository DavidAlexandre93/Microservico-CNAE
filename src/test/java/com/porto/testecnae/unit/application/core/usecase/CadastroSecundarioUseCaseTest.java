package com.porto.testecnae.unit.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import com.porto.testecnae.application.core.usecase.CadastroSecundarioUseCase;
import com.porto.testecnae.application.ports.in.CadastrarCadastroSecundarioCommand;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import com.porto.testecnae.application.ports.out.CadastroSecundarioOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastroSecundarioUseCaseTest {

    private static final AtividadeEconomicaCnae CNAE = new AtividadeEconomicaCnae(
            5L,
            "6201-5/01",
            "Desenvolvimento de programas de computador sob encomenda",
            "Tecnologia"
    );

    @Mock
    private CadastroSecundarioOutputPort cadastroOutputPort;

    @Mock
    private AtividadeEconomicaCnaeOutputPort cnaeOutputPort;

    private CadastroSecundarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CadastroSecundarioUseCase(cadastroOutputPort, cnaeOutputPort);
    }

    @Test
    void deveCadastrarQuandoCnaeExistir() {
        var command = new CadastrarCadastroSecundarioCommand(
                "Tech Porto",
                "12345678000199",
                CNAE.codigo()
        );
        var salvo = new CadastroSecundario(10L, command.nomeFantasia(), command.documento(), CNAE);
        when(cnaeOutputPort.buscarPorCodigo(CNAE.codigo())).thenReturn(Optional.of(CNAE));
        when(cadastroOutputPort.salvar(any(CadastroSecundario.class))).thenReturn(salvo);

        var resultado = useCase.cadastrar(command);

        var captor = ArgumentCaptor.forClass(CadastroSecundario.class);
        verify(cadastroOutputPort).salvar(captor.capture());
        assertAll(
                () -> assertSame(salvo, resultado),
                () -> assertNull(captor.getValue().id()),
                () -> assertEquals(command.nomeFantasia(), captor.getValue().nomeFantasia()),
                () -> assertEquals(command.documento(), captor.getValue().documento()),
                () -> assertSame(CNAE, captor.getValue().cnae())
        );
    }

    @Test
    void naoDeveSalvarQuandoCnaeNaoExistir() {
        var command = new CadastrarCadastroSecundarioCommand(
                "Tech Porto",
                "12345678000199",
                "0000-0/00"
        );
        when(cnaeOutputPort.buscarPorCodigo(command.codigoCnae())).thenReturn(Optional.empty());

        assertThrows(CnaeNaoEncontradoException.class, () -> useCase.cadastrar(command));
        verify(cadastroOutputPort, never()).salvar(any());
    }

    @Test
    void deveValidarCnaeEListarCadastrosPelasPortasDeSaida() {
        var cadastros = List.of(new CadastroSecundario(10L, "Tech Porto", "12345678000199", CNAE));
        when(cnaeOutputPort.buscarPorCodigo(CNAE.codigo())).thenReturn(Optional.of(CNAE));
        when(cadastroOutputPort.buscarTodos()).thenReturn(cadastros);

        assertSame(CNAE, useCase.validarCnae(CNAE.codigo()));
        assertSame(cadastros, useCase.listarTodos());
        verify(cnaeOutputPort).buscarPorCodigo(CNAE.codigo());
        verify(cadastroOutputPort).buscarTodos();
    }
}
