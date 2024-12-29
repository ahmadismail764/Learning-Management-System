package org.software.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageService(tempDir.toString());
    }

    @Test
    void testStoreFile() {
        // Prepare test data
        String content = "test content";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                content.getBytes()
        );
        Long courseId = 1L;
        Long assignmentId = 2L;
        Long studentId = 3L;

        // Test file storage
        String storedFileName = fileStorageService.storeFile(file, courseId, assignmentId, studentId);

        // Verify results
        assertNotNull(storedFileName);
        assertTrue(storedFileName.contains(courseId.toString()));
        assertTrue(storedFileName.contains(assignmentId.toString()));
        assertTrue(storedFileName.contains(studentId.toString()));
        assertTrue(Files.exists(tempDir.resolve(storedFileName)));
    }

    @Test
    void testStoreFile_WithInvalidPath() {
        // Prepare test data with invalid file name
        String content = "test content";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "../test.txt",
                "text/plain",
                content.getBytes()
        );
        Long courseId = 1L;
        Long assignmentId = 2L;
        Long studentId = 3L;

        // Test and verify exception
        assertThrows(RuntimeException.class, () -> {
            fileStorageService.storeFile(file, courseId, assignmentId, studentId);
        });
    }

    @Test
    void testViewFile() throws IOException {
        // Prepare test data
        String content = "test content";
        String fileName = "test.txt";
        Path filePath = tempDir.resolve(fileName);
        Files.write(filePath, content.getBytes());

        // Test file viewing
        FileStorageService.FileResponse response = fileStorageService.viewFile(fileName);

        // Verify results
        assertNotNull(response);
        assertEquals(fileName, response.getFileName());
        assertEquals("text/plain", response.getFileType());
        assertArrayEquals(content.getBytes(StandardCharsets.ISO_8859_1),
                response.getContentAsBytes());
    }

    @Test
    void testViewFile_FileNotFound() {
        // Test viewing non-existent file
        assertThrows(FileNotFoundException.class, () -> {
            fileStorageService.viewFile("nonexistent.txt");
        });
    }

    @Test
    void testFileResponse() {
        // Test FileResponse inner class
        FileStorageService.FileResponse response = new FileStorageService.FileResponse();
        String fileName = "test.txt";
        String fileType = "text/plain";
        String content = "test content";

        response.setFileName(fileName);
        response.setFileType(fileType);
        response.setContent(content);

        assertEquals(fileName, response.getFileName());
        assertEquals(fileType, response.getFileType());
        assertEquals(content, response.getContent());
        assertArrayEquals(content.getBytes(StandardCharsets.ISO_8859_1),
                response.getContentAsBytes());
    }

    @Test
    void testFileResponse_NullContent() {
        FileStorageService.FileResponse response = new FileStorageService.FileResponse();
        assertArrayEquals(new byte[0], response.getContentAsBytes());
    }
}