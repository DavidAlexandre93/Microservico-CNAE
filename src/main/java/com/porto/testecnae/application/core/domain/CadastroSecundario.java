package com.porto.testecnae.application.core.domain;

public record CadastroSecundario(
        Long id,
        String nomeFantasia,
        String documento,
        AtividadeEconomicaCnae cnae
) {

    public static CadastroSecundario novo(
            String nomeFantasia,
            String documento,
            AtividadeEconomicaCnae cnae
    ) {
        return new CadastroSecundario(null, nomeFantasia, documento, cnae);
    }
}
