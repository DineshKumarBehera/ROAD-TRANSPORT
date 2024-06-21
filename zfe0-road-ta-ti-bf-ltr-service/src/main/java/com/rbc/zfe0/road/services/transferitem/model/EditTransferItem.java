package com.rbc.zfe0.road.services.transferitem.model;

import com.rbc.zfe0.road.services.transferitem.entity.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditTransferItem {

    private int transferItemId;
    private int transferAgentId;
    private String altBranchCode;
    private String statusCode;
    private String repId;
    private String itemAcct;
    private String acctTaxId;
    private String dispositionCode;
    private String dispositionDate;
    private String transferAgentName;
    private String deliveryInstructionId;
    private String mailReceiptNumber;
    private String closeType;
    private String controlId;
    private String confirmationReceiveDDt;
    private String transferDate;
    private String confirmationSentDt;
    private String itemRecd;
    private String closeDate;
    private OriginalIssue originalIssue;
    private TransferAgent transferAgent;
    private List<HistoryNotes> historyNotes;
    private NewIssueRegistration newIssueRegistrations;
    private List<NewIssueCash> newIssueCash;
    private List<NewIssueSecurities> newIssueSecurities;
    private TransferTypeResponse transferTypeResponse;
    private TransferAgentResponse transferAgentDetails;
    private DeliveryInstructionResponse deliveryInstructionResponse;
    private String dainFirmAccountNumber;
    private String adpAccountType;
    private String adpAccountCheckDigit;
    private String receivedDt;
    private String transferredDt;
    private String accountTaxId;
    private String transferEffectiveDt;
    private String registrationTaxId;
    private String registrationDescr;
    private String adpBranchCode;
    private String closeDt;
    private String firmId;
    private String originalSecurityDescr;
    private String originalDainSecurityNumber;
    private String originalAdpSecurityNumber;
    private String originalCusIp;
    private BigDecimal originalQty;
    private String originalDenominationDescr;
    private String originalEntryCode;
    private String originalEntryDt;
    private String originalDleAccountNumber;
    private String originalDleAccountType;
    private String originalDleAccountCheckDigit;
    private String originalCseAccountNumber;
    private String originalCseAccountType;
    private String originalCseAccountCheckDigit;
    private String lastUpdateName;
    private String lastUpdateDt;
    private String tleTransferType;
    private String giftIndicator;
    private String lastUpdateRole;
    private Boolean newStatus;
    private List<Integer> deletedIssueIds = new ArrayList<>();

    public boolean isAccountFreeze() {
        return false;
    }

    private List notes = new ArrayList();

    public void addNote(Notes note) {
        notes.add(note);
    }

    public void addNote(String eventCode, String text, String userId) {
        Notes noteTemp = new Notes();
        noteTemp.setNotesStsCode(this.getStatusCode());
        noteTemp.setNotesTxt(text);
        noteTemp.setLastNoteUpdateName(userId);
        noteTemp.setNotesEventCode(eventCode);
        notes.add(noteTemp);
    }

    List<Issue> issue;
    private String transferType;
}
