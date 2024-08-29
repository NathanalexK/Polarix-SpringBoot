package com.example.demo.api.misc;


import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.model.message.Conversation;
import com.example.demo.model.misc.Notification;
import com.example.demo.repository.message.ConversationRepository;
import com.example.demo.service.util.ImageUploaderService;
import com.example.demo.service.util.WebSocketService;
import com.example.demo.util.Pagination;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class TestController {

    private final WebSocketService webSocketService;
    private final ImageUploaderService imageUploaderService;
    private final ConversationRepository conversationRepository;

    public TestController(WebSocketService webSocketService, ImageUploaderService imageUploaderService, ConversationRepository conversationRepository) {
        this.webSocketService = webSocketService;
        this.imageUploaderService = imageUploaderService;
        this.conversationRepository = conversationRepository;
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

    @Transactional
    @GetMapping("/conversation/{id}")
    public ResponseEntity<Pagination<ConversationDTO>> testConversation(@PathVariable("id") Integer id){
        Page<ConversationDTO> dto = conversationRepository.findAllConversationByUserPageable(id, PageRequest.of(0, 20));
        return ResponseEntity.ok(new Pagination<>(dto));
    }
}
