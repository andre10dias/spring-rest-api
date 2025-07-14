package com.github.andre10dias.spring_rest_api.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailRequestDTO {

    private String to;
    private String subject;
    private String body;

}
