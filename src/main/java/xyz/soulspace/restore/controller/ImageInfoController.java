package xyz.soulspace.restore.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.service.ImageInfoService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 07:04:23
 */
@Controller
@RequestMapping("/restore/imageInfo")
@Slf4j
@CrossOrigin
@Tag(name = "图像信息控制器(ImageInfoController)")
@ResponseBody
public class ImageInfoController {
    @Autowired
    ImageInfoService imageInfoService;

    @Value("${image.userImagePath}")
    String userImageDir;

    @Operation(summary = "上传图像信息")
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST,
            consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public ResponseEntity<?> uploadImage(@RequestParam Long userId,
                                         @RequestParam MultipartFile file) {
        return imageInfoService.uploadImageByUserId(userId, file);
    }

    @Operation(summary = "分页获取图像路径信息")
    @RequestMapping(value = "/getImagePathPage", method = RequestMethod.GET)
    public ResponseEntity<?> getImagePathPage(@Param("currentPage") Integer currentPage,
                                              @Param("pageSize") Integer pageSize) {
        List<String> imagePathPage = imageInfoService.getImagePathPage(currentPage, pageSize);
        return ResponseEntity.ok(CommonResult.success("success", JSON.toJSONString(imagePathPage)));
    }

    @Operation(summary = "分页获取图像所有信息")
    @RequestMapping(value = "/getImageInfoPage", method = RequestMethod.GET)
    public ResponseEntity<?> getImageInfoPage(@Param("currentPage") Integer currentPage,
                                              @Param("pageSize") Integer pageSize) {
        List<ImageInfo> imagePathPage = imageInfoService.getImageInfoPage(currentPage, pageSize);
        return ResponseEntity.ok(CommonResult.success("success", JSON.toJSONString(imagePathPage)));
    }

    @Operation(summary = "获取公共图像数量")
    @RequestMapping(value = "/getPublicImageCount", method = RequestMethod.GET)
    public ResponseEntity<?> getPublicImageCount() {
        CommonResult<?> countByUserId = imageInfoService.countByUserId(null);
        return ResponseEntity.ok(countByUserId);
    }

    @Operation(summary = "根据用户id获取对应的图像数量")
    @RequestMapping(value = "/getImageCountByUserId", method = RequestMethod.GET)
    public ResponseEntity<?> getImageCountByUserId(
            @RequestParam(value = "userId") Long userId
    ) {
        CommonResult<?> countByUserId = imageInfoService.countByUserId(userId);
        return ResponseEntity.ok(countByUserId);
    }

    @Operation(summary = "分页获取某一用户图像所有信息")
    @RequestMapping(value = "/getImageInfoPageByUserId", method = RequestMethod.GET)
    public ResponseEntity<?> getImageInfoPageByUserId(@Param("currentPage") Integer currentPage,
                                                      @Param("pageSize") Integer pageSize,
                                                      @Param("userId") Long userId) {
        List<ImageInfo> imageInfoPageByUserId = imageInfoService.getImageInfoPageByUserId(currentPage, pageSize, userId);
        return ResponseEntity.ok(CommonResult.success("success", JSON.toJSONString(imageInfoPageByUserId)));
    }

    @Operation(summary = "通过图像id获取图像路径")
    @RequestMapping(value = "/getImagePathById", method = RequestMethod.GET)
    public ResponseEntity<?> getImagePathById(@Param("id") Long id) {
        CommonResult<?> imagePathById = imageInfoService.getImagePathById(id);
        return ResponseEntity.ok(imagePathById);
    }

    @Operation(summary = "通过图像id对图像进行处理")
    @RequestMapping(value = "imageRestoreById", method = RequestMethod.POST)
    public ResponseEntity<?> imageRestoreById(Long imageId) {
        CommonResult<?> commonResult = imageInfoService.imageRestoreById(imageId);
        if (commonResult.getCode() == 0)
            return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }
}
