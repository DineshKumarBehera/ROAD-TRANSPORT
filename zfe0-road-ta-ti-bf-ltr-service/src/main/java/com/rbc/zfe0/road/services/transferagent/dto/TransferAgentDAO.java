package com.rbc.zfe0.road.services.transferagent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TransferAgentDAO {

    private long AGT_ID;
    private String AGT_NM;
    private String ADDR_BOX;
    private String TEL;
    private String FAX;
    private String FEE_AMT;
    private Date LST_USED_DT;
    private String LST_UPDT_USR_NM;
    private Date LST_UPDT_DT_TM;
    private int ACTV_FLG_IND;
    private String EMAIL_ID;
}
