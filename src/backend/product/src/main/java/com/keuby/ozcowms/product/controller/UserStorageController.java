package com.keuby.ozcowms.product.controller;

import com.keuby.ozcowms.common.response.JsonResp;
import com.keuby.ozcowms.product.domain.Transaction;
import com.keuby.ozcowms.product.domain.UserStorage;
import com.keuby.ozcowms.product.model.AmountOperation;
import com.keuby.ozcowms.product.model.OperationType;
import com.keuby.ozcowms.product.service.StorageService;
import com.keuby.ozcowms.product.service.TransactionService;
import com.keuby.ozcowms.product.service.UserStorageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-storage")
@Validated
public class UserStorageController {
    private static final String REGX_IDS = "^[\\d]+(,[\\d]+)*$";
    private static final String REGX_OPT_TYPES;

    static {
        List<String> types = Arrays.stream(OperationType.class.getEnumConstants()).map(Objects::toString).collect(Collectors.toList());
        String regxTypes = String.join("|", types);
        REGX_OPT_TYPES = String.format("^(%s)(,(%s))*$", regxTypes, regxTypes);
    }

    private final UserStorageService userStorageService;
    private final TransactionService transactionService;
    private final StorageService storageService;

    public UserStorageController(
            UserStorageService userStorageService,
            StorageService storageService,
            TransactionService transactionService) {
        this.userStorageService = userStorageService;
        this.storageService = storageService;
        this.transactionService = transactionService;
    }

    @GetMapping("/category")
    public JsonResp category(
            @RequestHeader(value = "x-user-id", required = false) @NotNull(message = "字段必须存在") Long  userId,
            @RequestParam(value = "category") @NotBlank(message = "不能为空") String category) {
        List<UserStorage> userStorageList = userStorageService.findByUserIdAndCategory(userId, category);
        ArrayList storageList = (ArrayList) userStorageList.stream().map(UserStorage::getStorage).collect(Collectors.toList());
        return JsonResp.ok(storageList);
    }

    @GetMapping("/transaction")
    public JsonResp transaction(
            @RequestParam(value = "storageIds") @NotBlank(message = "字段必须存在") String storageIds,
            @RequestParam(value = "types", defaultValue = "") String types,
            @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        if (page <= 0 || size <= 0) {
            return JsonResp.ok(new ArrayList<>());
        }
        if (!Pattern.matches(REGX_IDS, storageIds)) {
            return JsonResp.paramsError("storageIds 参数不正确，请将多个id用逗号分割");
        } else if (!Pattern.matches(REGX_OPT_TYPES, types)) {
            return JsonResp.paramsError("types 参数不正确，请将多个type用逗号分割, type必须为大写");
        }
        List<Long> storageIdList = Arrays.stream(storageIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
        List<OperationType> operationTypes = Arrays.stream(types.split(",")).map(OperationType::valueOf).collect(Collectors.toList());
        List<Transaction> transactions = transactionService.find(storageIdList, operationTypes, page, size);
        return JsonResp.ok(transactions);
    }

    @PostMapping("/total")
    public JsonResp addTotal(
            @RequestHeader(value = "x-user-id", required = false) @NotNull(message = "字段必须存在") Long  userId,
            @RequestBody @Valid AmountOperation amountOperation) {
        storageService.addTotal(userId, amountOperation);
        return JsonResp.ok();
    }

    @PostMapping("/staged")
    public JsonResp addStaged(
            @RequestHeader(value = "x-user-id", required = false) @NotNull(message = "字段必须存在") Long  userId,
            @RequestBody @Valid AmountOperation amountOperation) {
        storageService.addStaged(userId, amountOperation);
        return JsonResp.ok();
    }

    @PostMapping("/sales")
    public JsonResp addSales(
            @RequestHeader(value = "x-user-id", required = false) @NotNull(message = "字段必须存在") Long  userId,
            @RequestBody @Valid AmountOperation amountOperation) {
        storageService.sale(userId, amountOperation);
        return JsonResp.ok();
    }
}