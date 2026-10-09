package com.porto.testecnae.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.ports.in.AtividadeEconomicaCnaeInputPort;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;

import java.util.List;

public class AtividadeEconomicaCnaeUseCase implements AtividadeEconomicaCnaeInputPort {

    private final AtividadeEconomicaCnaeOutputPort outputPort;

    public AtividadeEconomicaCnaeUseCase(AtividadeEconomicaCnaeOutputPort outputPort) {
        this.outputPort = outputPort;
    }

    @Override
    public List<AtividadeEconomicaCnae> listarTodas() {
        return outputPort.buscarTodas();
    }

    @Override
    public List<AtividadeEconomicaCnae> buscarPorDescricao(String termo) {
        return outputPort.buscarPorDescricao(termo);
    }

    @Override
    public AtividadeEconomicaCnae buscarPorCodigo(String codigo) {
        return outputPort.buscarPorCodigo(codigo)
                .orElseThrow(() -> new com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException(codigo));
    }
}
