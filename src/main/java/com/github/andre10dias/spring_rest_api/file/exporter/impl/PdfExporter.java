package com.github.andre10dias.spring_rest_api.file.exporter.impl;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.exception.FileStorageException;
import com.github.andre10dias.spring_rest_api.file.exporter.MediaTypes;
import com.github.andre10dias.spring_rest_api.file.exporter.contract.FileExporter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(MediaTypes.PDF) // Nome do bean será a chave do Map em FileExporterFactory
public class PdfExporter implements FileExporter {

    @Value("${reports.path}")
    private String reportPath;

    @Override
    public Resource exportFile(List<PersonDTO> people) throws IOException {
        String file = reportPath + "/people.jrxml";
        Map<String, Object> param = new HashMap<>();

        try (
                InputStream inputStream = getClass().getResourceAsStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            if (inputStream == null) {
                throw new FileStorageException("Arquivo do relatório não encontrado: " + file);
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        } catch (JRException e) {
            throw new FileStorageException("Erro ao processar o relatório: " + file, e);
        } catch (IOException e) {
            throw new FileStorageException("Erro de I/O ao lidar com o arquivo do relatório: " + file, e);
        }
    }
}
