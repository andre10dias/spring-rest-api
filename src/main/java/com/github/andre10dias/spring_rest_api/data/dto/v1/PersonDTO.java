package com.github.andre10dias.spring_rest_api.data.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

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
@Relation(collectionRelation = "people")
public class PersonDTO extends RepresentationModel<PersonDTO> {
    Long id;
    String firstName;
    String lastName;
    String address;
    String gender;
    boolean enabled;

    public String getName() {
        return firstName + " " + lastName;
    }
}
