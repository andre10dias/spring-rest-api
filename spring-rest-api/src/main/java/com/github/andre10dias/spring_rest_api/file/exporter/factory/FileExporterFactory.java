package com.github.andre10dias.spring_rest_api.file.exporter.factory;

import com.github.andre10dias.spring_rest_api.file.exporter.contract.PersonExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileExporterFactory {

    private final Map<String, PersonExporter> exporters;

    public PersonExporter getFileExporter(String acceptHeader) {
        PersonExporter exporter = exporters.get(acceptHeader);
        if (exporter == null) {
            throw new UnsupportedOperationException("Unsupported file type: " + acceptHeader);
        }
        return exporter;
    }
}
