package com.porto.testecnae.application.ports.out;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;

import java.util.List;
import java.util.Optional;

/** Contrato de saida para consultas de atividades economicas na persistencia. */
public interface AtividadeEconomicaCnaeOutputPort {

    /**
     * Recupera todas as atividades economicas disponiveis.
     *
     * @return lista de atividades economicas
     */
    List<AtividadeEconomicaCnae> buscarTodas();

    /**
     * Recupera as atividades cuja descricao corresponde ao termo informado.
     *
     * @param termo trecho usado na pesquisa da descricao
     * @return atividades cuja descricao corresponde ao termo
     */
    List<AtividadeEconomicaCnae> buscarPorDescricao(String termo);

    /**
     * Recupera uma atividade pelo codigo, sem transformar ausencia em excecao.
     *
     * @param codigo codigo CNAE procurado
     * @return atividade encontrada ou {@link Optional#empty()} quando ausente
     */
    Optional<AtividadeEconomicaCnae> buscarPorCodigo(String codigo);
}
