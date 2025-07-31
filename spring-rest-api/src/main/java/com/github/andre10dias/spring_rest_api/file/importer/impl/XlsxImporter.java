package com.github.andre10dias.spring_rest_api.file.importer.impl;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class XlsxImporter implements FileImporter {

    private static final int COL_FIRST_NAME = 0;
    private static final int COL_LAST_NAME = 1;
    private static final int COL_ADDRESS = 2;
    private static final int COL_GENDER = 3;

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next(); // pula o cabeçalho
            }
            return parseRowsToPersonDTOList(rowIterator);
        }
    }

    private List<PersonDTO> parseRowsToPersonDTOList(Iterator<Row> rowIterator) {
        List<PersonDTO> people = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowValid(row)) {
                people.add(parseRowToPersonDTO(row));
            }
        }
        return people;
    }

    private PersonDTO parseRowToPersonDTO(Row row) {
        PersonDTO person = new PersonDTO();
        person.setFirstName(getSafeCellValue(row.getCell(COL_FIRST_NAME)));
        person.setLastName(getSafeCellValue(row.getCell(COL_LAST_NAME)));
        person.setAddress(getSafeCellValue(row.getCell(COL_ADDRESS)));
        person.setGender(getSafeCellValue(row.getCell(COL_GENDER)));
        person.setEnabled(true);
        return person;
    }

    private static String getSafeCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue());
        if (cell.getCellType() == CellType.BOOLEAN) return String.valueOf(cell.getBooleanCellValue());
        return cell.toString(); // fallback para outros tipos
    }

    private static boolean isRowValid(Row row) {
        return !getSafeCellValue(row.getCell(COL_FIRST_NAME)).isEmpty()
                || !getSafeCellValue(row.getCell(COL_LAST_NAME)).isEmpty()
                || !getSafeCellValue(row.getCell(COL_ADDRESS)).isEmpty()
                || !getSafeCellValue(row.getCell(COL_GENDER)).isEmpty();
    }
}