package com.github.andre10dias.spring_rest_api.data.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class BookDTO extends RepresentationModel<BookDTO> {
    Long id;
    String title;
    String author;
    LocalDateTime launchDate;
    BigDecimal price;
}
