package com.rowland.engineering.rowbank.exception;

public class AccountDetailsMismatch extends RuntimeException{
    public AccountDetailsMismatch(String message) {
        super(message);
    }
}
