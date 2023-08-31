package ru.sevmash.timesheetaccounting.controllers.advice;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.sevmash.timesheetaccounting.exception.PersonNotFound;
import ru.sevmash.timesheetaccounting.model.ErrorMessage;

import java.util.Date;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(PersonNotFound.class)
    public ResponseEntity<ErrorMessage> exceptionPersonNotFound(PersonNotFound personNotFound) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(System.currentTimeMillis()),
                "Пользователь не найден (" + personNotFound.getMessage() + ")",
                "Здесь должно быть описание ошибки"
        );
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> exceptionMicroServiceUnAvailable(Exception exception, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "Сервис не доступен " + exception.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.internalServerError().body(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return message;
    }


}
