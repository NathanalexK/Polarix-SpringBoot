package com.example.demo.repository.post;

import com.example.demo.model.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

    @Query("select p from PostLike p where p.post.id = :idPost and p.user.id = :idUser")
    PostLike findPostLikeByPostAndUser(Integer idPost, Integer idUser);
}
