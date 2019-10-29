package com.keuby.ozcowms.user.controller;

import com.keuby.ozcowms.common.response.JsonResp;
import com.keuby.ozcowms.user.domain.User;
import com.keuby.ozcowms.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public JsonResp getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return JsonResp.notFound("user does not exist");
        }
        return JsonResp.ok(user);
    }

    @GetMapping
    public JsonResp getByOpenId(@RequestParam(value = "open_id") String openId) {
        User user = userService.getByOpenId(openId);
        if (user == null) {
            return JsonResp.notFound("user does not exist");
        }
        return JsonResp.ok(user);
    }
    
    @PutMapping("/{id}/session_key")
    public JsonResp setSessionKey(@PathVariable Long id, @RequestParam(value = "session_key") String sessionKey) {
        if (StringUtils.isEmpty(sessionKey)) {
            return JsonResp.paramsMissing("session_key is empty");
        }
        userService.setSessionKey(id, sessionKey);
        return JsonResp.ok();
    }

    @GetMapping("/{id}/session_key")
    public JsonResp getSessionKey(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return JsonResp.notFound("user does not exist");
        }
        return JsonResp.ok(user.getSessionKey());
    }

    @PostMapping
    public JsonResp create(@RequestBody User user) {
        User userInDB = userService.checkUserExist(user);
        if (userInDB == null) {
            userInDB = userService.create(user);
            if (userInDB == null) {
                return JsonResp.error("create failed");
            }
            logger.info("user created", userInDB);
            return JsonResp.ok(userInDB);
        } else {
            userService.setSessionKey(userInDB.getId(), user.getSessionKey());
            logger.info("user session key updated", userInDB);
            return JsonResp.ok(userInDB);
        }
    }

    @PutMapping
    public JsonResp update(@RequestBody User user) {
        if (user.getId() == null && StringUtils.isEmpty(user.getOpenId())) {
            return JsonResp.paramsMissing("id or openid is missing");
        }
        User userInDB = userService.update(user);
        return JsonResp.ok(userInDB);
    }
}
