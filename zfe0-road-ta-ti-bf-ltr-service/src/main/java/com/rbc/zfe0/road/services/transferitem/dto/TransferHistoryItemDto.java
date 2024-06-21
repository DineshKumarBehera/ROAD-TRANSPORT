package com.rbc.zfe0.road.services.transferitem.dto;

import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItemHistory;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TransferHistoryItemDto {
    private int transferHistoryItem;
    private String statusCode;
    private String accountNumber;
    private Date itemReceivedFrom;
    private Date itemReceivedTo;
    private Date transferredDateFrom;
    private Date transferredDateTo;
    private Date confirmedDateFrom;
    private Date confirmedDateTo;
    private Date lastModifiedFrom;
    private Date lastModifiedTo;
    private String lastModifiedBy;
    private String accountTaxId;
    private String description;
    private String originalCusIp;
    private String longDesc;
    private String securityNumber;
    private BigDecimal quantity;
    private String dispositionCode;
    private String transferTypeDesc;
    private String transferAgent;
    private String insuranceValue;
    private String oldSecurityDesc;
    private String newSecurityDesc;
    private String oldCusip;
    private String newCusip;
    private String oldCreditShortEntryAccount;
    private String newCreditShortEntryAccount;
    private String oldDebitLongEntryAccount;
    private String newDebitLongEntryAccount;
    private String origAdpSecurityNumber;
    private String adpSecurityNumber;
    private String oldDainSecurityNumber;
    private String oldQuantity;
    private BigDecimal newQuantity;


    public TransferHistoryItemDto mapTransferItemToTransferHistoryItemDto(TransferItem transferItem) {
        TransferHistoryItemDto transferHistoryItemDto = new TransferHistoryItemDto();
        transferHistoryItemDto.setStatusCode(transferItem.getStatusCode());
        transferHistoryItemDto.setTransferHistoryItem(transferItem.getTransferItemId());
        transferHistoryItemDto.setAccountNumber(transferItem.getAdpAccountNumber());
        transferHistoryItemDto.setLastModifiedBy(transferItem.getLastUpdateName());
        transferHistoryItemDto.setAccountTaxId(transferItem.getAccountTaxId());
        transferHistoryItemDto.setDescription(transferItem.getOriginalSecurityDescr());
        transferHistoryItemDto.setOriginalCusIp(transferItem.getOriginalCusIp());
        transferHistoryItemDto.setLongDesc(transferItem.getOriginalDenominatorDescr());
        transferHistoryItemDto.setSecurityNumber(transferItem.getOriginalAdpSecurityNumber());
        transferHistoryItemDto.setQuantity(transferItem.getOriginalQty());
        transferHistoryItemDto.setDispositionCode(transferItem.getDispositionCode());
        transferHistoryItemDto.setTransferTypeDesc(transferItem.getTransferType().getTransferTypeCode());
        transferHistoryItemDto.setTransferAgent(transferItem.getTransferAgent().getTransferAgentName());
        transferHistoryItemDto.setOldSecurityDesc(transferItem.getOriginalSecurityDescr());
        return transferHistoryItemDto;
    }

    public TransferHistoryItemDto mapTransferHistoryItemToTransferHistoryItemDto(TransferItemHistory transferHistoryItem) {
        TransferHistoryItemDto transferHistoryItemDto = new TransferHistoryItemDto();
        transferHistoryItemDto.setStatusCode(transferHistoryItem.getStatusCode());
        transferHistoryItemDto.setTransferHistoryItem(transferHistoryItem.getTransferItemId());
        transferHistoryItemDto.setAccountNumber(transferHistoryItem.getAdpAccountNumber());
        transferHistoryItemDto.setLastModifiedBy(transferHistoryItem.getLastUpdateName());
        transferHistoryItemDto.setAccountTaxId(transferHistoryItem.getAccountTaxId());
        transferHistoryItemDto.setDescription(transferHistoryItem.getOriginalSecurityDescr());
        transferHistoryItemDto.setOriginalCusIp(transferHistoryItem.getOriginalCusIp());
        transferHistoryItemDto.setLongDesc(transferHistoryItem.getOriginalDenominationDescr());
        transferHistoryItemDto.setSecurityNumber(transferHistoryItem.getOriginalAdpSecurityNumber());
        transferHistoryItemDto.setQuantity(transferHistoryItem.getOriginalQty());
        transferHistoryItemDto.setDispositionCode(transferHistoryItem.getDispositionCode());
        transferHistoryItemDto.setTransferTypeDesc(transferHistoryItem.getTransferTypeCode());
        transferHistoryItemDto.setTransferAgent(transferHistoryItem.getTransferAgentName());
        transferHistoryItemDto.setOldSecurityDesc(transferHistoryItem.getOriginalSecurityDescr());

        return transferHistoryItemDto;
    }

}
