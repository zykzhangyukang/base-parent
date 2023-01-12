package com.coderman.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class TxUtil {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void doAfterCommit(Runnable runnable){

        if(TransactionSynchronizationManager.isActualTransactionActive()){


            TransactionSynchronizationManager.registerSynchronization(
                    new TxTransactionSynchronization(runnable)
            );
        }else {


            log.error("当前方法无事务,不执行目标操作!");
        }
    }


    public void beforeCommit(Runnable runnable){

        if(TransactionSynchronizationManager.isActualTransactionActive()){


            TransactionSynchronizationManager.registerSynchronization(
                    new TxTransactionSynchronization(runnable)
            );
        }else {


            log.error("当前方法无事务,不执行目标操作!");
        }

    }


    static class TxTransactionSynchronization implements TransactionSynchronization{

        private final Runnable runnable;

        public TxTransactionSynchronization(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void afterCommit() {
            executorService.execute(runnable);
        }

        @Override
        public void beforeCommit(boolean readOnly) {
            executorService.execute(runnable);
        }
    }
}
