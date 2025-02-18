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
 * 均摊算法抽象类，提供通用分摊逻辑
 *
 * @param <I> 自定义输入类型
 * @param <O> 继承自 ShareResultDetailVO 的输出类型
 */
public abstract class AbstractShare<I, O extends ShareResultDetailVO> {

    /**
     * 准备分摊数据
     *
     * @param i 自定义入参
     * @return 待分摊参数列表
     */
    protected abstract List<ShareParamVO> prepareShare(I i);

    /**
     * 是否需要执行分摊算法
     *
     * @param i 自定义入参
     * @return 是否执行
     */
    protected boolean needShare(I i) {
        return true;
    }

    /**
     * 分摊主流程
     *
     * @param i 自定义入参
     * @return 分摊结果
     */
    public ShareResultVO<O> share(I i) {
        ShareResultVO<O> shareResultVO = new ShareResultVO<>();
        List<O> shareResultDetailVOList = new ArrayList<>();
        shareResultVO.setShareResultDetailVOList(shareResultDetailVOList);

        // 如果本次不需要执行分摊逻辑，直接返回
        if (!needShare(i)) {
            return this.afterShare(i, shareResultVO);
        }

        // 获取待分摊参数
        List<ShareParamVO> shareParamVOList = this.prepareShare(i);
        if (CollectionUtils.isEmpty(shareParamVOList)) {
            return this.afterShare(i, shareResultVO);
        }

        for (ShareParamVO shareParam : shareParamVOList) {
            // 获取待分摊金额
            BigDecimal waitShare = shareParam.getWaitShare();
            List<ShareParamDetailVO> shareMoneyDetailVOS = shareParam.getShareParamDetailList();

            if (CollectionUtils.isEmpty(shareMoneyDetailVOS)) {
                continue;
            }

            // 正序排序，使较小的分摊项优先计算
            Collections.sort(shareMoneyDetailVOS);

            // 获取待分摊分母
            BigDecimal waitShareDenominator = shareParam.getWaitShareDenominator();

            // 剩余可分摊金额
            BigDecimal restShare = waitShare;

            int shareCount = 0;
            Map<Integer, ShareResultDetailVO> shareResultDetailMap = new HashMap<>();

            // 计算每个分摊项的金额
            for (ShareParamDetailVO shareParamDetailVO : shareMoneyDetailVOS) {
                shareCount++;
                O shareResultDetailVO = createResultDetailVO();
                BeanUtils.copyProperties(shareParamDetailVO, shareResultDetailVO);
                shareResultDetailMap.put(shareParamDetailVO.getShareDetailId(), shareResultDetailVO);

                // 如果分子为 0，则该项分摊金额为 0
                if (shareParamDetailVO.getShareNumerator().compareTo(BigDecimal.ZERO) <= 0) {
                    shareResultDetailVO.setShareValue(BigDecimal.ZERO);
                    continue;
                }

                // 计算分摊比例 = (分子 / 分母)
                BigDecimal scale = shareParamDetailVO.getShareNumerator()
                        .divide(waitShareDenominator, 2, BigDecimal.ROUND_HALF_UP);

                // 计算当前项应该分摊的金额
                BigDecimal shareValue = scale.multiply(waitShare).setScale(2, BigDecimal.ROUND_HALF_UP);

                // 如果是完全摊分 (`isShareAll`)，最后一个元素要调整到剩余金额
                if (shareParam.getIsShareAll() && shareCount == shareMoneyDetailVOS.size()) {
                    shareValue = restShare;
                }

                // 不能超过该项允许的最大分摊金额 `shareLimit`
                if (shareValue.compareTo(shareParamDetailVO.getShareLimit()) > 0) {
                    shareValue = shareParamDetailVO.getShareLimit();
                }

                // 不能超过剩余可分摊金额
                if (shareValue.compareTo(restShare) > 0) {
                    shareValue = restShare;
                }

                // 更新剩余待分摊金额
                restShare = restShare.subtract(shareValue);

                // 记录最终分摊金额
                shareResultDetailVO.setShareValue(shareValue);
                shareResultDetailVOList.add(shareResultDetailVO);
            }

            // **补偿逻辑**：如果 `isShareAll = true` 且仍有剩余金额，则补偿
            if (shareParam.getIsShareAll() && restShare.compareTo(BigDecimal.ZERO) > 0) {
                for (int j = shareMoneyDetailVOS.size() - 1; j >= 0; j--) {
                    ShareParamDetailVO shareParamDetailVO = shareMoneyDetailVOS.get(j);
                    ShareResultDetailVO shareDetailResult = shareResultDetailMap.get(shareParamDetailVO.getShareDetailId());

                    // 如果该项已达到 `shareLimit`，则跳过
                    if (shareParamDetailVO.getShareLimit().compareTo(shareDetailResult.getShareValue()) == 0) {
                        continue;
                    }

                    // 计算补偿后的金额
                    BigDecimal compareShare = shareDetailResult.getShareValue().add(restShare);

                    // 如果补偿后金额仍然小于等于 `shareLimit`，直接补偿
                    if (shareParamDetailVO.getShareLimit().compareTo(compareShare) >= 0) {
                        shareDetailResult.setShareValue(compareShare);
                        restShare = BigDecimal.ZERO;
                        break;
                    } else {
                        // 如果超出了 `shareLimit`，只补偿 `shareLimit` 允许的部分
                        BigDecimal tempShare = shareParamDetailVO.getShareLimit()
                                .subtract(shareDetailResult.getShareValue());
                        restShare = restShare.subtract(tempShare);
                        shareDetailResult.setShareValue(shareParamDetailVO.getShareLimit());
                    }
                }
            }
        }

        return this.afterShare(i, shareResultVO);
    }

    /**
     * 创建返回对象，子类需要实现此方法
     *
     * @return 返回对象
     */
    protected abstract O createResultDetailVO();

    /**
     * 分摊后处理方法，子类可以重写此方法
     *
     * @param i             初始入参
     * @param shareResultVO 分摊结果
     * @return 分摊结果
     */
    protected ShareResultVO<O> afterShare(I i, ShareResultVO<O> shareResultVO) {
        return shareResultVO;
    }

    /**
     * 额外的业务逻辑
     *
     * @param i 自定义入参
     */
    public abstract void raise(I i);
}
