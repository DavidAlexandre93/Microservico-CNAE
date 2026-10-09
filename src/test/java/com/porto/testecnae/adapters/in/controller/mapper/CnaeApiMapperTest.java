package com.porto.testecnae.adapters.in.controller.mapper;

import com.porto.testecnae.adapters.in.api.model.CadastroSecundarioRequest;
import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CnaeApiMapperTest {

    private static final AtividadeEconomicaCnae CNAE = new AtividadeEconomicaCnae(
            5L,
            "6201-5/01",
            "Desenvolvimento de programas de computador sob encomenda",
            "Tecnologia"
    );

    @Test
    void deveMapearCnaeDoDominioParaResposta() {
        var response = CnaeApiMapper.toResponse(CNAE);

        assertAll(
                () -> assertEquals(CNAE.id(), response.getId()),
                () -> assertEquals(CNAE.codigo(), response.getCodigo()),
                () -> assertEquals(CNAE.descricao(), response.getDescricao()),
                () -> assertEquals(CNAE.secao(), response.getSecao())
        );
    }

    @Test
    void deveMapearCadastroDoDominioParaRespostaComCnae() {
        var cadastro = new CadastroSecundario(10L, "Tech Porto", "12345678000199", CNAE);

        var response = CnaeApiMapper.toResponse(cadastro);

        assertAll(
                () -> assertEquals(cadastro.id(), response.getId()),
                () -> assertEquals(cadastro.nomeFantasia(), response.getNomeFantasia()),
                () -> assertEquals(cadastro.documento(), response.getDocumento()),
                () -> assertEquals(CNAE.id(), response.getCnae().getId()),
                () -> assertEquals(CNAE.codigo(), response.getCnae().getCodigo()),
                () -> assertEquals(CNAE.descricao(), response.getCnae().getDescricao()),
                () -> assertEquals(CNAE.secao(), response.getCnae().getSecao())
        );
    }

    @Test
    void deveMapearRequisicaoParaComandoDaAplicacao() {
        var request = new CadastroSecundarioRequest("Tech Porto", "12345678000199", CNAE.codigo());

        var command = CnaeApiMapper.toCommand(request);

        assertAll(
                () -> assertEquals(request.getNomeFantasia(), command.nomeFantasia()),
                () -> assertEquals(request.getDocumento(), command.documento()),
                () -> assertEquals(request.getCodigoCnae(), command.codigoCnae())
        );
    }
}
