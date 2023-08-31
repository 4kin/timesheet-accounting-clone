package ru.sevmash.timesheetaccounting.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sevmash.timesheetaccounting.model.TransferHoursFromTo;
import ru.sevmash.timesheetaccounting.domain.PersonEntity;
import ru.sevmash.timesheetaccounting.domain.TimeSheetEntity;
import ru.sevmash.timesheetaccounting.model.TypesOfTimeEnum;
import ru.sevmash.timesheetaccounting.repository.PersonRepository;
import ru.sevmash.timesheetaccounting.repository.TimeSheetRepository;

@Service
public class TimeManagementService {

    private final TimeSheetRepository timeSheetRepository;
    private final PersonRepository personRepository;

    TimeManagementService(TimeSheetRepository timeSheetRepository, PersonRepository personRepository
    ) {
        this.timeSheetRepository = timeSheetRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public void transferHours(@NotNull TransferHoursFromTo transferHoursFromTo) {
        PersonEntity senderPersonEntity = personRepository.findById(transferHoursFromTo.getIdSender()).orElseThrow();
        PersonEntity receiverPersonEntity = personRepository.findById(transferHoursFromTo.getIdReceiver()).orElseThrow();

        //todo написть проверку наличия часов для переода

        TimeSheetEntity addTimeSheetEntity = new TimeSheetEntity();
        addTimeSheetEntity.setPerson(receiverPersonEntity);
        addTimeSheetEntity.setTypes(TypesOfTimeEnum.TRANSFER_ADD);
        addTimeSheetEntity.setHours(transferHoursFromTo.getHours());
        addTimeSheetEntity.setNotes(String.format("принято от пользователя %s %s.%s. (id=%d)",
                senderPersonEntity.getFirstName(),
                senderPersonEntity.getSecondName().substring(0, 1),
                senderPersonEntity.getMiddleName().substring(0, 1),
                senderPersonEntity.getId())
        );
//        addTimeSheetEntity.setDate(new Date(System.currentTimeMillis()));

        TimeSheetEntity subtractTimeSheetEntity = new TimeSheetEntity();
        subtractTimeSheetEntity.setPerson(senderPersonEntity);
        subtractTimeSheetEntity.setHours(transferHoursFromTo.getHours());
        subtractTimeSheetEntity.setTypes(TypesOfTimeEnum.TRANSFER_REMOVE);
        subtractTimeSheetEntity.setNotes(
                String.format("передано пользователю  %s %s.%s. (id=%d)",
                        receiverPersonEntity.getFirstName(),
                        receiverPersonEntity.getSecondName().substring(0, 1),
                        receiverPersonEntity.getMiddleName().substring(0, 1),
                        receiverPersonEntity.getId()
                )
        );
//        subtractTimeSheetEntity.setDate(new Date(System.currentTimeMillis()));

        timeSheetRepository.save(addTimeSheetEntity);
        timeSheetRepository.save(subtractTimeSheetEntity);

        // todo сделать возврат сообщения

    }
}