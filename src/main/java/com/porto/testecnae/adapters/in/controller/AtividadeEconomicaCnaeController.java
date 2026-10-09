package com.porto.testecnae.adapters.in.controller;

import com.porto.testecnae.adapters.in.api.CnaesApi;
import com.porto.testecnae.adapters.in.api.model.AtividadeEconomicaCnaeResponse;
import com.porto.testecnae.adapters.in.controller.mapper.CnaeApiMapper;
import com.porto.testecnae.application.ports.in.AtividadeEconomicaCnaeInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AtividadeEconomicaCnaeController implements CnaesApi {

    private final AtividadeEconomicaCnaeInputPort inputPort;

    @Override
    public ResponseEntity<AtividadeEconomicaCnaeResponse> buscarCnaePorCodigo(String codigo) {
        return ResponseEntity.ok(CnaeApiMapper.toResponse(inputPort.buscarPorCodigo(codigo)));
    }

    @Override
    public ResponseEntity<List<AtividadeEconomicaCnaeResponse>> buscarCnaesPorDescricao(String termo) {
        var response = inputPort.buscarPorDescricao(termo)
                .stream()
                .map(CnaeApiMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<AtividadeEconomicaCnaeResponse>> listarCnaes() {
        var response = inputPort.listarTodas()
                .stream()
                .map(CnaeApiMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
