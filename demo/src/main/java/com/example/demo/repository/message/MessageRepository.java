package com.example.demo.repository.message;

import com.example.demo.dto.conversation.MessageDTO;
import com.example.demo.model.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("""
        SELECT new com.example.demo.dto.conversation.MessageDTO(m, :idUser)
        FROM Message m
        WHERE m.conversation.id = :idConversation
        ORDER BY m.createdAt ASC 
    """)
    Page<MessageDTO> findAllMessageByIdConversationPageable(
        @Param("idConversation") Integer idConversation,
        @Param("idUser") Integer idUser,
        Pageable pageable
    );
}
