package com.porto.testecnae.adapters.in.controller;

import com.porto.testecnae.adapters.in.api.CnaesApi;
import com.porto.testecnae.adapters.in.api.model.AtividadeEconomicaCnaeResponse;
import com.porto.testecnae.adapters.in.controller.mapper.CnaeApiMapper;
import com.porto.testecnae.application.ports.in.AtividadeEconomicaCnaeInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Adaptador HTTP que expoe as consultas de atividades economicas CNAE.
 * Delega as regras aos casos de uso e converte os resultados para DTOs da API.
 */
@RestController
@RequiredArgsConstructor
public class AtividadeEconomicaCnaeController implements CnaesApi {

    private final AtividadeEconomicaCnaeInputPort inputPort;
    private final CnaeApiMapper mapper;

    /**
     * Busca uma atividade economica pelo codigo CNAE.
     *
     * @param codigo codigo CNAE procurado
     * @return resposta HTTP com os dados da atividade
     */
    @Override
    public ResponseEntity<AtividadeEconomicaCnaeResponse> buscarCnaePorCodigo(String codigo) {
        return ResponseEntity.ok(mapper.toResponse(inputPort.buscarPorCodigo(codigo)));
    }

    /**
     * Busca atividades economicas por um termo presente na descricao.
     *
     * @param termo trecho usado na pesquisa
     * @return resposta HTTP com as atividades correspondentes
     */
    @Override
    public ResponseEntity<List<AtividadeEconomicaCnaeResponse>> buscarCnaesPorDescricao(String termo) {
        var response = inputPort.buscarPorDescricao(termo)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as atividades economicas CNAE cadastradas.
     *
     * @return resposta HTTP com todas as atividades
     */
    @Override
    public ResponseEntity<List<AtividadeEconomicaCnaeResponse>> listarCnaes() {
        var response = inputPort.listarTodas()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
