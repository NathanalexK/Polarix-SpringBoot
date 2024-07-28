package com.example.demo.restController.misc;


import com.example.demo.model.misc.Notification;
import com.example.demo.service.util.WebSocketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class TestController {

    private final WebSocketService webSocketService;

    public TestController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
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
}
