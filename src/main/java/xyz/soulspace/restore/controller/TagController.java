package xyz.soulspace.restore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public ResponseEntity<?> saveTagInfo(@Param("currentPage") String tagName,
                                         HttpServletRequest request) {
        UserBasicDTO userBasicDTO = userService.whoAmI(request);
        CommonResult<?> commonResult = tagService.saveTag(tagName, (long) userBasicDTO.getUserId());
        if (commonResult.isSuccess()) return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }

    @Operation(summary = "保存标签图像关系")
    @RequestMapping(value = "/saveTagImageRe", method = RequestMethod.GET)
    public ResponseEntity<?> saveTagImageRe(@Param("tagId") Long tagId,
                                            @Param("imageId") Long imageId) {
        CommonResult<?> commonResult = tagService.saveTagImageRelation(tagId, imageId);
        if (commonResult.isSuccess()) return ResponseEntity.ok(commonResult);
        else return ResponseEntity.internalServerError().body(commonResult);
    }

    @Operation(summary = "通过标签获取图像信息")
    @RequestMapping(value = "getImageByTags", method = RequestMethod.GET)
    public ResponseEntity<?> getImageByTags(@Param("tags") List<String> tags) {
        CommonResult<?> imageByTags = tagService.findImageByTags(tags);
        if (imageByTags.isSuccess()) return ResponseEntity.ok(imageByTags);
        else return ResponseEntity.internalServerError().body(imageByTags);
    }

    @Operation(summary = "通过创建者id获取标签信息")
    @RequestMapping(value = "getTagsByCreatorIdPage", method = RequestMethod.GET)
    public ResponseEntity<?> getTagsByCreatorIdPage(@Param("currentPage") Integer currentPage,
                                                    @Param("pageSize") Integer pageSize,
                                                    @Param("creatorId") Integer creatorId) {
        CommonResult<?> tagsByCreatorIdPage = tagService.getTagsByCreatorIdPage(currentPage, pageSize, Long.valueOf(creatorId));
        if (tagsByCreatorIdPage.isSuccess()) return ResponseEntity.ok(tagsByCreatorIdPage);
        else return ResponseEntity.internalServerError().body(tagsByCreatorIdPage);
    }

    @Operation(summary = "根据创建者id获取标签数量")
    @RequestMapping(value = "countByCreatorId", method = RequestMethod.GET)
    public ResponseEntity<?> countByCreatorId(@Param("creatorId") Long creatorId) {
        CommonResult<?> countByCreatorId = tagService.countByCreatorId(creatorId);
        if (countByCreatorId.isSuccess()) return ResponseEntity.ok(countByCreatorId);
        else return ResponseEntity.internalServerError().body(countByCreatorId);
    }
}
