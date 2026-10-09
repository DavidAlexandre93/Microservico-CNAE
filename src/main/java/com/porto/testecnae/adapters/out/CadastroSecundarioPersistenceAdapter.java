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

/**
 * Adaptador de persistencia que converte cadastros entre dominio e entidades JPA.
 */
@Component
@RequiredArgsConstructor
public class CadastroSecundarioPersistenceAdapter implements CadastroSecundarioOutputPort {

    private final CadastroSecundarioDao cadastroDao;
    private final AtividadeEconomicaCnaeDao cnaeDao;

    /**
     * Persiste o cadastro associado a um CNAE existente e devolve o dominio salvo.
     *
     * @param cadastro cadastro de dominio que sera persistido
     * @return cadastro persistido com seu identificador
     */
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

    /**
     * Consulta cadastros com seus CNAEs e converte cada resultado para o dominio.
     *
     * @return lista de cadastros secundarios
     */
    @Override
    @Transactional(readOnly = true)
    public List<CadastroSecundario> buscarTodos() {
        return cadastroDao.buscarTodosComCnae()
                .stream()
                .map(CadastroSecundarioPersistenceAdapter::toDomain)
                .toList();
    }

    /**
     * Converte a entidade JPA e seu CNAE associado para objetos de dominio.
     *
     * @param entity entidade JPA de cadastro
     * @return cadastro correspondente no dominio
     */
    private static CadastroSecundario toDomain(CadastroSecundarioEntity entity) {
        return new CadastroSecundario(
                entity.getId(),
                entity.getNomeFantasia(),
                entity.getDocumento(),
                AtividadeEconomicaCnaePersistenceAdapter.toDomain(entity.getCnae())
        );
    }
}
