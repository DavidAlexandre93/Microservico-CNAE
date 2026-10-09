package com.porto.testecnae.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import com.porto.testecnae.application.ports.in.AtividadeEconomicaCnaeInputPort;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AtividadeEconomicaCnaeUseCase implements AtividadeEconomicaCnaeInputPort {

    private static final Logger log = LoggerFactory.getLogger(AtividadeEconomicaCnaeUseCase.class);

    private final AtividadeEconomicaCnaeOutputPort outputPort;

    public AtividadeEconomicaCnaeUseCase(AtividadeEconomicaCnaeOutputPort outputPort) {
        this.outputPort = outputPort;
    }

    @Override
    public List<AtividadeEconomicaCnae> listarTodas() {
        log.info("Listando todas as atividades economicas CNAE");
        return outputPort.buscarTodas();
    }

    @Override
    public List<AtividadeEconomicaCnae> buscarPorDescricao(String termo) {
        log.info("Buscando CNAEs por descricao: {}", termo);
        return outputPort.buscarPorDescricao(termo);
    }

    @Override
    public AtividadeEconomicaCnae buscarPorCodigo(String codigo) {
        log.info("Buscando CNAE por codigo: {}", codigo);
        return outputPort.buscarPorCodigo(codigo)
                .orElseThrow(() -> {
                    log.warn("CNAE nao encontrado para o codigo: {}", codigo);
                    return new CnaeNaoEncontradoException(codigo);
                });
    }
}
