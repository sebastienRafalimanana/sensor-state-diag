package fr.sensorintegration.core.exception;

import fr.sensorintegration.core.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> badRequestException(BadRequestException e){
       ErrorModel errorMessage = ErrorModel.builder()
                .message(e.getMessage())
                .success(false)
                .timestamp(new Date())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> notFoundException(NotFoundException e){
        ErrorModel errorMessage = ErrorModel.builder()
                .message(e.getMessage())
                .success(false)
                .timestamp(new Date())
                .code(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {OperationNotAllowedException.class})
    public ResponseEntity<Object> operationNotAllowedException(OperationNotAllowedException ex) {
        ErrorModel errorMessage = ErrorModel.builder()
                .message(ex.getMessage())
                .success(false)
                .timestamp(new Date())
                .code(HttpStatus.FORBIDDEN.value())
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> runtimeException(RuntimeException ex) {
        ErrorModel errorMessage = ErrorModel.builder()
                .message(ex.getMessage())
                .success(false)
                .timestamp(new Date())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
