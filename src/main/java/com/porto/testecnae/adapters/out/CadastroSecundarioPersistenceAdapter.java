package com.porto.testecnae.adapters.out;

import com.porto.testecnae.adapters.out.repository.SpringDataAtividadeEconomicaCnaeRepository;
import com.porto.testecnae.adapters.out.repository.SpringDataCadastroSecundarioRepository;
import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import com.porto.testecnae.application.ports.out.CadastroSecundarioOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CadastroSecundarioPersistenceAdapter implements CadastroSecundarioOutputPort {

    private final SpringDataCadastroSecundarioRepository repository;
    private final SpringDataAtividadeEconomicaCnaeRepository cnaeRepository;

    @Override
    @Transactional
    public CadastroSecundario salvar(CadastroSecundario cadastro) {
        var cnaeEntity = cnaeRepository.findByCodigo(cadastro.cnae().codigo())
                .orElseThrow(() -> new CnaeNaoEncontradoException(cadastro.cnae().codigo()));

        var entity = CadastroSecundarioEntity.builder()
                .id(cadastro.id())
                .nomeFantasia(cadastro.nomeFantasia())
                .documento(cadastro.documento())
                .cnae(cnaeEntity)
                .build();

        return toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CadastroSecundario> buscarTodos() {
        return repository.buscarTodosComCnae()
                .stream()
                .map(CadastroSecundarioPersistenceAdapter::toDomain)
                .toList();
    }

    private static CadastroSecundario toDomain(CadastroSecundarioEntity entity) {
        return new CadastroSecundario(
                entity.getId(),
                entity.getNomeFantasia(),
                entity.getDocumento(),
                AtividadeEconomicaCnaePersistenceAdapter.toDomain(entity.getCnae())
        );
    }
}
