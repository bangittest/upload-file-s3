package com.example.demos.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String uploadFile(MultipartFile file);
    byte[]downloadFile(String fileName);
    String deleteFile(String fileName);
    List<String> listAllFiles();
}
