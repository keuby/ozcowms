package com.keuby.ozcowms.product.service;

import com.keuby.ozcowms.product.domain.Transaction;
import com.keuby.ozcowms.product.model.OperationType;
import com.keuby.ozcowms.product.model.TransactionCreation;

import java.util.List;

public interface TransactionService {
    void create(TransactionCreation tc);

    List<Transaction> find(List<Long> storageIds, List<OperationType> types, int page, int size);
}
