package com.keuby.ozcowms.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keuby.ozcowms.common.model.BaseModel;
import com.keuby.ozcowms.product.model.OperationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "t_transactions")
public class Transaction extends BaseModel {

    /**
     * 操作数量
     */
    private Long amount;

    /**
     * 操作数量的描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    
    /**
     * 操作相关联系人
     */
    private Long contactId;

    /**
     * 所操作的用户仓库数据
     */
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_storage_id")
    private UserStorage userStorage;

    /**
     * 操作父级， 一次操作可能多条transaction
     */
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Transaction parent;

    /**
     * 操作子集， 和父级相对
     */
    @OneToMany(mappedBy = "parent")
    private Set<Transaction> children;

    public String getProductName() {
        if (userStorage == null || userStorage.getStorage() == null || userStorage.getStorage().getProduct() == null) {
            return null;
        }
        return userStorage.getStorage().getProduct().getName();
    }
}
