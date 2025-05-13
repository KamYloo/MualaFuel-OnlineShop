package com.example.MualaFuel_Backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveFile(MultipartFile file, String directory);
}
