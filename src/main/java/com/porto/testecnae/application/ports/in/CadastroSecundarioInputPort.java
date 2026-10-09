package com.porto.testecnae.application.ports.in;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;

import java.util.List;

/**
 * Contrato de entrada para operacoes relacionadas a cadastros secundarios.
 * Define os casos de uso expostos para criacao, validacao e consulta.
 */
public interface CadastroSecundarioInputPort {

    /**
     * Cria um cadastro secundario a partir do comando recebido da camada HTTP.
     *
     * @param command dados usados para criar e validar o cadastro
     * @return cadastro persistido
     * @throws com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException se o CNAE informado nao existir
     */
    CadastroSecundario cadastrar(CadastrarCadastroSecundarioCommand command);

    /**
     * Valida se o codigo CNAE informado existe e pode ser usado no cadastro.
     *
     * @param codigoCnae codigo CNAE a validar
     * @return atividade economica correspondente ao codigo
     * @throws com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException se o codigo nao existir
     */
    AtividadeEconomicaCnae validarCnae(String codigoCnae);

    /**
     * Lista todos os cadastros secundarios ja registrados.
     *
     * @return lista de cadastros secundarios
     */
    List<CadastroSecundario> listarTodos();
}
