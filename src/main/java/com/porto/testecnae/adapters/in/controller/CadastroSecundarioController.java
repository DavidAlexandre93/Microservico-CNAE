package com.porto.testecnae.adapters.in.controller;

import com.porto.testecnae.adapters.in.api.CadastrosSecundariosApi;
import com.porto.testecnae.adapters.in.api.model.AtividadeEconomicaCnaeResponse;
import com.porto.testecnae.adapters.in.api.model.CadastroSecundarioRequest;
import com.porto.testecnae.adapters.in.api.model.CadastroSecundarioResponse;
import com.porto.testecnae.adapters.in.controller.mapper.CnaeApiMapper;
import com.porto.testecnae.application.ports.in.CadastroSecundarioInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Adaptador HTTP para criacao, consulta e validacao de cadastros secundarios.
 * Traduz os DTOs da API e encaminha as operacoes a porta de entrada.
 */
@RestController
@RequiredArgsConstructor
public class CadastroSecundarioController implements CadastrosSecundariosApi {

    private final CadastroSecundarioInputPort inputPort;
    private final CnaeApiMapper mapper;

    /**
     * Cria um cadastro secundario e responde com HTTP 201.
     *
     * @param request dados enviados para criacao do cadastro
     * @return resposta HTTP com o cadastro criado
     */
    @Override
    public ResponseEntity<CadastroSecundarioResponse> cadastrarCadastroSecundario(
            CadastroSecundarioRequest request
    ) {
        var response = mapper.toResponse(inputPort.cadastrar(mapper.toCommand(request)));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retorna todos os cadastros secundarios registrados.
     *
     * @return resposta HTTP com a lista de cadastros
     */
    @Override
    public ResponseEntity<List<CadastroSecundarioResponse>> listarCadastrosSecundarios() {
        var response = inputPort.listarTodos()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Confirma a existencia do CNAE informado e retorna seus dados.
     *
     * @param codigoCnae codigo CNAE a validar
     * @return resposta HTTP com os dados do CNAE
     */
    @Override
    public ResponseEntity<AtividadeEconomicaCnaeResponse> validarCnae(String codigoCnae) {
        return ResponseEntity.ok(mapper.toResponse(inputPort.validarCnae(codigoCnae)));
    }
}
