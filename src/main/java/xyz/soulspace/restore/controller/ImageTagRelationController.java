package xyz.soulspace.restore.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.service.ImageTagRelationService;
import xyz.soulspace.restore.service.TagService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author soulspace
 * @since 2023-05-01 02:05:18
 */
@Controller
@RequestMapping("/restore/tag")
@Tag(name = "图像标签关系控制器(ImageTagRelationController)")
@Slf4j
@CrossOrigin
@ResponseBody
public class ImageTagRelationController {
    @Autowired
    ImageTagRelationService imageTagRelationService;
    @Autowired
    TagService tagService;

    @Operation(summary = "根据图像md5获取图像标签信息")
    @RequestMapping(value = "/getTagsByImageMd5", method = RequestMethod.GET)
    public ResponseEntity<?> getTagsByImageMd5(@RequestParam("imageMd5") String imageMd5) {
        CommonResult<?> tagsByImageMd5 = imageTagRelationService.getTagsByImageMd5(imageMd5);
        if (tagsByImageMd5.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(tagsByImageMd5));
        return ResponseEntity.internalServerError().body(JSON.toJSONString(tagsByImageMd5));
    }

    @Operation(summary = "通过多标签筛选，获取图像基本信息增加分页")
    @RequestMapping(value = "/getImageBaseInfoByTagPage", method = RequestMethod.GET)
    public ResponseEntity<?> getImageBaseInfoByTagPage(@RequestParam("tags") List<String> tag,
                                                       @RequestParam("currentPage") Integer currentPage,
                                                       @RequestParam("pageSize") Integer pageSize,
                                                       @RequestParam(value = "userId", required = false) Integer userId) {
        CommonResult<?> imageBaseInfoByTagPage = imageTagRelationService.getImageBaseInfoByTagPage(tag, currentPage, pageSize, userId);
        if (imageBaseInfoByTagPage.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(imageBaseInfoByTagPage));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(imageBaseInfoByTagPage));
    }

    @Operation(summary = "保存标签图像关系")
    @RequestMapping(value = "/saveTagImageRe", method = RequestMethod.GET)
    public ResponseEntity<?> saveTagImageRe(@RequestParam("tagId") Long tagId,
                                            @RequestParam("imageId") Long imageId) {
        CommonResult<?> commonResult = tagService.saveTagImageRelation(tagId, imageId);
        if (commonResult.isSuccess()) return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }

    @Operation(summary = "删除标签图像关系")
    @RequestMapping(value = "/deleteTagImageRe", method = RequestMethod.GET)
    public ResponseEntity<?> deleteTagImageRe(@RequestParam("tagId") Long tagId,
                                              @RequestParam("imageId") Long imageId) {
        CommonResult<?> commonResult = imageTagRelationService.deleteTagImageRelation(tagId, imageId);
        if (commonResult.isSuccess()) return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }
}
