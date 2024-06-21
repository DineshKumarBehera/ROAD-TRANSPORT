package com.rbc.zfe0.road.services.dto.transfertype;

import com.rbc.zfe0.road.services.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransferTypeDTO implements Serializable {

    private String transferTypeCode;
    private String description;
    private String code;
    private String twoCharCode;
    private Boolean isCashIssues;
    private Boolean isSecurityIssues;
    private Boolean uiActive;
    private Boolean autoRoadBlock;
    private String lastUpdateName;
    private Date lastUpdateDt;
    private BookkeepingDTO bookkeeping;
    private BatchEntryDTO batchEntry;
}
