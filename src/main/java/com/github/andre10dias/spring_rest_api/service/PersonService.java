package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.model.Person;
import com.github.andre10dias.spring_rest_api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<Person> findAll() {
        logger.info("findAll");
        return personRepository.findAll();
    }


    public Person findById(Long id) {
        logger.info("findById: " + id);
        return personRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Person not found")
        );
    }

    public Person create(Person person) {
        logger.info("create: " + person);
        return personRepository.save(person);
    }

    public Person update(Person person) {
        logger.info("update: " + person);
        Person personToUpdate = personRepository.findById(person.getId()).orElseThrow(
                () -> new RuntimeException("Person not found")
        );
        personToUpdate.setFirstName(person.getFirstName());
        personToUpdate.setLastName(person.getLastName());
        personToUpdate.setAddress(person.getAddress());
        personToUpdate.setGender(person.getGender());
        return personRepository.save(person);
    }

    public void delete(Long id) {
        logger.info("delete: " + id);
        Person personToDelete = personRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Person not found")
        );
        personRepository.delete(personToDelete);
    }

}
