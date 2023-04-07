package xyz.soulspace.restore.dto;

import lombok.Data;

@Data
public class UserBasicDTO {
    private long userId;
    private String nickName;
    private String avatarUri;
}
