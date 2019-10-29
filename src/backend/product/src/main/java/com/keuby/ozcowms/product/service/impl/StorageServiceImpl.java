package com.keuby.ozcowms.product.service.impl;

import com.keuby.ozcowms.common.dto.ContactDTO;
import com.keuby.ozcowms.common.exception.ServiceException;
import com.keuby.ozcowms.common.response.JsonResp;
import com.keuby.ozcowms.product.domain.Storage;
import com.keuby.ozcowms.product.domain.Unit;
import com.keuby.ozcowms.product.domain.UserStorage;
import com.keuby.ozcowms.product.model.AmountOperation;
import com.keuby.ozcowms.product.model.OperationType;
import com.keuby.ozcowms.product.model.TransactionCreation;
import com.keuby.ozcowms.product.respository.StorageRepository;
import com.keuby.ozcowms.product.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    private final TransactionService transactionService;

    private final UnitService unitService;

    private final UserStorageService userStorageService;

    private final ContactService contactService;

    public StorageServiceImpl(
            StorageRepository storageRepository,
            TransactionService transactionService,
            UnitService unitService, UserStorageService userStorageService,
            ContactService contactService) {
        this.storageRepository = storageRepository;
        this.transactionService = transactionService;
        this.unitService = unitService;
        this.userStorageService = userStorageService;
        this.contactService = contactService;
    }

    @Override
    @Transactional
    public void addTotal(Long userId, AmountOperation amountOperation) {
        handle(userId, amountOperation, OperationType.TOTAL_ADD, (storage, amount) -> storage.setTotal(storage.getTotal() + amount));
    }

    @Override
    @Transactional
    public void addStaged(Long userId, AmountOperation amountOperation) {
        handle(userId, amountOperation, OperationType.STAGED_ADD, (storage, amount) -> {
            if (storage.getTotal() < storage.getStaged() + amount) {
                throw new ServiceException("商品总量不足");
            }
            storage.setStaged(storage.getStaged() + amount);
        });
    }

    @Override
    @Transactional
    public void sale(Long userId, AmountOperation amountOperation) {
        handle(userId, amountOperation, OperationType.SALES, (storage, amount) -> {
            if (storage.getStaged() < storage.getSales() + amount) {
                throw new ServiceException("库存不足");
            }
            storage.setSales(storage.getSales() + amount);
        });
    }

    private void handle(Long userId, AmountOperation amountOperation, OperationType type, AmountUpdateExecution execution) {
        UserStorage userStorage = userStorageService.findByUserIdAndProduct(userId, amountOperation.getProductId());
        if (userStorage == null) {
            throw new ServiceException("用户没有操作这件商品的权限");
        }
        Long operatorId = amountOperation.getOperatorId();
        String operatorName = amountOperation.getOperatorName();
        // 若 联系人不存在 并且 有联系人姓名 则创建该联系人
        if (operatorId == null && !StringUtils.isEmpty(operatorName)) {
            JsonResp<ContactDTO> contact = contactService.create(userId, operatorName, null);
            if (contact.isOk()) {
                operatorId = contact.getData().getId();
            }
        }

        String remark = amountOperation.getRemark();
        Storage storage = userStorage.getStorage();

        long total = 0;
        for (AmountOperation.Amount amount : amountOperation.getAmounts()) {
            // 获取单位
            Unit unit = unitService.getById(amount.getUnitId());
            if (unit == null) {
                throw new ServiceException("单位错误, id: " + amount.getUnitId());
            }

            // 计算并更新数据
            int multiple = computeMultiple(storage, unit);
            long count = amount.getCount() * multiple;
            execution.updateAmount(storage, count);
            storageRepository.save(storage);
            total += count;
        }
        // 记录
        TransactionCreation tc = new TransactionCreation();
        tc.setUserStorage(userStorage);
        tc.setAmount(total);
        tc.setAmountDetail(amountOperation.getAmounts());
        tc.setRemark(remark);
        tc.setOperatorId(operatorId);
        tc.setType(type);
        tc.setParent(null);
        transactionService.create(tc);
    }

    private int computeMultiple(Storage storage, Unit unit) {
        Unit dstUnit = storage.getUnit();
        int multiple = unitService.computeMultiple(unit, dstUnit, false);
        if (multiple == 0) {
            throw new ServiceException("该商品不能使用这个单位");
        }
        return multiple;
    }

    private interface AmountUpdateExecution {
        void updateAmount(Storage storage, long amount);
    }
}
