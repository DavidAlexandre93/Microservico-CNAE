package com.porto.testecnae.unit.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import com.porto.testecnae.application.core.usecase.AtividadeEconomicaCnaeUseCase;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtividadeEconomicaCnaeUseCaseTest {

    private static final AtividadeEconomicaCnae CNAE = new AtividadeEconomicaCnae(
            5L,
            "6201-5/01",
            "Desenvolvimento de programas de computador sob encomenda",
            "Tecnologia"
    );

    @Mock
    private AtividadeEconomicaCnaeOutputPort outputPort;

    private AtividadeEconomicaCnaeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtividadeEconomicaCnaeUseCase(outputPort);
    }

    @Test
    void deveConsultarCnaesPelaPortaDeSaida() {
        var atividades = List.of(CNAE);
        when(outputPort.buscarTodas()).thenReturn(atividades);
        when(outputPort.buscarPorDescricao("programas")).thenReturn(atividades);

        assertSame(atividades, useCase.listarTodas());
        assertSame(atividades, useCase.buscarPorDescricao("programas"));
        verify(outputPort).buscarTodas();
        verify(outputPort).buscarPorDescricao("programas");
    }

    @Test
    void deveRetornarCnaeQuandoCodigoExistir() {
        when(outputPort.buscarPorCodigo(CNAE.codigo())).thenReturn(Optional.of(CNAE));

        assertSame(CNAE, useCase.buscarPorCodigo(CNAE.codigo()));
        verify(outputPort).buscarPorCodigo(CNAE.codigo());
    }

    @Test
    void deveRejeitarCodigoInexistente() {
        var codigo = "0000-0/00";
        when(outputPort.buscarPorCodigo(codigo)).thenReturn(Optional.empty());

        var exception = assertThrows(
                CnaeNaoEncontradoException.class,
                () -> useCase.buscarPorCodigo(codigo)
        );

        assertEquals("CNAE nao encontrado para o codigo: " + codigo, exception.getMessage());
    }
}
