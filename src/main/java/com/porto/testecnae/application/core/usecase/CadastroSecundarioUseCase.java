package com.porto.testecnae.application.core.usecase;

import com.porto.testecnae.application.core.domain.AtividadeEconomicaCnae;
import com.porto.testecnae.application.core.domain.CadastroSecundario;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import com.porto.testecnae.application.ports.in.CadastrarCadastroSecundarioCommand;
import com.porto.testecnae.application.ports.in.CadastroSecundarioInputPort;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import com.porto.testecnae.application.ports.out.CadastroSecundarioOutputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CadastroSecundarioUseCase implements CadastroSecundarioInputPort {

    private static final Logger log = LoggerFactory.getLogger(CadastroSecundarioUseCase.class);

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
        log.info("Cadastrando cadastro secundario para o CNAE: {}", command.codigoCnae());
        var cnae = buscarCnae(command.codigoCnae());
        var cadastro = CadastroSecundario.novo(command.nomeFantasia(), command.documento(), cnae);

        return cadastroOutputPort.salvar(cadastro);
    }

    @Override
    public AtividadeEconomicaCnae validarCnae(String codigoCnae) {
        log.info("Validando CNAE: {}", codigoCnae);
        return buscarCnae(codigoCnae);
    }

    @Override
    public List<CadastroSecundario> listarTodos() {
        log.info("Listando cadastros secundarios");
        return cadastroOutputPort.buscarTodos();
    }

    private AtividadeEconomicaCnae buscarCnae(String codigoCnae) {
        return cnaeOutputPort.buscarPorCodigo(codigoCnae)
                .orElseThrow(() -> {
                    log.warn("Tentativa de cadastro/validacao com CNAE inexistente: {}", codigoCnae);
                    return new CnaeNaoEncontradoException(codigoCnae);
                });
    }
}
