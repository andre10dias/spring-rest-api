package com.github.andre10dias.spring_rest_api.controller;

import com.github.andre10dias.spring_rest_api.model.Greeting;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequiredArgsConstructor
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();
    private final String template = "Hello, %s!";

    // htpp://localhost:8080/greeting?name=Leandro
    @RequestMapping("/greeting")
    public Greeting getGreeting(
            @RequestParam(value = "name", defaultValue = "World") String name
    ) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}
