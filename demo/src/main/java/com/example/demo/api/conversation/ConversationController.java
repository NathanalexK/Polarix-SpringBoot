package com.example.demo.api.conversation;

import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.service.conversation.ConversationService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getConversation(@NotNull @PathVariable("id") Integer idConversation) {
        ConversationDTO conversationDTO = conversationService.getConversationFromAuthUser(idConversation);
        return ResponseEntity.ok(conversationDTO);
    }

    @GetMapping("/typing/{idConversation}/{isTyping}")
    public ResponseEntity<Void> setIsTyping(
        @NotNull @PathVariable("idConversation") Integer idConversation,
        @NotNull @PathVariable("isTyping") Boolean isTyping
    ) {
        conversationService.setIsTyping(idConversation, isTyping);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/visit/{idConversation}")
    public ResponseEntity<Void> visitConversation(@NotNull @PathVariable("idConversation") Integer idConversation){
        conversationService.visitConversation(idConversation);
        return ResponseEntity.ok(null);
    }
}
