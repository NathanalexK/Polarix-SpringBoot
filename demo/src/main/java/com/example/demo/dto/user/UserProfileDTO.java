package com.example.demo.dto.user;

import com.example.demo.model.user.AppUser;
import lombok.Data;

@Data
public class UserProfileDTO {
    private String name;
    private String username;
    private String userPicture;
    private String biography;
    private String birthdate;
    private String createdAt;
    private String lastOnline;
    private String sex;
    private Integer friendCount;
    private Integer likeCount;
    private Integer postCount;
    private String friendType;
    private Boolean isCurrentUser;

    public UserProfileDTO() {
    }

    public UserProfileDTO(AppUser user,
                          Integer friendCount,
                          Integer likeCount,
                          Integer postCount,
                          Boolean isCurrentUser
    ) {
        this.setName(user.getName());
        this.setUsername(user.getUsername());
        this.setUserPicture(user.getPicture());
        this.setBiography(user.getBiography());
        this.setBirthdate(String.valueOf(user.getBirthdate()));
        this.setCreatedAt(String.valueOf(user.getCreatedAt()));
        this.setLastOnline(String.valueOf(user.getLastOnline()));
        this.setSex(user.getSex().name());

        this.setFriendCount(friendCount);
        this.setLikeCount(likeCount);
        this.setPostCount(postCount);

        this.setIsCurrentUser(isCurrentUser);
    }

}
