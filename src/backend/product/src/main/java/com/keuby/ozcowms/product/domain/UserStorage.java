package com.keuby.ozcowms.product.domain;

import com.keuby.ozcowms.common.model.PlainModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "t_user_storage")
public class UserStorage extends PlainModel {

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;
}
