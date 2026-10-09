package com.porto.testecnae.adapters.out;

import com.porto.testecnae.adapters.out.repository.SpringDataAtividadeEconomicaCnaeRepository;
import com.porto.testecnae.adapters.out.repository.SpringDataCadastroSecundarioRepository;
import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;
import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;
import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastroSecundarioPersistenceAdapterTest {

    private static final AtividadeEconomicaCnaeEntity CNAE_ENTITY = AtividadeEconomicaCnaeEntity.builder()
            .id(5L)
            .codigo("6201-5/01")
            .descricao("Desenvolvimento de programas de computador sob encomenda")
            .secao("Tecnologia")
            .build();
    private static final AtividadeEconomicaCnae CNAE = new AtividadeEconomicaCnae(
            CNAE_ENTITY.getId(),
            CNAE_ENTITY.getCodigo(),
            CNAE_ENTITY.getDescricao(),
            CNAE_ENTITY.getSecao()
    );

    @Mock
    private SpringDataCadastroSecundarioRepository repository;

    @Mock
    private SpringDataAtividadeEconomicaCnaeRepository cnaeRepository;

    private CadastroSecundarioPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new CadastroSecundarioPersistenceAdapter(repository, cnaeRepository);
    }

    @Test
    void deveSalvarCadastroComEntidadeCnaeEncontrada() {
        var cadastro = CadastroSecundario.novo("Tech Porto", "12345678000199", CNAE);
        var entitySalva = CadastroSecundarioEntity.builder()
                .id(10L)
                .nomeFantasia(cadastro.nomeFantasia())
                .documento(cadastro.documento())
                .cnae(CNAE_ENTITY)
                .build();
        when(cnaeRepository.findByCodigo(CNAE.codigo())).thenReturn(Optional.of(CNAE_ENTITY));
        when(repository.save(any(CadastroSecundarioEntity.class))).thenReturn(entitySalva);

        var resultado = adapter.salvar(cadastro);

        var captor = ArgumentCaptor.forClass(CadastroSecundarioEntity.class);
        verify(repository).save(captor.capture());
        var entityParaSalvar = captor.getValue();
        assertAll(
                () -> assertEquals(cadastro.id(), entityParaSalvar.getId()),
                () -> assertEquals(cadastro.nomeFantasia(), entityParaSalvar.getNomeFantasia()),
                () -> assertEquals(cadastro.documento(), entityParaSalvar.getDocumento()),
                () -> assertSame(CNAE_ENTITY, entityParaSalvar.getCnae()),
                () -> assertEquals(entitySalva.getId(), resultado.id()),
                () -> assertEquals(cadastro.nomeFantasia(), resultado.nomeFantasia()),
                () -> assertEquals(CNAE.codigo(), resultado.cnae().codigo())
        );
    }

    @Test
    void naoDeveSalvarQuandoCnaeNaoExistirNaPersistencia() {
        var cadastro = CadastroSecundario.novo("Tech Porto", "12345678000199", CNAE);
        when(cnaeRepository.findByCodigo(CNAE.codigo())).thenReturn(Optional.empty());

        var exception = assertThrows(CnaeNaoEncontradoException.class, () -> adapter.salvar(cadastro));

        assertEquals("CNAE nao encontrado para o codigo: " + CNAE.codigo(), exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveListarEntidadesComoCadastrosDeDominioComCnae() {
        var entity = CadastroSecundarioEntity.builder()
                .id(10L)
                .nomeFantasia("Tech Porto")
                .documento("12345678000199")
                .cnae(CNAE_ENTITY)
                .build();
        when(repository.buscarTodosComCnae()).thenReturn(List.of(entity));

        var resultado = adapter.buscarTodos();

        assertEquals(1, resultado.size());
        var cadastro = resultado.getFirst();
        assertAll(
                () -> assertEquals(entity.getId(), cadastro.id()),
                () -> assertEquals(entity.getNomeFantasia(), cadastro.nomeFantasia()),
                () -> assertEquals(entity.getDocumento(), cadastro.documento()),
                () -> assertEquals(CNAE.id(), cadastro.cnae().id()),
                () -> assertEquals(CNAE.codigo(), cadastro.cnae().codigo()),
                () -> assertEquals(CNAE.descricao(), cadastro.cnae().descricao()),
                () -> assertEquals(CNAE.secao(), cadastro.cnae().secao())
        );
        verify(repository).buscarTodosComCnae();
    }
}
