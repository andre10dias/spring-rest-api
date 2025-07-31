package com.github.andre10dias.spring_rest_api.file.importer.impl;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class CsvImporter implements FileImporter {

    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_ADDRESS = "address";
    private static final String COL_GENDER = "gender";

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws IOException {
        var format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        try (var reader = new InputStreamReader(inputStream);
             var parser = new CSVParser(reader, format)
        ) {
            return parseRecordsToPersonDTO(parser.getRecords());
        }
    }

    private List<PersonDTO> parseRecordsToPersonDTO(List<CSVRecord> csvRecords) {
        return csvRecords.stream()
                .map(csvRecord -> {
                    PersonDTO person = new PersonDTO();
                    person.setFirstName(getSafeValue(csvRecord, COL_FIRST_NAME));
                    person.setLastName(getSafeValue(csvRecord, COL_LAST_NAME));
                    person.setAddress(getSafeValue(csvRecord, COL_ADDRESS));
                    person.setGender(getSafeValue(csvRecord, COL_GENDER));
                    person.setEnabled(true);
                    return person;
                })
                .toList();
    }

    private String getSafeValue(CSVRecord csvRecord, String columnName) {
        return csvRecord.isMapped(columnName) ? csvRecord.get(columnName) : "";
    }

}