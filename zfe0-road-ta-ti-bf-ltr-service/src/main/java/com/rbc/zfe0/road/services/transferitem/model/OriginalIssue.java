package com.rbc.zfe0.road.services.transferitem.model;

import lombok.*;

import java.math.BigDecimal;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OriginalIssue {

    private String originalAdpSecNo;
    private int issueId;
    private int transferItemId;
    private String cusipNo;
    private String originalSecDesc;
    private BigDecimal originalQty;
    private String  denomination;
    private String dleAccountNumberFormatted;
    private String cseAccountNumberFormatted;
    private String originalDenDesc;
    private String originalCertificate;
    private String originalDlAccNo;
    private String cseAccNo;


    public boolean isCashEntry() {
        return false;
    }
}
