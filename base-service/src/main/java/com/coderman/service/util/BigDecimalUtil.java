package com.coderman.service.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class BigDecimalUtil {

    /**
     * 单价
     */
    public static final Integer UNIT_PRICE = 6;

    /**
     * 总价
     */
    public static final Integer TOTAL_PRICE = 2;


    /**
     * 是否大于等于0
     *
     * @param bigDecimal
     * @return
     */
    public static boolean isBigDecimalGTEZero(BigDecimal bigDecimal) {
        return null != bigDecimal && BigDecimal.ZERO.compareTo(bigDecimal) < 1;
    }


    /**
     * 是否大于0
     *
     * @param bigDecimal
     * @return
     */
    public static boolean isBigDecimalGTZero(BigDecimal bigDecimal) {
        return null != bigDecimal && BigDecimal.ZERO.compareTo(bigDecimal) < 0;
    }


    /**
     * String 类型转换为BigDecimal
     *
     * @param str
     * @param scale
     * @param round
     * @return
     */
    public static BigDecimal toBigDecimal(String str, int scale, int round) {

        if (StringUtils.isBlank(str)) {

            throw new RuntimeException("参数为空");
        } else {

            return new BigDecimal(str).setScale(scale, round);
        }
    }

    /**
     * String 类型转换为BigDecimal
     *
     * @param str
     * @return
     */
    public static BigDecimal toBigDecimalPrice(String str) {

        if (StringUtils.isBlank(str)) {

            throw new RuntimeException("参数为空");
        } else {
            return new BigDecimal(str).setScale(UNIT_PRICE, BigDecimal.ROUND_UP);
        }
    }


    /**
     * String 类型转换为BigDecimal
     *
     * @param str
     * @return
     */
    public static BigDecimal toBigDecimalTotal(String str) {

        if (StringUtils.isBlank(str)) {

            throw new RuntimeException("参数为空");
        } else {
            return new BigDecimal(str).setScale(TOTAL_PRICE, BigDecimal.ROUND_UP);
        }
    }


    /**
     * 加法
     *
     * @param decimalA
     * @return
     */
    public static BigDecimal addPrice(BigDecimal decimalA, BigDecimal decimalB) {

        check(decimalA, decimalB);
        return decimalA.add(decimalB).setScale(UNIT_PRICE, BigDecimal.ROUND_UP);
    }


    /**
     * 加法
     *
     * @param decimalA
     * @return
     */
    public static BigDecimal addTotal(BigDecimal decimalA, BigDecimal decimalB) {

        check(decimalA, decimalB);
        return decimalA.add(decimalB).setScale(TOTAL_PRICE, BigDecimal.ROUND_UP);
    }


    /**
     * 加法
     *
     * @param decimalA
     * @return
     */
    public static BigDecimal add(BigDecimal decimalA, BigDecimal decimalB, int scale, int round) {

        check(decimalA, decimalB);
        return decimalA.add(decimalB).setScale(scale, round);
    }


    /**
     * 乘法
     *
     * @param decimalA
     * @return
     */
    public static BigDecimal multiply(BigDecimal decimalA, BigDecimal decimalB, int scale, int round) {

        check(decimalA, decimalB);
        return decimalA.multiply(decimalB).setScale(scale, round);
    }


    /**
     * 乘法
     *
     * @param decimalA
     * @return
     */
    public static BigDecimal multiplyPrice(BigDecimal decimalA, BigDecimal decimalB) {

        check(decimalA, decimalB);
        return decimalA.multiply(decimalB).setScale(UNIT_PRICE, BigDecimal.ROUND_UP);
    }


    /**
     * 乘法
     *
     * @param decimalA
     * @return
     */
    public static BigDecimal multiplyTotal(BigDecimal decimalA, BigDecimal decimalB) {

        check(decimalA, decimalB);
        return decimalA.multiply(decimalB).setScale(TOTAL_PRICE, BigDecimal.ROUND_UP);
    }


    /**
     * 减法
     *
     * @param decimalA
     * @param decimalB
     * @param scale
     * @param round
     * @param isGTEZero
     * @return
     */
    public static BigDecimal subtract(BigDecimal decimalA, BigDecimal decimalB, int scale, int round, boolean isGTEZero) {

        check(decimalA, decimalB);

        BigDecimal bigDecimal = decimalA.subtract(decimalB).setScale(scale, round);

        if (!isBigDecimalGTEZero(decimalA) && isGTEZero) {
            throw new RuntimeException("结果为负数");
        }

        return bigDecimal;
    }


    /**
     * 减法
     *
     * @param decimalA
     * @param decimalB
     * @return
     */
    public static BigDecimal subtractPrice(BigDecimal decimalA, BigDecimal decimalB) {

        check(decimalA, decimalB);

        BigDecimal bigDecimal = decimalA.subtract(decimalB).setScale(UNIT_PRICE, BigDecimal.ROUND_UP);


        if (!isBigDecimalGTEZero(bigDecimal)) {
            throw new RuntimeException("结果为负数");
        }

        return bigDecimal;
    }


    /**
     * 减法
     *
     * @param decimalA
     * @param decimalB
     * @return
     */
    public static BigDecimal subtractTotal(BigDecimal decimalA, BigDecimal decimalB) {

        check(decimalA, decimalB);

        BigDecimal bigDecimal = decimalA.subtract(decimalB).setScale(TOTAL_PRICE, BigDecimal.ROUND_UP);


        if (!isBigDecimalGTEZero(bigDecimal)) {
            throw new RuntimeException("结果为负数");
        }

        return bigDecimal;
    }


    /**
     * 除法
     *
     * @param decimalA
     * @param decimalB
     * @param scale
     * @param round
     * @return
     */
    public static BigDecimal divide(BigDecimal decimalA, BigDecimal decimalB, int scale, int round) {
        check(decimalA, decimalB);

        if (BigDecimal.ZERO.compareTo(decimalB) == 0) {
            throw new RuntimeException("除数不能为0");
        }

        return decimalA.divide(decimalB, scale, round);
    }


    /**
     * 除法
     *
     * @param decimalA
     * @param decimalB
     * @return
     */
    public static BigDecimal dividePrice(BigDecimal decimalA, BigDecimal decimalB) {
        check(decimalA, decimalB);

        if (BigDecimal.ZERO.compareTo(decimalB) == 0) {
            throw new RuntimeException("除数不能为0");
        }

        return decimalA.divide(decimalB, UNIT_PRICE, BigDecimal.ROUND_UP);
    }


    /**
     * 除法
     *
     * @param decimalA
     * @param decimalB
     * @return
     */
    public static BigDecimal divideTotal(BigDecimal decimalA, BigDecimal decimalB) {
        check(decimalA, decimalB);

        if (BigDecimal.ZERO.compareTo(decimalB) == 0) {
            throw new RuntimeException("除数不能为0");
        }

        return decimalA.divide(decimalB, TOTAL_PRICE, BigDecimal.ROUND_UP);
    }


    public static void check(BigDecimal decimalA, BigDecimal decimalB) {

        if (null == decimalA || null == decimalB) {
            throw new RuntimeException("参数为空");
        }
    }
}
