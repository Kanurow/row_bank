package com.rowland.engineering.rowbank.exception;

public class SavingNotFoundException extends RuntimeException{
    public SavingNotFoundException(String message){
        super(message);
    }
}
