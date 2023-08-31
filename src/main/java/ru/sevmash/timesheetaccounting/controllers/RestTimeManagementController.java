package ru.sevmash.timesheetaccounting.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sevmash.timesheetaccounting.model.TransferHoursFromTo;
import ru.sevmash.timesheetaccounting.services.TimeManagementService;

@RestController
@RequestMapping("/api/tm")
public class RestTimeManagementController {

    private final TimeManagementService timeManagementService;


    public RestTimeManagementController(TimeManagementService timeManagementService) {
        this.timeManagementService = timeManagementService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferTime(@RequestBody TransferHoursFromTo transferHoursFromTo) {
        timeManagementService.transferHours(transferHoursFromTo);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .header("message",
                        String.format("transfer from id=%d to id=%d add %d hours successful",
                                transferHoursFromTo.getIdSender(),
                                transferHoursFromTo.getIdReceiver(),
                                transferHoursFromTo.getHours()
                        )
                )                .body(transferHoursFromTo);
    }


}


