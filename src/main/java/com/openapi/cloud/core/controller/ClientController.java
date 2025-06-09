package com.openapi.cloud.core.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ClientController {

    @Value("${my.greeting.message}")
    private String greetingMessage;

    @Value("${app.message}")
    private String appMessage;

    @GetMapping("/client")
    public String clientCall() {
        log.info("Client ");
        return "Hi from Client";
    }

    @GetMapping("/greeting")
    public String greeting() {

        log.info("Reading Greeting config from Config Server properties ");
        return "greeting config from config server is : " + greetingMessage;

    }

    @GetMapping("/message")
    public String appMessage() {
        log.info("Reading Greeting config from Local properties ");
        return "greeting config from local properties server is : " + appMessage;
    }

}
