package com.porto.testecnae.adapters.out.repository.dao;

import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;

import java.util.List;

public interface CadastroSecundarioDao {

    CadastroSecundarioEntity salvar(CadastroSecundarioEntity cadastro);

    List<CadastroSecundarioEntity> buscarTodosComCnae();
}
