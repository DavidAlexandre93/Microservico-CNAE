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

@RestController
@RequiredArgsConstructor
public class CadastroSecundarioController implements CadastrosSecundariosApi {

    private final CadastroSecundarioInputPort inputPort;

    @Override
    public ResponseEntity<CadastroSecundarioResponse> cadastrarCadastroSecundario(
            CadastroSecundarioRequest request
    ) {
        var response = CnaeApiMapper.toResponse(inputPort.cadastrar(CnaeApiMapper.toCommand(request)));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<CadastroSecundarioResponse>> listarCadastrosSecundarios() {
        var response = inputPort.listarTodos()
                .stream()
                .map(CnaeApiMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AtividadeEconomicaCnaeResponse> validarCnae(String codigoCnae) {
        return ResponseEntity.ok(CnaeApiMapper.toResponse(inputPort.validarCnae(codigoCnae)));
    }
}
