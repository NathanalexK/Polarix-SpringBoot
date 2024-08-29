package com.example.demo.repository.message;

import com.example.demo.dto.conversation.ConversationDTO;
import com.example.demo.model.message.Conversation;
import com.example.demo.util.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    @Query("""
        SELECT new com.example.demo.dto.conversation.ConversationDTO(cm) 
        FROM ConversationMember cm 
        where cm.user.id = :idUser
    """)
    Page<ConversationDTO> findAllConversationByUserPageable(@Param("idUser") Integer idUser, Pageable pageable);

    @Query("""
        SELECT new com.example.demo.dto.conversation.ConversationDTO(cm) 
        FROM ConversationMember cm 
        where cm.conversation.id = :idConversation AND cm.user.id = :idUser
    """)
    Optional<ConversationDTO> findConversationByUserPageable(@Param("idConversation") Integer idConversation, @Param("idUser") Integer idUser);

    @Override
    List<Conversation> findAll();
}
