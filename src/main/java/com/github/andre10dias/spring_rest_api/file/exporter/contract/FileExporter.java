package com.github.andre10dias.spring_rest_api.file.exporter.contract;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws IOException;
    Resource exportFileByPerson(PersonDTO person) throws IOException;

}
