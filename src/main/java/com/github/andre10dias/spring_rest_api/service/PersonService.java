package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.controller.PersonController;
import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.exception.RequiredObjectIsNullException;
import com.github.andre10dias.spring_rest_api.model.Person;
import com.github.andre10dias.spring_rest_api.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseListObject;
import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseObject;
import static com.github.andre10dias.spring_rest_api.mapper.custom.PersonMapper.toPerson;
import static com.github.andre10dias.spring_rest_api.mapper.custom.PersonMapper.toPersonDTOv2;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<PersonDTO> findAll() {
        logger.info("findAll");
        List<PersonDTO> dtos = parseListObject(personRepository.findAll(), PersonDTO.class);
        dtos.forEach(PersonService::hateoasLinkAdd);
        return dtos;
    }

    public PersonDTO findById(Long id) {
        logger.info("findById: " + id);
        var person = personRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Person not found with id: " + id)
        );
        var dto = parseObject(person, PersonDTO.class);
        hateoasLinkAdd(dto);
        return dto;
    }


    public PersonDTO create(PersonDTO personDto) {
        logger.info("create: " + personDto);
        if (personDto == null) {
            throw new RequiredObjectIsNullException();
        }

        var personToSave = parseObject(personDto, Person.class);
        var dto = parseObject(personRepository.save(personToSave), PersonDTO.class);
        hateoasLinkAdd(dto);
        return dto;
    }

    public PersonDTO update(PersonDTO personDto) {
        logger.info("update: " + personDto);
        if (personDto == null) {
            throw new RequiredObjectIsNullException();
        }

        var personToUpdate = personRepository.findById(personDto.getId()).orElseThrow(
                () -> new RuntimeException("Person not found")
        );
        personToUpdate.setFirstName(personDto.getFirstName());
        personToUpdate.setLastName(personDto.getLastName());
        personToUpdate.setAddress(personDto.getAddress());
        personToUpdate.setGender(personDto.getGender());
        var dto = parseObject(personRepository.save(personToUpdate), PersonDTO.class);
        hateoasLinkAdd(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("disabling person id: " + id);

        // Verifica se existe
        personRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Person not found")
        );

        // Atualiza
        personRepository.disablePersonById(id);

        // Recarrega a entidade atualizada
        var updatedPerson = personRepository.findById(id).get();

        var dto = parseObject(updatedPerson, PersonDTO.class);
        hateoasLinkAdd(dto);
        return dto;
    }


    public void delete(Long id) {
        logger.info("delete: " + id);
        var personToDelete = personRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Person not found")
        );
        personRepository.delete(personToDelete);
    }

    public PersonDTOv2 createV2(PersonDTOv2 personDto) {
        logger.info("create: " + personDto);
        var personToSave = toPerson(personDto);
        var savedPerson = personRepository.save(personToSave);
        var dto = toPersonDTOv2(savedPerson);
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        return dto;
    }

    private static void hateoasLinkAdd(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
