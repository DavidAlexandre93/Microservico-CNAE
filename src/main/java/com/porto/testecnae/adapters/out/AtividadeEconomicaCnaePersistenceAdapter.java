package com.porto.testecnae.adapters.out;

import com.porto.testecnae.adapters.out.repository.dao.AtividadeEconomicaCnaeDao;
import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;
import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AtividadeEconomicaCnaePersistenceAdapter implements AtividadeEconomicaCnaeOutputPort {

    private final AtividadeEconomicaCnaeDao dao;

    @Override
    public List<AtividadeEconomicaCnae> buscarTodas() {
        return dao.buscarTodas()
                .stream()
                .map(AtividadeEconomicaCnaePersistenceAdapter::toDomain)
                .toList();
    }

    @Override
    public List<AtividadeEconomicaCnae> buscarPorDescricao(String termo) {
        return dao.buscarPorDescricao(termo)
                .stream()
                .map(AtividadeEconomicaCnaePersistenceAdapter::toDomain)
                .toList();
    }

    @Override
    public Optional<AtividadeEconomicaCnae> buscarPorCodigo(String codigo) {
        return dao.buscarPorCodigo(codigo)
                .map(AtividadeEconomicaCnaePersistenceAdapter::toDomain);
    }

    static AtividadeEconomicaCnae toDomain(AtividadeEconomicaCnaeEntity entity) {
        return new AtividadeEconomicaCnae(
                entity.getId(),
                entity.getCodigo(),
                entity.getDescricao(),
                entity.getSecao()
        );
    }
}
