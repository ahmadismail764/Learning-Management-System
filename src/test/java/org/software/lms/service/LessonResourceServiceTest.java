package org.software.lms.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.software.lms.model.Lesson;
import org.software.lms.model.LessonResource;
import org.software.lms.model.ResourceType;
import org.software.lms.repository.LessonResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LessonResourceServiceTest {

    @Mock
    private LessonResourceRepository lessonResourceRepository;

    @InjectMocks
    private LessonResourceService lessonResourceService;

    private Lesson lesson;

    @BeforeEach
    void setUp() {
        // Setting up a mock lesson object
        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("Mock Lesson");
    }

    @Test
    void testGetAllResources() {
        // Creating two LessonResource objects and setting values via setters
        LessonResource resource1 = new LessonResource();
        resource1.setId(1L);
        resource1.setLesson(lesson);
        resource1.setFileName("file1.pdf");
        resource1.setFileUrl("http://url1.com");
        resource1.setType(ResourceType.DOCUMENT);
        resource1.setFileSize(5000);
        resource1.setUploadedAt(new Date());

        LessonResource resource2 = new LessonResource();
        resource2.setId(2L);
        resource2.setLesson(lesson);
        resource2.setFileName("file2.pdf");
        resource2.setFileUrl("http://url2.com");
        resource2.setType(ResourceType.DOCUMENT);
        resource2.setFileSize(8000);
        resource2.setUploadedAt(new Date());

        // Mocking the repository's behavior
        when(lessonResourceRepository.findAll()).thenReturn(Arrays.asList(resource1, resource2));

        // Calling the service method
        List<LessonResource> resources = lessonResourceService.getAllResources();

        // Verifying results
        assertEquals(2, resources.size());
        assertEquals("file1.pdf", resources.get(0).getFileName());
        assertEquals("file2.pdf", resources.get(1).getFileName());

        // Verifying that the repository's findAll method was called exactly once
        verify(lessonResourceRepository, times(1)).findAll();
    }

    @Test
    void testSaveResource() {
        // Creating a new LessonResource object and setting values via setters
        LessonResource resource = new LessonResource();
        resource.setLesson(lesson);
        resource.setFileName("file3.pdf");
        resource.setFileUrl("http://url3.com");
        resource.setType(ResourceType.DOCUMENT);
        resource.setFileSize(12000);
        resource.setUploadedAt(new Date());

        // Mocking the repository's behavior
        when(lessonResourceRepository.save(resource)).thenReturn(resource);

        // Calling the service method
        LessonResource savedResource = lessonResourceService.saveResource(resource);

        // Verifying the saved resource
        assertNotNull(savedResource);
        assertEquals("file3.pdf", savedResource.getFileName());
        verify(lessonResourceRepository, times(1)).save(resource);
    }

    @Test
    void testDeleteResource() {
        // Given a resource ID to delete
        Long resourceId = 1L;

        // Calling the service method
        lessonResourceService.deleteResource(resourceId);

        // Verifying that the repository's deleteById method was called with the correct ID
        verify(lessonResourceRepository, times(1)).deleteById(resourceId);
    }
}
