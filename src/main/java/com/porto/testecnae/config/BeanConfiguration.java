package com.porto.testecnae.config;

import com.porto.testecnae.application.core.usecase.AtividadeEconomicaCnaeUseCase;
import com.porto.testecnae.application.core.usecase.CadastroSecundarioUseCase;
import com.porto.testecnae.application.ports.in.AtividadeEconomicaCnaeInputPort;
import com.porto.testecnae.application.ports.in.CadastroSecundarioInputPort;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import com.porto.testecnae.application.ports.out.CadastroSecundarioOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Registra os casos de uso como implementacoes das portas de entrada. */
@Configuration
public class BeanConfiguration {

    /**
     * Cria o caso de uso de consulta de atividades CNAE.
     *
     * @param outputPort porta de acesso aos dados CNAE
     * @return implementacao da porta de entrada para consultas CNAE
     */
    @Bean
    AtividadeEconomicaCnaeInputPort atividadeEconomicaCnaeInputPort(
            AtividadeEconomicaCnaeOutputPort outputPort
    ) {
        return new AtividadeEconomicaCnaeUseCase(outputPort);
    }

    /**
     * Cria o caso de uso de cadastro secundario com suas portas de persistencia.
     *
     * @param cadastroOutputPort porta de persistencia dos cadastros
     * @param cnaeOutputPort porta de consulta dos CNAEs
     * @return implementacao da porta de entrada para cadastros secundarios
     */
    @Bean
    CadastroSecundarioInputPort cadastroSecundarioInputPort(
            CadastroSecundarioOutputPort cadastroOutputPort,
            AtividadeEconomicaCnaeOutputPort cnaeOutputPort
    ) {
        return new CadastroSecundarioUseCase(cadastroOutputPort, cnaeOutputPort);
    }
}
