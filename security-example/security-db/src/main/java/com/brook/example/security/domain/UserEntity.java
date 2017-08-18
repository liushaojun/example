package com.brook.example.security.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Table(name = "user")
@Data
@Entity
public class UserEntity extends BaseEntity<Long>{
    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 权限
     */
    private String roles;

}