package com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDTO {
    Long id;
    String firstName;
    String lastName;
    String address;
    String gender;
    boolean enabled;
}
