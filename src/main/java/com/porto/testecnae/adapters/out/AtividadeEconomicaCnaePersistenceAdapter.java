package com.porto.testecnae.adapters.out;

import com.porto.testecnae.adapters.out.repository.dao.AtividadeEconomicaCnaeDao;
import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;
import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa as consultas CNAE usando o DAO e mapeia entidades ORM para dominio.
 */
@Component
@RequiredArgsConstructor
public class AtividadeEconomicaCnaePersistenceAdapter implements AtividadeEconomicaCnaeOutputPort {

    private final AtividadeEconomicaCnaeDao dao;

    /**
     * Busca todas as entidades CNAE e as converte para objetos de dominio.
     *
     * @return lista de atividades economicas
     */
    @Override
    public List<AtividadeEconomicaCnae> buscarTodas() {
        return dao.buscarTodas()
                .stream()
                .map(AtividadeEconomicaCnaePersistenceAdapter::toDomain)
                .toList();
    }

    /**
     * Busca entidades por descricao e converte os resultados para o dominio.
     *
     * @param termo trecho usado na pesquisa da descricao
     * @return atividades cuja descricao corresponde ao termo
     */
    @Override
    public List<AtividadeEconomicaCnae> buscarPorDescricao(String termo) {
        return dao.buscarPorDescricao(termo)
                .stream()
                .map(AtividadeEconomicaCnaePersistenceAdapter::toDomain)
                .toList();
    }

    /**
     * Busca uma entidade pelo codigo e converte o resultado, se encontrado.
     *
     * @param codigo codigo CNAE procurado
     * @return atividade encontrada ou {@link Optional#empty()} quando ausente
     */
    @Override
    public Optional<AtividadeEconomicaCnae> buscarPorCodigo(String codigo) {
        return dao.buscarPorCodigo(codigo)
                .map(AtividadeEconomicaCnaePersistenceAdapter::toDomain);
    }

    /**
     * Converte uma entidade de persistencia em uma atividade economica do dominio.
     *
     * @param entity entidade JPA de origem
     * @return atividade economica correspondente no dominio
     */
    static AtividadeEconomicaCnae toDomain(AtividadeEconomicaCnaeEntity entity) {
        return new AtividadeEconomicaCnae(
                entity.getId(),
                entity.getCodigo(),
                entity.getDescricao(),
                entity.getSecao()
        );
    }
}
