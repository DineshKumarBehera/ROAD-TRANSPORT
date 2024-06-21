package com.rbc.zfe0.road.services.dto.transfertype;

import com.rbc.zfe0.road.services.dto.enumtype.CodeType;
import com.rbc.zfe0.road.services.dto.enumtype.DebitCreditType;
import com.rbc.zfe0.road.services.dto.enumtype.IssueTypeCode;
import com.rbc.zfe0.road.services.dto.enumtype.RecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entry implements Serializable {
    private Integer id;
    private Boolean isDelete = false;
    private String entryType;
    private String entryTypeOption;
    private RecordType recordType;
    private DebitCreditType debitOrCredit;
    private String batchCode;
    private String entryCode;
    private String acctBoxLoc;
    private CodeType qty;
    private CodeType dollars;
    private CodeType security;
    private IssueTypeCode entryTypeIssueCode;
    private Boolean cse;
    private Boolean updateDb;
    private Boolean box97;
    private Boolean dummy;
    private Boolean originalIssue;
}
