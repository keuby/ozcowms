package com.keuby.ozcowms.product.model;

import com.keuby.ozcowms.product.domain.Transaction;
import com.keuby.ozcowms.product.domain.UserStorage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TransactionCreation {
    private UserStorage userStorage;
    private long amount;
    private List<AmountOperation.Amount> amountDetail;
    private String remark;
    private Long operatorId;
    private OperationType type;
    private Transaction parent;
}
