package com.porto.testecnae.adapters.in.controller.mapper;

import com.porto.testecnae.adapters.in.api.model.AtividadeEconomicaCnaeResponse;
import com.porto.testecnae.adapters.in.api.model.CadastroSecundarioRequest;
import com.porto.testecnae.adapters.in.api.model.CadastroSecundarioResponse;
import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.ports.in.CadastrarCadastroSecundarioCommand;

public final class CnaeApiMapper {

    private CnaeApiMapper() {
    }

    public static AtividadeEconomicaCnaeResponse toResponse(AtividadeEconomicaCnae atividade) {
        return new AtividadeEconomicaCnaeResponse(
                atividade.id(),
                atividade.codigo(),
                atividade.descricao(),
                atividade.secao()
        );
    }

    public static CadastroSecundarioResponse toResponse(CadastroSecundario cadastro) {
        return new CadastroSecundarioResponse(
                cadastro.id(),
                cadastro.nomeFantasia(),
                cadastro.documento(),
                toResponse(cadastro.cnae())
        );
    }

    public static CadastrarCadastroSecundarioCommand toCommand(CadastroSecundarioRequest request) {
        return new CadastrarCadastroSecundarioCommand(
                request.getNomeFantasia(),
                request.getDocumento(),
                request.getCodigoCnae()
        );
    }
}
