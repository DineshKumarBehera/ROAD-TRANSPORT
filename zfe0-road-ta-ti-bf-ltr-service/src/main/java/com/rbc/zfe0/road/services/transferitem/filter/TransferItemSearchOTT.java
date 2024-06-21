package com.rbc.zfe0.road.services.transferitem.filter;

import com.rbc.zfe0.road.services.transferitem.dto.TransferItemDto;
import com.rbc.zfe0.road.services.transferitem.entity.Issue;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import jakarta.persistence.criteria.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferItemSearchOTT implements Specification<TransferItem> {

    @Autowired
    private TransferItemDto transferItemDto;

    @Override
    public Predicate toPredicate(Root<TransferItem> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        Join<TransferItem, Issue> issueJoin = root.join("issue", JoinType.RIGHT);
        if (transferItemDto.getTransferItem() != 0) {
            predicates.add(builder.equal(root.get("transferItemId"), transferItemDto.getTransferItem()));
        }
        if (transferItemDto.getStatusCode() != null) {
            predicates.add(builder.equal(root.get("statusCode"), transferItemDto.getStatusCode()));
        }
        if (transferItemDto.getAccountNumber() != null) {
            predicates.add(builder.equal(root.get("adpAccountNumber"), transferItemDto.getAccountNumber()));
        }
        addDatePredicate(predicates, builder, root, "receivedDt", transferItemDto.getItemReceivedFrom(), transferItemDto.getItemReceivedTo());
        addDatePredicate(predicates, builder, root, "confirmationReceivedDt", transferItemDto.getConfirmedDateFrom(), transferItemDto.getConfirmedDateTo());
        addDatePredicate(predicates, builder, root, "lastUpdateDt", transferItemDto.getLastModifiedFrom(), transferItemDto.getLastModifiedTo());
        addDatePredicate(predicates, builder, root, "transferredDt", transferItemDto.getTransferredDateFrom(), transferItemDto.getTransferredDateTo());

        if (transferItemDto.getInsuranceValue() != null) {
            predicates.add(builder.equal(root.get("insuranceValue"), transferItemDto.getInsuranceValue()));
        }
        if (transferItemDto.getLastModifiedBy() != null) {
            String pattern = "%" + transferItemDto.getLastModifiedBy() + "%";
            predicates.add(builder.like(root.get("lastUpdateName"), pattern));
        }
        if (transferItemDto.getAccountTaxId() != null) {
            predicates.add(builder.equal(root.get("accountTaxId"), transferItemDto.getAccountTaxId()));
        }

        if (transferItemDto.getOriginalCusIp() != null) {
            predicates.add(builder.equal(root.get("originalCusIp"), transferItemDto.getOriginalCusIp()));
        }
        if (transferItemDto.getOrigAdpSecurityNumber() != null) {
            predicates.add(builder.equal(root.get("originalAdpSecurityNumber"), transferItemDto.getOrigAdpSecurityNumber()));
        }
        if (transferItemDto.getLongDesc() != null) {
            predicates.add(builder.equal(root.get("originalSecurityDescr"), transferItemDto.getLongDesc()));
        }

        if (transferItemDto.getOriginalQuantity() != null) {
            predicates.add(builder.equal(root.get("originalQty"), transferItemDto.getOriginalQuantity()));
        }
        if (transferItemDto.getDispositionCode() != null) {
            predicates.add(builder.equal(root.get("dispositionCode"), transferItemDto.getDispositionCode()));
        }
        if (transferItemDto.getTransferTypeDesc() != null) {
            predicates.add(builder.equal(root.get("transferType"), transferItemDto.getTransferTypeDesc()));
        }
        if (transferItemDto.getTransferAgent() != null) {
            predicates.add(builder.equal(root.get("transferAgent"), transferItemDto.getTransferAgent()));
        }
// add a condition to filter by some property of Issue
        if (transferItemDto.getNewSecurityDesc() != null) {
            predicates.add(builder.equal(root.get("securityDescription"), transferItemDto.getNewSecurityDesc()));
        }
        if (transferItemDto.getNewDebitLongEntryAccount() != null) {
            predicates.add(builder.equal(root.get("newDebitLongEntryAccount"), transferItemDto.getNewDebitLongEntryAccount()));
        }
        if (transferItemDto.getOldCusip() != null) {
            predicates.add(builder.equal(root.get("originalCusIp"), transferItemDto.getOldCusip()));
        }
        if (transferItemDto.getOldCreditShortEntryAccount() != null) {
            predicates.add(builder.equal(root.get("originalCseaAccountNumber"), transferItemDto.getOldCreditShortEntryAccount()));
        }
        if (transferItemDto.getOldDebitLongEntryAccount() != null) {
            predicates.add(builder.equal(root.get("originalDleaAccountNumber"), transferItemDto.getOldDebitLongEntryAccount()));
        }

        if (transferItemDto.getOrigAdpSecurityNumber() != null) {
            predicates.add(builder.equal(root.get("originalAdpSecurityNumber"), transferItemDto.getOrigAdpSecurityNumber()));
        }
        if (transferItemDto.getAdpSecurityNumber() != null) {
            predicates.add(builder.equal(root.get("originalDainSecurityNumber"), transferItemDto.getAdpSecurityNumber()));
        }
        if (transferItemDto.getNewCusip() != null) {
            predicates.add(builder.equal(issueJoin.get("cusip"), transferItemDto.getNewCusip()));
        }
        if (transferItemDto.getNewCreditShortEntryAccount() != null) {
            predicates.add(builder.equal(issueJoin.get("cseAccountNumber"), transferItemDto.getNewCreditShortEntryAccount()));
        }
        if (transferItemDto.getNewQuantity() != null) {
            predicates.add(builder.equal(issueJoin.get("quantity"), transferItemDto.getNewQuantity()));
        }
        if (transferItemDto.getInsuranceValue() != null) {
            predicates.add(builder.equal(issueJoin.get("insuranceValue"), transferItemDto.getInsuranceValue()));
        }
        if (transferItemDto.getOldSecurityDesc() != null) {
            predicates.add(builder.equal(root.get("originalSecurityDescr"), transferItemDto.getOldSecurityDesc()));
        }
        if (transferItemDto.getDescription() != null) {
            predicates.add(builder.equal(issueJoin.get("originalSecurityDescr"), transferItemDto.getDescription()));
        }
        if (transferItemDto.getDescription() != null) {
            predicates.add(builder.equal(issueJoin.get("originalSecurityDescr"), transferItemDto.getDescription()));
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private void addDatePredicate(List<Predicate> predicates, CriteriaBuilder builder, Root<TransferItem> root, String dateField, Date fromDate, Date toDate) {
        if (fromDate != null && toDate != null) {
            predicates.add(builder.between(root.get(dateField), fromDate, toDate));
        } else if (fromDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(dateField), fromDate));
        } else if (toDate != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(dateField), toDate));
        }
    }

}