package ru.sevmash.timesheetaccounting.exception;

public class MicroServiceUnAvailable extends RuntimeException {
    public MicroServiceUnAvailable(String message) {
        super(message);
    }
}
