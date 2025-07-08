package com.github.andre10dias.spring_rest_api.file.exporter.factory;

import com.github.andre10dias.spring_rest_api.file.exporter.contract.FileExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileExporterFactory {

    private final Map<String, FileExporter> exporters;

    public FileExporter getFileExporter(String acceptHeader) {
        FileExporter exporter = exporters.get(acceptHeader);
        if (exporter == null) {
            throw new UnsupportedOperationException("Unsupported file type: " + acceptHeader);
        }
        return exporter;
    }
}
