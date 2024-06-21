package com.rbc.zfe0.road.deliveryinstruction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Mapping TransferItem entity to T_TRANSFERITEM table in database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "T_TRANSFERITEM")
public class TransferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSFERITEM_ID")
    private Integer transferItemId;

    @Column(name = "TRANSFERTYPECODE")
    private String transferTypeCode;

    @Column(name = "DELIVERYINSTRUCTION_ID")
    private Integer deliveryInstructionId;

    @Column(name = "TRANSFERAGENT_ID")
    private Integer transferAgentId;

    @Column(name = "STATUSCODE")
    private String statusCode;

    @Column(name = "TRANSFERAGENTATTENTIONNAME")
    private String transferAgentAttentionName;

    @Column(name = "DAINFIRMACCOUNTNUMER")
    private String dainFirmAccountNumber;

    @Column(name = "ADPACCOUNTNUMBER")
    private String adpAccountNumber;

    @Column(name = "ADPACCOUNTTYPE")
    private String adpAccountType;

    @Column(name = "ADPACCOUNTCHECKDIGIT")
    private String adpAccountCheckDigit;

    @Column(name = "DISPOSITIONCODE")
    private String dispositionCode;

    @Column(name = "DISPOSITIONDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dispositionDt;

    @Column(name = "RECEIVEDDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receivedDt;

    @Column(name = "TRANSFERREDDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transferredDt;

    @Column(name = "CONFIRMATIONRECEIVEDDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmationReceivedDt;

    @Column(name = "CONFIRMATIONSENTDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmationSentDt;

    @Column(name = "ACCOUNTTAXID")
    private String accountTaxId;

    @Column(name = "REPID")
    private String repId;

    @Column(name = "TRANSFEREFFECTIVEDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transferEffectiveDt;

    @Column(name = "REGISTRATIONTAXID")
    private String registrationTaxId;

    @Column(name = "REGISTRATIONDESCR")
    private String registrationDescr;

    @Column(name = "MAILRECEIPTNUMBER")
    private String mailReceiptNumber;

    @Column(name = "ADPBRANCHCODE")
    private String adpBranchCode;

    @Column(name = "ALTBRANCHCODE")
    private String altBranchCode;

    @Column(name = "CLOSEDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeDt;

    @Column(name = "FIRMID")
    private String firmId;

    @Column(name = "ORIGINALSECURITYDESCR")
    private String originalSecurityDescr;

    @Column(name = "ORIGINALDAINSECURITYNUMBER")
    private String originalDainSecurityNumber;

    @Column(name = "ORIGINALADPSECURITYNUMBER")
    private String originalAdpSecurityNumber;

    @Column(name = "ORIGINALCUSIP")
    private String originalCusIp;

    @Column(name = "ORIGINALQTY")
    private Float originalQty;

    @Column(name = "ORIGINALDENOMINATORDESCR")
    private String originalDenominatorDescr;

    @Column(name = "ORIGINALENTRYCODE")
    private String originalEntryCode;

    @Column(name = "ORIGINALENTRYDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime originalEntryDt;

    @Column(name = "ORIGINALDLEAACCOUNTNUMBER")
    private String originalDleaAccountNumber;

    @Column(name = "ORIGINALDLEAACCOUNTTYPE")
    private Float originalDleaAccountType;

    @Column(name = "ORIGINALDLEAACCOUNTCHECKDIGIT")
    private String originalDleaAccountCheckDigit;

    @Column(name = "ORIGINALCSEAACCOUNTNUMBER")
    private String originalCseaAccountNumber;

    @Column(name = "ORIGINALCSEAACCOUNTTYPE")
    private Float originalCseaAccountType;

    @Column(name = "ORIGINALCSEAACCOUNTCHECKDIGIT")
    private String originalCseaAccountCheckDigit;

    @Column(name = "BLOCKFLAG")
    private Integer blockFlag;

    @Column(name = "LASTUPDATENAME")
    private String lastUpdateName;

    @Column(name = "LASTUPDATEDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDt;

    @Column(name = "TLETRANSFERTYPE")
    private String tleTransferType;

    @Column(name = "GIFTINDICATOR")
    private String giftIndicator;

    @Column(name = "CONTROLID")
    private String controlId;

    @Column(name = "LASTUPDATEROLE")
    private String lastUpdateRole;
}
