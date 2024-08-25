package com.example.demo.api.misc;


import com.example.demo.model.misc.Notification;
import com.example.demo.service.util.ImageUploaderService;
import com.example.demo.service.util.WebSocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class TestController {

    private final WebSocketService webSocketService;
    private final ImageUploaderService imageUploaderService;

    public TestController(WebSocketService webSocketService, ImageUploaderService imageUploaderService) {
        this.webSocketService = webSocketService;
        this.imageUploaderService = imageUploaderService;
    }

    @GetMapping("/test")
    public String testNotification(){
        new Thread(() -> {
            while (true){
                System.out.println("herrrree");
                webSocketService.sendNotification("/topic/user", Notification.info("test", "test is working"));
                try {
                    Thread.sleep(2000);
                }catch (Exception e){
                }
            }
        }).start();
        return "ok";
    }

    @GetMapping("/simpleTest")
    public ResponseEntity<String> swingTest(){
        return ResponseEntity.ok("Hello from Spring");
    }

    @RequestMapping("/image")
    public String postImage(@RequestParam("file") MultipartFile file) throws IOException {
        return imageUploaderService.uploadImage(file, "/test");
    }
}
