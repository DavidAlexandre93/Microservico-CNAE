package com.porto.testecnae.adapters.out;

import com.porto.testecnae.adapters.out.repository.SpringDataAtividadeEconomicaCnaeRepository;
import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtividadeEconomicaCnaePersistenceAdapterTest {

    private static final AtividadeEconomicaCnaeEntity ENTITY = AtividadeEconomicaCnaeEntity.builder()
            .id(5L)
            .codigo("6201-5/01")
            .descricao("Desenvolvimento de programas de computador sob encomenda")
            .secao("Tecnologia")
            .build();

    @Mock
    private SpringDataAtividadeEconomicaCnaeRepository repository;

    private AtividadeEconomicaCnaePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new AtividadeEconomicaCnaePersistenceAdapter(repository);
    }

    @Test
    void deveListarEntidadesComoObjetosDeDominio() {
        when(repository.findAll()).thenReturn(List.of(ENTITY));

        var resultado = adapter.buscarTodas();

        assertEquals(1, resultado.size());
        assertDomain(resultado.getFirst());
        verify(repository).findAll();
    }

    @Test
    void deveBuscarPorDescricaoEMapearResultado() {
        when(repository.findByDescricaoContainingIgnoreCaseOrderByCodigo("programas"))
                .thenReturn(List.of(ENTITY));

        var resultado = adapter.buscarPorDescricao("programas");

        assertEquals(1, resultado.size());
        assertDomain(resultado.getFirst());
        verify(repository).findByDescricaoContainingIgnoreCaseOrderByCodigo("programas");
    }

    @Test
    void deveBuscarPorCodigoEMapearEntidadeEncontrada() {
        when(repository.findByCodigo(ENTITY.getCodigo())).thenReturn(Optional.of(ENTITY));

        var resultado = adapter.buscarPorCodigo(ENTITY.getCodigo());

        assertTrue(resultado.isPresent());
        assertDomain(resultado.orElseThrow());
        verify(repository).findByCodigo(ENTITY.getCodigo());
    }

    @Test
    void deveRetornarOptionalVazioQuandoCodigoNaoExistir() {
        when(repository.findByCodigo("0000-0/00")).thenReturn(Optional.empty());

        var resultado = adapter.buscarPorCodigo("0000-0/00");

        assertFalse(resultado.isPresent());
    }

    private static void assertDomain(com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae domain) {
        assertAll(
                () -> assertEquals(ENTITY.getId(), domain.id()),
                () -> assertEquals(ENTITY.getCodigo(), domain.codigo()),
                () -> assertEquals(ENTITY.getDescricao(), domain.descricao()),
                () -> assertEquals(ENTITY.getSecao(), domain.secao())
        );
    }
}
