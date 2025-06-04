package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.service.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileStorageServiceTest {

    FileStorageServiceImpl fileStorageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageServiceImpl();
        ReflectionTestUtils.setField(fileStorageService, "imageDir", tempDir.toString() + "/");
    }

    @Test
    void testSaveFileSuccess() throws Exception {
        MultipartFile multipartFile = mock(MultipartFile.class);
        String originalFileName = "test.png";
        String directory = "avatars/";

        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        when(multipartFile.getInputStream()).thenReturn(InputStream.nullInputStream());

        String result = fileStorageService.saveFile(multipartFile, directory);

        assertTrue(result.startsWith(directory));
        Path expectedDir = tempDir.resolve(directory);
        assertTrue(Files.exists(expectedDir));
        assertTrue(result.contains(originalFileName));
    }

    @Test
    void testSaveFileThrowsCustomExceptionOnIOException() throws Exception {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("fail.png");
        when(multipartFile.getInputStream()).thenThrow(new IOException("IO error"));

        CustomException ex = assertThrows(CustomException.class,
                () -> fileStorageService.saveFile(multipartFile, "fail/"));

        assertEquals(BusinessErrorCodes.IMAGE_FETCH_FAILED, ex.getErrorCode());
    }
}
