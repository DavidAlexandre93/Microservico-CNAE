package com.porto.testecnae.application.ports.out;

import com.porto.testecnae.application.core.domain.CadastroSecundario;

import java.util.List;

/** Contrato de saida para gravacao e consulta de cadastros secundarios. */
public interface CadastroSecundarioOutputPort {

    /**
     * Persiste um cadastro e retorna sua representacao atualizada.
     *
     * @param cadastro cadastro que sera persistido
     * @return cadastro persistido com os dados atualizados
     */
    CadastroSecundario salvar(CadastroSecundario cadastro);

    /**
     * Recupera os cadastros secundarios persistidos.
     *
     * @return lista de cadastros secundarios
     */
    List<CadastroSecundario> buscarTodos();
}
