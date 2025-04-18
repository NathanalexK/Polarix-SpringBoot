package com.example.demo.api.user;

import com.example.demo.dto.friend.*;
import com.example.demo.dto.user.UserSimpleDetailsDTO;
import com.example.demo.model.misc.Notification;
import com.example.demo.model.user.Friend;
import com.example.demo.service.user.AppUserService;
import com.example.demo.service.user.FriendService;
import com.example.demo.service.util.WebSocketService;
import com.example.demo.util.Pagination;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;
    private final WebSocketService webSocketService;
    private final AppUserService appUserService;

    public FriendController(FriendService friendService, WebSocketService webSocketService, AppUserService appUserService) {
        this.friendService = friendService;
        this.webSocketService = webSocketService;
        this.appUserService = appUserService;
    }

//    @GetMapping("/list")
//    public ResponseEntity<List<FriendRowDTO>> getAuthenticatedUserFriend(){
//        List<FriendRowDTO> friendDTOList = friendService.getFriendListByAuthenticatedUser();
//        return ResponseEntity.ok(friendDTOList);
//    }

    @GetMapping("/request")
    public ResponseEntity<FriendRequestListDTO> getAuthenticatedFriendRequest(){
        List<Friend> friendList = friendService.getFriendRequestFromAuthenticatedUser();
        return ResponseEntity.ok(new FriendRequestListDTO(friendList));
    }

    @PostMapping("/send")
    public ResponseEntity<FriendDTO> sendFriendRequest(@NotNull @RequestParam("username") String username) {
        Friend friend = friendService.sendFriendRequest(username);
        return ResponseEntity.ok(new FriendDTO(friend));
    }

    @PostMapping("/accept")
    public ResponseEntity<FriendDTO> acceptFriendRequest(@NotNull @RequestParam("username") String username) {
        Friend friend = friendService.acceptFriendRequest(username);
        System.out.println("ok");
//        webSocketService.sendNotification(
//                "/topic/user/" + username,
//                Notification.info("Friend Accepted", friend.getSender().getUsername() + " accepted your invitation")
//        );
        return ResponseEntity.ok(new FriendDTO(friend));
    }

    @DeleteMapping("/reject")
    public ResponseEntity<Void> rejectFriendRequest(@NotNull @RequestParam("username") String username){
        friendService.rejectFriendRequest(username);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFriend(@NotNull @RequestParam("username") String username){
        friendService.deleteFriend(username);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/list/{username}/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<UserSimpleDetailsDTO>> getAllFriend(
        @NotNull @PathVariable("username") String username,
        @NotNull @PathVariable("pageNumber") Integer pageNumber,
        @NotNull @PathVariable("pageSize") Integer pageSize
    ) {
        Pagination<UserSimpleDetailsDTO> pagination = appUserService.getFriendByUsername(username, pageNumber, pageSize);
        return ResponseEntity.ok(pagination);
    }

    @GetMapping("/request/list/{username}/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<UserSimpleDetailsDTO>> getAllFriendRequest(
        @NotNull @PathVariable("username") String username,
        @NotNull @PathVariable("pageNumber") Integer pageNumber,
        @NotNull @PathVariable("pageSize") Integer pageSize
    ) {
        Pagination<UserSimpleDetailsDTO> pagination = appUserService.getFriendRequestByUsername(username, pageNumber, pageSize);
        return ResponseEntity.ok(pagination);
    }

    @GetMapping("/not/list/{username}/{pageNumber}/{pageSize}")
    public ResponseEntity<Pagination<UserSimpleDetailsDTO>> getAllNotFriend(
        @NotNull @PathVariable("username") String username,
        @NotNull @PathVariable("pageNumber") Integer pageNumber,
        @NotNull @PathVariable("pageSize") Integer pageSize
    ) {
        Pagination<UserSimpleDetailsDTO> pagination = appUserService.getNotFriendByUsername(username, pageNumber, pageSize);
        return ResponseEntity.ok(pagination);
    }


}
