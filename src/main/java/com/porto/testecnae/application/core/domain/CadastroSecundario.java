package com.porto.testecnae.application.core.domain;

/**
 * Representa um cadastro secundario vinculado a uma atividade economica CNAE.
 * Mantem o relacionamento entre a entidade de negocio e o CNAE validado.
 *
 * @param id identificador do cadastro persistido
 * @param nomeFantasia nome fantasia do cadastro
 * @param documento documento associado ao cadastro
 * @param cnae atividade economica vinculada ao cadastro
 */
public record CadastroSecundario(
        Long id,
        String nomeFantasia,
        String documento,
        AtividadeEconomicaCnae cnae
) {

    /**
     * Cria uma nova instancia de cadastro com id ainda nulo.
      *
      * @param nomeFantasia nome fantasia do cadastro
      * @param documento documento associado ao cadastro
      * @param cnae atividade economica vinculada ao cadastro
      * @return cadastro novo, ainda sem identificador persistido
     */
    public static CadastroSecundario novo(
            String nomeFantasia,
            String documento,
            AtividadeEconomicaCnae cnae
    ) {
        return new CadastroSecundario(null, nomeFantasia, documento, cnae);
    }
}
