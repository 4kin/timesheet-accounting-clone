package ru.sevmash.timesheetaccounting.exception;

public class TransferError extends RuntimeException {
    public TransferError(String message) {
        super(message);
    }
}
