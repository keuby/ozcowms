package com.keuby.ozcowms.product.service;

import com.keuby.ozcowms.product.model.AmountOperation;

public interface StorageService {

    /**
     * 添加商品总量
     * @param userId 用户ID
     * @param productAmount 商品和数量的描述
     */
    void addTotal(Long userId, AmountOperation productAmount);

    /**
     * 添加本地仓库暂存数量
     * @param userId 用户ID
     × @param amountUpdate 商品和数量的描述
     *
     */
    void addStaged(Long userId, AmountOperation productAmount);

    /**
     * 卖出商品
     * @param userId 用户ID
     × @param amountUpdate 商品和数量的描述
     *
     */
    void sale(Long userId, AmountOperation productAmount);
}
