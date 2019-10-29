package com.keuby.ozcowms.product.controller;

import com.keuby.ozcowms.common.response.JsonResp;
import com.keuby.ozcowms.product.domain.UserStorage;
import com.keuby.ozcowms.product.service.UserStorageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/milk-powder")
@Validated
public class MilkPowderController {

    private static final String MILK_POWDER_CATEGORY = "milk-powder";
    private static final String MILK_POWDER_UNIT = "袋";

    private final UserStorageService userStorageService;

    public MilkPowderController(UserStorageService userStorageService) {
        this.userStorageService = userStorageService;
    }

    @PostMapping(value = "/init")
    public JsonResp init(@RequestHeader(value = "x-user-id") @Positive long userId) {
        userStorageService.init(userId, MILK_POWDER_CATEGORY, MILK_POWDER_UNIT);
        return JsonResp.ok();
    }

    @GetMapping(value = "/detail")
    public JsonResp detail(@RequestHeader(value = "x-user-id") @Positive long userId) {
        List<UserStorage> userStorageList = userStorageService.findByUserIdAndCategory(userId, MILK_POWDER_CATEGORY);
        if (userStorageList == null || userStorageList.size() <= 0) {
            userStorageList = userStorageService.init(userId, MILK_POWDER_CATEGORY, MILK_POWDER_UNIT);
        }
        ArrayList storageList = (ArrayList) userStorageList.stream().map(UserStorage::getStorage).collect(Collectors.toList());
        return JsonResp.ok(storageList);
    }
}
