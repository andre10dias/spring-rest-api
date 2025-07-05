package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.config.FileStorageConfig;
import com.github.andre10dias.spring_rest_api.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            logger.info("Upload directory created/verified: {}", this.fileStorageLocation);
        } catch (IOException | SecurityException e) {
            logger.error("Could not create the directory: {}", this.fileStorageLocation);
            throw new FileStorageException("Could not create the directory: " + fileStorageLocation, e);
        }
    }

    public String storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (file.isEmpty()) {
                logger.error("Empty file: {}", filename);
                throw new FileStorageException("Empty file: " + filename);
            }

            if (filename.contains("..")) {
                logger.error("Invalid path sequence in filename: {}", filename);
                throw new FileStorageException("Invalid path sequence in filename: " + filename);
            }

            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Stored file: {}", filename);
            return filename;

        } catch (Exception e) {
            logger.error("Could not store file: {}", filename);
            throw new FileStorageException("Could not store file " + filename, e);
        }
    }

}
