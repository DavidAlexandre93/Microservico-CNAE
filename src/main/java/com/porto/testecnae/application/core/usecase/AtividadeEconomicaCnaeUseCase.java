package com.porto.testecnae.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.ports.in.AtividadeEconomicaCnaeInputPort;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;

import java.util.List;

/**
 * Caso de uso responsavel por consultar informacoes de CNAE.
 * Encapsula a regra de negocio para listagem e busca por codigo ou descricao.
 */
public class AtividadeEconomicaCnaeUseCase implements AtividadeEconomicaCnaeInputPort {

    private final AtividadeEconomicaCnaeOutputPort outputPort;

    /**
     * Cria o caso de uso com a porta de consulta de CNAEs.
     *
     * @param outputPort porta usada para consultar os dados de CNAE
     */
    public AtividadeEconomicaCnaeUseCase(AtividadeEconomicaCnaeOutputPort outputPort) {
        this.outputPort = outputPort;
    }

    /**
     * Lista todas as atividades economicas disponiveis.
      *
      * @return lista de atividades economicas
     */
    @Override
    public List<AtividadeEconomicaCnae> listarTodas() {
        return outputPort.buscarTodas();
    }

    /**
     * Busca atividades cuja descricao contenha o termo informado.
      *
      * @param termo trecho usado na pesquisa da descricao
      * @return atividades cuja descricao corresponde ao termo
     */
    @Override
    public List<AtividadeEconomicaCnae> buscarPorDescricao(String termo) {
        return outputPort.buscarPorDescricao(termo);
    }

    /**
     * Busca um CNAE pelo codigo e lanca excecao caso nao exista.
      *
      * @param codigo codigo CNAE procurado
      * @return atividade economica correspondente ao codigo
      * @throws com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException se o codigo nao existir
     */
    @Override
    public AtividadeEconomicaCnae buscarPorCodigo(String codigo) {
        return outputPort.buscarPorCodigo(codigo)
                .orElseThrow(() -> new com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException(codigo));
    }
}
