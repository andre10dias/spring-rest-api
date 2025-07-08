package com.github.andre10dias.spring_rest_api.file.exporter.impl;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.exception.FileExportException;
import com.github.andre10dias.spring_rest_api.file.exporter.Enums.ColumnNames;
import com.github.andre10dias.spring_rest_api.file.exporter.contract.FileExporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.github.andre10dias.spring_rest_api.file.exporter.MediaTypes.CSV;

@Component(CSV)
public class CsvExporter implements FileExporter {

    private static final Logger logger = LoggerFactory.getLogger(CsvExporter.class);

    @Override
    public Resource exportFile(List<PersonDTO> people) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.Builder.create(CSVFormat.DEFAULT)
                     .setHeader(getHeaders())
                     .setSkipHeaderRecord(false)
                     .build())) {

            for (PersonDTO person : people) {
                csvPrinter.printRecord(
                        person.getId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getGender(),
                        person.isEnabled() ? "Yes" : "No"
                );
            }

            csvPrinter.flush();
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            String message = "Erro ao exportar arquivo XLSX";
            logger.error(message, e);
            throw new FileExportException(message, e);
        }
    }

    private String[] getHeaders() {
        return Arrays.stream(ColumnNames.values())
                .map(ColumnNames::getLabel)
                .toArray(String[]::new);
    }
}
