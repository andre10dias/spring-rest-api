package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.controller.PersonController;
import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.exception.RequiredObjectIsNullException;
import com.github.andre10dias.spring_rest_api.exception.ResourceNotFoundException;
import com.github.andre10dias.spring_rest_api.model.Person;
import com.github.andre10dias.spring_rest_api.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseObject;
import static com.github.andre10dias.spring_rest_api.mapper.custom.PersonMapper.toPerson;
import static com.github.andre10dias.spring_rest_api.mapper.custom.PersonMapper.toPersonDTOv2;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PagedResourcesAssembler<PersonDTO> assembler;
    private static final Logger logger = Logger.getLogger(PersonService.class.getName());

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("findAll");
        return toPagedModel(personRepository.findAll(pageable), pageable);
    }

    public PagedModel<EntityModel<PersonDTO>> findPeopleByFirstName(String firstName, Pageable pageable) {
        logger.info("Find People By FirstName: " + firstName);
        return toPagedModel(personRepository.findPeopleByFirstName(firstName, pageable), pageable);
    }

    public PersonDTO findById(Long id) {
        logger.info("findById: " + id);
        var person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
        var dto = parseObject(person, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO personDto) {
        logger.info("create: " + personDto);
        if (personDto == null) {
            throw new RequiredObjectIsNullException();
        }

        var personToSave = parseObject(personDto, Person.class);
        var savedPerson = personRepository.save(personToSave);
        var dto = parseObject(savedPerson, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO update(PersonDTO personDto) {
        logger.info("update: " + personDto);
        if (personDto == null) {
            throw new RequiredObjectIsNullException();
        }

        var personToUpdate = personRepository.findById(personDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + personDto.getId()));

        personToUpdate.setFirstName(personDto.getFirstName());
        personToUpdate.setLastName(personDto.getLastName());
        personToUpdate.setAddress(personDto.getAddress());
        personToUpdate.setGender(personDto.getGender());

        var updatedPerson = personRepository.save(personToUpdate);
        var dto = parseObject(updatedPerson, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("disabling person id: " + id);

        personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));

        personRepository.disablePersonById(id);

        var updatedPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found after disabling with id: " + id));

        var dto = parseObject(updatedPerson, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("delete: " + id);
        var person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
        personRepository.delete(person);
    }

    public PersonDTOv2 createV2(PersonDTOv2 personDto) {
        logger.info("create v2: " + personDto);
        var savedPerson = personRepository.save(toPerson(personDto));
        var dto = toPersonDTOv2(savedPerson);
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        return dto;
    }

    // =========================================
    // PRIVATE HELPERS
    // =========================================

    private PagedModel<EntityModel<PersonDTO>> toPagedModel(Page<Person> peoplePage, Pageable pageable) {
        Page<PersonDTO> peopleDTOPage = peoplePage.map(p -> parseObject(p, PersonDTO.class));

        Sort.Order order = pageable.getSort().stream()
                .findFirst()
                .orElse(Sort.Order.by("firstName"));

        String direction = order.getDirection().name().toLowerCase();
        String orderBy = order.getProperty();

        Link selfLink = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber() + 1, pageable.getPageSize(), direction, orderBy))
                .withSelfRel();

        return assembler.toModel(peopleDTOPage, selfLink);
    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(0, 12, "asc", "firstName")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findPeopleByFirstName("", 0, 12, "asc", "firstName")).withRel("findPeopleByFirstName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}