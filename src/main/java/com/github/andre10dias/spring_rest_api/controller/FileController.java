package com.github.andre10dias.spring_rest_api.controller;

import com.github.andre10dias.spring_rest_api.controller.docs.FileControllerDocs;
import com.github.andre10dias.spring_rest_api.data.dto.v1.UploadFileResponseDTO;
import com.github.andre10dias.spring_rest_api.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file/v1")
public class FileController implements FileControllerDocs {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileStorageService service;

    @PostMapping("/uploadFile")
    @Override
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        var filename = service.storeFile(file);
        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/{filename}")
                .buildAndExpand(filename)
                .encode()
                .toUriString();
        return new UploadFileResponseDTO(
                filename,
                fileDownloadUri,
                file.getContentType(),
                file.getSize()
        );
    }

    @PostMapping("/uploadMultipleFiles")
    @Override
    public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .toList();
    }

    @GetMapping("/downloadFile/{filename:.+}")
    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request) {
        Resource resource = service.loadFileAsResource(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception e) {
            logger.error("Could not determine file type: {}", filename);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header("Content-Disposition", "attachment; " +
                        "filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
