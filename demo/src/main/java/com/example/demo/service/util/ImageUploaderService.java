package com.example.demo.service.util;

import com.example.demo.DemoApplication;
import com.example.demo.util.ImageCompressor;
import com.example.demo.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.Resource;
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
//        String dirPath = UPLOAD_DIR + prefixPath;

        String dirPath = createDirectory(prefixPath);
//        System.out.println(directory.toURI());

        String uuid = UUIDGenerator.generateUUID();
        url = dirPath + "/" + uuid + "." + fileExtension;

        Path path = Paths.get(url);
        Files.write(path, originalImage);
        return url;
    }

    private String createDirectory(String prefixPath)
            throws IOException {
//        Resource resource = resourceLoader.getResource("res");
//        System.out.println(resource.getURL());
//        ApplicationHome home = new ApplicationHome(DemoApplication.class);
//        File directory = new File("res" + "/" + prefixPath);
        File directory = Paths.get("upload", prefixPath).toFile();
//        File directory = new File(home.getDir(), "../../src/main/resources/static/" + UPLOAD_DIR + prefixPath);
        if(!directory.exists()){
            if(directory.mkdirs()){
                System.out.println("Directory created: " + directory.getAbsolutePath());
            } else {
                System.out.println("Error while creating directory: " + directory.getAbsolutePath());
            }
        } else {
            System.out.println("Directory already created");
        }

        return directory.getCanonicalPath();

    }
}
