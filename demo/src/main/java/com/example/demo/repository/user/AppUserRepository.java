package com.example.demo.repository.user;

import com.example.demo.dto.user.UserDetailsDTO;
import com.example.demo.dto.user.UserProfileDTO;
import com.example.demo.dto.user.UserSimpleDetailsDTO;
import com.example.demo.model.user.AppUser;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    @Query("select u from AppUser u where u.username = :username")
    public AppUser findAppUserByUsername(@Param("username") String username);

    @Query("select u from AppUser u where u.username = :username and u.password = :password")
    public AppUser findAppUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Query("update AppUser u set u.password = :password where u = :user")
    public AppUser updateAppUserPassword(@NotNull @Param("user") AppUser user, @NotNull @Param("password") String newPassword);

    @Query("select new com.example.demo.dto.user.UserDetailsDTO(u) from AppUser u where (u.name ilike %:name% or u.username ilike %:name%) and u.role = 1")
    public Page<UserDetailsDTO> searchAppUserByName(@Param("name") String name, Pageable pageable);

    @Query("select new com.example.demo.dto.user.UserDetailsDTO(u) from AppUser u where u.role = 1")
    public Page<UserDetailsDTO> findAllAppUser(Pageable pageable);

//    @Query("""
//        select new com.example.demo.dto.user.UserProfileDTO(
//             au,
//             f.friendCount,
//             p.likeCount,
//             p.postCount
//        ) from (
//            select a from AppUser a where a.id = :idUser
//        ) as au
//        join(select
//                p.user.id,
//                count(p) as postCount,
//                sum(p.likeCount) as likeCount
//            from Post p
//            where p.user.id = :idUser
//            group by p.user.id) as p
//        on au.id = p.id
//        join(select
//                count(f) as friendCount
//            from Friend f
//            where (f.sender = :idUser or f.receiver = :idUser) and f.dateConfirm is not null
//        ) as f
//    """)
    @Query("""
        select new com.example.demo.dto.user.UserProfileDTO(
            au,
            (select cast(count(f) as integer) from Friend f where (f.sender.id = au.id or f.receiver.id = au.id) and f.dateConfirm is not null),
            (select cast(sum(p.likeCount) as integer) from Post p where p.user.id = au.id),
            (select cast(count(p) as integer) from Post p where p.user.id = au.id),
            (:idViewer = :idUser)
        )
        from AppUser au
        where au.id = :idUser
    """)
    public UserProfileDTO findUserProfileByIdUser(Integer idUser, Integer idViewer);

    @Query("""
        SELECT new com.example.demo.dto.user.UserSimpleDetailsDTO(
               u,
               CASE
                   WHEN f1.id IS NULL AND f2.id IS NULL THEN 'NONE'
                   WHEN f1.id IS NOT NULL AND f1.dateConfirm IS NULL THEN 'CONFIRM'
                   WHEN f2.id IS NOT NULL AND f2.dateConfirm IS NULL THEN 'SEND'
                   WHEN (f1.id IS NOT NULL AND f1.dateConfirm IS NOT NULL) OR
                        (f2.id IS NOT NULL AND f2.dateConfirm IS NOT NULL) THEN 'FRIEND'
                   END 
               )
        FROM AppUser u
                 LEFT JOIN Friend f1 ON f1.sender.id = u.id AND f1.receiver.id = :idUser
                 LEFT JOIN Friend f2 ON f2.receiver.id = u.id AND f2.sender.id = :idUser
        WHERE u.id <> :idUser
    """)
    public Page<UserSimpleDetailsDTO> findAllUserSimpleDetailsByIdUserPageable(Integer idUser, Pageable pageable);

    @Query("""
        SELECT new com.example.demo.dto.user.UserSimpleDetailsDTO(
               u,
               'NONE'
        )
        FROM AppUser u
                 LEFT JOIN Friend f1 ON f1.sender.id = u.id AND f1.receiver.id = :idUser
                 LEFT JOIN Friend f2 ON f2.receiver.id = u.id AND f2.sender.id = :idUser
        WHERE u.id <> :idUser AND f1.id IS NULL AND f2.id IS NULL
    """)
    public Page<UserSimpleDetailsDTO> findAllUserNotFriendByIdUserPageable(Integer idUser, Pageable pageable);

    @Query("""
        SELECT new com.example.demo.dto.user.UserSimpleDetailsDTO(
               u,
               'FRIEND'
               )
        FROM AppUser u
                 LEFT JOIN Friend f1 ON f1.sender.id = u.id AND f1.receiver.id = :idUser
                 LEFT JOIN Friend f2 ON f2.receiver.id = u.id AND f2.sender.id = :idUser
        WHERE u.id <> :idUser AND ((f1.id IS NOT NULL AND f1.dateConfirm IS NOT NULL) OR
                        (f2.id IS NOT NULL AND f2.dateConfirm IS NOT NULL))
    """)
    public Page<UserSimpleDetailsDTO> findAllFriendByIdUserPageable(Integer idUser, Pageable pageable);

    @Query("""
        SELECT new com.example.demo.dto.user.UserSimpleDetailsDTO(
               u,
               'CONFIRM'
               )
        FROM AppUser u
                 LEFT JOIN Friend f1 ON f1.sender.id = u.id AND f1.receiver.id = :idUser
                 LEFT JOIN Friend f2 ON f2.receiver.id = u.id AND f2.sender.id = :idUser
        WHERE u.id <> :idUser AND f1.id IS NOT NULL AND f1.dateConfirm IS NULL
    """)
    public Page<UserSimpleDetailsDTO> findAllFriendRequestByIdUserPageable(Integer idUser, Pageable pageable);

}
