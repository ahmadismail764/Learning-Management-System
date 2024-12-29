package org.software.lms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.*;
import java.nio.file.*;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, Long courseId, Long assignmentId, Long studentId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file path sequence " + fileName);
            }

            String storedFileName = courseId + "_" + assignmentId + "_" + studentId + "_" + fileName;
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return storedFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName, ex);
        }
    }

    public FileResponse viewFile(String fileName) throws IOException {
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        String contentType = Files.probeContentType(filePath);
        byte[] fileContent = Files.readAllBytes(filePath);

        FileResponse response = new FileResponse();
        response.setFileName(fileName);
        response.setFileType(contentType);

        // Return raw bytes for both PDF and text files
        // This allows Postman to handle the content properly
        response.setContent(new String(fileContent, StandardCharsets.ISO_8859_1));

        return response;
    }

    // Inner class to represent file response
    public static class FileResponse {
        private String fileName;
        private String fileType;
        private String content;

        // Getters and setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
        public byte[] getContentAsBytes() {
            if (content == null) {
                return new byte[0];
            }
            return content.getBytes(StandardCharsets.ISO_8859_1);
        }
    }
}