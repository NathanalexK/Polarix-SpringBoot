package com.example.demo.service.user;

import com.example.demo.dto.friend.SendFriendActionDTO;
import com.example.demo.dto.friend.SendFriendRequestDTO;
import com.example.demo.dto.friend.FriendRowDTO;
import com.example.demo.exception.CustomHttpException;
import com.example.demo.model.misc.Notification;
import com.example.demo.model.user.AppUser;
import com.example.demo.model.user.Friend;
import com.example.demo.repository.user.AppUserRepository;
import com.example.demo.repository.user.FriendRepository;
import com.example.demo.service.util.WebSocketService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FriendService {
    private final AuthService authService;
    private final FriendRepository friendRepository;
    private final AppUserRepository appUserRepository;
    private final JdbcTemplate jdbcTemplate;
    private final WebSocketService webSocketService;

    public FriendService(AuthService authService, FriendRepository friendRepository, AppUserRepository appUserRepository, JdbcTemplate jdbcTemplate, WebSocketService webSocketService) {
        this.authService = authService;
        this.friendRepository = friendRepository;
        this.appUserRepository = appUserRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.webSocketService = webSocketService;
    }

    public Friend sendFriendRequest(String username) {
        AppUser currentUser = authService.getAuthenticatedAppUser();
        AppUser request = appUserRepository.findAppUserByUsername(username);

        Friend friendCheck = friendRepository.findFriendByTwoUsers(currentUser.getUsername(), username);

        if (request == null) {
            throw new CustomHttpException("Unknown user for username: " + username + " !", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(friendCheck != null) {
            throw new CustomHttpException("Friend already requested!", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        try {
            Friend friend = new Friend();
            friend.setSender(currentUser);
            friend.setReceiver(request);
            friend.setDateSend(LocalDate.now());
            friend.setDateConfirm(null);
            Friend saved = friendRepository.save(friend);
            webSocketService.sendNotification(
                "/topic/user/" + username,
                Notification.info("Friend Request", "<b>" + currentUser.getName() + "</b> send you an invitation")
            );
            return saved;
        } catch (Exception e) {
            throw new CustomHttpException("Friend request already sended!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public Friend acceptFriendRequest(String username) {
        Authentication auth = authService.getAuthenticated();
        return acceptFriendRequest(username, auth.getName());
    }

    @Transactional
    public Friend acceptFriendRequest(String sender, String receiver) {
        Friend friend = friendRepository.findFriendBySenderAndReceiver(sender, receiver);

        if (friend == null) {
            throw new CustomHttpException("Unknown friend request between sender: " + receiver + " and receiver: " + sender + " !", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        friend.setDateConfirm(LocalDate.now());
        Friend saved = friendRepository.save(friend);
        webSocketService.sendNotification(
                "/topic/user/" + sender,
                Notification.info("Friend Accepted", "<b>" + friend.getSender().getName() + "</b> accepted your invitation")
        );
        return saved;
    }

    @Transactional
    public void rejectFriendRequest(String username) {
        Authentication auth = authService.getAuthenticated();
        Friend friend = friendRepository.findFriendBySenderAndReceiver(username, auth.getName());

        if(friend == null){
            throw new CustomHttpException("Unknown friend request between sender: " + username + " and receiver: " + auth.getName() + " !", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        friendRepository.delete(friend);
    }

    @Transactional
    public void deleteFriend(String username){
        Authentication auth = authService.getAuthenticated();
        Friend friend = friendRepository.findFriendByTwoUsers(auth.getName(), username);

        if(friend == null){
            throw new CustomHttpException("Unknown friend request between sender: " + username + " and receiver: " + auth.getName() + " !", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        friendRepository.delete(friend);
    }

    public List<Friend> getFriendRequestFromUsername(String username){
        List<Friend> requestFriendList = friendRepository.findFriendRequestByUser(username);
        return requestFriendList;
    }

    public List<Friend> getFriendRequestFromAuthenticatedUser(){
        Authentication auth = authService.getAuthenticated();
        return this.getFriendRequestFromUsername(auth.getName());
    }



    public List<FriendRowDTO> getFriendListByUser(String username) {
        AppUser user = appUserRepository.findAppUserByUsername(username);
        String sql = """
            select distinct
                username,
                last_online,
                picture
            from(
            select
                id_receiver as user
            from friend
            where friend.id_sender = ? and friend.date_confirm is not null
            union
            select
                id_sender as user
            from friend
            where friend.id_receiver = ? and friend.date_confirm is not null) as a
            join app_user
            on app_user.id = a.user
        """;
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, user.getId(), user.getId());

        List<FriendRowDTO> friendRowDTOList = new ArrayList<>();

        for (Map<String, Object> map : mapList) {
            FriendRowDTO friendRowDTO = new FriendRowDTO();
            friendRowDTO.setUsername(String.valueOf(map.get("username")));
            friendRowDTO.setLastOnline(String.valueOf(map.get("last_online")));
            friendRowDTO.setPicture(String.valueOf(map.get("picture")));
            friendRowDTOList.add(friendRowDTO);
        }

        return friendRowDTOList;
    }

    public List<FriendRowDTO> getFriendListByAuthenticatedUser() {
        System.out.println("ok");
        Authentication auth = authService.getAuthenticated();
        return this.getFriendListByUser(auth.getName());
    }
}

