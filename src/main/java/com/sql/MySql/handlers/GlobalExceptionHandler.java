package com.sql.MySql.handlers;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.sql.MySql.response.ResponseMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessage> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleGenericException(Exception ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Validation failed: " + ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
}
