package com.example.demo.repository.message;

import com.example.demo.model.message.ConversationMember;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Integer> {
    @Override
    List<ConversationMember> findAll();

    @Query("""
        SELECT cm FROM ConversationMember cm WHERE cm.conversation.id = :idConversation AND cm.user.id = :idUser
    """)
    Optional<ConversationMember> findConversationMemberByConversationAndIdUser(
        @NotNull @Param("idConversation") Integer idConversation,
        @NotNull @Param("idUser") Integer idUser
    );
}
