package com.porto.testecnae.application.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CadastroSecundarioTest {

    @Test
    void deveCriarNovoCadastroSemIdentificador() {
        var cnae = new AtividadeEconomicaCnae(5L, "6201-5/01", "Desenvolvimento", "Tecnologia");

        var cadastro = CadastroSecundario.novo("Tech Porto", "12345678000199", cnae);

        assertAll(
                () -> assertNull(cadastro.id()),
                () -> assertEquals("Tech Porto", cadastro.nomeFantasia()),
                () -> assertEquals("12345678000199", cadastro.documento()),
                () -> assertEquals(cnae, cadastro.cnae())
        );
    }
}
