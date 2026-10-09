package com.porto.testecnae.application.ports.in;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;

import java.util.List;

public interface AtividadeEconomicaCnaeInputPort {

    List<AtividadeEconomicaCnae> listarTodas();

    List<AtividadeEconomicaCnae> buscarPorDescricao(String termo);

    AtividadeEconomicaCnae buscarPorCodigo(String codigo);
}
