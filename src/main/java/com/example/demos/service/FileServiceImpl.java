package com.example.demos.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    @Value("${bucketName}")
    private String bucketName;
    private final AmazonS3 s3;
    @Override
    public String uploadFile(MultipartFile file) {
//        TransferManager transferManager = TransferManagerBuilder.standard()
//                .withS3Client(s3)
//                .build();
        String originalFileName = file.getOriginalFilename();
        try {
            File file1 =converMutiPartToFile(file);
            PutObjectResult putObjectResult=s3.putObject(bucketName,originalFileName,file1);
            return putObjectResult.getContentMd5();
        }catch (IOException e){
            throw new RuntimeException();
        }
//        finally {
//            transferManager.shutdownNow();
//        }
    }

    @Override
    public byte[] downloadFile(String fileName) {
        S3Object object = s3.getObject(bucketName,fileName);
        S3ObjectInputStream objContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objContent);
        }catch (IOException e){
            throw new RuntimeException();
        }

    }

    @Override
    public String deleteFile(String fileName) {
        s3.deleteObject(bucketName,fileName);
        return "delete";
    }

    @Override
    public List<String> listAllFiles() {
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
        return listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }
    private File converMutiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }catch (IOException e) {
            e.printStackTrace();
        }
//        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        fos.close();
        return convFile;
    }
}
