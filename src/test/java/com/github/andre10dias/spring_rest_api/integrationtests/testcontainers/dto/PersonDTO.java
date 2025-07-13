package com.github.andre10dias.spring_rest_api.integrationtests.testcontainers.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.andre10dias.spring_rest_api.data.dto.v1.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    String profileUrl;
    String photoUrl;

    @JsonIgnore
    List<BookDTO> books;

    @JsonIgnore
    public String getName() {
        return firstName + " " + lastName;
    }
}
