/*
 * This file is generated by jOOQ.
*/
package com.brook.example.jooq.tables.records;


import com.brook.example.jooq.tables.MockUser;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
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
public class MockUserRecord extends UpdatableRecordImpl<MockUserRecord> implements Record10<Long, Long, Long, String, String, String, String, Timestamp, Timestamp, String> {

    private static final long serialVersionUID = -471784901;

    /**
     * Setter for <code>caishen_backstage.mock_user.id</code>. id
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.id</code>. id
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.user_id</code>. userid
     */
    public void setUserId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.user_id</code>. userid
     */
    public Long getUserId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.user_code</code>. user_code
     */
    public void setUserCode(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.user_code</code>. user_code
     */
    public Long getUserCode() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.mobile_num</code>. 手机号
     */
    public void setMobileNum(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.mobile_num</code>. 手机号
     */
    public String getMobileNum() {
        return (String) get(3);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.real_name</code>. 真实姓名
     */
    public void setRealName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.real_name</code>. 真实姓名
     */
    public String getRealName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.id_num</code>. 身份证号
     */
    public void setIdNum(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.id_num</code>. 身份证号
     */
    public String getIdNum() {
        return (String) get(5);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.bank_no</code>. 银行卡
     */
    public void setBankNo(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.bank_no</code>. 银行卡
     */
    public String getBankNo() {
        return (String) get(6);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.update_time</code>. 创建时间
     */
    public void setUpdateTime(Timestamp value) {
        set(7, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.update_time</code>. 创建时间
     */
    public Timestamp getUpdateTime() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.create_time</code>. 创建时间
     */
    public void setCreateTime(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.create_time</code>. 创建时间
     */
    public Timestamp getCreateTime() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>caishen_backstage.mock_user.is_delete</code>. 是否删除
     */
    public void setIsDelete(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>caishen_backstage.mock_user.is_delete</code>. 是否删除
     */
    public String getIsDelete() {
        return (String) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, Long, String, String, String, String, Timestamp, Timestamp, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, Long, String, String, String, String, Timestamp, Timestamp, String> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return MockUser.MOCK_USER.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return MockUser.MOCK_USER.USER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return MockUser.MOCK_USER.USER_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return MockUser.MOCK_USER.MOBILE_NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return MockUser.MOCK_USER.REAL_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return MockUser.MOCK_USER.ID_NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return MockUser.MOCK_USER.BANK_NO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return MockUser.MOCK_USER.UPDATE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return MockUser.MOCK_USER.CREATE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return MockUser.MOCK_USER.IS_DELETE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component2() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component3() {
        return getUserCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getMobileNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getRealName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getIdNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getBankNo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component8() {
        return getUpdateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component9() {
        return getCreateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getIsDelete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getUserCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getMobileNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getRealName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getIdNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getBankNo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getUpdateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value9() {
        return getCreateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getIsDelete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value2(Long value) {
        setUserId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value3(Long value) {
        setUserCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value4(String value) {
        setMobileNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value5(String value) {
        setRealName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value6(String value) {
        setIdNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value7(String value) {
        setBankNo(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value8(Timestamp value) {
        setUpdateTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value9(Timestamp value) {
        setCreateTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord value10(String value) {
        setIsDelete(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MockUserRecord values(Long value1, Long value2, Long value3, String value4, String value5, String value6, String value7, Timestamp value8, Timestamp value9, String value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MockUserRecord
     */
    public MockUserRecord() {
        super(MockUser.MOCK_USER);
    }

    /**
     * Create a detached, initialised MockUserRecord
     */
    public MockUserRecord(Long id, Long userId, Long userCode, String mobileNum, String realName, String idNum, String bankNo, Timestamp updateTime, Timestamp createTime, String isDelete) {
        super(MockUser.MOCK_USER);

        set(0, id);
        set(1, userId);
        set(2, userCode);
        set(3, mobileNum);
        set(4, realName);
        set(5, idNum);
        set(6, bankNo);
        set(7, updateTime);
        set(8, createTime);
        set(9, isDelete);
    }
}
