package ru.sevmash.timesheetaccounting.bootstrap;

import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.sevmash.timesheetaccounting.domain.PersonEntity;
import ru.sevmash.timesheetaccounting.domain.TimeSheetEntity;
import ru.sevmash.timesheetaccounting.model.TypesOfTimeEnum;
import ru.sevmash.timesheetaccounting.repository.PersonRepository;
import ru.sevmash.timesheetaccounting.repository.TimeSheetRepository;
import ru.sevmash.timesheetaccounting.services.PersonService;
import ru.sevmash.timesheetaccounting.services.TimeSheetService;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;

@Component
public class bootstrap implements CommandLineRunner {
    private final PersonRepository personRepository;
    private final TimeSheetRepository timeSheetRepository;
    private final PersonService personService;
    private final TimeSheetService timeSheetService;
    private final Environment environment;
    Logger logger = LogManager.getLogger(this.getClass());

    public bootstrap(PersonRepository personRepository, TimeSheetRepository timeSheetRepository, PersonService personService, TimeSheetService timeSheetService, Environment environment) {
        this.personRepository = personRepository;
        this.timeSheetRepository = timeSheetRepository;
        this.personService = personService;
        this.timeSheetService = timeSheetService;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        logger.info("Генерация и загрузка данных");
        loadPersons();
        logger.info("Загруженно людей " + personRepository.findAll().size());
        logger.info("Загруженно записей по времени " + timeSheetRepository.findAll().size());

        logger.info("Загрузка данных из сервиса");
        try {
            loadPersonFormService();
        } catch (RuntimeException e) {
            logger.error("Сервис не доступен. Проверьте адресс " + environment.getProperty("name.service.url"));
            logger.warn("Запустите модуль ta-microservice-fake-data");
        }
        logger.info("Итого людей " + personRepository.findAll().size());
        logger.info("Итого записей по времени " + timeSheetRepository.findAll().size());
    }

    private void loadPersonFormService() {
        for (int i = 0; i < 10; i++) {
            PersonEntity personEntity = personService.addNewRandomPerson();
            Long idPerson = personEntity.getId();
            for (int j = 0; j < 10; j++) {
                TimeSheetEntity fakeTimeSheet = timeSheetService.getFakeTimeSheet(idPerson);
            }
        }
    }

    private TimeSheetEntity loadTimeSheets(PersonEntity person) {


        Faker faker = new Faker(new Locale.Builder().setLanguage("ru").setRegion("RU").build());
        TimeSheetEntity timeSheetEntity = new TimeSheetEntity();
        timeSheetEntity.setHours((byte) faker.number().numberBetween(1, 8));
        timeSheetEntity.setNotes(faker.lorem().maxLengthSentence(150));
        timeSheetEntity.setPerson(person);
        timeSheetEntity.setDeleted(faker.bool().bool());

        timeSheetEntity.setFileName(faker.file().fileName());
        timeSheetEntity.setTypes(TypesOfTimeEnum.randomType());

        java.util.Date date = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -2);
        java.util.Date stratDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 2);
        calendar.add(Calendar.MONTH, -1);
        java.util.Date endDate = calendar.getTime();
        timeSheetEntity.setDate(faker.date().between(stratDate, endDate));
        return timeSheetEntity;
    }

    private void loadPersons() {
        //TODO переписать на стримах
        for (int i = 1; i <= 10; i++) {
            getFakePerson();
        }
//        logger.info("Person loaded");
    }

    private PersonEntity getFakePerson() {
        PersonEntity person = new PersonEntity();
        Faker faker = new Faker(new Locale.Builder().setLanguage("ru").setRegion("RU").build());
        String[] fio = faker.name().nameWithMiddle().split(" ");
        person.setFirstName(fio[2]);
        person.setMiddleName(fio[1]);
        person.setSecondName(fio[0]);
        person.setDeleted(faker.bool().bool());
        person.setDateOfBirth(new Date(faker.date().birthday(15, 78).getTime()));
        person.setPersonNumber(faker.number().numberBetween(1, 300));
        personRepository.save(person);

        //TODO переписать на стримах
        for (int i = 0; i < 10; i++) {
            TimeSheetEntity timeSheetEntity = loadTimeSheets(person);
            timeSheetRepository.save(timeSheetEntity);
        }

        return person;

    }
}
