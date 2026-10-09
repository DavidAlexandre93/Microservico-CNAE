package com.porto.testecnae.application.ports.out;

import com.porto.testecnae.application.core.domain.CadastroSecundario;

import java.util.List;

public interface CadastroSecundarioOutputPort {

    CadastroSecundario salvar(CadastroSecundario cadastro);

    List<CadastroSecundario> buscarTodos();
}
