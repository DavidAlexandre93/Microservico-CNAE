package com.porto.testecnae.application.core.exception;

/**
 * Excecao de dominio disparada quando um codigo CNAE informado nao existe.
 */
public class CnaeNaoEncontradoException extends RuntimeException {

    /**
     * Cria a excecao com a mensagem padronizada do codigo nao encontrado.
      *
      * @param codigo codigo CNAE que nao foi localizado
     */
    public CnaeNaoEncontradoException(String codigo) {
        super("CNAE nao encontrado para o codigo: " + codigo);
    }
}
