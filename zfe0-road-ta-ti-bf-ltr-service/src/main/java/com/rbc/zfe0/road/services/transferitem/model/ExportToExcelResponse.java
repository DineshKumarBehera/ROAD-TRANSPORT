package com.rbc.zfe0.road.services.transferitem.model;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExportToExcelResponse {
    private byte[] excelData;
    private Integer transferItemId;
    private String statusCode;
    private String branch;
    private String altBranchCode;
    private String repId;
    private String itemAcctNumber;
    private String acctTaxId;
    private String itemRecd;
    private String nIRecd;
    private String itemType;
    private String confirmRecd;
    private String confirmSent;
    private String secNumber;
    private String cUSIP;
    private String secDesc;
    private String transferAgentName;
    private int transferAgentId;
    private BigDecimal qTY;
    private String certificate;

    private String certDate;
    private String certNbr;
    private String transferTypeCode;
    private String transferType;
    private String deliveryInstruction;

    private String disposition;
    private String mailReceipt;
    private String regTaxID;

    private String nIRegistration;

    private String nIDebit;
    private String nICredit;
    private String nIBatch;
    private String nIEntry;

    private BigDecimal value;
    private BigDecimal checkAmt;

    private String checkWire;

    private String transferredDt;
    private String controlId;

    private String lastModifiedDt;
    private String lastUpdatedBy;

    public ExportToExcelResponse(byte[] excelData) {
        this.excelData = excelData;
    }
}
