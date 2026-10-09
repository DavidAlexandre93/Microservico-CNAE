package com.porto.testecnae.application.core.domain;

/**
 * Representa uma atividade economica CNAE do dominio da aplicacao.
 * Mantem apenas a informacao de negocio relevante para consulta e validacao.
 *
 * @param id identificador da atividade persistida
 * @param codigo codigo CNAE
 * @param descricao descricao da atividade economica
 * @param secao secao CNAE a que a atividade pertence
 */
public record AtividadeEconomicaCnae(
        Long id,
        String codigo,
        String descricao,
        String secao
) {
}
