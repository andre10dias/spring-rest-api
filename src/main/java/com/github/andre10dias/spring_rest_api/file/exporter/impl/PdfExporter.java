package com.github.andre10dias.spring_rest_api.file.exporter.impl;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.exception.FileStorageException;
import com.github.andre10dias.spring_rest_api.file.exporter.MediaTypes;
import com.github.andre10dias.spring_rest_api.file.exporter.contract.FileExporter;
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
public class PdfExporter implements FileExporter {

    @Value("${reports.path}")
    private String reportPath;

    private final QRCodeService qrCodeService;

    @Override
    public Resource exportFile(List<PersonDTO> people) throws IOException {
        String file = reportPath + "/person.jrxml";
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
    public Resource exportFileByPerson(PersonDTO person) throws IOException {
        String mainReportFile = reportPath + "/people.jrxml";
        String subReportFile = reportPath + "/book.jrxml";

        try (
                InputStream mainInputStream = getClass().getResourceAsStream(mainReportFile);
                InputStream subInputStream = getClass().getResourceAsStream(subReportFile);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {

            String fileNotFoundMessage = "Arquivo do relatório nao encontrado: ";
            if (mainInputStream == null) {
                throw new FileStorageException(fileNotFoundMessage + mainReportFile);
            }

            if (subInputStream == null) {
                throw new FileStorageException(fileNotFoundMessage + subReportFile);
            }

            InputStream qrCodeStream = qrCodeService.generateQRCode(person.getProfileUrl(), 200, 200);

            JasperReport mainJasperReport = JasperCompileManager.compileReport(mainInputStream);
            JasperReport subJasperReport = JasperCompileManager.compileReport(subInputStream);

            JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));
            JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(person.getBooks());

            Map<String, Object> param = new HashMap<>();
            param.put("SUB_REPORT_DATA_SOURCE", subDataSource);
            param.put("BOOK_SUB_REPORT", subReportFile);
            param.put("QR_CODE_IMAGE", qrCodeStream);

            JasperPrint mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, param, mainDataSource);
            JasperPrint subJasperPrint = JasperFillManager.fillReport(subJasperReport, param, subDataSource);

            JasperExportManager.exportReportToPdfStream(mainJasperPrint, outputStream);
            JasperExportManager.exportReportToPdfStream(subJasperPrint, outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        } catch (JRException e) {
            throw new FileStorageException("Erro ao processar o relatório: " + mainReportFile, e);
        } catch (IOException e) {
            throw new FileStorageException("Erro de I/O ao lidar com o arquivo do relatório: " + mainReportFile, e);
        }
    }

}
