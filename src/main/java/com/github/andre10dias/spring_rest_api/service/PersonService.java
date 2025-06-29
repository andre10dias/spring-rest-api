package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.data.dto.PersonDTO;
import com.github.andre10dias.spring_rest_api.mapper.ObjectMapper;
import com.github.andre10dias.spring_rest_api.model.Person;
import com.github.andre10dias.spring_rest_api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseListObject;
import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseObject;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<PersonDTO> findAll() {
        logger.info("findAll");
        List<Person> person = personRepository.findAll();
        return parseListObject(person, PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("findById: " + id);
        var person = personRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Person not found")
        );
        return parseObject(person, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO personDto) {
        logger.info("create: " + personDto);
        var personToSave = parseObject(personDto, Person.class);
        return parseObject(personRepository.save(personToSave), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO personDto) {
        logger.info("update: " + personDto);
        var personToUpdate = personRepository.findById(personDto.getId()).orElseThrow(
                () -> new RuntimeException("Person not found")
        );
        personToUpdate.setFirstName(personDto.getFirstName());
        personToUpdate.setLastName(personDto.getLastName());
        personToUpdate.setAddress(personDto.getAddress());
        personToUpdate.setGender(personDto.getGender());
        return parseObject(personRepository.save(personToUpdate), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("delete: " + id);
        var personToDelete = personRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Person not found")
        );
        personRepository.delete(personToDelete);
    }

}
