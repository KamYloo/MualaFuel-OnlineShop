package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${application.file.image-dir}")
    private String imageDir;

    @Override
    public String saveFile(MultipartFile file, String directory) {
        try {
            Path folderPath = Paths.get(imageDir + directory);

            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = folderPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return directory+fileName;
        } catch (IOException e) {
            throw new CustomException(BusinessErrorCodes.IMAGE_FETCH_FAILED);
        }
    }
}
