package com.github.andre10dias.spring_rest_api.file.exporter.impl;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.exception.FileStorageException;
import com.github.andre10dias.spring_rest_api.file.exporter.MediaTypes;
import com.github.andre10dias.spring_rest_api.file.exporter.contract.PersonExporter;
import com.github.andre10dias.spring_rest_api.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component(MediaTypes.PDF) // Nome do bean será a chave do Map em FileExporterFactory
public class PdfExporter implements PersonExporter {

    @Value("${reports.path}")
    private String reportPath;

    private final QRCodeService qrCodeService;

    @Override
    public Resource exportPeople(List<PersonDTO> people) throws IOException {
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

    @Override
    public Resource exportPerson(PersonDTO person) throws IOException {
        try (
                InputStream mainInputStream = loadReportFile("/person.jrxml");
                InputStream subInputStream = loadReportFile("/book.jrxml");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            JasperReport subReport = compileReport(subInputStream);
            JasperReport mainReport = compileReport(mainInputStream);

            InputStream qrCodeStream = qrCodeService.generateQRCode(person.getProfileUrl(), 200, 200);

            Map<String, Object> parameters = buildReportParameters(person, subReport, qrCodeStream);

            JasperPrint print = JasperFillManager.fillReport(mainReport, parameters,
                    new JRBeanCollectionDataSource(Collections.singletonList(person)));

            JasperExportManager.exportReportToPdfStream(print, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        } catch (JRException e) {
            throw new FileStorageException("Erro ao processar o relatório", e);
        } catch (IOException e) {
            throw new FileStorageException("Erro de I/O ao lidar com o relatório", e);
        }
    }

    private InputStream loadReportFile(String relativePath) {
        String fullPath = reportPath + relativePath;
        InputStream inputStream = getClass().getResourceAsStream(fullPath);
        if (inputStream == null) {
            throw new FileStorageException("Arquivo do relatório não encontrado: " + fullPath);
        }
        return inputStream;
    }

    private JasperReport compileReport(InputStream inputStream) throws JRException {
        return JasperCompileManager.compileReport(inputStream);
    }

    private Map<String, Object> buildReportParameters(PersonDTO person, JasperReport subReport, InputStream qrCodeStream) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("PERSON_ID", person.getId());
        parameters.put("BOOK_SUB_REPORT", subReport);
        parameters.put("QR_CODE_IMAGE", qrCodeStream);
        parameters.put("SUB_REPORT_DATA_SOURCE", new JRBeanCollectionDataSource(person.getBooks()));
        return parameters;
    }

}
