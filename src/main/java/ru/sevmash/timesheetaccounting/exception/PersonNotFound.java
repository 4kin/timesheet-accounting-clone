package ru.sevmash.timesheetaccounting.exception;

public class PersonNotFound extends RuntimeException {
    public PersonNotFound(String message) {
        super(message);
    }

    public PersonNotFound(Long id) {
        super("Персона не найдена с ID = " + id);
    }
}
