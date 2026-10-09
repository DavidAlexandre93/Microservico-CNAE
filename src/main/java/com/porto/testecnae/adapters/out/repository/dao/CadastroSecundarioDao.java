package com.porto.testecnae.adapters.out.repository.dao;

import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;

import java.util.List;

/** Acesso a dados de cadastros secundarios oferecido a camada de persistencia. */
public interface CadastroSecundarioDao {

    /**
     * Persiste uma entidade de cadastro secundario.
     *
     * @param cadastro entidade de cadastro a persistir
     * @return entidade persistida
     */
    CadastroSecundarioEntity salvar(CadastroSecundarioEntity cadastro);

    /**
     * Busca cadastros carregando junto a associacao CNAE.
     *
     * @return entidades de cadastro com seus CNAEs
     */
    List<CadastroSecundarioEntity> buscarTodosComCnae();
}
