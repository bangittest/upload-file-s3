package com.example.demos.controller;

import com.example.demos.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3Controller {
    private final FileService fileService;
    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file){
        return (fileService.uploadFile(file));
    }

    @GetMapping("/dowload/{file-name}")
    public ResponseEntity<byte[]> dowLoad(@PathVariable ("file-name") String fileName){
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; filename"+fileName);
        byte[]bytes = fileService.downloadFile(fileName);
        return ResponseEntity.status(HttpStatus.SC_OK).headers(headers).body(bytes);
    }

    @DeleteMapping("/{file-name}")
    private String deleteFile(@PathVariable ("file-name") String fileName){
        return fileService.deleteFile(fileName);
    }
    @GetMapping()
    public List<String> getFileNames(){
        return fileService.listAllFiles();
    }
}
