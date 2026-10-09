package com.porto.testecnae.application.ports.in;

/**
 * Comando de entrada usado para registrar um cadastro secundario.
 * Contem os dados necessarios para validar o CNAE e persistir o cadastro.
 *
 * @param nomeFantasia nome fantasia do cadastro solicitado
 * @param documento documento associado ao cadastro
 * @param codigoCnae codigo CNAE que deve ser validado
 */
public record CadastrarCadastroSecundarioCommand(
        String nomeFantasia,
        String documento,
        String codigoCnae
) {
}
