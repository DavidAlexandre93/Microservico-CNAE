package com.porto.testecnae.adapters.out.repository.dao;

import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;

import java.util.List;
import java.util.Optional;

public interface AtividadeEconomicaCnaeDao {

    List<AtividadeEconomicaCnaeEntity> buscarTodas();

    List<AtividadeEconomicaCnaeEntity> buscarPorDescricao(String termo);

    Optional<AtividadeEconomicaCnaeEntity> buscarPorCodigo(String codigo);

    AtividadeEconomicaCnaeEntity buscarPorId(Long id);
}
