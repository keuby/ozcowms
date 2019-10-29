package com.keuby.ozcowms.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keuby.ozcowms.common.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "t_storage")
public class Storage extends BaseModel {

    /**
     * 商品总量，包括已存在仓库中，以及下单但未到的商品
     */
    private Long total = 0L;

    /**
     * 已存在仓库中商品数量
     */
    private Long staged = 0L;

    /**
     * 已卖出的商品数量
     */
    private Long sales = 0L;

    /**
     * 商品
     */
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    /**
     * 描述的单位
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="unit_id")
    private Unit unit;

    public String getBaseUnit() {
        return unit.getName();
    }


    public Long getRemain() {
        return staged - sales;
    }

    public Long getUnstaged() {
        return total - staged;
    }

    public Map getTotalAmount() {
        Map<String, Long> amount = new HashMap<>();
        normalize(unit, total, amount);
        return amount;
    }

    public Map getStagedAmount() {
        Map<String, Long> amount = new HashMap<>();
        normalize(unit, staged, amount);
        return amount;
    }

    public Map getSalesAmount() {
        Map<String, Long> amount = new HashMap<>();
        normalize(unit, sales, amount);
        return amount;
    }

    public Map getRemainAmount() {
        Map<String, Long> amount = new HashMap<>();
        normalize(unit, getRemain(), amount);
        return amount;
    }

    public Map getUnstagedAmount() {
        Map<String, Long> amount = new HashMap<>();
        normalize(unit, getUnstaged(), amount);
        return amount;
    }

    private long normalize(Unit unit, long amount, Map<String, Long> normalizeAmount) {
        if (unit == null) {
            return amount;
        }
        Unit child = unit.getChild();
        if (child != null) {
            amount = normalize(child, amount, normalizeAmount);
        }

        if (amount == 0) return 0;

        long multiple = unit.getMultiple();
        long unitAmount = amount / multiple;
        long remain = amount % multiple;
        normalizeAmount.put(unit.getName(), unitAmount);
        return remain;
    }
}
