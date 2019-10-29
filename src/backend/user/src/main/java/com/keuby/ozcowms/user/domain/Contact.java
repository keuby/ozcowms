package com.keuby.ozcowms.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keuby.ozcowms.common.dto.ContactDTO;
import com.keuby.ozcowms.common.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "t_contacts")
public class Contact extends BaseModel {
    @Column(length = 15)
    private String phone;

    @Column(length = 30)
    private String remark;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private User contact;

    @JsonIgnore
    public ContactDTO toDTO() {
        ContactDTO dto = new ContactDTO();
        dto.setId(getId());
        dto.setRemark(remark);
        dto.setPhone(phone);
        dto.setCreatedTime(getCreatedTime());
        dto.setUpdatedTime(getUpdatedTime());
        if (contact != null) {
            dto.setName(contact.getName());
            dto.setAvatar(contact.getAvatar());
        }
        return dto;
    }
}
