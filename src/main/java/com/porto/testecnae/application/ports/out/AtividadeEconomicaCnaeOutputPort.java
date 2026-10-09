package com.porto.testecnae.application.ports.out;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;

import java.util.List;
import java.util.Optional;

public interface AtividadeEconomicaCnaeOutputPort {

    List<AtividadeEconomicaCnae> buscarTodas();

    List<AtividadeEconomicaCnae> buscarPorDescricao(String termo);

    Optional<AtividadeEconomicaCnae> buscarPorCodigo(String codigo);
}
