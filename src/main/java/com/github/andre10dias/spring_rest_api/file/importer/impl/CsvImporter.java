package com.github.andre10dias.spring_rest_api.file.importer.impl;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.StreamSupport;

public class CsvImporter implements FileImporter {

    private static final String COL_FIRST_NAME = "firstName";
    private static final String COL_LAST_NAME = "lastName";
    private static final String COL_ADDRESS = "address";
    private static final String COL_GENDER = "gender";

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws IOException {
        // Os métodos abaixo estão deprecated na 1.14.0, mas ainda são a melhor forma estável de uso
        CSVFormat format = CSVFormat.DEFAULT
                .withHeader() // assume que a primeira linha contém os nomes das colunas
                .withSkipHeaderRecord(true)
                .withIgnoreEmptyLines(true)
                .withTrim();

        Iterable<CSVRecord> csvRecords = format.parse(new InputStreamReader(inputStream));
        return parseRecordsToPersonDTO(csvRecords);
    }

    private List<PersonDTO> parseRecordsToPersonDTO(Iterable<CSVRecord> csvRecords) {
        return StreamSupport.stream(csvRecords.spliterator(), false)
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