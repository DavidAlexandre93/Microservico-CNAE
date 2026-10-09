package com.porto.testecnae.application.ports.in;

public record CadastrarCadastroSecundarioCommand(
        String nomeFantasia,
        String documento,
        String codigoCnae
) {
}
