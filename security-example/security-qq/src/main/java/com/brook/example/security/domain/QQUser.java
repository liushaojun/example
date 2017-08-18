package com.brook.example.security.domain;

import lombok.Data;

@Data
public class QQUser {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private String gender;

    /**
     * 省份
     */
    private String province;

    /**
     * 出生年
     */
    private String year;

    /**
     * 头像
     */
    private String avatar;

}
