package com.github.andre10dias.spring_rest_api.data.dto.v2;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

/*
 * ⚠️ O Dozer não suporta record diretamente porque records não têm construtor default e são imutáveis.
 *
 * Soluções possíveis:
 * ✅ Trocar para class (como na Opção 1), ou
 * 🔁 Usar um mapper alternativo como MapStruct (mais moderno, mais performático e compatível com record)
 * */

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDTOv2 extends RepresentationModel<PersonDTO> {
    Long id;
    String firstName;
    String lastName;
    String address;
    String gender;
    LocalDate birthday;
}
