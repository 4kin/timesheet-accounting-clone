package ru.sevmash.timesheetaccounting.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferHoursFromTo {
    Long idSender;
    Long idReceiver;
    byte hours;
}
