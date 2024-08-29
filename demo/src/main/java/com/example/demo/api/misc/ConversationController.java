package com.example.demo.api.misc;

import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.service.conversation.ConversationService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/list/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<ConversationDTO>> getAllConversation(
        @NotNull @PathVariable("pageNumber") Integer pageNumber,
        @NotNull @PathVariable("pageSize") Integer pageSize
    ){
        Pagination<ConversationDTO> pagination = conversationService.getAllConversationFromAuthUSer(pageNumber, pageSize);
        return ResponseEntity.ok(pagination);
    }
}
