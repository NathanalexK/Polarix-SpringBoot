package com.example.demo.repository.post;

import com.example.demo.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select p from Post p order by p.id desc")
    Page<Post> findAllPostPageable(Pageable pageable);
}
