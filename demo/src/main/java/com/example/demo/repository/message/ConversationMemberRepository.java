package com.example.demo.repository.message;

import com.example.demo.model.message.ConversationMember;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Integer> {
    @Override
    List<ConversationMember> findAll();

    @Query("""
        select a.conversation from ConversationMember a
    """)
    List<ConversationMember> test();
}
