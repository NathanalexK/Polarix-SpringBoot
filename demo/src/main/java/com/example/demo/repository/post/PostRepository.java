package com.example.demo.repository.post;

import com.example.demo.dto.post.PostDetailsDTO;
import com.example.demo.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select p from Post p order by p.id desc")
    Page<Post> findAllPostPageable(Pageable pageable);

    @Modifying
    @Query("update Post p set p.likeCount = p.likeCount + 1 where  p.id = :idPost")
    void incrementLike(Integer idPost);

    @Modifying
    @Query("update Post p set p.likeCount = p.likeCount - 1 where  p.id = :idPost")
    void decrementLike(Integer idPost);

    @Modifying
    @Query("update Post p set p.commentCount = p.commentCount + 1 where p.id = :idPost")
    void incrementComment(Integer idPost);

    @Query("""
        select new com.example.demo.dto.post.PostDetailsDTO (
            p.id,
            p.user.username,
            p.user.name,
            p.user.picture,
            p.date,
            p.text,
            p.picture,
            p.likeCount,
            p.commentCount,
            case when pl.id is null then false else true end
        ) 
        from Post p
        left join PostLike pl
        on pl.post.id = p.id and pl.user.id = :idUser
        order by p.id desc 
    """)
    Page<PostDetailsDTO> findAllPostPageableWithLikeStatus(@Param("idUser") Integer idUser, Pageable pageable);



    @Query("""
        select new com.example.demo.dto.post.PostDetailsDTO (
            p.id,
            p.user.username,
            p.user.name,
            p.user.picture,
            p.date,
            p.text,
            p.picture,
            p.likeCount,
            p.commentCount,
            case when pl.id is null then false else true end
        ) 
        from Post p
        left join PostLike pl
        on pl.post.id = p.id and pl.user.id = :idUser
        where p.id = :idPost
    """)
    PostDetailsDTO findPostByIdWithLikeStatus(@Param("idUser") Integer idUser, @Param("idPost") Integer idPost);

    @Query("""
        select new com.example.demo.dto.post.PostDetailsDTO (
            p.id,
            p.user.username,
            p.user.name,
            p.user.picture,
            p.date,
            p.text,
            p.picture,
            p.likeCount,
            p.commentCount,
            case when pl.id is null then false else true end
        ) 
        from Post p
        left join PostLike pl
        on pl.post.id = p.id and pl.user.username = :viewer
        where p.user.username = :user
    """)
    Page<PostDetailsDTO> findPostByUserWithLikeStatusPageable(
            @Param("viewer") String viewerUsername,
            @Param("user") String currentUsername,
            Pageable pageable
    );
}
