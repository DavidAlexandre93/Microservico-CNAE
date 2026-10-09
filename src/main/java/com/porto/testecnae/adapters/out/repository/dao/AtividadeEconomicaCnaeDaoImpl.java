package com.porto.testecnae.adapters.out.repository.dao;

import com.porto.testecnae.adapters.out.repository.SpringDataAtividadeEconomicaCnaeRepository;
import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Implementacao do DAO CNAE delegando as consultas ao repositorio Spring Data. */
@Repository
@RequiredArgsConstructor
public class AtividadeEconomicaCnaeDaoImpl implements AtividadeEconomicaCnaeDao {

    private final SpringDataAtividadeEconomicaCnaeRepository repository;

    /**
     * Retorna todos os registros CNAE.
     *
     * @return entidades CNAE cadastradas
     */
    @Override
    public List<AtividadeEconomicaCnaeEntity> buscarTodas() {
        return repository.findAll();
    }

    /**
     * Busca descricoes sem diferenciar letras maiusculas e minusculas.
     *
     * @param termo trecho usado na pesquisa
     * @return entidades correspondentes ao termo
     */
    @Override
    public List<AtividadeEconomicaCnaeEntity> buscarPorDescricao(String termo) {
        return repository.findByDescricaoContainingIgnoreCaseOrderByCodigo(termo);
    }

    /**
     * Localiza um registro pelo codigo CNAE exato.
     *
     * @param codigo codigo CNAE procurado
     * @return entidade encontrada ou {@link Optional#empty()} quando ausente
     */
    @Override
    public Optional<AtividadeEconomicaCnaeEntity> buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    /**
     * Retorna uma referencia JPA para o registro informado.
     *
     * @param id identificador da entidade
     * @return referencia JPA para o registro solicitado
     */
    @Override
    public AtividadeEconomicaCnaeEntity buscarPorId(Long id) {
        return repository.getReferenceById(id);
    }
}
