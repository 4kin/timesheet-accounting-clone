package ru.sevmash.timesheetaccounting.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sevmash.timesheetaccounting.domain.PersonEntity;
import ru.sevmash.timesheetaccounting.domain.TimeSheetEntity;

@FeignClient(name = "name", url = "${name.service.url}")
public interface FakeDataProxy {
    @GetMapping("/random-person")
    PersonEntity getRandomPerson();

    @GetMapping("/fake-ts")
    TimeSheetEntity getRandomTimeSheetEntity();
}
