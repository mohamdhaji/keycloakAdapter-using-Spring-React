package com.dress.shop.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//The ControllerAdvice is triggered by RuntimeExceptions , so thrown exceptions need to be runtime exceptions .
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleProductIdException(ProductIdException ex, WebRequest request){
        ProductIdExceptionResponse exceptionResponse = new ProductIdExceptionResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request){
        ProductNotFoundExceptionResponse exceptionResponse = new ProductNotFoundExceptionResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, WebRequest request){
        UsernameAlreadyExistsResponse exceptionResponse = new UsernameAlreadyExistsResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleTypeNotFoundException(TypeNotFoundException ex, WebRequest request){
        TypeNotFoundExceptionResponse exceptionResponse = new TypeNotFoundExceptionResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAuthException(AuthException ex, WebRequest request){
        AuthExceptionResponse exceptionResponse = new AuthExceptionResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleAuthorizationException(AuthorizationException ex, WebRequest request){
        AuthorizationExceptionResponse exceptionResponse = new AuthorizationExceptionResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    // it will work even without value because it will catch all other runtime exceptions that are not mentioned above
    @ExceptionHandler(value={com.dress.shop.exceptions.UserDoseNotExistException.class})
    public final ResponseEntity<Object> handleUserDoseNotExistException(RuntimeException ex){
        UserDoseNotExistExceptionResponse exceptionResponse = new UserDoseNotExistExceptionResponse(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }



}
