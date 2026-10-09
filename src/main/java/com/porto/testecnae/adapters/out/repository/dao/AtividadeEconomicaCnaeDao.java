package com.porto.testecnae.adapters.out.repository.dao;

import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;

import java.util.List;
import java.util.Optional;

/** Acesso a dados CNAE oferecido a camada de adaptadores de persistencia. */
public interface AtividadeEconomicaCnaeDao {

    /**
     * Retorna todas as entidades CNAE.
     *
     * @return entidades CNAE cadastradas
     */
    List<AtividadeEconomicaCnaeEntity> buscarTodas();

    /**
     * Pesquisa entidades por trecho da descricao.
     *
     * @param termo trecho usado na pesquisa
     * @return entidades correspondentes ao termo
     */
    List<AtividadeEconomicaCnaeEntity> buscarPorDescricao(String termo);

    /**
     * Pesquisa uma entidade pelo codigo CNAE.
     *
     * @param codigo codigo CNAE procurado
     * @return entidade encontrada ou {@link Optional#empty()} quando ausente
     */
    Optional<AtividadeEconomicaCnaeEntity> buscarPorCodigo(String codigo);

    /**
     * Obtem uma referencia JPA para a entidade identificada pelo id.
     *
     * @param id identificador da entidade
     * @return referencia JPA para a entidade solicitada
     */
    AtividadeEconomicaCnaeEntity buscarPorId(Long id);
}
