package com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.PersonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonEmbeddedDTO {

    @JsonProperty("people")
    private List<PersonDTO> peopleDto;

}
