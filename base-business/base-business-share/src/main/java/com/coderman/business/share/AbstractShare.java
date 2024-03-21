package com.coderman.business.share;

import com.coderman.business.request.ShareParamDetailVO;
import com.coderman.business.request.ShareParamVO;
import com.coderman.business.response.ShareResultDetailVO;
import com.coderman.business.response.ShareResultVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author coderman
 */
public abstract class AbstractShare<I, O extends ShareResultDetailVO> {


    /**
     * 准备分摊数据
     *
     * @param i
     * @return
     */
    protected abstract List<ShareParamVO> prepareShare(I i);


    /**
     * 本次是否要执行分摊算法
     *
     * @param i
     * @return
     */
    protected boolean needShare(I i) {
        return true;
    }


    /**
     * 分摊
     *
     * @param i 自定义入参
     * @return 分摊结果
     */
    public ShareResultVO<O> share(I i) {

        ShareResultVO<O> shareResultVO = new ShareResultVO<>();
        List<O> shareResultDetailVOList = new ArrayList<>();
        shareResultVO.setShareResultDetailVOList(shareResultDetailVOList);

        // 本次不需要执行分摊逻辑 直接转到下一次
        if (!needShare(i)) {

            return this.afterShare(i, shareResultVO);
        }

        // 取得待分摊参数
        List<ShareParamVO> shareParamVOList = this.prepareShare(i);

        if (CollectionUtils.isEmpty(shareParamVOList)) {

            return this.afterShare(i, shareResultVO);
        }


        for (ShareParamVO shareParam : shareParamVOList) {


            // 待分摊总数
            BigDecimal waitShare = shareParam.getWaitShare();

            List<ShareParamDetailVO> shareMoneyDetailVOS = shareParam.getShareParamDetailList();

            if (CollectionUtils.isEmpty(shareMoneyDetailVOS)) {
                continue;
            }

            // 分摊项分子正序
            Collections.sort(shareMoneyDetailVOS);

            // 待分摊分母
            BigDecimal waitShareDenominator = shareParam.getWaitShareDenominator();


            // 剩余分摊数
            BigDecimal restShare = waitShare;


            int shareCount = 0;

            // key 明细id v分摊结果
            Map<Integer, ShareResultDetailVO> shareResultDetailMap = new HashMap<>();


            for (ShareParamDetailVO shareParamDetailVO : shareMoneyDetailVOS) {


                shareCount++;


                O shareResultDetailVO = createResultDetailVO();

                // 映射入参合出参的属性
                BeanUtils.copyProperties(shareParamDetailVO, shareResultDetailVO);

                shareResultDetailMap.put(shareParamDetailVO.getShareDetailId(), shareResultDetailVO);

                // 分子为0 直接返回0
                if (shareParamDetailVO.getShareNumerator().compareTo(BigDecimal.ZERO) <= 0) {

                    shareResultDetailVO.setShareValue(BigDecimal.ZERO);
                    continue;
                }

                // 待分摊分子 / 分母 明细占比
                BigDecimal scale = shareParamDetailVO.getShareNumerator().divide(waitShareDenominator, 2, BigDecimal.ROUND_HALF_UP);

                // 根据占比 算出应该要分摊多少待分摊项
                BigDecimal shareValue = scale.multiply(waitShare).setScale(2, BigDecimal.ROUND_HALF_UP);

                // 如果待分摊项都需要摊在明细上 这里是需要倒减的 如果只是拆出去一部分 不需要倒减
                if (shareParam.getIsShareAll()) {

                    if (shareCount == shareMoneyDetailVOS.size()) {

                        shareValue = restShare;
                    }

                }

                // 分摊金额 是否大于了最大允许分摊金额
                if (shareValue.compareTo(shareParamDetailVO.getShareLimit()) > 0) {

                    shareValue = shareParamDetailVO.getShareLimit();
                }

                // 分摊金额 大于剩余可分摊金额
                if (shareValue.compareTo(restShare) > 0) {

                    shareValue = restShare;
                }

                restShare = restShare.subtract(shareValue);

                shareResultDetailVO.setShareValue(shareValue);
                shareResultDetailVOList.add(shareResultDetailVO);

            }


            // 由于做了shareValue 不能大于shareLimit 的限制 所以可能会出现 waiteShareMoney 非分摊完的情况,这里在做一次补偿
            if (shareParam.getIsShareAll() && restShare.compareTo(BigDecimal.ZERO) > 0) {


                // 上面已经正序排列了 这里倒叙循环
                for (int j = shareMoneyDetailVOS.size() - 1; j >= 0; j--) {

                    ShareParamDetailVO shareParamDetailVO = shareMoneyDetailVOS.get(j);
                    // 明细分摊到的金额
                    ShareResultDetailVO shareDetailResult = shareResultDetailMap.get(shareParamDetailVO.getShareDetailId());

                    //如果分摊金额 已经达到最大允许分摊金额
                    if (shareParamDetailVO.getShareLimit().compareTo(shareDetailResult.getShareValue()) == 0) {


                        continue;
                    }

                    // 分摊到的金额 + 剩余金额
                    BigDecimal compareShare = shareDetailResult.getShareValue().add(restShare);
                    if (shareParamDetailVO.getShareLimit().compareTo(compareShare) >= 0) {

                        // 最大允许金额 依然满足
                        shareDetailResult.setShareValue(compareShare);
                        break;
                    } else {


                        BigDecimal tempShare = shareParamDetailVO.getShareLimit().subtract(shareDetailResult.getShareValue());
                        restShare = restShare.subtract(tempShare);
                        shareDetailResult.setShareValue(shareParamDetailVO.getShareLimit());
                    }

                }


            }


        }


        return this.afterShare(i, shareResultVO);
    }


    /**
     * 实例化一个返回对象 如果有继承ShareResultDetailVO 返回子类
     *
     * @return
     */
    protected abstract O createResultDetailVO();

    /**
     * 分摊后处理
     *
     * @param i             初始入参
     * @param shareResultVO 分摊结果
     * @return 分摊结果
     */
    protected ShareResultVO<O> afterShare(I i, ShareResultVO<O> shareResultVO) {

        return shareResultVO;
    }


    /**
     * 调整拆分折扣金额
     * @param i
     */
    public abstract void raise(I i);
}













