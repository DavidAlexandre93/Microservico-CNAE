package com.porto.testecnae.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.ports.in.CadastrarCadastroSecundarioCommand;
import com.porto.testecnae.application.ports.in.CadastroSecundarioInputPort;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import com.porto.testecnae.application.ports.out.CadastroSecundarioOutputPort;

import java.util.List;

/**
 * Caso de uso responsavel por validar CNAEs e registrar cadastros secundarios.
 * Mantem a regra de negocio de integridade entre o cadastro e o CNAE existente.
 */
public class CadastroSecundarioUseCase implements CadastroSecundarioInputPort {

    private final CadastroSecundarioOutputPort cadastroOutputPort;
    private final AtividadeEconomicaCnaeOutputPort cnaeOutputPort;

    /**
     * Cria o caso de uso com as portas de persistencia e consulta de CNAEs.
     *
     * @param cadastroOutputPort porta usada para persistir e consultar cadastros
     * @param cnaeOutputPort porta usada para consultar atividades CNAE
     */
    public CadastroSecundarioUseCase(
            CadastroSecundarioOutputPort cadastroOutputPort,
            AtividadeEconomicaCnaeOutputPort cnaeOutputPort
    ) {
        this.cadastroOutputPort = cadastroOutputPort;
        this.cnaeOutputPort = cnaeOutputPort;
    }

    /**
     * Cria um cadastro secundario apenas quando o CNAE informado existe.
      *
      * @param command dados usados para criar e validar o cadastro
      * @return cadastro persistido
      * @throws com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException se o CNAE informado nao existir
     */
    @Override
    public CadastroSecundario cadastrar(CadastrarCadastroSecundarioCommand command) {
        var cnae = buscarCnae(command.codigoCnae());
        var cadastro = CadastroSecundario.novo(command.nomeFantasia(), command.documento(), cnae);

        return cadastroOutputPort.salvar(cadastro);
    }

    /**
     * Valida se o codigo CNAE informado pode ser usado em um cadastro.
      *
      * @param codigoCnae codigo CNAE a validar
      * @return atividade economica correspondente ao codigo
      * @throws com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException se o codigo nao existir
     */
    @Override
    public AtividadeEconomicaCnae validarCnae(String codigoCnae) {
        return buscarCnae(codigoCnae);
    }

    /**
     * Lista os cadastros secundarios persistidos.
      *
      * @return lista de cadastros secundarios
     */
    @Override
    public List<CadastroSecundario> listarTodos() {
        return cadastroOutputPort.buscarTodos();
    }

    /**
     * Busca um CNAE pelo codigo e dispara excecao quando nao for localizado.
      *
      * @param codigoCnae codigo CNAE procurado
      * @return atividade economica correspondente ao codigo
      * @throws com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException se o codigo nao existir
     */
    private AtividadeEconomicaCnae buscarCnae(String codigoCnae) {
        return cnaeOutputPort.buscarPorCodigo(codigoCnae)
                .orElseThrow(() -> new com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException(codigoCnae));
    }
}
