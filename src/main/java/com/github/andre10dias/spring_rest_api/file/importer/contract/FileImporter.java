package com.github.andre10dias.spring_rest_api.file.importer.contract;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.exception.UnsupportedFileException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileImporter {
    
    List<PersonDTO> importFile(InputStream inputStream) throws UnsupportedFileException, IOException;
    
}
