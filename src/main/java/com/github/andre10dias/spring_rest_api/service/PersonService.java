package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<Person> findAll() {
        logger.info("findAll");
        return List.of(
                new Person(counter.incrementAndGet(), "Leandro",
                "Dias", "Rua 1", "M"),
                new Person(counter.incrementAndGet(), "Pedro",
                        "Santos", "Rua A", "M"),
                new Person(counter.incrementAndGet(), "Maria",
                        "Silva", "Rua B3", "F")
        );
    }


    public Person findById(String id) {
        logger.info("findById: " + id);
        return new Person(counter.incrementAndGet(), "Leandro",
                "Dias", "Rua 1", "M");
    }

    public Person create(Person person) {
        logger.info("create: " + person);
        person.setId(counter.incrementAndGet());
        return person;
    }

    public Person update(Person person) {
        logger.info("update: " + person);
        return person;
    }

    public void delete(String id) {
        logger.info("delete: " + id);
    }

}
