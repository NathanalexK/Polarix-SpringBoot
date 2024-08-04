package com.example.demo.service.util;

import com.example.demo.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageUploaderService {
    private static final String UPLOAD_DIR = "upload/";
    private static final float MAX_COMPRESSION_SIZE = 2000;

    @Autowired
    private ResourceLoader resourceLoader;

    public String uploadImage(MultipartFile multipartFile, String prefixPath)
            throws IOException {
        String url = "";
        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = "";

//        ImageCompressor imageCompressor = new ImageCompressor(MAX_COMPRESSION_SIZE);
        byte[] originalImage = multipartFile.getBytes();
//        byte[] newImage = imageCompressor.compressImage(originalImage, 1);
        System.out.println("ext:"+originalFileName);

        if(originalFileName.contains(".")){
            String[] split =  originalFileName.split("\\.");
            fileExtension = split[split.length - 1];
        }

        createDirectoryIfNotExist(prefixPath);

        String uuid = UUIDGenerator.generateUUID();
        url = prefixPath + "/" + uuid + "." + fileExtension;

        Path path = Paths.get(UPLOAD_DIR, url);
        Files.write(path, originalImage);
        return url;
    }

    private Path createDirectoryIfNotExist(String prefixPath)
            throws IOException {
        Path path = Paths.get(UPLOAD_DIR, prefixPath);
        File directory = path.toFile();

        if(!directory.exists()){
            if(directory.mkdirs()){
                System.out.println("Directory created: " + directory.getAbsolutePath());
            } else {
                System.out.println("Error while creating directory: " + directory.getAbsolutePath());
            }
        } else {
            System.out.println("Directory already created");
        }

        return path;
    }
}
