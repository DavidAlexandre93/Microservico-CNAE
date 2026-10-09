package com.porto.testecnae.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import com.porto.testecnae.application.ports.in.CadastrarCadastroSecundarioCommand;
import com.porto.testecnae.application.ports.in.CadastroSecundarioInputPort;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import com.porto.testecnae.application.ports.out.CadastroSecundarioOutputPort;

import java.util.List;

public class CadastroSecundarioUseCase implements CadastroSecundarioInputPort {

    private final CadastroSecundarioOutputPort cadastroOutputPort;
    private final AtividadeEconomicaCnaeOutputPort cnaeOutputPort;

    public CadastroSecundarioUseCase(
            CadastroSecundarioOutputPort cadastroOutputPort,
            AtividadeEconomicaCnaeOutputPort cnaeOutputPort
    ) {
        this.cadastroOutputPort = cadastroOutputPort;
        this.cnaeOutputPort = cnaeOutputPort;
    }

    @Override
    public CadastroSecundario cadastrar(CadastrarCadastroSecundarioCommand command) {
        var cnae = buscarCnae(command.codigoCnae());
        var cadastro = CadastroSecundario.novo(command.nomeFantasia(), command.documento(), cnae);

        return cadastroOutputPort.salvar(cadastro);
    }

    @Override
    public AtividadeEconomicaCnae validarCnae(String codigoCnae) {
        return buscarCnae(codigoCnae);
    }

    @Override
    public List<CadastroSecundario> listarTodos() {
        return cadastroOutputPort.buscarTodos();
    }

    private AtividadeEconomicaCnae buscarCnae(String codigoCnae) {
        return cnaeOutputPort.buscarPorCodigo(codigoCnae)
                .orElseThrow(() -> new CnaeNaoEncontradoException(codigoCnae));
    }
}
