package ru.skypro.skypro_exercises_course4_hw3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {
    @ExceptionHandler
//    @ExceptionHandler(value = {IndexOutOfBoundsException.class, IOException.class})
    public ResponseEntity<?> handlerSQLException(IndexOutOfBoundsException indexOutOfBoundsException) {
        return new ResponseEntity<>("Index out of bounds", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
//    @ExceptionHandler(value = {IndexOutOfBoundsException.class, IOException.class})
    public ResponseEntity<?> handlerNumberFormatException(NumberFormatException numberFormatException) {
        return new ResponseEntity<>("Wrong number format", HttpStatus.BAD_REQUEST);
    }
}
