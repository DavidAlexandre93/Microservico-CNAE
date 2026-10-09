package com.porto.testecnae.adapters.out.repository;

import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Repositorio Spring Data para operacoes JPA sobre atividades economicas CNAE. */
public interface SpringDataAtividadeEconomicaCnaeRepository
        extends JpaRepository<AtividadeEconomicaCnaeEntity, Long> {

    /**
     * Localiza um registro pelo codigo CNAE exato.
     *
     * @param codigo codigo CNAE procurado
     * @return entidade encontrada ou {@link Optional#empty()} quando ausente
     */
    Optional<AtividadeEconomicaCnaeEntity> findByCodigo(String codigo);

    /**
     * Pesquisa descricao sem diferenciar caixa e ordena os resultados pelo codigo.
     *
     * @param termo trecho usado na pesquisa
     * @return entidades correspondentes, ordenadas pelo codigo CNAE
     */
    List<AtividadeEconomicaCnaeEntity> findByDescricaoContainingIgnoreCaseOrderByCodigo(String termo);
}
