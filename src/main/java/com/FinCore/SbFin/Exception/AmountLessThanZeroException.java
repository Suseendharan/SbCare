package com.FinCore.SbFin.Exception;

public class AmountLessThanZeroException extends RuntimeException {
    public AmountLessThanZeroException(String message) {
        super(message);
    }
}
