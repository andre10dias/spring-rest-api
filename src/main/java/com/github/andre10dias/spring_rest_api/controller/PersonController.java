package com.github.andre10dias.spring_rest_api.controller;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.data.dto.v2.PersonDTOv2;
import com.github.andre10dias.spring_rest_api.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public List<PersonDTO> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public PersonDTO findById(@PathVariable("id") Long id) {
        return personService.findById(id);
    }

    @PostMapping
    public PersonDTO create(@RequestBody PersonDTO person) {
        return personService.create(person);
    }

    @PutMapping
    public PersonDTO update(@RequestBody PersonDTO person) {
        return personService.update(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/v2")
    public PersonDTOv2 create(@RequestBody PersonDTOv2 person) {
        return personService.createV2(person);
    }

}
