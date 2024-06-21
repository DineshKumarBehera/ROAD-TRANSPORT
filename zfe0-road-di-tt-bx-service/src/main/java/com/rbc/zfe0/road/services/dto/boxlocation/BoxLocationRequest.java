package com.rbc.zfe0.road.services.dto.boxlocation;

import com.rbc.zfe0.road.services.dto.enumtype.CodeType;
import com.rbc.zfe0.road.services.dto.enumtype.DebitCreditType;
import com.rbc.zfe0.road.services.dto.enumtype.RecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoxLocationRequest implements Serializable {

    private Boolean userChoice;
    private String batchCode;
    private String entryCode;
    private String entryType;
    private RecordType recordType;
    private DebitCreditType debitOrCredit;
    private String memoAccountNumber;
    private String memoAccountType;
    private String memoAccountCheckDigit;
    private Boolean dummyAccountFlag;
    private Boolean originalIssueFlag;
    private Boolean dbUpdateFlag;
    private CodeType qty;
    private CodeType dollars;
    private CodeType security;
    private Boolean box97EntryFlag;
    private Boolean deleteOneCsEntryFlag;
    private Boolean mapToClientAcct;
    private String lastUpdateName;
}
