package com.rbc.zfe0.road.eod.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Aspect
public class TransactionalAspect {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Around("execution(* com.rbc.zfe0.road.eod.*.*(..))")
    public Object handleTransaction(ProceedingJoinPoint pjp) throws Throwable {
        final Object[] result = new Object[1];
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    result[0] = pjp.proceed();
                } catch (Throwable throwable) {
                    status.setRollbackOnly();
                }
            }
        });
        return result[0];
    }
}
