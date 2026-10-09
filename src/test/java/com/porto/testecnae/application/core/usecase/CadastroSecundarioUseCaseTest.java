package com.porto.testecnae.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
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
        var novoCadastro = captor.getValue();
        assertAll(
                () -> assertSame(salvo, resultado),
                () -> assertNull(novoCadastro.id()),
                () -> assertEquals(command.nomeFantasia(), novoCadastro.nomeFantasia()),
                () -> assertEquals(command.documento(), novoCadastro.documento()),
                () -> assertSame(CNAE, novoCadastro.cnae())
        );
        verify(cnaeOutputPort).buscarPorCodigo(CNAE.codigo());
    }

    @Test
    void naoDeveSalvarCadastroQuandoCnaeNaoExistir() {
        var command = new CadastrarCadastroSecundarioCommand(
                "Tech Porto",
                "12345678000199",
                "0000-0/00"
        );
        when(cnaeOutputPort.buscarPorCodigo(command.codigoCnae())).thenReturn(Optional.empty());

        var exception = assertThrows(CnaeNaoEncontradoException.class, () -> useCase.cadastrar(command));

        assertEquals(
                "CNAE nao encontrado para o codigo: " + command.codigoCnae(),
                exception.getMessage()
        );
        verify(cadastroOutputPort, never()).salvar(any());
    }

    @Test
    void deveValidarCnaeExistente() {
        when(cnaeOutputPort.buscarPorCodigo(CNAE.codigo())).thenReturn(Optional.of(CNAE));

        var resultado = useCase.validarCnae(CNAE.codigo());

        assertSame(CNAE, resultado);
        verify(cnaeOutputPort).buscarPorCodigo(CNAE.codigo());
    }

    @Test
    void deveRejeitarValidacaoDeCnaeInexistente() {
        var codigo = "0000-0/00";
        when(cnaeOutputPort.buscarPorCodigo(codigo)).thenReturn(Optional.empty());

        assertThrows(CnaeNaoEncontradoException.class, () -> useCase.validarCnae(codigo));
    }

    @Test
    void deveListarCadastrosPelaPortaDeSaida() {
        var cadastros = List.of(new CadastroSecundario(10L, "Tech Porto", "12345678000199", CNAE));
        when(cadastroOutputPort.buscarTodos()).thenReturn(cadastros);

        var resultado = useCase.listarTodos();

        assertSame(cadastros, resultado);
        verify(cadastroOutputPort).buscarTodos();
    }
}
