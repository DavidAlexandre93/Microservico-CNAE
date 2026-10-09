package com.porto.testecnae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Ponto de entrada que inicializa a aplicacao Spring Boot. */
@SpringBootApplication
public class TesteCnaeApplication {

    /**
     * Inicializa o contexto Spring e inicia o servidor da aplicacao.
     *
     * @param args argumentos de inicializacao da aplicacao
     */
    public static void main(String[] args) {
        SpringApplication.run(TesteCnaeApplication.class, args);
    }
}
