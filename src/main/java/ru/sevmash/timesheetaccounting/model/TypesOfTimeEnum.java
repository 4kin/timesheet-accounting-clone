package ru.sevmash.timesheetaccounting.model;

import java.util.Random;

public enum TypesOfTimeEnum {
    ADD, REMOVE, BONUS, TRANSFER_ADD, TRANSFER_REMOVE;

    private static final Random RND = new Random();

    public static TypesOfTimeEnum randomType() {
        TypesOfTimeEnum[] types = values();
        return types[RND.nextInt(types.length)];
    }
    }
