package com.example.demo.api.conversation;

import com.example.demo.dto.conversation.MessageDTO;
import com.example.demo.dto.conversation.SendMessageDTO;
import com.example.demo.service.conversation.ConversationService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final ConversationService conversationService;

    public MessageController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("list/{idConversation}/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<MessageDTO>> getMessages(
        @NotNull @PathVariable("idConversation") Integer idConversation,
        @NotNull @PathVariable("pageNumber") Integer pageNumber,
        @NotNull @PathVariable("pageSize") Integer pageSize
    ) {
        Pagination<MessageDTO> messageDTOPagination = conversationService.getAllMessageFromConversation(idConversation, pageNumber, pageSize);
        return ResponseEntity.ok(messageDTOPagination);
    }

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(@NotNull @RequestBody SendMessageDTO sendMessageDTO) {
        MessageDTO messageDTO = conversationService.sendMessage(sendMessageDTO);
        return ResponseEntity.ok(messageDTO);
    }
}
