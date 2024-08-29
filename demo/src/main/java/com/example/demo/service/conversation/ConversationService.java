package com.example.demo.service.conversation;

import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.dto.conversation.MessageDTO;
import com.example.demo.dto.conversation.SendMessageDTO;
import com.example.demo.model.message.Conversation;
import com.example.demo.model.message.Message;
import com.example.demo.model.user.AppUser;
import com.example.demo.repository.message.ConversationRepository;
import com.example.demo.repository.message.MessageRepository;
import com.example.demo.service.user.AuthService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final AuthService authService;
    private final MessageRepository messageRepository;

    public ConversationService(ConversationRepository conversationRepository, AuthService authService,
                               MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.authService = authService;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public Pagination<ConversationDTO> getAllConversationFromAuthUSer(@NotNull Integer pageNumber,@NotNull Integer pageSize) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ConversationDTO> page = conversationRepository.findAllConversationByUserPageable(currentUser.getId(), pageable);
        return new Pagination<ConversationDTO>(page);
    }

    @Transactional
    public ConversationDTO getConversationFromAuthUser(@NotNull Integer idConversation) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        ConversationDTO conversation = conversationRepository.findConversationByUserPageable(idConversation, currentUser.getId()).orElseThrow();
        return conversation;
    }

    @Transactional
    public MessageDTO sendMessage(SendMessageDTO sendMessageDTO){
        AppUser currentUser = authService.getAuthenticatedAppUser();
        Conversation conversation = conversationRepository.findById(sendMessageDTO.getIdConversation()).orElseThrow();

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setContent(sendMessageDTO.getContent());
        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        return new MessageDTO(savedMessage, currentUser.getId());
    }

    @Transactional
    public Pagination<MessageDTO> getAllMessageFromConversation(@NotNull Integer idConversation, Integer pageNumber, Integer pageSize) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<MessageDTO> messageDTOPage = messageRepository.findAllMessageByIdConversationPageable(idConversation, currentUser.getId(), pageable);
        return new Pagination<MessageDTO>(messageDTOPage);
    }
}
