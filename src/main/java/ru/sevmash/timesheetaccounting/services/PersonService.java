package ru.sevmash.timesheetaccounting.services;

import org.springframework.stereotype.Service;
import ru.sevmash.timesheetaccounting.convertor.PersonConverter;
import ru.sevmash.timesheetaccounting.domain.PersonDto;
import ru.sevmash.timesheetaccounting.domain.PersonEntity;
import ru.sevmash.timesheetaccounting.exception.PersonNotFound;
import ru.sevmash.timesheetaccounting.proxy.FakeDataProxy;
import ru.sevmash.timesheetaccounting.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonConverter personConverter;

    private final FakeDataProxy fakeDataProxy;

    public PersonService(PersonRepository personRepository, PersonConverter personConverter, FakeDataProxy fakeDataProxy) {
        this.personRepository = personRepository;
        this.personConverter = personConverter;
        this.fakeDataProxy = fakeDataProxy;
    }

    public List<PersonDto> getAllPersons() {

        return personRepository.findAllByDeletedIsFalse()
                .stream()
                .map(personConverter::toDto)
                .collect(Collectors.toList());
    }

    public PersonEntity getPersonById(Long id) {
        // Ниже отработка не найденной персоны переписано как полагается (:
        Optional<PersonEntity> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            return optionalPerson.get();
        } else {
            throw new PersonNotFound(id);
        }
    }

    public List<PersonDto> getAllDeletedPersons() {

        return personRepository.findAllByDeletedIsTrue()
                .stream()
                .map(personConverter::toDto)
                .collect(Collectors.toList());
    }

    public PersonDto addNewPerson(PersonDto personDto) {
        return personConverter.toDto(
                personRepository.save(
                        personConverter.toEntity(personDto)
                )
        );
    }


    public PersonDto updatePerson(PersonDto personDto) {
        personRepository.findById(personDto.getId()).orElseThrow(() -> new PersonNotFound(personDto.getId()));
        PersonEntity person = personConverter.toEntity(personDto);
        PersonEntity saved = personRepository.save(person);
        return personConverter.toDto(saved);
    }

    public PersonDto setDeletedPersonById(Long id) {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(() -> new PersonNotFound(id));
        personEntity.setDeleted(true);
        return personConverter.toDto(personRepository.save(personEntity));
    }

    public PersonDto restoreDeletedPerson(Long id) {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(() -> new PersonNotFound(id));
        personEntity.setDeleted(false);
        return personConverter.toDto(personRepository.save(personEntity));
    }

    public PersonEntity addNewRandomPerson() {
        // TODO: 29.08.2023 сделать обработку кода прокси сервис не доступен
//        try {
        PersonEntity randomPerson = fakeDataProxy.getRandomPerson();
//        };
        return personRepository.save(randomPerson);
    }

}
