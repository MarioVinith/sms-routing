package com.sinch.smsrouting.web.rest.exception;

import com.sinch.smsrouting.exception.NumberValidationException;
import com.sinch.smsrouting.exception.OptedOutException;
import com.sinch.smsrouting.model.Message;
import com.sinch.smsrouting.model.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OptedOutException.class)
    public ResponseEntity<Message> handleOptedOutException(OptedOutException ex) {
        Message message = new Message();
        message.setId(ex.getMessage());
        message.setStatus(Status.BLOCKED);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberValidationException.class)
    public ResponseEntity<Message> handleNumberValidationException(NumberValidationException ex) {
        Message message = new Message();
        message.setId(ex.getMessage());
        message.setStatus(Status.BLOCKED);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
