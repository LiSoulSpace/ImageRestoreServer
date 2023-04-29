package xyz.soulspace.restore.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.MD5;
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
import xyz.soulspace.restore.dto.UserBasicDTO;
import xyz.soulspace.restore.dto.UserRequestBody;
import xyz.soulspace.restore.dto.UserUpdateDTO;
import xyz.soulspace.restore.service.ImageInfoService;
import xyz.soulspace.restore.service.UserService;
import xyz.soulspace.restore.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 03:46:51
 */
@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/user")
@Tag(name = "用户控制器(UserController)")
public class UserController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${Cros.set-domain}")
    private String domain;
    @Autowired
    UserService userService;

    @Autowired
    ImageInfoService imageInfoService;

    @Operation(summary = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody UserRequestBody user,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        Map<String, String> map = userService.login(user.getUsername(), user.getPassword());
        if (map.get("token") == null) {
            String msg = map.get("msg");
            return ResponseEntity.ok(CommonResult.failed(1, msg, null));
        }
        CookieUtil.setCookie(request, response, "token", map.get("token"), 24 * 60 * 60);
        CookieUtil.setCookie(request, response, "tokenHead", tokenHead);
        response.setHeader("token", map.get("token"));
        response.setHeader("tokenHead", tokenHead);
        response.setHeader("tokenHeader", tokenHeader);
        return ResponseEntity.ok(CommonResult.success(map));
    }

    @Operation(summary = "退出登录")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, "token", "", 24 * 60 * 60);
        CookieUtil.setCookie(request, response, "tokenHead", "");
        return ResponseEntity.ok(CommonResult.success(null));
    }

    @Operation(summary = "whoami")
    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    public ResponseEntity<?> whoAmI(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        UserBasicDTO userBasicDTO = userService.whoAmI(token);
        return ResponseEntity.ok(CommonResult.success(userBasicDTO));
    }

    @Operation(summary = "修改个人信息")
    @RequestMapping(value = "/update", method = RequestMethod.GET,
            consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public ResponseEntity<?> updateInfo(UserUpdateDTO userUpdateDTO,
                                        @RequestParam MultipartFile avatar) {
        try {
            String userPath = System.getProperty("user.dir");
            log.warn(userUpdateDTO.toString());
            String contentType = avatar.getContentType();
            assert contentType != null;
            String[] split = contentType.split("/");
            String encode = Base64.encode(String.valueOf(userUpdateDTO.getUserId()));
            String avatarFileRelativePath = "img/avatar/" + encode + ',' + split[1];
            String avatarFileAbsolutePath = userPath + '/' + avatarFileRelativePath;
            File file = new File(avatarFileAbsolutePath);
            avatar.transferTo(file);
            log.warn("图片文件保存成功{}", file.getAbsolutePath());
            String fileMD5 = MD5.create().digestHex16(file);
            boolean b = userService.updateUserInfo(userUpdateDTO.getNickName(),
                    fileMD5, userUpdateDTO.getUserId(), split[1], avatarFileRelativePath);
            if (b) return ResponseEntity.ok(CommonResult.success("修改完成"));
            else return ResponseEntity.ok(CommonResult.failed(1, "修改失败", null));
        } catch (IOException e) {
            log.error(e.toString());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "获取个人信息")
    @RequestMapping(value = "/sig", method = RequestMethod.GET)
    public ResponseEntity<?> onesInfo(@RequestParam Long userId) {
        return ResponseEntity.ok(userId);
    }

    @Operation(summary = "获取用户角色信息")
    @RequestMapping(value = "/getRoleInfoByUserId", method = RequestMethod.GET)
    public ResponseEntity<?> getRoleInfoByUserId(@Param("userId") Long userId) {
        CommonResult<?> commonResult = userService.selectRoleByUserId(userId);
        if (commonResult.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(commonResult));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(commonResult));
    }

    @Operation(summary = "判断是否为管理员")
    @RequestMapping(value = "/checkIsAdminByUserId", method = RequestMethod.GET)
    public ResponseEntity<?> checkIsAdminByUserId(@Param("userId") Long userId) {
        CommonResult<?> commonResult = userService.checkIsAdminByUserId(userId);
        if (commonResult.isSuccess())
            return ResponseEntity.ok(JSON.toJSONString(commonResult));
        else return ResponseEntity.internalServerError().body(JSON.toJSONString(commonResult));
    }
}
