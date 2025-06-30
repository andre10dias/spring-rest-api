package com.github.andre10dias.spring_rest_api.data.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
 * ⚠️ O Dozer não suporta record diretamente porque records não têm construtor default e são imutáveis.
 *
 * Soluções possíveis:
 * ✅ Trocar para class (como na Opção 1), ou
 * 🔁 Usar um mapper alternativo como MapStruct (mais moderno, mais performático e compatível com record)
 * */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDTOv2 {
    Long id;
    String firstName;
    String lastName;
    String address;
    String gender;
    LocalDate birthday;
}
