package com.porto.testecnae.application.ports.in;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;

import java.util.List;

/**
 * Contrato de entrada para consultas de atividades economicas CNAE.
 * Centraliza as operacoes que a camada HTTP pode solicitar ao caso de uso.
 */
public interface AtividadeEconomicaCnaeInputPort {

    /**
     * Retorna todas as atividades economicas cadastradas.
     *
     * @return lista de atividades economicas
     */
    List<AtividadeEconomicaCnae> listarTodas();

    /**
     * Busca atividades cujo texto da descricao contenha o termo informado.
     *
     * @param termo trecho usado na pesquisa da descricao
     * @return atividades cuja descricao corresponde ao termo
     */
    List<AtividadeEconomicaCnae> buscarPorDescricao(String termo);

    /**
     * Busca uma atividade economica pelo codigo CNAE informado.
     *
     * @param codigo codigo CNAE procurado
     * @return atividade economica correspondente ao codigo
     * @throws com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException se o codigo nao existir
     */
    AtividadeEconomicaCnae buscarPorCodigo(String codigo);
}
