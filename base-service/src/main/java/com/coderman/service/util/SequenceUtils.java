package com.coderman.service.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class SequenceUtils {

    private static final Object lock = new Object();

    private static SequenceUtils instance;

    /**
     * The FieldPosition.
     */
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

    /**
     * This Format for format the number to special format.
     */
    private final static NumberFormat numberFormat = new DecimalFormat("00000000");

    /**
     * This int is the sequence number ,the default value is 1.
     */
    private static final AtomicInteger seq = new AtomicInteger(1);

    private static final int MAX = 99999999;

    private SequenceUtils() {
    }

    public static SequenceUtils getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SequenceUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 生成业务流水号(唯一标示一笔业务)
     *
     * @param bizSeqNoPrefix 业务前缀(2字符,参见Constant.**_BIZ_SEQUENCE_NO_PREFIX)
     * @return
     */
    public String generateBizSeqNo(String bizSeqNoPrefix) {
        return bizSeqNoPrefix + DateFormatUtils.format(new Date(), "yyMMddHHmmss") +
                getSeq();
    }

    private String getSeq() {
        StringBuffer sb = new StringBuffer();
        numberFormat.format(seq, sb, HELPER_POSITION);
        if (!seq.compareAndSet(MAX, 0)) {
            seq.incrementAndGet();
        }
        return sb.toString();
    }


}
