package com.keuby.ozcowms.product.service;

import com.keuby.ozcowms.product.domain.Unit;

import java.util.List;

public interface UnitService {
    Unit findTopUnitByTag(String tag);
    Unit create(String name, String tag);
    Unit create(String name, int multiple, Unit parentUnit);
    Unit create(String name, int multiple, Long parentUnitId);
    void updateMultiple(long unitId, int multiple);
    void delete(long unitId);
    Unit getById(long id);

    /**
     * 计算单位 src 与 单位 dst 之间的倍数
     * @param src 被换算的单位
     * @param dst 换算成的单位
     * @param isChild 为 true 时， 小单位换算成大单位 例如 g -> kg = 1000, 反之则大单位换小单位 例如 kg -> g = 1000
     * @return 两个单位之间的倍数， 若返回0则表示 src 不是 dst 的子单位
     */
    int computeMultiple(Unit src, Unit dst, boolean isChild);
}
