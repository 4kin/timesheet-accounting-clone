package ru.sevmash.timesheetaccounting.domain;

import lombok.Data;
import ru.sevmash.timesheetaccounting.model.TypesOfTimeEnum;

import java.util.Date;

/**
 * A DTO for the {@link TimeSheetEntity} entity
 */
@Data
public class TimeSheetDto {
    private Long id;
    private Long person_id;
    private TypesOfTimeEnum types;
    private Date date;
    private byte hours;
    private String fileName;
    private String notes;
    private boolean deleted;
}