package com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WrapperPersonDTO {

    @JsonProperty("_embedded")
    private PersonEmbeddedDTO embedded;

}
