package com.brook.asm.utils;

import org.apache.commons.lang3.StringUtils;

public class DesensitizeUtil {
    /**
     * 只显示第一个汉字，
     *
     * <pre>
     *   chineseName("李四");// 李*
     * </pre>
     * @param fullName 中文全名
     * @return a desensitized fullname.
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * @param familyName 名字
     * @param mask 掩码
     */
    public static String chineseName(String familyName, String mask) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(mask)) {
            return "";
        }
        return chineseName(familyName + mask);
    }

    /**
     *  110****58，前面保留3位明文，后面保留2位明文
     *
     * @param idcard idcard
     */
    public static String idcard(String idcard) {
        if (StringUtils.isBlank(idcard)) {
            return "";
        }
        return StringUtils.left(idcard, 3).concat(
            StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idcard, 2), StringUtils
                .length(idcard), "*"), "***"));
    }

    /**
     * [手机号码] 前四位，后四位，其他隐藏<例子:138****1234>
     * @param mobile
     *          a mobile .
     * @return a new mobile for masked.
     */
    public static String mobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return "";
        }
        return StringUtils.left(mobile, 3).concat(StringUtils
            .removeStart(StringUtils.leftPad(StringUtils.right(mobile, 4), StringUtils.length(mobile),
                "*"), "***"));
    }

}