package com.github.andre10dias.spring_rest_api.file.exporter.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ColumnNames {
    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    ADDRESS("address"),
    GENDER("gender"),
    ENABLED("enabled");

    private final String label;
}
