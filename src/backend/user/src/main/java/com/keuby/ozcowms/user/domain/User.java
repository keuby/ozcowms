package com.keuby.ozcowms.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keuby.ozcowms.common.dto.ContactDTO;
import com.keuby.ozcowms.common.dto.UserDTO;
import com.keuby.ozcowms.common.enums.UserStatus;
import com.keuby.ozcowms.common.model.BaseModel;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "t_users")
public class User extends BaseModel {

    private String name;
    private String nickname;
    private String openId;
    private String avatar;
    @JsonIgnore
    private String sessionKey;
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.NO_LOGIN;
    @OneToMany(mappedBy = "user")
    private Set<Contact> contacts;

    public Set<ContactDTO> getContacts() {
        if (this.contacts == null || this.contacts.size() <= 0) {
            return new HashSet<>();
        }
        return this.contacts.stream().map(Contact::toDTO).collect(Collectors.toSet());
    }
}
