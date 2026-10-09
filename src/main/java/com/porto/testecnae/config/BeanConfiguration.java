package com.porto.testecnae.config;

import com.porto.testecnae.application.core.usecase.AtividadeEconomicaCnaeUseCase;
import com.porto.testecnae.application.core.usecase.CadastroSecundarioUseCase;
import com.porto.testecnae.application.ports.in.AtividadeEconomicaCnaeInputPort;
import com.porto.testecnae.application.ports.in.CadastroSecundarioInputPort;
import com.porto.testecnae.application.ports.out.AtividadeEconomicaCnaeOutputPort;
import com.porto.testecnae.application.ports.out.CadastroSecundarioOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    AtividadeEconomicaCnaeInputPort atividadeEconomicaCnaeInputPort(
            AtividadeEconomicaCnaeOutputPort outputPort
    ) {
        return new AtividadeEconomicaCnaeUseCase(outputPort);
    }

    @Bean
    CadastroSecundarioInputPort cadastroSecundarioInputPort(
            CadastroSecundarioOutputPort cadastroOutputPort,
            AtividadeEconomicaCnaeOutputPort cnaeOutputPort
    ) {
        return new CadastroSecundarioUseCase(cadastroOutputPort, cnaeOutputPort);
    }
}
