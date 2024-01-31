package com.rowland.engineering.rowbank.handler;

import com.rowland.engineering.rowbank.exception.IncorrectBankNameException;
import com.rowland.engineering.rowbank.exception.InsufficientFundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BankExceptionHandler {

    @ExceptionHandler(IncorrectBankNameException.class)
    public ProblemDetail handleIncorrectBankNameException(IncorrectBankNameException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }
    @ExceptionHandler(InsufficientFundException.class)
    public ProblemDetail handleInsufficientFundException(InsufficientFundException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
