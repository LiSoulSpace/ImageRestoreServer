package xyz.soulspace.restore.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.dto.UserBasicDTO;
import xyz.soulspace.restore.service.TagService;
import xyz.soulspace.restore.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 标签信息控制器
 * </p>
 *
 * @author soulspace
 * @since 2023-03-27 04:00:28
 */
@Controller
@RequestMapping("/restore/tag")
@Slf4j
@CrossOrigin
@Tag(name = "标签信息控制器(TagInfoController)")
@ResponseBody
public class TagController {
    @Autowired
    TagService tagService;
    @Autowired
    UserService userService;

    @Operation(summary = "保存标签信息")
    @RequestMapping(value = "/saveTagInfo", method = RequestMethod.GET)
    public ResponseEntity<?> saveTagInfo(@RequestParam("currentPage") String tagName,
                                         HttpServletRequest request) {
        UserBasicDTO userBasicDTO = userService.whoAmI(request);
        CommonResult<?> commonResult = tagService.saveTag(tagName, userBasicDTO.getUserId());
        if (commonResult.isSuccess()) return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }

    @Operation(summary = "通过标签获取图像信息")
    @RequestMapping(value = "/getImageByTags", method = RequestMethod.GET)
    public ResponseEntity<?> getImageByTags(@RequestParam("tags") List<String> tags,
                                            @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        log.info("{},[{},{}]", tags, currentPage, pageSize);
        if (currentPage != null && pageSize != null) {
            CommonResult<?> imageByTags = tagService.findImageByTagsPage(tags, currentPage, pageSize);
            if (imageByTags.isSuccess()) return ResponseEntity.ok(imageByTags);
            else return ResponseEntity.internalServerError().body(imageByTags);
        } else {
            CommonResult<?> imageByTags = tagService.findImageByTags(tags);
            if (imageByTags.isSuccess()) return ResponseEntity.ok(imageByTags);
            else return ResponseEntity.internalServerError().body(imageByTags);
        }
    }

    @Operation(summary = "通过标签获取图像总数量")
    @RequestMapping(value = "/getImageCountByTags", method = RequestMethod.GET)
    public ResponseEntity<?> getImageCountByTags(@RequestParam("tags") List<String> tags) {
        CommonResult<?> commonResult = tagService.countImageByTags(tags);
        if (commonResult.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(commonResult));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(commonResult));
    }


    @Operation(summary = "通过创建者id分页获取标签(Tag)信息")
    @RequestMapping(value = "/getTagsByCreatorIdPage", method = RequestMethod.GET)
    public ResponseEntity<?> getTagsByCreatorIdPage(@Param("currentPage") Integer currentPage,
                                                    @Param("pageSize") Integer pageSize,
                                                    @Param("creatorId") Integer creatorId) {
        CommonResult<?> tagsByCreatorIdPage = tagService.getTagsByCreatorIdPage(currentPage, pageSize, Long.valueOf(creatorId));
        if (tagsByCreatorIdPage.isSuccess()) return ResponseEntity.ok(tagsByCreatorIdPage);
        else return ResponseEntity.internalServerError().body(tagsByCreatorIdPage);
    }

    @Operation(summary = "获取全部标签信息")
    @RequestMapping(value = "/getTagsAll", method = RequestMethod.GET)
    public ResponseEntity<?> getTagsAll() {
        CommonResult<?> allTags = tagService.getAllTags();
        if (allTags.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(allTags));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(allTags));
    }

    @Operation(summary = "通过创建者id获取全部标签(Tag)信息")
    @RequestMapping(value = "/getTagsByCreatorId", method = RequestMethod.GET)
    public ResponseEntity<?> getTagsByCreatorId(@RequestParam("creatorId") Integer creatorId) {
        CommonResult<?> tagsByCreatorIdPage = tagService.getTagsByCreatorIdPage(1, Integer.MAX_VALUE, Long.valueOf(creatorId));
        if (tagsByCreatorIdPage.isSuccess()) return ResponseEntity.ok(tagsByCreatorIdPage);
        else return ResponseEntity.internalServerError().body(tagsByCreatorIdPage);
    }

    @Operation(summary = "根据创建者id获取标签数量")
    @RequestMapping(value = "/countByCreatorId", method = RequestMethod.GET)
    public ResponseEntity<?> countByCreatorId(@RequestParam("creatorId") Long creatorId) {
        CommonResult<?> countByCreatorId = tagService.countByCreatorId(creatorId);
        if (countByCreatorId.isSuccess()) return ResponseEntity.ok(countByCreatorId);
        else return ResponseEntity.internalServerError().body(countByCreatorId);
    }

    @Operation(summary = "获取主要标签")
    @RequestMapping(value = "/getMainTags", method = RequestMethod.GET)
    public ResponseEntity<?> getMainTags() {
        CommonResult<?> mainTags = tagService.getMainTags();
        if (mainTags.isSuccess()) return ResponseEntity.ok(mainTags);
        else return ResponseEntity.internalServerError().body(mainTags);
    }

    @Operation(summary = "获取公共标签(排除主要标签)")
    @RequestMapping(value = "/getPublicTags", method = RequestMethod.GET)
    public ResponseEntity<?> getPublicTags() {
        CommonResult<?> publicTags = tagService.getPublicTags();
        if (publicTags.isSuccess()) return ResponseEntity.ok(publicTags);
        else return ResponseEntity.internalServerError().body(publicTags);
    }
}
