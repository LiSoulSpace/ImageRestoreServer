package xyz.soulspace.restore.dto;

import lombok.Data;

@Data
public class ImageBaseInfoDTO {
    private String imageName;
    private String sourcePath;
    private String thumbnailPath;
    private String imageMd5;
}
