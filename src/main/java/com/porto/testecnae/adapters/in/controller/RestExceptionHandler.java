package com.porto.testecnae.adapters.in.controller;

import com.porto.testecnae.adapters.in.api.model.ApiErrorResponse;
import com.porto.testecnae.application.core.exception.CnaeNaoEncontradoException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Converte excecoes da aplicacao e da requisicao em respostas HTTP padronizadas.
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * Responde com HTTP 404 quando o codigo CNAE nao existe.
     *
     * @param exception excecao lancada ao consultar o codigo
     * @return resposta de erro HTTP 404
     */
    @ExceptionHandler(CnaeNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleCnaeNaoEncontrado(CnaeNaoEncontradoException exception) {
        log.warn("CNAE nao encontrado na requisicao: {}", exception.getMessage());
        return error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Responde com HTTP 404 para recursos ou rotas inexistentes.
     *
     * @return resposta de erro HTTP 404
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRecursoNaoEncontrado() {
        log.warn("Recurso nao encontrado na requisicao");
        return error(HttpStatus.NOT_FOUND, "Recurso nao encontrado");
    }

    /**
     * Responde com HTTP 400 para parametros ou corpo invalidos.
     *
     * @return resposta de erro HTTP 400
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiErrorResponse> handleRequisicaoInvalida() {
        log.warn("Parametros ou corpo da requisicao invalidos");
        return error(HttpStatus.BAD_REQUEST, "Parametros ou corpo da requisicao invalidos");
    }

    /**
     * Registra falhas inesperadas e evita expor detalhes internos ao cliente.
     *
     * @param exception falha inesperada durante o processamento
     * @return resposta de erro HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleErroInesperado(Exception exception) {
        log.error("Erro inesperado ao processar a requisicao", exception);

        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }

    /**
     * Monta a representacao padrao de erro para o status e a mensagem recebidos.
     *
     * @param status status HTTP da resposta
     * @param mensagem descricao apresentada ao cliente
     * @return resposta HTTP com o corpo de erro padronizado
     */
    private ResponseEntity<ApiErrorResponse> error(HttpStatus status, String mensagem) {
        var response = new ApiErrorResponse(status.value(), status.getReasonPhrase(), mensagem);
        return ResponseEntity.status(status).body(response);
    }
}
