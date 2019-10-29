package com.keuby.ozcow.miniapp.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.keuby.ozcow.miniapp.service.GatewayService;
import com.keuby.ozcow.miniapp.service.MiniappService;
import com.keuby.ozcow.miniapp.service.UserService;
import com.keuby.ozcowms.common.dto.UserDTO;
import com.keuby.ozcowms.common.enums.UserStatus;
import com.keuby.ozcowms.common.response.JsonResp;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.HashMap;

@RestController
@RequestMapping("/user/{appId}")
public class WxMaUserController {

    private static final String accessToken = "Bearer NDE2OTYyNjk1NzQzNDJlYjk2ZmY3YzNkOGU3ODdhNWQ=";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MiniappService miniappService;
    private final UserService userService;
    private final GatewayService gatewayService;

    public WxMaUserController(
            MiniappService miniappService,
            UserService userService,
            GatewayService gatewayService) {
        this.userService = userService;
        this.miniappService = miniappService;
        this.gatewayService = gatewayService;
    }

    @GetMapping("/login")
    public JsonResp login(@PathVariable String appId, String code) {
        if (StringUtils.isEmpty(code)) {
            return JsonResp.paramsMissing("code is empty");
        }

        final WxMaService wxService = miniappService.getMaService(appId);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            UserDTO user = new UserDTO();
            user.setOpenId(session.getOpenid());
            user.setSessionKey(session.getSessionKey());
            JsonResp<UserDTO> userResp = userService.create(user);
            if (userResp.isOk()) {
                JsonResp<String> authResp = gatewayService.auth(accessToken, userResp.getData().getId());
                if (authResp.isOk()) {
                    logger.info(userResp.getData().getOpenId() + " login");
                    return JsonResp.ok(authResp.getData());
                }
                return JsonResp.error("授权失败");
            }
            return JsonResp.error("登录失败, 用户中心异常");
        } catch (WxErrorException e) {
            return JsonResp.error(e.getError().getErrorMsg());
        }
    }

    @GetMapping("/info")
    public JsonResp info(
            @PathVariable(value = "appId") @NotBlank String appId,
            @RequestHeader(value = "x-user-id") @Positive Long userId,
            @RequestParam(value = "signature") @NotBlank String signature,
            @RequestParam(value = "rawData") @NotBlank String rawData,
            @RequestParam(value = "encryptedData") @NotBlank String encryptedData,
            @RequestParam(value = "iv") @NotBlank String iv) {
        final WxMaService wxService = miniappService.getMaService(appId);
        JsonResp<String> sessionKeyResp = userService.getSessionKey(userId);
        if (!sessionKeyResp.isOk()) {
            return JsonResp.error("user does not login");
        }

        // 用户信息校验
        String sessionKey = sessionKeyResp.getData();
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return JsonResp.error("user info data is incorrect");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        UserDTO user = new UserDTO();
        user.setId(userId);
        user.setAvatar(userInfo.getAvatarUrl());
        user.setNickname(userInfo.getNickName());
        user.setStatus(UserStatus.USER_INFO);
        JsonResp resp = userService.update(user);
        return  resp.isOk() ? JsonResp.ok(resp.getData()) : JsonResp.error("update user info failed");
    }
}
