package com.keuby.ozcowms.product.service.impl;

import com.keuby.ozcowms.product.domain.*;
import com.keuby.ozcowms.product.model.AmountOperation.Amount;
import com.keuby.ozcowms.product.model.OperationType;
import com.keuby.ozcowms.product.model.TransactionCreation;
import com.keuby.ozcowms.product.respository.TransactionRepository;
import com.keuby.ozcowms.product.service.TransactionService;
import com.keuby.ozcowms.product.service.UnitService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final UnitService unitService;

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            UnitService unitService) {
        this.transactionRepository = transactionRepository;
        this.unitService = unitService;
    }

    @Override
    public void create(TransactionCreation tc) {
        Transaction transaction = new Transaction();
        transaction.setAmount(tc.getAmount());
        transaction.setDescription(getDescription(tc.getAmountDetail()));
        transaction.setRemark(tc.getRemark());
        transaction.setContactId(tc.getOperatorId());
        transaction.setUserStorage(tc.getUserStorage());
        transaction.setOperationType(tc.getType());
        transaction.setParent(tc.getParent());
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> find(List<Long> storageIds, List<OperationType> types, int page, int size) {
        if (page <= 0 || size <= 0) {
            return new ArrayList<>();
        }
        List<String> stringTypes = types.stream().map(Enum::toString).collect(Collectors.toList());
        if (storageIds == null || storageIds.size() <= 0) {
            return new ArrayList<>();
        } else if (storageIds.size() == 1) {
            return transactionRepository.findTransactions(storageIds.get(0), stringTypes, (page - 1) * size, size);
        } else {
            return transactionRepository.findTransactions(storageIds, stringTypes, (page - 1) * size, size);
        }
    }

    private String getDescription (List<Amount> amounts) {
        Unit unit = null;
        for (Amount amount : amounts) {
            unit = unitService.getById(amount.getUnitId());
            if (unit != null) break;
        }
        if (unit == null) return "";
        while (unit.getChild() != null) unit = unit.getChild();
        ArrayList<String> description = new ArrayList<>();
        do {
            for (Amount amount : amounts) {
                if (unit.getId().equals(amount.getUnitId())) {
                    if (amount.getCount() == 0) break;
                    description.add(amount.getCount().toString());
                    description.add(unit.getName());
                    break;
                }
            }
            unit = unit.getParent();
        } while (unit != null);
        return String.join(" ", description);
    }
}
