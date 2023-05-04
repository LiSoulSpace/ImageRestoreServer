package xyz.soulspace.restore.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.service.ImageInfoService;
import xyz.soulspace.restore.service.UserService;

import java.util.List;

/**
 * <p>
 * 图像信息控制器
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
    @Autowired
    UserService userService;

    @Value("${image.userImagePath}")
    String userImageDir;

    @Operation(summary = "上传图像信息")
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST, consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public ResponseEntity<?> uploadImage(@RequestParam Long userId, @RequestParam MultipartFile file) {
        return imageInfoService.uploadImageByUserId(userId, file);
    }

    @Operation(summary = "分页获取图像路径信息")
    @RequestMapping(value = "/getImagePathPage", method = RequestMethod.GET)
    public ResponseEntity<?> getImagePathPage(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        List<String> imagePathPage = imageInfoService.getImagePathPage(currentPage, pageSize);
        return ResponseEntity.ok(CommonResult.success("success", JSON.toJSONString(imagePathPage)));
    }

    @Operation(summary = "分页获取图像所有信息")
    @RequestMapping(value = "/getImageInfoPage", method = RequestMethod.GET)
    public ResponseEntity<?> getImageInfoPage(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        List<ImageInfo> imagePathPage = imageInfoService.getImageInfoPage(currentPage, pageSize);
        return ResponseEntity.ok(CommonResult.success("success", JSON.toJSONString(imagePathPage)));
    }

    @Operation(summary = "获取图像最大的宽度和高度")
    @RequestMapping(value = "/getImageMaxWidHei", method = RequestMethod.GET)
    public ResponseEntity<?> getImageMaxWidHei() {
        CommonResult<?> imageMaxWidHei = imageInfoService.getImageMaxWidHei();
        if (imageMaxWidHei.isSuccess()) return ResponseEntity.ok(JSON.toJSONString(imageMaxWidHei));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(imageMaxWidHei));
    }

    @Operation(summary = "获取图像信息的数量")
    @RequestMapping(value = "/getImageInfoCount", method = RequestMethod.GET)
    public ResponseEntity<?> getImageInfoCount() {
        CommonResult<?> imageInfoCount = imageInfoService.getImageInfoCount();
        if (imageInfoCount.isSuccess()) return ResponseEntity.ok(JSON.toJSONString(imageInfoCount));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(imageInfoCount));
    }

    @Operation(summary = "分页获取图像基本信息")
    @RequestMapping(value = "/getImageBaseInfoPage", method = RequestMethod.GET)
    public ResponseEntity<?> getImageBaseInfoPage(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        CommonResult<?> imageBaseInfoPage = imageInfoService.getImageBaseInfoPage(currentPage, pageSize);
        if (imageBaseInfoPage.isSuccess()) return ResponseEntity.ok(imageBaseInfoPage);
        else return ResponseEntity.internalServerError().body(imageBaseInfoPage);
    }

    @Operation(summary = "通过图像md5获取图像信息")
    @RequestMapping(value = "/getImageInfoByMd5", method = RequestMethod.GET)
    public ResponseEntity<?> getImageInfoByMd5(@RequestParam("imageMd5") String imageMd5) {
        CommonResult<?> imageInfoByMd5 = imageInfoService.getImageInfoByMd5(imageMd5);
        if (imageInfoByMd5.isSuccess()) return ResponseEntity.ok(JSON.toJSONString(imageInfoByMd5));
        else return ResponseEntity.internalServerError().body(imageInfoByMd5);
    }

    @Operation(summary = "通过图像id获取图像路径")
    @RequestMapping(value = "/getImagePathById", method = RequestMethod.GET)
    public ResponseEntity<?> getImagePathById(@RequestParam("id") Long id) {
        CommonResult<?> imagePathById = imageInfoService.getImagePathById(id);
        if (imagePathById.isSuccess()) return ResponseEntity.ok(imagePathById);
        else return ResponseEntity.internalServerError().body(imagePathById);
    }

    @Operation(summary = "通过图像id 图像修复")
    @RequestMapping(value = "/imageRestoreById", method = RequestMethod.GET)
    public ResponseEntity<?> imageRestoreById(@RequestParam("imageId") Long imageId) {
        CommonResult<?> commonResult = imageInfoService.imageRestoreById(imageId);
        if (commonResult.getCode() == 0) return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }

    @Operation(summary = "通过图像id 图像上色")
    @RequestMapping(value = "/imageColorizeById", method = RequestMethod.GET)
    public ResponseEntity<?> imageColorizeById(@RequestParam("imageId") Long imageId) {
        CommonResult<?> commonResult = imageInfoService.imageColorizeById(imageId);
        if (commonResult.isSuccess()) return ResponseEntity.ok(JSON.toJSONString(commonResult));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(commonResult));
    }

    @Operation(summary = "通过图像id 生成图像缩略图")
    @RequestMapping(value = "/imageResizeById", method = RequestMethod.GET)
    public ResponseEntity<?> imageResizeById(@RequestParam("imageId") Long imageId) {
        CommonResult<?> commonResult = imageInfoService.imageFixSmallById(imageId);
        if (commonResult.getCode() == 0) return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }

    @Operation(summary = "通过图像id 删除对应的图片及相关信息")
    @RequestMapping(value = "/deleteImageById", method = RequestMethod.GET)
    public ResponseEntity<?> deleteImageById(@RequestParam("imageId") Long imageId) {
        CommonResult<?> commonResult = imageInfoService.deleteImageInfoById(imageId);
        if (commonResult.getCode() == 0) return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }
}
