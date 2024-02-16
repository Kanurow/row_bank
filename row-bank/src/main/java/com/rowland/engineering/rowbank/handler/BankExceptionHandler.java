package com.rowland.engineering.rowbank.handler;

import com.rowland.engineering.rowbank.exception.IncorrectBankNameException;
import com.rowland.engineering.rowbank.exception.IncorrectSavingTypeException;
import com.rowland.engineering.rowbank.exception.InsufficientFundException;
import com.rowland.engineering.rowbank.exception.SavingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

    @ExceptionHandler(IncorrectSavingTypeException.class)
    public ProblemDetail handleIncorrectSavingTypeException(IncorrectSavingTypeException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(SavingNotFoundException.class)
    public ProblemDetail handleSavingNotFoundException(SavingNotFoundException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
