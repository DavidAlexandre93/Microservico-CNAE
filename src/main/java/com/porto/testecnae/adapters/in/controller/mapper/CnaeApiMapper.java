package com.porto.testecnae.adapters.in.controller.mapper;

import com.porto.testecnae.adapters.in.api.model.AtividadeEconomicaCnaeResponse;
import com.porto.testecnae.adapters.in.api.model.CadastroSecundarioRequest;
import com.porto.testecnae.adapters.in.api.model.CadastroSecundarioResponse;
import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.ports.in.CadastrarCadastroSecundarioCommand;
import org.mapstruct.Mapper;

/**
 * Converte objetos do dominio em DTOs HTTP e requisicoes em comandos de aplicacao.
 */
@Mapper(componentModel = "spring")
public interface CnaeApiMapper {

    /**
     * Converte uma atividade economica do dominio para o DTO de resposta.
     *
     * @param atividade atividade economica a converter
     * @return DTO de resposta da atividade
     */
    AtividadeEconomicaCnaeResponse toResponse(AtividadeEconomicaCnae atividade);

    /**
     * Converte um cadastro secundario do dominio para o DTO de resposta.
     *
     * @param cadastro cadastro secundario a converter
     * @return DTO de resposta do cadastro
     */
    CadastroSecundarioResponse toResponse(CadastroSecundario cadastro);

    /**
     * Converte o DTO recebido na API para o comando de cadastro da aplicacao.
     *
     * @param request DTO recebido na requisicao
     * @return comando de cadastro para a camada de aplicacao
     */
    CadastrarCadastroSecundarioCommand toCommand(CadastroSecundarioRequest request);
}
