package com.coderman.business.chain;

import com.coderman.business.share.AbstractShare;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 分摊链
 *
 * @param <I> 需要在链中传递的对象
 */
public abstract class ShareChain<I> {

    private final List<AbstractShare> filters = new ArrayList<>();

    @PostConstruct
    private void init() {

        this.initChain();
    }


    /**
     * 加入链中
     *
     * @param abstractShare 均摊过滤器
     * @return
     */
    public ShareChain<I> addFilter(AbstractShare abstractShare) {
        filters.add(abstractShare);
        return this;
    }


    public void doShareTimer(I i) {
        for (AbstractShare filter : this.filters) {

            filter.share(i);
        }
    }


    public void doShareRaiseTimer(I i) {
        for (AbstractShare filter : this.filters) {

            filter.raise(i);
        }
    }


    /**
     * 定义链式调用的顺序
     */
    protected abstract void initChain();


}
