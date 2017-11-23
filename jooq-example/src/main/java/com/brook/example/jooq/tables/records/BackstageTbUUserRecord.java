/*
 * This file is generated by jOOQ.
*/
package com.brook.example.jooq.tables.records;


import com.brook.example.jooq.tables.BackstageTbUUser;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BackstageTbUUserRecord extends UpdatableRecordImpl<BackstageTbUUserRecord> implements Record7<Integer, String, String, String, String, Integer, String> {

    private static final long serialVersionUID = 1168627969;

    /**
     * Setter for <code>caishen_backstage.backstage_tb_u_user.u_id</code>.
     */
    public void setUId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>caishen_backstage.backstage_tb_u_user.u_id</code>.
     */
    public Integer getUId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>caishen_backstage.backstage_tb_u_user.u_login_name</code>. 用户名
     */
    public void setULoginName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>caishen_backstage.backstage_tb_u_user.u_login_name</code>. 用户名
     */
    public String getULoginName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>caishen_backstage.backstage_tb_u_user.u_password</code>. 密码
     */
    public void setUPassword(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>caishen_backstage.backstage_tb_u_user.u_password</code>. 密码
     */
    public String getUPassword() {
        return (String) get(2);
    }

    /**
     * Setter for <code>caishen_backstage.backstage_tb_u_user.u_email</code>. 邮箱
     */
    public void setUEmail(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>caishen_backstage.backstage_tb_u_user.u_email</code>. 邮箱
     */
    public String getUEmail() {
        return (String) get(3);
    }

    /**
     * Setter for <code>caishen_backstage.backstage_tb_u_user.u_mobile</code>. 手机号
     */
    public void setUMobile(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>caishen_backstage.backstage_tb_u_user.u_mobile</code>. 手机号
     */
    public String getUMobile() {
        return (String) get(4);
    }

    /**
     * Setter for <code>caishen_backstage.backstage_tb_u_user.u_status</code>. 状态
     */
    public void setUStatus(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>caishen_backstage.backstage_tb_u_user.u_status</code>. 状态
     */
    public Integer getUStatus() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>caishen_backstage.backstage_tb_u_user.u_gen_time</code>. 创建时间
     */
    public void setUGenTime(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>caishen_backstage.backstage_tb_u_user.u_gen_time</code>. 创建时间
     */
    public String getUGenTime() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, String, String, String, String, Integer, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Integer, String, String, String, String, Integer, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return BackstageTbUUser.BACKSTAGE_TB_U_USER.U_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return BackstageTbUUser.BACKSTAGE_TB_U_USER.U_LOGIN_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return BackstageTbUUser.BACKSTAGE_TB_U_USER.U_PASSWORD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return BackstageTbUUser.BACKSTAGE_TB_U_USER.U_EMAIL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return BackstageTbUUser.BACKSTAGE_TB_U_USER.U_MOBILE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return BackstageTbUUser.BACKSTAGE_TB_U_USER.U_STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return BackstageTbUUser.BACKSTAGE_TB_U_USER.U_GEN_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getUId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getULoginName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getUPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getUEmail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getUMobile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component6() {
        return getUStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getUGenTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getUId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getULoginName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getUPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getUEmail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getUMobile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getUStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getUGenTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackstageTbUUserRecord value1(Integer value) {
        setUId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackstageTbUUserRecord value2(String value) {
        setULoginName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackstageTbUUserRecord value3(String value) {
        setUPassword(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackstageTbUUserRecord value4(String value) {
        setUEmail(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackstageTbUUserRecord value5(String value) {
        setUMobile(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackstageTbUUserRecord value6(Integer value) {
        setUStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackstageTbUUserRecord value7(String value) {
        setUGenTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BackstageTbUUserRecord values(Integer value1, String value2, String value3, String value4, String value5, Integer value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BackstageTbUUserRecord
     */
    public BackstageTbUUserRecord() {
        super(BackstageTbUUser.BACKSTAGE_TB_U_USER);
    }

    /**
     * Create a detached, initialised BackstageTbUUserRecord
     */
    public BackstageTbUUserRecord(Integer uId, String uLoginName, String uPassword, String uEmail, String uMobile, Integer uStatus, String uGenTime) {
        super(BackstageTbUUser.BACKSTAGE_TB_U_USER);

        set(0, uId);
        set(1, uLoginName);
        set(2, uPassword);
        set(3, uEmail);
        set(4, uMobile);
        set(5, uStatus);
        set(6, uGenTime);
    }
}
