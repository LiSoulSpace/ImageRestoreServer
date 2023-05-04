package xyz.soulspace.restore.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.service.ImageInfoService;
import xyz.soulspace.restore.service.UserImageInfoService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author soulspace
 * @since 2023-05-01 02:36:00
 */
@Controller
@CrossOrigin
@Slf4j
@Tag(name = "用户图像关系控制器(UserImageInfoController)")
@RequestMapping("/restore/imageInfo")
public class UserImageInfoController {
    @Autowired
    UserImageInfoService userImageInfoService;
    @Autowired
    ImageInfoService imageInfoService;

    @Operation(summary = "判断图像用户关系")
    @RequestMapping(value = "checkImageUserRela", method = RequestMethod.GET)
    public ResponseEntity<?> checkImageUserRela(@RequestParam("imageMd5") String imageMd5,
                                                @RequestParam("userId") Long userId) {
        CommonResult<?> commonResult = userImageInfoService.checkImageUserRela(imageMd5, userId);
        if (commonResult.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(commonResult));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(commonResult));
    }

    @Operation(summary = "分页获取某一用户图像所有信息")
    @RequestMapping(value = "/getImageInfoPageByUserId", method = RequestMethod.GET)
    public ResponseEntity<?> getImageInfoPageByUserId(@RequestParam("currentPage") Integer currentPage,
                                                      @RequestParam("pageSize") Integer pageSize,
                                                      @RequestParam("userId") Long userId) {
        List<ImageInfo> imageInfoPageByUserId = imageInfoService.getImageInfoPageByUserId(currentPage, pageSize, userId);
        return ResponseEntity.ok(CommonResult.success("success", JSON.toJSONString(imageInfoPageByUserId)));
    }

    @Operation(summary = "绑定或取消用户图像信息")
    @RequestMapping(value = "/setImageUserRela", method = RequestMethod.GET)
    public ResponseEntity<?> setImageUserRela(@RequestParam("imageMd5") String imageMd5,
                                              @RequestParam("userId") Long userId,
                                              @RequestParam("isSet") Integer isSet) {
        CommonResult<?> commonResult = userImageInfoService.setImageUserRela(imageMd5, userId, isSet);
        if (commonResult.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(commonResult));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(commonResult));
    }
}
