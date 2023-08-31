package ru.sevmash.timesheetaccounting.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "ru.sevmash.timesheetaccounting.proxy")
public class OpenFeignConfig {

}
