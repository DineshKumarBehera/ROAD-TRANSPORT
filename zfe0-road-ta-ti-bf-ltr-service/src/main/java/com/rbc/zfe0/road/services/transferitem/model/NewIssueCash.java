package com.rbc.zfe0.road.services.transferitem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbc.zfe0.road.services.transferitem.entity.Issue;
import com.rbc.zfe0.road.services.transferitem.utils.DateTimeDeserializer;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewIssueCash {
    private Integer issueId;
    private BigDecimal amtRequested;
    private BigDecimal amtReceived;
    private Date newIssueRecd;
    private String checkOrWire;
    private String debitLong;
    private String creditShort;
    private String batchCode;
    private String entryCode;
    private BigDecimal insuranceValue;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date entryDate;
    private String closedFlag;
    private String certificateNumber;

    private boolean receivedButFrozen = false;


}