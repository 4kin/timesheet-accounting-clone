package ru.sevmash.timesheetaccounting.exception;

public class NotEnoughHours  extends RuntimeException{

    public NotEnoughHours(String message) {
        super(message);
    }
}
