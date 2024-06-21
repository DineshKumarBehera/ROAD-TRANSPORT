package com.rbc.zfe0.road.services.transferitem.filter;

import com.rbc.zfe0.road.services.transferitem.dto.TransferHistoryItemDto;
import com.rbc.zfe0.road.services.transferitem.entity.IssueHistory;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItemHistory;
import com.rbc.zfe0.road.services.transferitem.utils.Constants;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class TransferItemHistorySearchClose implements Specification<TransferItemHistory> {

    @Autowired
    private TransferHistoryItemDto transferHistoryItemDto;



     @Override
    public Predicate toPredicate(Root<TransferItemHistory> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
// Add join with IssueHistory table
        Join<TransferItemHistory, IssueHistory> issueHistoryJoin = root.join("issueHistory", JoinType.RIGHT);
        if (transferHistoryItemDto.getTransferHistoryItem() != 0) {
            predicates.add(builder.equal(root.get("transferItemId"), transferHistoryItemDto.getTransferHistoryItem()));
        }
        if (transferHistoryItemDto.getStatusCode() != null) {
            predicates.add(builder.equal(root.get("statusCode"), transferHistoryItemDto.getStatusCode()));
        }
        if (transferHistoryItemDto.getAccountNumber() != null) {
            predicates.add(builder.equal(root.get("adpAccountNumber"), transferHistoryItemDto.getAccountNumber()));
        }
        addDatePredicate(predicates, builder, root, "receivedDt", transferHistoryItemDto.getItemReceivedFrom(), transferHistoryItemDto.getItemReceivedTo());
        addDatePredicate(predicates, builder, root, "confirmationReceivedDt", transferHistoryItemDto.getConfirmedDateFrom(), transferHistoryItemDto.getConfirmedDateTo());
        addDatePredicate(predicates, builder, root, "lastUpdateDt", transferHistoryItemDto.getLastModifiedFrom(), transferHistoryItemDto.getLastModifiedTo());
        addDatePredicate(predicates, builder, root, "transferredDt", transferHistoryItemDto.getTransferredDateFrom(), transferHistoryItemDto.getTransferredDateTo());

        if (transferHistoryItemDto.getInsuranceValue() != null) {
            predicates.add(builder.equal(root.get("insuranceValue"), transferHistoryItemDto.getInsuranceValue()));
        }
        if (transferHistoryItemDto.getLastModifiedBy() != null) {
            String pattern = "%" + transferHistoryItemDto.getLastModifiedBy() + "%";
            predicates.add(builder.like(root.get("lastUpdateName"), pattern));
        }
        if (transferHistoryItemDto.getAccountTaxId() != null) {
            predicates.add(builder.equal(root.get("accountTaxId"), transferHistoryItemDto.getAccountTaxId()));
        }

        if (transferHistoryItemDto.getOriginalCusIp() != null) {
            predicates.add(builder.equal(root.get("originalCusIp"), transferHistoryItemDto.getOriginalCusIp()));
        }
        if (transferHistoryItemDto.getSecurityNumber() != null) {
            predicates.add(builder.equal(root.get("originalAdpSecurityNumber"), transferHistoryItemDto.getSecurityNumber()));
        }
        if (transferHistoryItemDto.getLongDesc() != null) {
            predicates.add(builder.equal(root.get("originalSecurityDescr"), transferHistoryItemDto.getLongDesc()));
        }

        if (transferHistoryItemDto.getQuantity() != null) {
            predicates.add(builder.equal(root.get("originalQty"), transferHistoryItemDto.getQuantity()));
        }
        if (transferHistoryItemDto.getDispositionCode() != null) {
            predicates.add(builder.equal(root.get("dispositionCode"), transferHistoryItemDto.getDispositionCode()));
        }
        if (transferHistoryItemDto.getTransferTypeDesc() != null) {
            predicates.add(builder.equal(root.get("transferTypeCode"), transferHistoryItemDto.getTransferTypeDesc()));
        }
        if (transferHistoryItemDto.getTransferAgent() != null) {
            predicates.add(builder.equal(root.get("transferAgentName"), transferHistoryItemDto.getTransferAgent()));
        }
// add a condition to filter by some property of Issue
        if (transferHistoryItemDto.getNewSecurityDesc() != null) {
            predicates.add(builder.equal(root.get("securityDescription"), transferHistoryItemDto.getNewSecurityDesc()));
        }
        if (transferHistoryItemDto.getNewCusip() != null) {
            predicates.add(builder.equal(issueHistoryJoin.get("cusip"), transferHistoryItemDto.getNewCusip()));
        }
        if (transferHistoryItemDto.getNewCreditShortEntryAccount() != null) {
            predicates.add(builder.equal(issueHistoryJoin.get("cseAccountNumber"), transferHistoryItemDto.getNewCreditShortEntryAccount()));
        }
        if (transferHistoryItemDto.getNewQuantity() != null) {
            predicates.add(builder.equal(issueHistoryJoin.get("quantity"), transferHistoryItemDto.getNewQuantity()));
        }
        if (transferHistoryItemDto.getInsuranceValue() != null) {
            predicates.add(builder.equal(issueHistoryJoin.get("insuranceValue"), transferHistoryItemDto.getInsuranceValue()));
        }
        if (transferHistoryItemDto.getOldSecurityDesc() != null) {
            predicates.add(builder.equal(root.get("originalSecurityDescr"), transferHistoryItemDto.getOldSecurityDesc()));
        }
        if (transferHistoryItemDto.getDescription() != null) {
            predicates.add(builder.equal(issueHistoryJoin.get("securityDescription"), transferHistoryItemDto.getDescription()));
        }
        if (transferHistoryItemDto.getNewDebitLongEntryAccount() != null) {
            predicates.add(builder.equal(root.get("newDebitLongEntryAccount"), transferHistoryItemDto.getNewDebitLongEntryAccount()));
        }
        if (transferHistoryItemDto.getOldCusip() != null) {
            predicates.add(builder.equal(root.get("originalCusIp"), transferHistoryItemDto.getOldCusip()));
        }
        if (transferHistoryItemDto.getOldCreditShortEntryAccount() != null) {
            predicates.add(builder.equal(root.get("originalCseaAccountNumber"), transferHistoryItemDto.getOldCreditShortEntryAccount()));
        }
        if (transferHistoryItemDto.getOldDebitLongEntryAccount() != null) {
            predicates.add(builder.equal(root.get("originalDleaAccountNumber"), transferHistoryItemDto.getOldDebitLongEntryAccount()));
        }

        if (transferHistoryItemDto.getOrigAdpSecurityNumber() != null) {
            predicates.add(builder.equal(root.get("originalAdpSecurityNumber"), transferHistoryItemDto.getOrigAdpSecurityNumber()));
        }
        if (transferHistoryItemDto.getAdpSecurityNumber() != null) {
            predicates.add(builder.equal(root.get("originalDainSecurityNumber"), transferHistoryItemDto.getAdpSecurityNumber()));
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private void addDatePredicate(List<Predicate> predicates, CriteriaBuilder builder, Root<TransferItemHistory> root, String dateField, Date fromDate, Date toDate) {
        if (fromDate != null && toDate != null) {
            predicates.add(builder.between(root.get(dateField), fromDate, toDate));
        } else if (fromDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(dateField), fromDate));
        } else if (toDate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(dateField), toDate));
        }
    }
}