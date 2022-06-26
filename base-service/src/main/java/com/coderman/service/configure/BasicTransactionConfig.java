package com.coderman.service.configure;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;
import java.util.List;

/**
 * @author coderman
 * @Title: 基础的配置事务
 * @Description: TOD
 * @date 2022/3/1618:05
 */
public class BasicTransactionConfig {


    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager) {

        NameMatchTransactionAttributeSource transactionAttributeSource = new NameMatchTransactionAttributeSource();
        List<RollbackRuleAttribute> rollbackRuleAttributeList = Collections.singletonList(new RollbackRuleAttribute(Throwable.class));


        // 使用事务
        RuleBasedTransactionAttribute requiredLongTx = new RuleBasedTransactionAttribute();
        requiredLongTx.setTimeout(30);
        requiredLongTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredLongTx.setRollbackRules(rollbackRuleAttributeList);
        transactionAttributeSource.addTransactionalMethod("save", requiredLongTx);

        // 使用事务
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setTimeout(15);
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setRollbackRules(rollbackRuleAttributeList);

        transactionAttributeSource.addTransactionalMethod("create*",requiredTx);
        transactionAttributeSource.addTransactionalMethod("insert*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("update*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("modify*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("delete*", requiredTx);
        transactionAttributeSource.addTransactionalMethod("remove*", requiredTx);

        // 使用事务
        RuleBasedTransactionAttribute requiredNewTx = new RuleBasedTransactionAttribute();
        requiredNewTx.setTimeout(15);
        requiredNewTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        requiredNewTx.setRollbackRules(rollbackRuleAttributeList);

        transactionAttributeSource.addTransactionalMethod("noTran*", requiredNewTx);

        // 使用事务
        RuleBasedTransactionAttribute requiredTimerTx = new RuleBasedTransactionAttribute();
        requiredTimerTx.setTimeout(50);
        requiredTimerTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTimerTx.setRollbackRules(rollbackRuleAttributeList);
        transactionAttributeSource.addTransactionalMethod("*Timer", requiredTimerTx);

        // 使用事务
        RuleBasedTransactionAttribute requiredLongTimerTx = new RuleBasedTransactionAttribute();
        requiredLongTimerTx.setTimeout(150);
        requiredLongTimerTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredLongTimerTx.setRollbackRules(rollbackRuleAttributeList);
        transactionAttributeSource.addTransactionalMethod("*LongTimer", requiredLongTimerTx);

        // 只读事务
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        transactionAttributeSource.addTransactionalMethod("*", readOnlyTx);

        return new TransactionInterceptor(transactionManager, transactionAttributeSource);
    }

    public Advisor advisor(Advice advisor, String expression) {

        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor(aspectJExpressionPointcut, advisor);
        defaultPointcutAdvisor.setOrder(5);

        return defaultPointcutAdvisor;
    }


}
