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

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(CnaeNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleCnaeNaoEncontrado(CnaeNaoEncontradoException exception) {
        var status = HttpStatus.NOT_FOUND;
        var response = new ApiErrorResponse(status.value(), status.getReasonPhrase(), exception.getMessage());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiErrorResponse> handleRequisicaoInvalida(Exception exception) {
        var status = HttpStatus.BAD_REQUEST;
        var response = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Parametros ou corpo da requisicao invalidos"
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleErroInesperado(Exception exception) {
        log.error("Erro inesperado ao processar a requisicao", exception);

        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var response = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Erro interno do servidor"
        );

        return ResponseEntity.status(status).body(response);
    }
}
