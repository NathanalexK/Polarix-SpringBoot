package com.example.demo.service.conversation;

import com.example.demo.dto.conversation.*;
import com.example.demo.dto.user.UserSimpleDetailsDTO;
import com.example.demo.model.message.Conversation;
import com.example.demo.model.message.ConversationMember;
import com.example.demo.model.message.Message;
import com.example.demo.model.user.AppUser;
import com.example.demo.repository.message.ConversationMemberRepository;
import com.example.demo.repository.message.ConversationRepository;
import com.example.demo.repository.message.MessageRepository;
import com.example.demo.service.user.AuthService;
import com.example.demo.service.util.WebSocketService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final AuthService authService;
    private final MessageRepository messageRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final WebSocketService webSocketService;

    public ConversationService(ConversationRepository conversationRepository, AuthService authService,
                               MessageRepository messageRepository, ConversationMemberRepository conversationMemberRepository, WebSocketService webSocketService) {
        this.conversationRepository = conversationRepository;
        this.authService = authService;
        this.messageRepository = messageRepository;
        this.conversationMemberRepository = conversationMemberRepository;
        this.webSocketService = webSocketService;
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
        System.out.println("idConversation: " + idConversation);
        System.out.println("idUser: " + currentUser.getId());
        Optional<ConversationDTO> conversation = conversationRepository.findConversationByIdAndIsUser(idConversation, currentUser.getId());
        System.out.println(conversation);
        return conversation.orElseThrow();
    }

    @Transactional
    public MessageDTO sendMessage(final SendMessageDTO sendMessageDTO){
        final AppUser currentUser = authService.getAuthenticatedAppUser();
        final Conversation conversation = conversationRepository.findById(sendMessageDTO.getIdConversation()).orElseThrow();
//        ConversationMember conversationMember = conversationMemberRepository.

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setContent(sendMessageDTO.getContent());
        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        conversation.setLastMessage(savedMessage);
        conversation.setLastUser(currentUser);

        Thread thread = new Thread(() -> {
            ConversationMember conversationMember = conversationMemberRepository.findConversationMemberByConversationAndIdUser(conversation.getId(), currentUser.getId()).orElseThrow();
            conversationMember.setLastSeenMessage(savedMessage);
            messageRepository.save(savedMessage);
            System.out.println("completed");
        });
        thread.run();

        return new MessageDTO(savedMessage, currentUser.getId());
    }

    @Transactional
    public Pagination<MessageDTO> getAllMessageFromConversation(@NotNull Integer idConversation, Integer pageNumber, Integer pageSize) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<MessageDTO> messageDTOPage = messageRepository.findAllMessageByIdConversationPageable(idConversation, currentUser.getId(), pageable);
        visitConversation(currentUser, idConversation);
        return new Pagination<MessageDTO>(messageDTOPage);
    }

    @Transactional
    public void visitConversation(AppUser currentUser, Integer idConversation){
        ConversationMember conversationMember = conversationMemberRepository.findConversationMemberByConversationAndIdUser(idConversation, currentUser.getId()).orElseThrow();
        Conversation conversation = conversationRepository.findById(idConversation).orElseThrow();
        Message lastMessage = conversation.getLastMessage();
        conversationMember.setLastSeenMessage(lastMessage);
        conversationMemberRepository.save(conversationMember);
    }

    @Transactional
    public void visitConversation(Integer idConversation) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        visitConversation(currentUser, idConversation);
    }

    @Transactional
    public void setIsTyping(Integer idConversation, Boolean isTyping){
        AppUser currentUser = authService.getAuthenticatedAppUser();
        TypingMessageDTO typingMessageDTO = new TypingMessageDTO();
        typingMessageDTO.setUser(new UserSimpleDetailsDTO(currentUser));
        typingMessageDTO.setIsTyping(isTyping);
        System.out.println("typing: " + isTyping);
        webSocketService.send("/topic/conversation/" + idConversation, typingMessageDTO);
    }

//    @Transactional
//    public UpdateMessageViewDTO updateMessageView(Integer idConversation) {
//        AppUser currentUser = authService.getAuthenticatedAppUser();
//        Conversation conversation = conversationRepository.findById(idConversation).orElseThrow();
//        Message lastMessage = conversation.getLastMessage();
//
//        ConversationMember conversationMember = conversationMemberRepository.findConversationMemberByConversationAndIdUser(idConversation, currentUser.getId()).orElseThrow();
//
//        conversationMember.setLastSeenMessage(lastMessage);
//
//    }
}
