package com.keuby.ozcowms.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keuby.ozcowms.common.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 商品计量单位
 */
@Getter
@Setter
@Entity
@Table(name = "t_units")
public class Unit extends BaseModel {

    /**
     * 单位名称
     */
    private String name;

    /**
     * 单位与父级单位的倍数
     */
    private Integer multiple;

    /**
     * 单位标签， 只存在于顶级的父级单位上，子级单位为null
     */
    private String tag;

    /**
     * 父级单位
     */
    @JsonIgnore
    @OneToOne
    @JoinColumn(name="parent_unit_id")
    private Unit parent;

    /**
     * 子级单位
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "parent")
    private Unit child;
}
