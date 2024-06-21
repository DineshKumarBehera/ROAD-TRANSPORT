package com.rbc.zfe0.road.eod.process.handler.mapper;

import com.rbc.zfe0.road.eod.process.handler.mapper.EodTransferItem;

public class EodIntactItem extends EodTransferItem {

    private String transactionType;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
