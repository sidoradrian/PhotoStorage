package com.nuadu.rest.exception.handler;

import com.nuadu.rest.exception.BaseNotFoundException;
import com.nuadu.rest.exception.FileStorageException;
import com.nuadu.rest.service.FileStorageService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Class to handle general exception and create custom error response.
 *
 * @author Adrian Sidor
 */
@ControllerAdvice
public class GeneralExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class, UnsupportedOperationException.class,
            BaseNotFoundException.class, FileStorageException.class})
    public ErrorMessage handleWithGeneralException(RuntimeException ex) {
        return new ErrorMessage(BAD_REQUEST.value(), ex.getLocalizedMessage());
    }

    private class ErrorMessage {
        private final int status;
        private final String message;

        ErrorMessage(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
