package com.example.demo.service.conversation;

import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.model.user.AppUser;
import com.example.demo.repository.message.ConversationRepository;
import com.example.demo.service.user.AuthService;
import com.example.demo.util.Pagination;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final AuthService authService;

    public ConversationService(ConversationRepository conversationRepository, AuthService authService) {
        this.conversationRepository = conversationRepository;
        this.authService = authService;
    }

    @Transactional
    public Pagination<ConversationDTO> getAllConversationFromAuthUSer(Integer pageNumber, Integer pageSize) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ConversationDTO> page = conversationRepository.findAllConversationByUserPageable(currentUser.getId(), pageable);
        return new Pagination<ConversationDTO>(page);
    }
}
