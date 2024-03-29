package br.org.fundatec.poctabd.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorDTO> handleNotFound(NoSuchElementException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(buildError("animal não encontrado"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ResponseEntity<ApiErrorDTO> handleUnkownError(Throwable e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(buildError("Este erro é desconhecido, então não temos um tratamento para isso ainda"),
                HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ApiErrorDTO> handleEmptyList(Throwable e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(buildError("Nenhum animal encontrado"), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorDTO> handleConstraintError(MethodArgumentTypeMismatchException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(buildError("Este método espera um parametro de tipo diferente"), HttpStatus.BAD_REQUEST);
    }

    private ApiErrorDTO buildError(String message) {
        return new ApiErrorDTO(message, LocalDateTime.now());
    }

    private String getSearchKey(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
