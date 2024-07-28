package com.example.demo.repository.user;

import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    @Query("select f from Friend f where f.sender.username = :sender and f.receiver.username = :receiver")
    public Friend findFriendBySenderAndReceiver(@Param("sender") String sender, @Param("receiver") String receiver);

    @Query("select f from Friend f where (f.sender.username = :user1 and f.receiver.username = :user2) or (f.sender.username = :user2 and f.receiver.username = :user1)")
    public Friend findFriendByTwoUsers(@Param("user1") String user1, @Param("user2") String user2);

    @Query("select f from Friend f where f.receiver.username = :user and f.dateConfirm is null")
    public List<Friend> findFriendRequestByUser(@Param("user") String username);
}
