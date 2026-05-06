package com.MyMDentis.MyMDentistComerce.Controller;

import com.MyMDentis.MyMDentistComerce.Exception.*;
import com.MyMDentis.MyMDentistComerce.Verification.Entities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = NullValuesEntityException.class)
    public ResponseEntity<DTONullException> nullExceptionHandler(NullValuesEntityException ex){
        DTONullException exception = DTONullException.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidValuesEntityException.class)
    public ResponseEntity<DTOInvalidValuesException> invalidValuesExceptionHandler(InvalidValuesEntityException ex){
        DTOInvalidValuesException exception = DTOInvalidValuesException.builder()
                .code(ex.getCode())
                .attribute(ex.getAttribute())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundEntityException.class)
    public ResponseEntity<DTONotFoundEntityException> notFoundEntityException(NotFoundEntityException ex){
        DTONotFoundEntityException exception = DTONotFoundEntityException.builder()
                .code(ex.getCode())
                .entity(ex.getEntity())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<DTOInvalidValuesException> badArgumentInRequestExceptionHandler(MethodArgumentTypeMismatchException ex){
        DTOInvalidValuesException exception = DTOInvalidValuesException.builder()
                .code(ExceptionValues.VALUES_NOT_COMPATIBLE_REQUEST_CODE)
                .message(ExceptionValues.VALUES_NOT_COMPATIBLE_REQUEST_MESSAGE)
                .attribute(Entities.REQUEST)
                .build();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

}
