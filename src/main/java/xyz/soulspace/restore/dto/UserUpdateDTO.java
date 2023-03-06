package xyz.soulspace.restore.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateDTO {
    private Long userId;
    private String nickName;
}
