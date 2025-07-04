package com.github.andre10dias.spring_rest_api.controller;

import com.github.andre10dias.spring_rest_api.controller.docs.PersonControllerDocs;
import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.exception.InvalidPageRequestException;
import com.github.andre10dias.spring_rest_api.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin({"http://localhost:8080", "http://localhost:4200"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/people/v1")
@Tag(name = "People", description = "Endpoints for Managing People.")
public class PersonController implements PersonControllerDocs {

    private final PersonService personService;

    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE
    })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "orderBy", defaultValue = "firstName") String orderBy
    ) {
        if (page < 1 || limit < 1) {
            throw new InvalidPageRequestException("Page and limit parameters must be greater than or equal to 1.");
        }
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page-1, limit, Sort.by(sortDirection, orderBy));
        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @GetMapping(value = "/firstName/{firstName}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE
    })
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPeopleByFirstName(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "orderBy", defaultValue = "firstName") String orderBy
    ) {
        if (page < 1 || limit < 1) {
            throw new InvalidPageRequestException("Page and limit parameters must be greater than or equal to 1.");
        }
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page-1, limit, Sort.by(sortDirection, orderBy));
        return ResponseEntity.ok(personService.findPeopleByFirstName(firstName, pageable));
    }

    @GetMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE
    })
    @Override
    public PersonDTO findById(@PathVariable("id") Long id) {
        return personService.findById(id);
    }

    @PostMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE
    })
    @Override
    public PersonDTO create(@RequestBody PersonDTO person) {
        return personService.create(person);
    }

    @PutMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE
    })
    @Override
    public PersonDTO update(@RequestBody PersonDTO person) {
        return personService.update(person);
    }

    @PatchMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE
    })
    @Override
    public PersonDTO disablePerson(@PathVariable("id") Long id) {
        return personService.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE
    })
    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/v2", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE
    })
    @Override
    public PersonDTOv2 create(@RequestBody PersonDTOv2 person) {
        return personService.createV2(person);
    }

}
