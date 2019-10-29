package com.keuby.ozcowms.product.domain;

import com.keuby.ozcowms.common.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "t_products")
public class Product extends BaseModel {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品类别
     */
    private String category;
}
