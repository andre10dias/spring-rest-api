package com.github.andre10dias.spring_rest_api.file.exporter.contract;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTO> people) throws IOException;
    Resource exportPerson(PersonDTO person) throws IOException;

}
