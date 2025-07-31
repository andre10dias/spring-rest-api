package com.github.andre10dias.spring_rest_api.file.importer.factory;

import com.github.andre10dias.spring_rest_api.file.importer.contract.FileImporter;
import com.github.andre10dias.spring_rest_api.file.importer.impl.CsvImporter;
import com.github.andre10dias.spring_rest_api.file.importer.impl.XlsxImporter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileImporterFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    private final ApplicationContext context;

    private static final Map<String, Class<? extends FileImporter>> importers = Map.of(
            "csv", CsvImporter.class,
            "xlsx", XlsxImporter.class
    );

    public FileImporter getFileImporter(String filename) {
        String extension = getFileExtension(filename);
        Class<? extends FileImporter> importerClass = importers.get(extension);

        if (importerClass == null) {
            String message = "Unsupported file format: " + filename;
            logger.warn(message);
            throw new UnsupportedOperationException(message);
        }

        return context.getBean(importerClass);
    }

    private String getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i == -1 || i == filename.length() - 1) return "";
        return filename.substring(i + 1).toLowerCase(Locale.ROOT);
    }

}