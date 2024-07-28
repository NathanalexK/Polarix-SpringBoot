package com.example.demo.restController.user;

import com.example.demo.dto.friend.*;
import com.example.demo.model.misc.Notification;
import com.example.demo.model.user.Friend;
import com.example.demo.service.user.FriendService;
import com.example.demo.service.util.WebSocketService;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;
    private final WebSocketService webSocketService;

    public FriendController(FriendService friendService, WebSocketService webSocketService) {
        this.friendService = friendService;
        this.webSocketService = webSocketService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<FriendRowDTO>> getAuthenticatedUserFriend(){
        List<FriendRowDTO> friendDTOList = friendService.getFriendListByAuthenticatedUser();
        return ResponseEntity.ok(friendDTOList);
    }

    @GetMapping("/request")
    public ResponseEntity<FriendRequestListDTO> getAuthenticatedFriendRequest(){
        List<Friend> friendList = friendService.getFriendRequestFromAuthenticatedUser();
        return ResponseEntity.ok(new FriendRequestListDTO(friendList));
    }

    @PostMapping("/send")
    public ResponseEntity<FriendDTO> sendFriendRequest(@NotNull @RequestBody SendFriendRequestDTO sendFriendRequestDTO) {
        Friend friend = friendService.sendFriendRequest(sendFriendRequestDTO);
        return ResponseEntity.ok(new FriendDTO(friend));
    }

    @PutMapping("/accept")
    public ResponseEntity<FriendDTO> acceptFriendRequest(@NotNull @RequestBody SendFriendActionDTO sendFriendActionDTO) {
        Friend friend = friendService.acceptFriendRequest(sendFriendActionDTO);
        webSocketService.sendNotification(
                "/topic/user/" + sendFriendActionDTO.getUsername(),
                Notification.info("Friend Accepted", friend.getSender().getUsername() + " accepted your invitation")
        );
        return ResponseEntity.ok(new FriendDTO(friend));
    }

    @DeleteMapping("/reject")
    public ResponseEntity<Void> rejectFriendRequest(@NotNull @RequestBody SendFriendActionDTO sendFriendActionDTO){
        friendService.rejectFriendRequest(sendFriendActionDTO);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletFriend(@NotNull @RequestBody SendFriendActionDTO sendFriendActionDTO){
        friendService.deleteFriend(sendFriendActionDTO);
        return ResponseEntity.ok(null);
    }
}
