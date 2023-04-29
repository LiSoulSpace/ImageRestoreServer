package xyz.soulspace.restore.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.service.ImageInfoService;
import xyz.soulspace.restore.service.OriginSmallRelationService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author soulspace
 * @since 2023-04-26 10:31:06
 */
@Controller
@RequestMapping("/restore/imageInfo")
public class OriginSmallRelationController {
    @Autowired
    ImageInfoService imageInfoService;
    @Autowired
    OriginSmallRelationService originSmallRelationService;

    @Operation(summary = "获取公共图像数量")
    @RequestMapping(value = "/getPublicImageCount", method = RequestMethod.GET)
    public ResponseEntity<?> getPublicImageCount() {
        CommonResult<?> countByUserId = originSmallRelationService.countByIsPublic((byte) 1);
        if (countByUserId.isSuccess()) return ResponseEntity.ok(JSON.toJSONString(countByUserId));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(countByUserId));
    }

    @Operation(summary = "根据用户id获取对应的图像数量")
    @RequestMapping(value = "/getImageCountByUserId", method = RequestMethod.GET)
    public ResponseEntity<?> getImageCountByUserId(
            @RequestParam(value = "userId") Long userId
    ) {
        CommonResult<?> countByUserId = imageInfoService.countByUserId(userId);
        if (countByUserId.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(countByUserId));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(countByUserId));
    }

    @Operation(summary = "分页获取用户图像的基本信息")
    @RequestMapping(value = "/getImageBaseInfoByUserIdPage", method = RequestMethod.GET)
    public ResponseEntity<?> getImageBaseInfoByUserIdPage(
            @RequestParam(value = "currentPage") Integer currentPage,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "userId") Long userId
    ) {
        CommonResult<?> imageBaseInfoPage = imageInfoService.getImageBaseInfoPage(currentPage, pageSize, userId);
        if (imageBaseInfoPage.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(imageBaseInfoPage));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(imageBaseInfoPage));
    }

}
