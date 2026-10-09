package com.porto.testecnae.application.ports.in;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;

import java.util.List;

public interface CadastroSecundarioInputPort {

    CadastroSecundario cadastrar(CadastrarCadastroSecundarioCommand command);

    AtividadeEconomicaCnae validarCnae(String codigoCnae);

    List<CadastroSecundario> listarTodos();
}
