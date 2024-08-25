package com.example.demo.repository.post;

import com.example.demo.dto.post.PostCommentDTO;
import com.example.demo.model.post.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

    @Query("select new com.example.demo.dto.post.PostCommentDTO(p) from PostComment p where p.post.id = :idPost")
    Page<PostCommentDTO> findAllCommentByIdPostPageable(@Param("idPost") Integer idPost, Pageable pageable);

    @Query("""
    select new com.example.demo.dto.post.PostCommentDTO(
        p.sender.username,
        p.sender.name,
        p.sender.picture,
        p.date,
        p.content
    ) from PostComment p
    where p.post.id = :idPost
    order by p.id desc 
    """)
    Page<PostCommentDTO> findPostCommentByIdPostPageable(@Param("idPost") Integer idPost, Pageable pageable);

}
