package com.porto.testecnae.adapters.out;

import com.porto.testecnae.adapters.out.repository.dao.AtividadeEconomicaCnaeDao;
import com.porto.testecnae.adapters.out.repository.dao.CadastroSecundarioDao;
import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.ports.out.CadastroSecundarioOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CadastroSecundarioPersistenceAdapter implements CadastroSecundarioOutputPort {

    private final CadastroSecundarioDao cadastroDao;
    private final AtividadeEconomicaCnaeDao cnaeDao;

    @Override
    @Transactional
    public CadastroSecundario salvar(CadastroSecundario cadastro) {
        var cnaeEntity = cnaeDao.buscarPorId(cadastro.cnae().id());

        var entity = CadastroSecundarioEntity.builder()
                .id(cadastro.id())
                .nomeFantasia(cadastro.nomeFantasia())
                .documento(cadastro.documento())
                .cnae(cnaeEntity)
                .build();

        var entitySalva = cadastroDao.salvar(entity);

        return new CadastroSecundario(
                entitySalva.getId(),
                entitySalva.getNomeFantasia(),
                entitySalva.getDocumento(),
                cadastro.cnae()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CadastroSecundario> buscarTodos() {
        return cadastroDao.buscarTodosComCnae()
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
