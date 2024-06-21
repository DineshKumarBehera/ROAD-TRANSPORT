package com.rbc.zfe0.road.services.transferitem.service;

import com.rbc.zfe0.road.services.transferitem.dao.*;
import com.rbc.zfe0.road.services.transferitem.dto.TransferHistoryItemDto;
import com.rbc.zfe0.road.services.transferitem.dto.TransferItemDto;
import com.rbc.zfe0.road.services.transferitem.dto.TransferItemResponse;
import com.rbc.zfe0.road.services.transferitem.entity.*;
import com.rbc.zfe0.road.services.transferitem.exception.ResourceNotFoundException;
import com.rbc.zfe0.road.services.transferitem.exception.TransferItemNotFoundException;
import com.rbc.zfe0.road.services.transferitem.filter.*;
import com.rbc.zfe0.road.services.transferitem.model.*;
import com.rbc.zfe0.road.services.transferitem.repository.*;
import com.rbc.zfe0.road.services.transferitem.utils.Constants;
import com.rbc.zfe0.road.services.transferitem.utils.NumberFormater;
import com.rbc.zfe0.road.services.transferitem.utils.RbcUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
public class TransferItemServiceImpl implements TransferItemService {

    @Autowired
    DeliveryInstructionRepository deliveryInstructionRepository;
    @Autowired
    TransferItemHistoryRepository transferItemHistoryRepository;
    @Autowired
    TransferItemRepository transferItemRepository;
    @Autowired
    TransferTypeRepository transferTypeRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    TransferAgentRepository transferAgentRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    DeliveryInstructionDAO deliveryInstructionDAO;

    @Autowired
    NotesDao notesDao;
    @Autowired
    TransferAgentService transferAgentService;
    @Autowired
    TransferAgentDao transferAgentDao;

    @Autowired
    IssueDao issueDao;
    @Autowired
    TransferTypeDao transferTypeDao;

    @Autowired
    EntityManager em;

    @Autowired
    RegistrationInformationRepository registrationInformationRepository;

    @Autowired

    private MessageSource messageSource;

    @Autowired

    private Environment env;

    public String getLabel(String key) {
        return env.getProperty(key);
    }

    private SimpleDateFormat dateFormatterWithOutTime = new SimpleDateFormat("MM/dd/yyyy");

    public void addNewTransferItem(Map<String, Object> requestParams) throws Exception {

        TransferItemResponse response = new TransferItemResponse();

        TransferItem item = new TransferItem();
        try {
            item.setStatusCode("Pend");

            String accountNumber = requestParams.get("accountNumber") + "";
            //First 8 digits of Account Number from UI
            item.setAdpAccountNumber(accountNumber.substring(0, 8));
            //9th digit from Account Number
            item.setAdpAccountType(accountNumber.substring(8, 9));
            //10th Digit from Account Number
            item.setAdpAccountCheckDigit(accountNumber.substring(9));

            item.setReceivedDt(new Date());

            String accountTaxId = requestParams.get("accountTaxId") + "";
            String taxIdType = requestParams.get("taxIdType") + "";

            if (taxIdType.equalsIgnoreCase("SSN")) {
                //123-45-6789
                item.setAccountTaxId(accountTaxId.substring(0, 3) + "-" + accountTaxId.substring(3, 5) + "-" + accountTaxId.substring(5));
            } else if (taxIdType.equalsIgnoreCase("EIN")) {
                //12-3456789
                item.setAccountTaxId(accountTaxId.substring(0, 2) + "-" + accountTaxId.substring(2));
            }
            item.setRepId("Rep");
            item.setRegistrationTaxId("1");
            item.setRegistrationDescr("MOCK TEST DATA - NO MF CALL");

            //First 3 digits of account Number
            item.setAdpBranchCode(accountNumber.substring(0, 3));
            item.setAltBranchCode("111");
            item.setFirmId("1101");
            item.setOriginalSecurityDescr("MOCK TEST DATA - NO MF CALL");
            item.setOriginalDainSecurityNumber(requestParams.get("dainSecNumber") + "");
            item.setOriginalAdpSecurityNumber(requestParams.get("ADPSecNumber") + "");
            item.setOriginalCusIp(requestParams.get("cusipNumber") + "");
            item.setOriginalQty(new BigDecimal(requestParams.get("quantity") + ""));

            item.setBlockFlag(0);
            item.setControlId(requestParams.get("controlId") + "");

            //123-12345-1-1
            String debitLong = requestParams.get("debitLong") + "";
            String creditShort = requestParams.get("creditShort") + "";

            String[] debitLongArray = debitLong.split("-");
            String[] creditShortArray = creditShort.split("-");

            if (debitLongArray != null && debitLongArray.length > -1) {
                //12312345
                item.setOriginalDleaAccountNumber(debitLongArray[0] + debitLongArray[1]);
                //1
                item.setOriginalDleaAccountType(debitLongArray[2]);
                //1
                item.setOriginalDleaAccountCheckDigit(debitLongArray[3]);
            }

            if (creditShortArray != null && creditShortArray.length > -1) {
                //12312345
                item.setOriginalCseaAccountNumber(creditShortArray[0] + creditShortArray[1]);
                //1
                item.setOriginalCseaAccountType(creditShortArray[2]);
                //1
                item.setOriginalCseaAccountCheckDigit(creditShortArray[3]);
            }

            item.setLastUpdateName(requestParams.get("updatedBy") + "");
            item.setLastUpdateDt(new Date());

            transferItemRepository.save(item);


        } catch (Exception e) {
            throw new Exception("An error occurred while saving the transfer item", e);
        }
    }

    @Override
    public List<TransferItemResponse> findTransportItem(String statusCode) {
        List<TransferItemResponse> responses;

        if (statusCode.equalsIgnoreCase(Constants.STATUS_DAILY_ITEMS)) {
            responses = getEntryTransferItemDetails(statusCode);
        } else if (statusCode.equalsIgnoreCase(Constants.STATUS_CLOSED)) {
            responses = getCloseTransferItemDetails(statusCode);
        } else if (statusCode.equalsIgnoreCase(Constants.STATUS_OUT_TO_TRANSFER)) {
            responses = getOttTransferItemDetails(statusCode);
        } else if (statusCode.equalsIgnoreCase(Constants.STATUS_PENDING)) {
            responses = getPendingTransferItemDetails(statusCode);
        } else {
            responses = new ArrayList<>();
        }

        log.info("count {}", responses.size());
        return responses;
    }

    private List<TransferItemResponse> getPendingTransferItemDetails(String statusCode) {
        List<TransferItemResponse> responses = new ArrayList<>();
        List<TransferItem> ti = transferItemRepository.findByStatusCode(statusCode);
        log.info("count of pending transfer item {}", ti.size());

        for (TransferItem tii : ti) {
            TransferItemResponse response = new TransferItemResponse();
            response.setTransferItemId(tii.getTransferItemId());
            response.setStatusCode(tii.getStatusCode());
            response.setItemRecd(convertDate(tii.getReceivedDt()));
            response.setItemRecdDateTo(convertDate(tii.getReceivedDt()));
            response.setTransferDate(convertDate(tii.getTransferredDt()));
            response.setTransferDateTo(convertDate(tii.getTransferredDt()));
            response.setLastModified(convertDate(tii.getLastUpdateDt()));
            response.setLastModifiedTo(convertDate(tii.getLastUpdateDt()));
            response.setConfirmationReceiveDDt(convertDate(tii.getConfirmationReceivedDt()));
            response.setConfirmationReceiveDDtTo(convertDate(tii.getConfirmationReceivedDt()));
            response.setCloseDate(convertDate(tii.getCloseDt()));
            response.setItemAcct(concatItemAcct(tii.getAdpAccountNumber(), tii.getAdpAccountType(), tii.getAdpAccountCheckDigit()));
            response.setSec(tii.getOriginalAdpSecurityNumber());
            response.setOriginalSec(tii.getOriginalAdpSecurityNumber());
            response.setCusIp(tii.getOriginalCusIp());
            response.setOriginalCusIp(tii.getOriginalCusIp());
            response.setItemQty(tii.getOriginalQty());
            response.setOriginalQuantity(tii.getOriginalQty());
            response.setSecDesc(tii.getOriginalSecurityDescr());
            response.setOriginalSecDesc(tii.getOriginalSecurityDescr());
            response.setLastModifiedBy(tii.getLastUpdateName());
            response.setIssueType("OI");
            responses.add(response);

            if (tii.getIssue().size() > 0) {
                for (Issue issueCheck : tii.getIssue()) {
                    TransferItemResponse niiResponse = new TransferItemResponse();
                    niiResponse.setTransferItemId(issueCheck.getTransferItem().getTransferItemId());
                    niiResponse.setStatusCode(issueCheck.getTransferItem().getStatusCode());
                    niiResponse.setItemRecd(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niiResponse.setItemRecdDateTo(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niiResponse.setTransferDate(convertDate(issueCheck.getTransferItem().getTransferredDt()));
                    niiResponse.setTransferDateTo(convertDate(issueCheck.getTransferItem().getTransferredDt()));
                    niiResponse.setLastModified(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setLastModifiedTo(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setConfirmationReceiveDDt(convertDate(issueCheck.getTransferItem().getConfirmationReceivedDt()));
                    niiResponse.setConfirmationReceiveDDtTo(convertDate(issueCheck.getTransferItem().getConfirmationReceivedDt()));
                    niiResponse.setCloseDate(convertDate(issueCheck.getTransferItem().getCloseDt()));
                    niiResponse.setItemAcct(concatItemAcct(issueCheck.getTransferItem().getAdpAccountNumber(), issueCheck.getTransferItem().getAdpAccountType(), issueCheck.getTransferItem().getAdpAccountCheckDigit()));
                    niiResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niiResponse.setOriginalSec(issueCheck.getTransferItem().getOriginalAdpSecurityNumber());
                    niiResponse.setCusIp(issueCheck.getCusip());
                    niiResponse.setItemValue(issueCheck.getInsuranceValue());
                    niiResponse.setOriginalCusIp(issueCheck.getTransferItem().getOriginalCusIp());
                    niiResponse.setItemQty(issueCheck.getQuantity());
                    niiResponse.setOriginalQuantity(issueCheck.getTransferItem().getOriginalQty());
                    niiResponse.setSecDesc(issueCheck.getSecurityDescription());
                    niiResponse.setOriginalSecDesc(issueCheck.getTransferItem().getOriginalSecurityDescr());
                    niiResponse.setLastModifiedBy(issueCheck.getLastUpdateName());
                    niiResponse.setCheckAmount(issueCheck.getCheckAmount());
                    niiResponse.setIssueType("NI");
                    responses.add(niiResponse);
                }
            }
        }
        return responses;
    }

    private List<TransferItemResponse> getOttTransferItemDetails(String statusCode) {
        List<TransferItemResponse> responses = new ArrayList<>();
        List<TransferItem> tiis = transferItemRepository.findByStatusCode(statusCode);
        Set<Integer> addedTransferItemIds = new HashSet<>();
        log.info("count of OTT transfer item {}", tiis.size());

        for (TransferItem tii : tiis) {
            if (!addedTransferItemIds.contains(tii.getTransferItemId())) {
                TransferItemResponse oiiResponse = new TransferItemResponse();
                oiiResponse.setTransferItemId(tii.getTransferItemId());
                oiiResponse.setStatusCode(tii.getStatusCode());
                oiiResponse.setItemRecd(convertDate(tii.getReceivedDt()));
                oiiResponse.setItemRecdDateTo(convertDate(tii.getReceivedDt()));
                oiiResponse.setTransferDate(convertDate(tii.getTransferredDt()));
                oiiResponse.setTransferDateTo(convertDate(tii.getTransferredDt()));
                oiiResponse.setLastModified(convertDate(tii.getLastUpdateDt()));
                oiiResponse.setLastModifiedTo(convertDate(tii.getLastUpdateDt()));
                oiiResponse.setConfirmationReceiveDDt(convertDate(tii.getConfirmationReceivedDt()));
                oiiResponse.setConfirmationReceiveDDtTo(convertDate(tii.getConfirmationReceivedDt()));
                oiiResponse.setCloseDate(convertDate(tii.getCloseDt()));
                oiiResponse.setItemAcct(concatItemAcct(tii.getAdpAccountNumber(), tii.getAdpAccountType(), tii.getAdpAccountCheckDigit()));
                oiiResponse.setSec(tii.getOriginalAdpSecurityNumber());
                oiiResponse.setOriginalSec(tii.getOriginalAdpSecurityNumber());
                oiiResponse.setCusIp(tii.getOriginalCusIp());
                oiiResponse.setOriginalCusIp(tii.getOriginalCusIp());
                oiiResponse.setItemQty(tii.getOriginalQty());
                oiiResponse.setOriginalQuantity(tii.getOriginalQty());
                oiiResponse.setSecDesc(tii.getOriginalSecurityDescr());
                oiiResponse.setOriginalSecDesc(tii.getOriginalSecurityDescr());
                oiiResponse.setLastModifiedBy(tii.getLastUpdateName());
                oiiResponse.setIssueType("OI");
                responses.add(oiiResponse);
                addedTransferItemIds.add(tii.getTransferItemId());
            }

            if (tii.getIssue().size() > 0) {
                for (Issue issueCheck : tii.getIssue()) {
                    TransferItemResponse niiResponse = new TransferItemResponse();
                    niiResponse.setTransferItemId(issueCheck.getTransferItem().getTransferItemId());
                    niiResponse.setStatusCode(issueCheck.getTransferItem().getStatusCode());
                    niiResponse.setItemRecd(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niiResponse.setItemRecdDateTo(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niiResponse.setTransferDate(convertDate(issueCheck.getTransferItem().getTransferredDt()));
                    niiResponse.setTransferDateTo(convertDate(issueCheck.getTransferItem().getTransferredDt()));
                    niiResponse.setLastModified(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setLastModifiedTo(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setConfirmationReceiveDDt(convertDate(issueCheck.getTransferItem().getConfirmationReceivedDt()));
                    niiResponse.setConfirmationReceiveDDtTo(convertDate(issueCheck.getTransferItem().getConfirmationReceivedDt()));
                    niiResponse.setCloseDate(convertDate(issueCheck.getTransferItem().getCloseDt()));
                    niiResponse.setItemAcct(concatItemAcct(issueCheck.getTransferItem().getAdpAccountNumber(), issueCheck.getTransferItem().getAdpAccountType(), issueCheck.getTransferItem().getAdpAccountCheckDigit()));
                    niiResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niiResponse.setOriginalSec(issueCheck.getTransferItem().getOriginalAdpSecurityNumber());
                    niiResponse.setCusIp(issueCheck.getCusip());
                    niiResponse.setOriginalCusIp(issueCheck.getTransferItem().getOriginalCusIp());
                    niiResponse.setItemQty(issueCheck.getQuantity());
                    niiResponse.setOriginalQuantity(issueCheck.getTransferItem().getOriginalQty());
                    niiResponse.setSecDesc(issueCheck.getSecurityDescription());
                    niiResponse.setItemValue(issueCheck.getInsuranceValue());
                    niiResponse.setOriginalSecDesc(issueCheck.getTransferItem().getOriginalSecurityDescr());
                    niiResponse.setLastModifiedBy(issueCheck.getLastUpdateName());
                    niiResponse.setIssueType("NI");
                    niiResponse.setCheckAmount(issueCheck.getCheckAmount());
                    responses.add(niiResponse);
                }
            }
        }

        log.info("count of OTT transfer item {}", responses.size());
        return responses;
    }

    private List<TransferItemResponse> getCloseTransferItemDetails(String statusCode) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransferItemHistory> query = builder.createQuery(TransferItemHistory.class);
        Root<TransferItemHistory> root = query.from(TransferItemHistory.class);
        List<Predicate> predicates = new ArrayList<>();
        List<TransferItemResponse> responses = new ArrayList<>();
        List<TransferItemHistory> tiHis = transferItemHistoryRepository.findByStatusCode(statusCode);
        /* keep track of added transfer items*/
        Set<Integer> addedTransferItemIds = new HashSet<>();
        log.info("count of CLOS transfer item {}", tiHis.size());

        for (TransferItemHistory tii : tiHis) {
            /*check if transfer item has already been added*/
            if (!addedTransferItemIds.contains(tii.getTransferItemId())) {
                TransferItemResponse oiiResponse = new TransferItemResponse();
                oiiResponse.setTransferItemId(tii.getTransferItemId());
                oiiResponse.setStatusCode(tii.getStatusCode());
                oiiResponse.setBranchCode(tii.getAdpBranchCode());
                oiiResponse.setAltBranchCode(tii.getAltBranchCode());
                oiiResponse.setRepId(tii.getRepId());
                oiiResponse.setItemRecd(convertDate(tii.getReceivedDt()));
                oiiResponse.setItemRecdDateTo(convertDate(tii.getReceivedDt()));
                oiiResponse.setTransferDate(convertDate(tii.getTransferredDt()));
                oiiResponse.setTransferDateTo(convertDate(tii.getTransferredDt()));
                oiiResponse.setLastModified(convertDate(tii.getLastUpdateDt()));
                oiiResponse.setLastModifiedTo(convertDate(tii.getLastUpdateDt()));
                oiiResponse.setConfirmationReceiveDDt(convertDate(tii.getConfirmationReceivedDt()));
                oiiResponse.setConfirmationReceiveDDtTo(convertDate(tii.getConfirmationReceivedDt()));
                oiiResponse.setCloseDate(convertDate(tii.getCloseDt()));
                oiiResponse.setItemAcct(concatItemAcct(tii.getAdpAccountNumber(), tii.getAdpAccountType(), tii.getAdpAccountCheckDigit()));
                oiiResponse.setSec(tii.getOriginalAdpSecurityNumber());
                oiiResponse.setOriginalSec(tii.getOriginalAdpSecurityNumber());
                oiiResponse.setCusIp(tii.getOriginalCusIp());
                oiiResponse.setOriginalCusIp(tii.getOriginalCusIp());
                oiiResponse.setItemQty(tii.getOriginalQty());
                oiiResponse.setOriginalQuantity(tii.getOriginalQty());
                oiiResponse.setSecDesc(tii.getOriginalSecurityDescr());
                oiiResponse.setOriginalSecDesc(tii.getOriginalSecurityDescr());
                oiiResponse.setLastModifiedBy(tii.getLastUpdateName());
                oiiResponse.setIssueType("OI");
                TransferItemSearchFilter filter = new TransferItemSearchFilter();
                filter.setDispositionCode(tii.getDispositionCode());

                Predicate dispositionCodePredicate = getDispositionCodePredicate(filter, builder, root);

                if (dispositionCodePredicate != null) {
                    predicates.add(dispositionCodePredicate);
                }

                oiiResponse.setIssueType("OI");
                responses.add(oiiResponse);

                if (tii.getIssueHistory().size() > 0) {
                    for (IssueHistory issueCheck : tii.getIssueHistory()) {
                        TransferItemResponse niiResponse = new TransferItemResponse();
                        niiResponse.setTransferItemId(issueCheck.getTransferItemHistory().getTransferItemId());
                        niiResponse.setStatusCode(issueCheck.getTransferItemHistory().getStatusCode());
                        niiResponse.setBranchCode(issueCheck.getTransferItemHistory().getAdpBranchCode());
                        niiResponse.setAltBranchCode(issueCheck.getTransferItemHistory().getAltBranchCode());
                        niiResponse.setRepId(issueCheck.getTransferItemHistory().getRepId());
                        niiResponse.setItemRecd(convertDate(issueCheck.getTransferItemHistory().getReceivedDt()));
                        niiResponse.setItemRecdDateTo(convertDate(issueCheck.getTransferItemHistory().getReceivedDt()));
                        niiResponse.setTransferDate(convertDate(issueCheck.getTransferItemHistory().getTransferredDt()));
                        niiResponse.setTransferDateTo(convertDate(issueCheck.getTransferItemHistory().getTransferredDt()));
                        niiResponse.setLastModified(convertDate(issueCheck.getTransferItemHistory().getLastUpdateDt()));
                        niiResponse.setLastModifiedTo(convertDate(issueCheck.getTransferItemHistory().getLastUpdateDt()));
                        niiResponse.setConfirmationReceiveDDt(convertDate(issueCheck.getTransferItemHistory().getConfirmationReceivedDt()));
                        niiResponse.setConfirmationReceiveDDtTo(convertDate(issueCheck.getTransferItemHistory().getConfirmationReceivedDt()));
                        niiResponse.setCloseDate(convertDate(issueCheck.getTransferItemHistory().getCloseDt()));
                        niiResponse.setItemAcct(concatItemAcct(issueCheck.getTransferItemHistory().getAdpAccountNumber(), issueCheck.getTransferItemHistory().getAdpAccountType(), issueCheck.getTransferItemHistory().getAdpAccountCheckDigit()));
                        niiResponse.setOriginalSec(issueCheck.getTransferItemHistory().getOriginalAdpSecurityNumber());
                        niiResponse.setSec(issueCheck.getAdpSecurityNumber());
                        niiResponse.setCusIp(issueCheck.getCusip());
                        niiResponse.setOriginalCusIp(issueCheck.getTransferItemHistory().getOriginalCusIp());
                        niiResponse.setItemQty(issueCheck.getQuantity());
                        niiResponse.setOriginalQuantity(issueCheck.getTransferItemHistory().getOriginalQty());
                        niiResponse.setSecDesc(issueCheck.getSecurityDescription());
                        niiResponse.setOriginalSecDesc(issueCheck.getTransferItemHistory().getOriginalSecurityDescr());
                        niiResponse.setLastModifiedBy(issueCheck.getLastUpdateName());
                        niiResponse.setItemValue(issueCheck.getInsuranceValue());
                        niiResponse.setIssueType("NI");
                        niiResponse.setCheckAmount(issueCheck.getCheckAmount());
                        responses.add(niiResponse);
                    }
                }
            }

        }

        log.info("count of CLOS transfer item after get response {}  ", responses.size());
        return responses;
    }

    private Predicate getDispositionCodePredicate(TransferItemSearchFilter filter, CriteriaBuilder builder, Root<TransferItemHistory> root) {
        Expression<String> dispositionCodeExpression = root.get("dispositionCode");
        Predicate dispositionCodePredicate = builder.equal(dispositionCodeExpression, filter.getDispositionCode());

        if (filter.getDispositionCode().equals(Constants.CLOSE_TYPE_REJECTED)) {
            String rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_REJECTED, null, Locale.getDefault());
            filter.setCloseType(rejectedLabel);
            Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
            Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
            return rejectedLabelPredicate;
        }

        if (filter.getDispositionCode().equals(Constants.CLOSE_TYPE_ESCHEATED)) {
            String rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_ESCHEATED, null, Locale.getDefault());
            filter.setCloseType(rejectedLabel);
            Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
            Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
            return rejectedLabelPredicate;
        }

        if (filter.getDispositionCode().equals(Constants.CLOSE_TYPE_CONFISCATED)) {
            String rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_CONFISCATED, null, Locale.getDefault());
            filter.setCloseType(rejectedLabel);
            Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
            Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
            return rejectedLabelPredicate;
        }

        if (filter.getDispositionCode().equals(Constants.CLOSE_TYPE_NORMAL)) {
            String rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_NORMAL, null, Locale.getDefault());
            filter.setCloseType(rejectedLabel);
            Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
            Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
            return rejectedLabelPredicate;
        }

        if (filter.getDispositionCode().equals(Constants.CLOSE_TYPE_NOT_NORMAL)) {
            String rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_NOT_NORMAL, null, Locale.getDefault());
            filter.setCloseType(rejectedLabel);
            Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
            Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
            return rejectedLabelPredicate;
        }

        if (filter.getDispositionCode().equals(Constants.CLOSE_TYPE_WORTHLESS)) {
            String rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_WORTHLESS, null, Locale.getDefault());
            filter.setCloseType(rejectedLabel);
            Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
            Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
            return rejectedLabelPredicate;
        } else {
            return dispositionCodePredicate;
        }
    }

    public List<TransferItemResponse> getEntryTransferItemDetails(String statusCode) {
        TransferItemSearchFilter filter = null;
        List<TransferItemResponse> responses = new ArrayList<>();
        SqlArgs sqlArgs = new SqlArgs();
        Date lastEodRunDate = null;
        RegistrationInformation ri = registrationInformationRepository.findByRegistrationKey("DEF");
        RangeCriteriaDate dateCriteria = null;
        lastEodRunDate = ri.getEodEndDate();

        if (lastEodRunDate == null) {
            dateCriteria = new RangeCriteriaDate(new Date(), new Date());
        } else {
            dateCriteria = new RangeCriteriaDate(lastEodRunDate, new Date(), false);
        }

        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<TransferItem> criteriaQuery = criteriaBuilder.createQuery(TransferItem.class);
        Root<TransferItem> root = criteriaQuery.from(TransferItem.class);
        predicates.add(criteriaBuilder.or(
                criteriaBuilder.between(root.get("originalEntryDt"), dateCriteria.getFrom(), dateCriteria.getTo()),
                criteriaBuilder.between(root.get("issue").get("entryDate"), dateCriteria.getFrom(), dateCriteria.getTo()),
                criteriaBuilder.between(root.get("transferredDt"), dateCriteria.getFrom(), dateCriteria.getTo()),
                criteriaBuilder.between(root.get("closeDt"), dateCriteria.getFrom(), dateCriteria.getTo())
        ));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<TransferItem> query = em.createQuery(criteriaQuery);
        List<TransferItem> tiis = query.getResultList();
        log.info("count of DI transfer item {}", tiis.size());

        responses = new ArrayList<>();

        for (TransferItem tii : tiis) {
            TransferItemResponse oiiResponse = new TransferItemResponse();
            oiiResponse.setTransferItemId(tii.getTransferItemId());
            oiiResponse.setStatusCode(tii.getStatusCode());
            oiiResponse.setItemRecd(convertDate(tii.getReceivedDt()));
            oiiResponse.setTransferDate(convertDate(tii.getTransferredDt()));
            oiiResponse.setCloseDate(convertDate(tii.getCloseDt()));
            oiiResponse.setItemAcct(concatItemAcct(tii.getAdpAccountNumber(), tii.getAdpAccountType(), tii.getAdpAccountCheckDigit()));
            oiiResponse.setIssueType("OI");
            oiiResponse.setSec(tii.getOriginalAdpSecurityNumber());
            oiiResponse.setOriginalSec(tii.getOriginalAdpSecurityNumber());
            oiiResponse.setCusIp(formatCUSIP(tii.getOriginalCusIp()));
            oiiResponse.setOriginalCusIp(formatCUSIP(tii.getOriginalCusIp()));
            oiiResponse.setItemQty(tii.getOriginalQty());
            oiiResponse.setOriginalQuantity(tii.getOriginalQty());
            oiiResponse.setSecDesc(tii.getOriginalSecurityDescr());
            oiiResponse.setOriginalSecDesc(tii.getOriginalSecurityDescr());
            oiiResponse.setLastModified(convertDate(tii.getLastUpdateDt()));
            oiiResponse.setLastModifiedBy(tii.getLastUpdateName());
            responses.add(oiiResponse);

            if (tii.getIssue().size() > 0) {
                for (Issue issueCheck : tii.getIssue()) {
                    TransferItemResponse niiResponse = new TransferItemResponse();
                    niiResponse.setTransferItemId(issueCheck.getTransferItem().getTransferItemId());
                    niiResponse.setStatusCode(issueCheck.getTransferItem().getStatusCode());
                    niiResponse.setLastModified(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setLastModifiedTo(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setItemRecd(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niiResponse.setItemRecd(convertDate(issueCheck.getEntryDate()));
                    niiResponse.setItemRecd(convertDate(issueCheck.getCertificateDate()));
                    niiResponse.setItemAcct(concatItemAcct(issueCheck.getTransferItem().getAdpAccountNumber(), issueCheck.getTransferItem().getAdpAccountType(), issueCheck.getTransferItem().getAdpAccountCheckDigit()));
                    niiResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niiResponse.setOriginalSec(issueCheck.getTransferItem().getOriginalAdpSecurityNumber());
                    niiResponse.setCusIp(issueCheck.getCusip());
                    niiResponse.setOriginalCusIp(issueCheck.getTransferItem().getOriginalCusIp());
                    niiResponse.setItemQty(issueCheck.getQuantity());
                    niiResponse.setItemValue(issueCheck.getInsuranceValue());
                    niiResponse.setOriginalQuantity(issueCheck.getTransferItem().getOriginalQty());
                    niiResponse.setSecDesc(issueCheck.getSecurityDescription());
                    niiResponse.setOriginalSecDesc(issueCheck.getTransferItem().getOriginalSecurityDescr());
                    niiResponse.setLastModifiedBy(issueCheck.getLastUpdateName());
                    niiResponse.setIssueType("NI");
                    responses.add(niiResponse);
                }
            }
        }

        log.info("count {}", responses.size());
        return responses;
    }

    @Override

    public EditTransferItem findByTransportItemId(int id) throws TransferItemNotFoundException {
        TransferItem ti = transferItemRepository.findById(id);
        EditTransferItem eItem = null;

        if (ti == null) {
            eItem = findByCLOSItemId(id);
        } else {
            eItem = new EditTransferItem();
            eItem.setTransferItemId(ti.getTransferItemId());
            eItem.setLastUpdateName(ti.getLastUpdateName());

            if (ti.getTransferAgent() != null) {
                if (ti.getTransferAgent().getTransferAgentId() != null) {
                    log.debug("Transfer agent: {}", ti.getTransferAgent());
                    eItem.setTransferAgentId(ti.getTransferAgent().getTransferAgentId());
                    eItem.setTransferAgentName(ti.getTransferAgent().getTransferAgentName());
                } else {
                    log.debug("Transfer agent ID is null for TransferItem: {}", ti);
                    eItem.setTransferAgentId(0);
                    eItem.setTransferAgentName(null);
                }
            } else {
                log.debug("Transfer agent is null for TransferItem: {}", ti);
                eItem.setTransferAgentId(0);
                eItem.setTransferAgentName(null);
            }

            if (ti.getDeliveryInstruction() != null) {
                if (ti.getDeliveryInstruction().getDeliveryInstructionId() != null) {
                    log.debug("Transfer Delivery Instruction: {}", ti.getDeliveryInstruction());
                    DeliveryInstructionResponse deliveryResponse = new DeliveryInstructionResponse();
                    deliveryResponse.setDeliveryInstructionId(ti.getDeliveryInstruction().getDeliveryInstructionId());
                    deliveryResponse.setDeliveryInstructionName(ti.getDeliveryInstruction().getDeliveryInstructionName());
                    deliveryResponse.setLastUpdateName(ti.getDeliveryInstruction().getLastUpdateName());
                    deliveryResponse.setLastUpdateDt(ti.getDeliveryInstruction().getLastUpdateDt());
                    eItem.setDeliveryInstructionResponse(deliveryResponse);
                } else {
                    log.debug("Transfer Delivery Instruction ID is null for TransferItem: {}", ti);
                    eItem.setDeliveryInstructionId(null);
                }
            } else {
                log.debug("Transfer Delivery Instruction is null for TransferItem: {}", ti);
                eItem.setDeliveryInstructionId(null);
            }

            eItem.setStatusCode(ti.getStatusCode());
            eItem.setTransferItemId(ti.getTransferItemId());
            eItem.setAltBranchCode(ti.getAltBranchCode());
            eItem.setRepId(ti.getRepId());
            eItem.setItemAcct(concatItemAcct(ti.getAdpAccountNumber(), ti.getAdpAccountType(), ti.getAdpAccountCheckDigit()));
            eItem.setAcctTaxId(ti.getAccountTaxId());
            eItem.setDispositionCode(ti.getDispositionCode());
            eItem.setMailReceiptNumber(ti.getMailReceiptNumber());
            //eItem.setCloseType(ti.getDispositionCode());
            eItem.setControlId(ti.getControlId());
            eItem.setItemRecd(convertDate(ti.getReceivedDt()));
            eItem.setTransferDate(convertDate(ti.getTransferredDt()));
            eItem.setConfirmationSentDt(convertDate(ti.getConfirmationSentDt()));
            eItem.setConfirmationReceiveDDt(convertDate(ti.getConfirmationReceivedDt()));
            eItem.setDispositionDate(convertDate(ti.getDispositionDt()));
            eItem.setCloseDate(convertDate(ti.getCloseDt()));
            /* ................OriginalIssue................ */
            OriginalIssue oi = new OriginalIssue();
            oi.setOriginalAdpSecNo(ti.getOriginalAdpSecurityNumber());
            oi.setCusipNo(NumberFormater.formatAccountNumber(ti.getOriginalCusIp()));
            oi.setOriginalSecDesc(ti.getOriginalSecurityDescr());
            oi.setOriginalQty(ti.getOriginalQty());
            oi.setDenomination(ti.getOriginalDenominatorDescr());
            oi.setDleAccountNumberFormatted(formatAccountNumber(ti.getOriginalDleaAccountNumber(), ti.getOriginalDleaAccountType(), ti.getOriginalDleaAccountCheckDigit()));
            oi.setCseAccountNumberFormatted(formatAccountNumber(ti.getOriginalCseaAccountNumber(), ti.getOriginalCseaAccountType(), ti.getOriginalCseaAccountCheckDigit()));
            eItem.setOriginalIssue(oi);

            Set<NewIssueCash> newCase = new HashSet<>();
            Set<NewIssueSecurities> newSec = new HashSet<>();
            List<HistoryNotes> hisNote = new ArrayList<>();
            List<TransferAgentResponse> transAgent = new ArrayList<>();
            NewIssueRegistration nir = new NewIssueRegistration();

            if (ti.getIssue().size() > 0) {
                for (Issue is : ti.getIssue()) {
                    /*................New Issue - Registration................*/

                    nir.setDainInfo(ti.getDainFirmAccountNumber());
                    nir.setRegistrationDetails(ti.getRegistrationDescr());
                    nir.setRegistrationTax(ti.getRegistrationTaxId());
                    eItem.setNewIssueRegistrations(nir);

                    /* ................New Issue - Cash................. */
                    if (is.getCashEntryFlag() == 1) {
                        for (Issue issues : ti.getIssue()) {
                            NewIssueCash newIssueCash = new NewIssueCash();
                            newIssueCash.setIssueId(issues.getIssueID());
                            newIssueCash.setAmtRequested(issues.getInsuranceValue());
                            newIssueCash.setAmtReceived(issues.getCheckAmount());
                            newIssueCash.setNewIssueRecd(issues.getEntryDate());
                            newIssueCash.setCheckOrWire(issues.getCheckNumber());

                            /* Debit */
                            newIssueCash.setDebitLong(NumberFormater.formatCreditDebitNumber(issues.getDleAccountNumber(), issues.getDleAccountType(), issues.getDleAccountCheckDigit()));
                            /* Credit */
                            newIssueCash.setCreditShort(NumberFormater.formatCreditDebitNumber(issues.getCseAccountNumber(), issues.getCseAccountType(), issues.getCseAccountCheckDigit()));

                            newIssueCash.setBatchCode(issues.getBatchCode());
                            newIssueCash.setEntryCode(issues.getEntryCode());
                            newCase.add(newIssueCash);
                        }
                        /*................New Issue - Securities.................*/
                    }

                    if (is.getCashEntryFlag() == 0) {
                        for (Issue iss : ti.getIssue()) {
                            NewIssueSecurities issueSecurities = new NewIssueSecurities();
                            issueSecurities.setIssueId(iss.getIssueID());
                            issueSecurities.setNewSecurity(iss.getAdpSecurityNumber());
                            issueSecurities.setNewCusip(iss.getCusip());
                            issueSecurities.setNewSecDesc(iss.getSecurityDescription());
                            issueSecurities.setNewQuantity(iss.getQuantity());
                            issueSecurities.setNewCart(iss.getCertificateNumber());
                            issueSecurities.setNewInsurance(iss.getInsuranceValue());
                            issueSecurities.setNewDenomination(iss.getDenomination());

                            /* Debit */
                            issueSecurities.setNewDebitValue(NumberFormater.formatCreditDebitNumber(iss.getDleAccountNumber(), iss.getDleAccountType(), iss.getDleAccountCheckDigit()));
                            /* Credit */
                            issueSecurities.setNewCredValue(NumberFormater.formatCreditDebitNumber(iss.getCseAccountNumber(), iss.getCseAccountType(), iss.getCseAccountCheckDigit()));
                            issueSecurities.setNewBatchCode(iss.getBatchCode());
                            issueSecurities.setNewEntryCode(iss.getEntryCode());
                            issueSecurities.setNewCartDate(iss.getCertificateDate());
                            issueSecurities.setNewEntryDate(iss.getEntryDate());
                            issueSecurities.setLastUpdateName(iss.getLastUpdateName());
                            newSec.add(issueSecurities);
                        }
                    }

                }
            }
            /* ................History/Notes................ */
            if (!ti.getNotes().isEmpty()) {
                ti.getNotes().sort(Comparator.comparing(Notes::getCreatedDt));

                for (Notes ns : ti.getNotes()) {
                    HistoryNotes histNotes = new HistoryNotes();
                    histNotes.setNotId(ns.getNoteId());
                    histNotes.setEventCode(ns.getNotesEventCode());
                    histNotes.setLastUptTime(convertDate(ns.getLastNoteUpdateDt()));
                    histNotes.setLastUpUsername(ns.getLastNoteUpdateName());
                    histNotes.setNoteTxt(ns.getNotesTxt());
                    histNotes.setStsCode(ns.getNotesStsCode());
                    hisNote.add(histNotes);

                }

                if (ti.getTransferType() != null) {
                    if (ti.getTransferType().getTransferTypeCode() != null) {
                        log.debug("Transfer type: {}", ti.getTransferType());
                        TransferTypeResponse traDetails = new TransferTypeResponse();
                        traDetails.setTransferTypeCode(ti.getTransferType().getTransferTypeCode());
                        traDetails.setTransferTypeName(ti.getTransferType().getTransferTypeName());
                        traDetails.setLastUpdateName(ti.getTransferType().getLastUpdateName());
                        traDetails.setLastUpdateDate(ti.getTransferType().getLastUpdateDt());
                        traDetails.setTranCode(ti.getTransferType().getTranCode());
                        traDetails.setAutoRoadBlockFlag(ti.getBlockFlag());
                        eItem.setTransferTypeResponse(traDetails);

                    } else if (ti.getTransferType() == null) {
                        log.debug("Transfer type CODE is null for TransferItem: {}", ti);
                        eItem.setTransferTypeResponse(null); // or some other default value
                    }
                } else if (ti.getTransferType() == null) {
                    log.debug("Transfer type is null for TransferItem: {}", ti);
                    eItem.setTransferTypeResponse(null); // or some other default value
                }

                if (ti.getTransferAgent() != null) {
                    if (ti.getTransferAgent().getTransferAgentId() != null) {
                        log.debug("Transfer agent: {}", ti.getTransferAgent());
                        TransferAgentResponse agentResponse = new TransferAgentResponse();
                        agentResponse.setTransferAgentId(ti.getTransferAgent().getTransferAgentId());
                        agentResponse.setTransferAgentName(ti.getTransferAgent().getTransferAgentName());
                        agentResponse.setLastUpdateName(ti.getTransferAgent().getLastUpdateName());
                        agentResponse.setLastUpdateDt(ti.getTransferAgent().getLastUpdateDt());
                        agentResponse.setPhoneNumber(ti.getTransferAgent().getPhoneNumber());
                        eItem.setTransferAgentDetails(agentResponse);
                    } else {
                        log.debug("Transfer agent ID is null for TransferItem: {}", ti);
                        eItem.setTransferAgentId(0);
                        eItem.setTransferAgentName(null);
                    }
                } else {
                    log.debug("Transfer agent is null for TransferItem: {}", ti);
                    eItem.setTransferAgentId(0);
                    eItem.setTransferAgentName(null);
                }

            }

            eItem.setNewIssueRegistrations(nir);
            eItem.setHistoryNotes(hisNote);
            eItem.setNewIssueCash(new ArrayList<>(newCase));
            eItem.setNewIssueSecurities(new ArrayList<>(newSec));

        }

        if (eItem == null) {
            throw new TransferItemNotFoundException("Transfer Item Id Not Found", Constants.ERROR_CODE_UNKNOWN, "please provide valid Transfer Id");
        }

        return eItem;
    }

    @Override

    public List<TransferItemResponse> findAllTransportItem() {
        List<TransferItem> tis = transferItemRepository.findAll();
        List<TransferItemResponse> responsesAll = new ArrayList<>();

        for (TransferItem ti : tis) {
            TransferItemResponse oiResponse = new TransferItemResponse();
            oiResponse.setTransferItemId(ti.getTransferItemId());
            oiResponse.setItemRecd(convertDate(ti.getReceivedDt()));
            oiResponse.setTransferDate(convertDate(ti.getTransferredDt()));
            oiResponse.setCloseDate(convertDate(ti.getCloseDt()));
            oiResponse.setItemAcct(concatItemAcct(ti.getAdpAccountNumber(), ti.getAdpAccountType(), ti.getAdpAccountCheckDigit()));
            oiResponse.setIssueType("OI");
            oiResponse.setSec(ti.getOriginalAdpSecurityNumber());
            oiResponse.setOriginalSec(ti.getOriginalAdpSecurityNumber());
            oiResponse.setCusIp(formatCUSIP(ti.getOriginalCusIp()));
            oiResponse.setOriginalCusIp(formatCUSIP(ti.getOriginalCusIp()));
            oiResponse.setItemQty(ti.getOriginalQty());
            oiResponse.setOriginalQuantity(ti.getOriginalQty());
            oiResponse.setSecDesc(ti.getOriginalSecurityDescr());
            oiResponse.setOriginalSecDesc(ti.getOriginalSecurityDescr());
            oiResponse.setLastModified(convertDate(ti.getLastUpdateDt()));
            oiResponse.setLastModifiedBy(ti.getLastUpdateName());
            responsesAll.add(oiResponse);

            if (ti.getIssue().size() > 0) {
                for (Issue issueCheck : ti.getIssue()) {
                    TransferItemResponse niResponse = new TransferItemResponse();
                    niResponse.setTransferItemId(ti.getTransferItemId());
                    niResponse.setItemAcct(concatItemAcct(ti.getAdpAccountNumber(), ti.getAdpAccountType(), ti.getAdpAccountCheckDigit()));
                    niResponse.setIssueType("NI");
                    niResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niResponse.setOriginalSec(ti.getAdpAccountNumber());
                    niResponse.setCusIp(formatCUSIP(issueCheck.getCusip()));
                    niResponse.setOriginalCusIp(formatCUSIP(ti.getOriginalCusIp()));
                    niResponse.setItemQty(issueCheck.getQuantity());
                    niResponse.setOriginalQuantity(issueCheck.getTransferItem().getOriginalQty());
                    niResponse.setSecDesc(issueCheck.getSecurityDescription());
                    niResponse.setOriginalSecDesc(ti.getOriginalSecurityDescr());
                    niResponse.setItemValue(insurenceValue(issueCheck.getInsuranceValue()));
                    niResponse.setLastModified(convertDate(issueCheck.getLastUpdateDt()));
                    niResponse.setItemRecd(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niResponse.setItemRecd(convertDate(issueCheck.getEntryDate()));
                    niResponse.setItemRecd(convertDate(issueCheck.getCertificateDate()));
                    niResponse.setLastModifiedBy(issueCheck.getLastUpdateName());

                    responsesAll.add(niResponse);

                }
            }
        }

        return responsesAll;
    }

    @Override

    public List<TransferItemResponse> advanceCriteriaSearchForClos(TransferHistoryItemDto transferHistoryItemDto, String statusCode) {
        Specification<TransferItemHistory> specificationThi = Specification.where(new TransferItemHistorySearchClose(transferHistoryItemDto))
                .and((root, query, builder) -> builder.equal(root.get("statusCode"), statusCode));
        List<TransferItemHistory> itemsTh = transferItemHistoryRepository.findAll(specificationThi);
        List<TransferItemResponse> responsesClos = new ArrayList<>();

        for (TransferItemHistory thid : itemsTh) {
            TransferItemResponse response = new TransferItemResponse();
            response.setStatusCode(thid.getStatusCode());
            response.setTransferItemId(thid.getTransferItemId());
            response.setItemRecd(convertDate(thid.getReceivedDt()));
            response.setItemRecdDateTo(convertDate(thid.getReceivedDt()));
            response.setTransferDate(convertDate(thid.getTransferredDt()));
            response.setTransferDateTo(convertDate(thid.getTransferredDt()));
            response.setLastModified(convertDate(thid.getLastUpdateDt()));
            response.setLastModifiedTo(convertDate(thid.getLastUpdateDt()));
            response.setConfirmationReceiveDDt(convertDate(thid.getConfirmationReceivedDt()));
            response.setConfirmationReceiveDDtTo(convertDate(thid.getConfirmationReceivedDt()));
            response.setCloseDate(convertDate(thid.getCloseDt()));
            response.setItemAcct(concatItemAcct(thid.getAdpAccountNumber(), thid.getAdpAccountType(), thid.getAdpAccountCheckDigit()));
            response.setSec(thid.getOriginalAdpSecurityNumber());
            response.setOriginalSec(thid.getOriginalAdpSecurityNumber());
            response.setCusIp(formatCUSIP(thid.getOriginalCusIp()));
            response.setOriginalCusIp(formatCUSIP(thid.getOriginalCusIp()));
            response.setItemQty(thid.getOriginalQty());
            response.setOriginalQuantity(thid.getOriginalQty());
            response.setSecDesc(thid.getOriginalSecurityDescr());
            response.setOriginalSecDesc(thid.getOriginalSecurityDescr());
            response.setLastModified(convertDate(thid.getLastUpdateDt()));
            response.setLastModifiedBy(thid.getLastUpdateName());
            response.setIssueType("OI");
            responsesClos.add(response);

            if (thid.getIssueHistory().size() > 0) {
                for (IssueHistory issueCheck : thid.getIssueHistory()) {
                    TransferItemResponse niResponse = new TransferItemResponse();
                    niResponse.setStatusCode(thid.getStatusCode());
                    niResponse.setTransferItemId(thid.getTransferItemId());
                    niResponse.setItemAcct(concatItemAcct(thid.getAdpAccountNumber(), thid.getAdpAccountType(), thid.getAdpAccountCheckDigit()));
                    niResponse.setIssueType("NI");
                    niResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niResponse.setCusIp(formatCUSIP(issueCheck.getCusip()));
                    niResponse.setOriginalCusIp(formatCUSIP(thid.getOriginalCusIp()));
                    niResponse.setItemQty(issueCheck.getQuantity());
                    niResponse.setSecDesc(issueCheck.getSecurityDescription());
                    niResponse.setItemValue(insurenceValue(issueCheck.getInsuranceValue()));
                    niResponse.setLastModified(convertDate(thid.getLastUpdateDt()));
                    niResponse.setLastModifiedBy(thid.getLastUpdateName());
                    niResponse.setIssueEntryDate(issueCheck.getEntryDate());
                    niResponse.setStatusCode(thid.getStatusCode());
                    responsesClos.add(niResponse);
                }
            }
        }

        return responsesClos;
    }

    @Override

    public EditTransferItem findByCLOSItemId(int id) throws TransferItemNotFoundException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TransferItemHistory> query = builder.createQuery(TransferItemHistory.class);
        Root<TransferItemHistory> root = query.from(TransferItemHistory.class);
        List<Predicate> predicates = new ArrayList<>();

        EditTransferItem eItem = new EditTransferItem();
        TransferItemHistory itemHistories = transferItemHistoryRepository.findByTransferItemId(id);
        try {
            if (itemHistories != null && !itemHistories.getIssueHistory().isEmpty()) {
                for (IssueHistory ih : itemHistories.getIssueHistory()) {
                    eItem.setTransferItemId(ih.getTransferItemHistory().getTransferItemId());

                    TransferType transferType = transferTypeRepository.findById(ih.getTransferItemHistory().getTransferTypeCode()).orElse(null);

                    if (transferType != null) {
                        if (ih.getTransferItemHistory().getTransferTypeCode() != null) {
                            log.debug("Transfer type: {}", ih.getTransferItemHistory().getTransferTypeCode());
                            TransferTypeResponse traDetails = new TransferTypeResponse();
                            traDetails.setTransferTypeCode(transferType.getTransferTypeCode());
                            traDetails.setTransferTypeName(transferType.getTransferTypeName());
                            traDetails.setTranCode(transferType.getTranCode());
                            eItem.setTransferTypeResponse(traDetails);

                        } else {
                            log.debug("Transfer type CODE is null for TransferItem: {}", ih);
                            eItem.setTransferTypeResponse(null); // or some other default value
                        }
                    } else {
                        log.debug("Transfer type is null for TransferItem: {}", ih);
                        eItem.setTransferTypeResponse(null); // or some other default value
                    }

                    eItem.setStatusCode(ih.getTransferItemHistory().getStatusCode());
                    eItem.setTransferAgentName(ih.getTransferItemHistory().getTransferAgentAttentionName());
                    eItem.setDainFirmAccountNumber(ih.getTransferItemHistory().getDainFirmAccountNumber());
                    eItem.setAdpAccountType(ih.getTransferItemHistory().getAdpAccountType());
                    eItem.setAdpAccountCheckDigit(ih.getTransferItemHistory().getAdpAccountCheckDigit());
                    //set dispositionCode....
                    String dispositionCode = ih.getTransferItemHistory().getDispositionCode();
                    TransferItemSearchFilter filter = new TransferItemSearchFilter();
                    filter.setDispositionCode(dispositionCode);
                    Predicate dispositionCodePredicate = getDispositionCodePredicate(filter, builder, root);
                    String rejectedLabel = null;

                    if (dispositionCode != null && dispositionCode.equals("CLRJ")) {
                        rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_REJECTED, null, Locale.getDefault());
                        filter.setCloseType(rejectedLabel);
                        Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
                        Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
                        predicates.add(rejectedLabelPredicate);
                    }

                    if (dispositionCode != null && dispositionCode.equals("CLWL")) {
                        rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_WORTHLESS, null, Locale.getDefault());
                        filter.setCloseType(rejectedLabel);
                        Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
                        Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
                        predicates.add(rejectedLabelPredicate);
                    }

                    if (dispositionCode != null && dispositionCode.equals("CLNC")) {
                        rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_NORMAL, null, Locale.getDefault());
                        filter.setCloseType(rejectedLabel);
                        Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
                        Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
                        predicates.add(rejectedLabelPredicate);
                    }

                    if (dispositionCode != null && dispositionCode.equals("CLES")) {
                        rejectedLabel = messageSource.getMessage(Constants.LABEL_CLOSE_TYPE_ESCHEATED, null, Locale.getDefault());
                        filter.setCloseType(rejectedLabel);
                        Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
                        Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
                        predicates.add(rejectedLabelPredicate);
                    }

                    if (dispositionCode != null && dispositionCode.equals("CLNN")) {
                        rejectedLabel = messageSource.getMessage(Constants.CLOSE_TYPE_NOT_NORMAL, null, Locale.getDefault());
                        filter.setCloseType(rejectedLabel);
                        Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
                        Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
                        predicates.add(rejectedLabelPredicate);
                    }

                    if (dispositionCode != null && dispositionCode.equals("CLCF")) {
                        rejectedLabel = messageSource.getMessage(Constants.CLOSE_TYPE_CONFISCATED, null, Locale.getDefault());
                        filter.setCloseType(rejectedLabel);
                        Expression<String> rejectedLabelExpression = builder.literal(rejectedLabel);
                        Predicate rejectedLabelPredicate = builder.equal(rejectedLabelExpression, filter.getCloseType());
                        predicates.add(rejectedLabelPredicate);
                    } else if (dispositionCodePredicate != null) {
                        predicates.add(dispositionCodePredicate);
                    }

                    eItem.setDispositionCode(dispositionCode);
                    eItem.setCloseType(filter.getCloseType());
                    eItem.setDispositionDate(convertDate(ih.getTransferItemHistory().getDispositionDt()));
                    eItem.setReceivedDt(convertDate(ih.getTransferItemHistory().getReceivedDt()));
                    eItem.setTransferDate(convertDate(ih.getTransferItemHistory().getTransferredDt()));
                    eItem.setConfirmationReceiveDDt(convertDate(ih.getTransferItemHistory().getConfirmationReceivedDt()));
                    eItem.setConfirmationSentDt(convertDate(ih.getTransferItemHistory().getConfirmationSentDt()));
                    eItem.setAcctTaxId(ih.getTransferItemHistory().getAccountTaxId());
                    eItem.setRepId(ih.getTransferItemHistory().getRepId());
                    eItem.setTransferEffectiveDt(convertDate(ih.getTransferItemHistory().getTransferEffectiveDt()));
                    eItem.setRegistrationTaxId(ih.getTransferItemHistory().getRegistrationTaxId());
                    eItem.setRegistrationDescr(ih.getTransferItemHistory().getRegistrationDescr());
                    eItem.setMailReceiptNumber(ih.getTransferItemHistory().getMailReceiptNumber());
                    eItem.setAdpBranchCode(ih.getTransferItemHistory().getAdpBranchCode());
                    eItem.setAltBranchCode(ih.getTransferItemHistory().getAltBranchCode());
                    eItem.setCloseDate(convertDate(ih.getTransferItemHistory().getCloseDt()));
                    eItem.setFirmId(ih.getTransferItemHistory().getFirmId());
                    eItem.setOriginalSecurityDescr(ih.getTransferItemHistory().getOriginalSecurityDescr());
                    eItem.setOriginalCusIp(ih.getTransferItemHistory().getOriginalCusIp());
                    eItem.setOriginalQty(ih.getTransferItemHistory().getOriginalQty());
                    eItem.setOriginalDenominationDescr(ih.getTransferItemHistory().getOriginalDenominationDescr());
                    eItem.setOriginalEntryCode(ih.getTransferItemHistory().getOriginalEntryCode());
                    eItem.setOriginalEntryDt(convertDate(ih.getTransferItemHistory().getOriginalEntryDt()));
                    eItem.setOriginalDleAccountNumber(ih.getTransferItemHistory().getOriginalDleAccountNumber());
                    eItem.setOriginalDleAccountType(ih.getTransferItemHistory().getOriginalDleAccountType());
                    eItem.setOriginalDleAccountCheckDigit(ih.getTransferItemHistory().getOriginalDleAccountCheckDigit());
                    eItem.setOriginalCseAccountNumber(ih.getTransferItemHistory().getOriginalCseAccountNumber());
                    eItem.setOriginalCseAccountType(ih.getTransferItemHistory().getOriginalCseAccountType());
                    eItem.setOriginalCseAccountCheckDigit(ih.getTransferItemHistory().getOriginalCseAccountCheckDigit());
                    eItem.setLastUpdateName(ih.getTransferItemHistory().getLastUpdateName());
                    eItem.setLastUpdateDt(convertDate(ih.getTransferItemHistory().getLastUpdateDt()));
                    eItem.setTleTransferType(ih.getTransferItemHistory().getTleTransferType());
                    eItem.setGiftIndicator(ih.getTransferItemHistory().getGiftIndicator());
                    eItem.setControlId(ih.getTransferItemHistory().getControlId());
                    eItem.setLastUpdateRole(ih.getTransferItemHistory().getLastUpdateRole());

                }

            }
            TransferItemHistory traHis = transferItemHistoryRepository.findById(itemHistories.getTransferItemHistoryId()).orElse(null);

            if (traHis != null) {
                if (traHis.getTransferAgentName() != null) {
                    log.debug("Transfer Agent: {}", traHis.getTransferAgentName());
                    TransferAgentResponse traDetails = new TransferAgentResponse();
                    traDetails.setTransferAgentName(traHis.getTransferAgentName());
                    eItem.setTransferAgentDetails(traDetails);

                } else {
                    log.debug("Transfer Agent is null for TransferItem: {}", traHis);
                    eItem.setTransferAgentName(null);
                }
            } else {
                log.debug("Transfer Agent is null for TransferItem: {}", traHis);
                eItem.setTransferAgentName(null);
            }

            if (itemHistories.getDeliveryInstName() != null) {
                DeliveryInstructionResponse deliveryResponse = new DeliveryInstructionResponse();
                deliveryResponse.setDeliveryInstructionName(itemHistories.getDeliveryInstName());
                deliveryResponse.toDeliveryInstruction();
                eItem.setDeliveryInstructionResponse(deliveryResponse);
            } else {
                eItem.setDeliveryInstructionResponse(null);
            }

            eItem.setStatusCode(itemHistories.getStatusCode());
            eItem.setTransferItemId(itemHistories.getTransferItemId());
            eItem.setAltBranchCode(itemHistories.getAltBranchCode());
            eItem.setRepId(itemHistories.getRepId());
            eItem.setItemAcct(concatItemAcct(itemHistories.getAdpAccountNumber(), itemHistories.getAdpAccountType(), itemHistories.getAdpAccountCheckDigit()));
            eItem.setAcctTaxId(itemHistories.getAccountTaxId());
            eItem.setDispositionCode(itemHistories.getDispositionCode());
            eItem.setMailReceiptNumber(itemHistories.getMailReceiptNumber());
            eItem.setControlId(itemHistories.getControlId());
            eItem.setItemRecd(convertDate(itemHistories.getReceivedDt()));
            eItem.setTransferDate(convertDate(itemHistories.getTransferredDt()));
            eItem.setConfirmationSentDt(convertDate(itemHistories.getConfirmationSentDt()));
            eItem.setConfirmationReceiveDDt(convertDate(itemHistories.getConfirmationReceivedDt()));
            eItem.setDispositionDate(convertDate(itemHistories.getDispositionDt()));
            /* ................OriginalIssue................ */
            OriginalIssue oi = new OriginalIssue();
            oi.setOriginalAdpSecNo(itemHistories.getOriginalAdpSecurityNumber());
            oi.setCusipNo(NumberFormater.formatAccountNumber(itemHistories.getOriginalCusIp()));
            oi.setOriginalSecDesc(itemHistories.getOriginalSecurityDescr());
            oi.setOriginalQty(itemHistories.getOriginalQty());
            oi.setDenomination(itemHistories.getOriginalDenominationDescr());
            oi.setDleAccountNumberFormatted(formatAccountNumber(itemHistories.getOriginalDleAccountNumber(), itemHistories.getOriginalDleAccountType(), itemHistories.getOriginalDleAccountCheckDigit()));
            oi.setCseAccountNumberFormatted(formatAccountNumber(itemHistories.getOriginalCseAccountNumber(), itemHistories.getOriginalCseAccountType(), itemHistories.getOriginalCseAccountCheckDigit()));
            eItem.setOriginalIssue(oi);

            List<NewIssueCash> newCase = new ArrayList<>();
            List<NewIssueSecurities> newSec = new ArrayList<>();
            List<HistoryNotes> hisNote = new ArrayList<>();
            NewIssueRegistration nir = new NewIssueRegistration();

            if (itemHistories.getIssueHistory().size() > 0) {
                for (IssueHistory is : itemHistories.getIssueHistory()) {
                    /*................New Issue - Registration................*/

                    nir.setDainInfo(itemHistories.getDainFirmAccountNumber());
                    nir.setRegistrationDetails(itemHistories.getRegistrationDescr());
                    nir.setRegistrationTax(itemHistories.getRegistrationTaxId());
                    eItem.setNewIssueRegistrations(nir);

                    /* ................New Issue - Cash................. */
                    if (is.getCashEntryFlag() == 1) {
                        for (IssueHistory issues : itemHistories.getIssueHistory()) {
                            NewIssueCash newIssueCash = new NewIssueCash();
                            newIssueCash.setIssueId(issues.getIssueHistoryId());
                            newIssueCash.setAmtRequested(issues.getInsuranceValue());
                            newIssueCash.setAmtReceived(issues.getCheckAmount());
                            newIssueCash.setNewIssueRecd(issues.getEntryDate());
                            newIssueCash.setCheckOrWire(issues.getCheckNumber());

                            /* Debit */
                            newIssueCash.setDebitLong(NumberFormater.formatCreditDebitNumber(issues.getDleAccountNumber(), issues.getDleAccountType(), issues.getDleAccountCheckDigit()));
                            /* Credit */
                            newIssueCash.setCreditShort(NumberFormater.formatCreditDebitNumber(issues.getCseAccountNumber(), issues.getCseAccountType(), issues.getCseAccountCheckDigit()));

                            newIssueCash.setBatchCode(issues.getBatchCode());
                            newIssueCash.setEntryCode(issues.getEntryCode());
                            newCase.add(newIssueCash);
                        }
                    } else if (is.getCashEntryFlag() == 0) {
                        /*
                         * ................New Issue - Securities.................
                         */
                        for (IssueHistory iss : itemHistories.getIssueHistory()) {
                            NewIssueSecurities issueSecurities = new NewIssueSecurities();
                            issueSecurities.setIssueId(iss.getIssueHistoryId());
                            issueSecurities.setNewSecurity(iss.getAdpSecurityNumber());
                            issueSecurities.setNewCusip(iss.getCusip());
                            issueSecurities.setNewSecDesc(iss.getSecurityDescription());
                            issueSecurities.setNewQuantity(iss.getQuantity());
                            issueSecurities.setNewCart(iss.getCertificateNumber());
                            issueSecurities.setNewInsurance(iss.getInsuranceValue());
                            issueSecurities.setNewDenomination(iss.getDenomination());

                            /* Debit */
                            issueSecurities.setNewDebitValue(NumberFormater.formatCreditDebitNumber(iss.getDleAccountNumber(), iss.getDleAccountType(), iss.getDleAccountCheckDigit()));
                            /* Credit */
                            issueSecurities.setNewCredValue(NumberFormater.formatCreditDebitNumber(iss.getCseAccountNumber(), iss.getCseAccountType(), iss.getCseAccountCheckDigit()));
                            issueSecurities.setNewBatchCode(iss.getBatchCode());
                            issueSecurities.setNewEntryCode(iss.getEntryCode());
                            issueSecurities.setNewCartDate(iss.getCertificateDate());
                            issueSecurities.setNewEntryDate(iss.getEntryDate());
                            newSec.add(issueSecurities);
                        }
                    }

                }

            }

            if (!itemHistories.getNotesHistories().isEmpty()) {
                itemHistories.getNotesHistories().sort(Comparator.comparing(NoteHistory::getLastUpdateDt));

                for (NoteHistory ns : itemHistories.getNotesHistories()) {
                    HistoryNotes histNotes = new HistoryNotes();
                    histNotes.setNotId(ns.getNoteHistoryId());
                    histNotes.setEventCode(ns.getEventCode());
                    histNotes.setLastUptTime(convertDate(ns.getLastUpdateDt()));
                    histNotes.setLastUpUsername(ns.getLastUpdateName());
                    histNotes.setNoteTxt(ns.getNote());
                    histNotes.setStsCode(ns.getStatusCode());
                    hisNote.add(histNotes);

                }
            }

            eItem.setNewIssueRegistrations(nir);
            eItem.setNewIssueCash(newCase);
            eItem.setHistoryNotes(hisNote);
            eItem.setNewIssueSecurities(newSec);

            return eItem;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("TransferItemHistory", "transferItemHistoryId", itemHistories.getTransferItemId());
        }

    }

    @Override

    public TransferItem getTransferItemById(Integer transferItemId) {
        return transferItemRepository.findById(transferItemId).orElseThrow(() -> new ResourceNotFoundException("TransferItem", "id", transferItemId));
    }

    @Override

    public void saveTransferItem(TransferItem transferItem, EditTransferItem editTransferItem, String user) {
        List<Issue> newSec = new ArrayList<>();
        List<Issue> newIssueCase = new ArrayList<>();
        String itemAcct = editTransferItem.getItemAcct();
        String dleAccount = editTransferItem.getOriginalIssue().getDleAccountNumberFormatted();
        String cseAccount = editTransferItem.getOriginalIssue().getCseAccountNumberFormatted();
        String cusIpNumber = editTransferItem.getOriginalIssue().getCusipNo();
        try {
            transferItem.setLastUpdateName(transferItem.getLastUpdateName());
            transferItem.setStatusCode(editTransferItem.getStatusCode());
            transferItem.setTransferItemId(editTransferItem.getTransferItemId());
            transferItem.setAltBranchCode(editTransferItem.getAltBranchCode());
            transferItem.setRepId(editTransferItem.getRepId());
            //set CUSIP Number remove "-" before saving
            setCusIpNumber(cusIpNumber, transferItem);
            transferItem.setAccountTaxId(editTransferItem.getAcctTaxId());
            transferItem.setDispositionCode(editTransferItem.getDispositionCode());
            transferItem.setMailReceiptNumber(editTransferItem.getMailReceiptNumber());
            transferItem.setDispositionCode(editTransferItem.getCloseType());
            transferItem.setControlId(editTransferItem.getControlId());
            //transferItem.setDispositionDt(new Date());
            // set the Value for transferItem{setAdpAccountNumber,setAdpAccountType,setAdpAccountCheckDigit}
            setTransferItemAdpAccountValues(itemAcct, transferItem);
            transferItem.setAccountTaxId(editTransferItem.getAccountTaxId());
            transferItem.setAltBranchCode(editTransferItem.getAltBranchCode());
            transferItem.setAdpBranchCode(editTransferItem.getAdpBranchCode());
            transferItem.setFirmId(editTransferItem.getFirmId());
            transferItem.setRepId(editTransferItem.getRepId());
            //transferItem.setReceivedDt(new Date());
            //transferItem.setTransferredDt(new Date());
            //transferItem.setConfirmationSentDt(new Date());
            //transferItem.setConfirmationReceivedDt(new Date());
            //transferItem.setDispositionDt(new Date());
            //Get the TransferAgent and Associated TransferAgent
            TransferAgent transferAgent = new TransferAgent();

            if (editTransferItem.getTransferAgentDetails() != null && editTransferItem.getTransferAgentDetails().getTransferAgentId() != null) {
                transferAgent = transferAgentDao.getTransferAgentById(editTransferItem.getTransferAgentDetails().getTransferAgentId());

                transferAgentDao.updateTransferAgentProperties(transferAgent, editTransferItem);

                // Get the currently associated agent, if any...
                TransferAgent associatedAgent = transferAgentDao.getAssociatedTransferAgent(transferItem.getOriginalAdpSecurityNumber());

                if (associatedAgent != null) {
                    transferAgentDao.saveAndAssociateTransferAgent(transferAgent, editTransferItem.getOriginalIssue().getOriginalAdpSecNo(), editTransferItem.getLastUpdateName());

                }
            }

            //Get the TransferType
            if (editTransferItem.getTransferTypeResponse() != null && editTransferItem.getTransferTypeResponse().getTransferTypeCode() != null) {
                //for set TransferType Details
                TransferType transferTypeByCode =
                        transferTypeDao.getTransferTypeByCode(editTransferItem.getTransferTypeResponse().getTransferTypeCode());
                TransferType setTransferTypeProperties =
                        transferTypeDao.updateTransferTypeProperties(transferTypeByCode,
                                editTransferItem);

                //Set TransferItem Details Using TransferType
                //transferItem.setTransferType(transferTypeByCode);
                transferItem.setTransferType(setTransferTypeProperties);

            }
            //Set Delivery Instruction Details
            DeliveryInstruction deliveryInstruction = new DeliveryInstruction();

            if (editTransferItem.getDeliveryInstructionResponse().getDeliveryInstructionName() != null && editTransferItem.getDeliveryInstructionResponse().getDeliveryInstructionId() != 0) {
                deliveryInstruction = deliveryInstructionDAO.getDeliveryInstructionDetails(deliveryInstruction, editTransferItem);
                transferItem.setDeliveryInstruction(deliveryInstruction);
            }
            /* ................OriginalIssue................ */
            transferItem.setOriginalAdpSecurityNumber(editTransferItem.getOriginalIssue().getOriginalAdpSecNo());
            transferItem.setOriginalDenominatorDescr(editTransferItem.getOriginalIssue().getDenomination());
            setOriginalDleaAccountNumberDetails(dleAccount, transferItem);
            setOriginalCseAccountNumberDetails(cseAccount, transferItem);

            if (transferItem.getIssue().size() > 0) {
                for (Issue is : transferItem.getIssue()) {
                    /*
                     * ................New Issue - Registration................
                     */
                    transferItem.setDainFirmAccountNumber(editTransferItem.getNewIssueRegistrations().getDainInfo());
                    transferItem.setRegistrationDescr(editTransferItem.getNewIssueRegistrations().getRegistrationDetails());
                    transferItem.setAccountTaxId(editTransferItem.getNewIssueRegistrations().getRegistrationTax());
                    is.setTransferItem(transferItem);
                    /* ................New Issue - Cash................. */
                    if (is.getCashEntryFlag() == 1) {
                        for (Issue iss : transferItem.getIssue()) {
                            Issue saveIssue = issueDao.saveNewIssueCash(is.getIssueID(), editTransferItem);
                            newIssueCase.add(saveIssue);
                        }

                    } else if (is.getCashEntryFlag() == 0) {
                        /*
                         * ................New Issue - Securities.................
                         */
                        for (Issue iss : transferItem.getIssue()) {
                            issueDao.saveNewIssueSecurities(is.getIssueID(), editTransferItem);
                            newSec.add(iss);
                        }
                    }
                }

                transferItem.setIssue(newIssueCase);
                transferItem.setIssue(newSec);
            }

            /* ...............History Notes............ */
            // loop through each history note and save it to the database
            for (Iterator<Notes> i = transferItem.getNotes().iterator(); i.hasNext(); ) {
                Notes note = i.next();

                if (note.getLastNoteUpdateName() == null || note.getLastNoteUpdateName().trim().isEmpty()) {
                    note.setLastNoteUpdateName(editTransferItem.getLastUpdateName());

                }
            }

            transferItem.setStatusCode(editTransferItem.getStatusCode());
            transferItem.setAdpBranchCode(editTransferItem.getAdpBranchCode());
            transferItem.setLastUpdateName(editTransferItem.getLastUpdateName());
            transferItem.setTransferAgent(transferAgent);

            //transferItem.setDispositionDt(new Date());
            //transferItem.setTransferredDt(new Date());
            transferItemRepository.save(transferItem);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the transfer item", e);
        }
    }


    public void setTransferItemAdpAccountValues(String itemAcct, TransferItem transferItem) {
        // split the itemAcct value into three parts using the '-' delimiter
        String[] parts = itemAcct.split("-");

        // set the three parts in the transferItem object
        transferItem.setAdpAccountNumber(parts[0]);
        transferItem.setAdpAccountType(parts[1]);
        transferItem.setAdpAccountCheckDigit(parts[2]);
    }

    public String convertStringToDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            Date dateIn = dateFormatterWithOutTime.parse(dateString);
            return dateFormatterWithOutTime.format(dateIn);
        } catch (ParseException e) {
            // Handle the parsing exception as required
            return null;
        }
    }

    @Override

    public void addIssueGeneratedNote(EditTransferItem itemIn, Issue orgIssueIn, String userIn) {
        String statusLabel = RbcUtil.getMessage("label.status." + itemIn.getStatusCode());
        String entryType;
        String longDebit;
        String shortCredit;

        if (orgIssueIn == null) {
            entryType = "original issue";
            longDebit = itemIn.getOriginalIssue().getDleAccountNumberFormatted();
            shortCredit = itemIn.getOriginalIssue().getCseAccountNumberFormatted();
        } else {
            longDebit = orgIssueIn.getDleAccountNumber();
            shortCredit = orgIssueIn.getCseAccountNumber();
            entryType = itemIn.getOriginalIssue().isCashEntry() ? "cash" : "security";
        }

        try {
            if (itemIn.isAccountFreeze()) {
                String msg = "Account is frozen";
                itemIn.addNote(Constants.NOTE_EVENT_ENTRY_GENERATED, msg, userIn);
            } else {
                String msg = String.format("Bookkeeping entry created for %s to %s status. Long (debit) is: %s Short (credit) is: %s", entryType, statusLabel, longDebit, shortCredit);
                itemIn.addNote(Constants.NOTE_EVENT_ENTRY_GENERATED, msg, userIn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertDate(Date dateIn) {
        String retVal = "";
        boolean includeTime = false;

        if (dateIn != null) {
            if (includeTime == true) {
                retVal = dateFormatterWithOutTime.format(dateIn);
            } else {
                retVal = dateFormatterWithOutTime.format(dateIn);
            }
        }

        return retVal;
    }

    private BigDecimal insurenceValue(BigDecimal data) {
        if (data != null) {
            BigDecimal rounded = data.setScale(2, RoundingMode.HALF_UP);
            return rounded;
        }
        return null;
    }

    private String concatItemAcct(String adpAccountNumber, String adpAccountType, String adpAccountCheckDigit) {
        String itemAcct = null;

        if (adpAccountNumber != null && adpAccountType != null && adpAccountCheckDigit != null) {
            itemAcct = adpAccountNumber + "-" + adpAccountType + "-" + adpAccountCheckDigit;

        }

        return itemAcct;
    }

    public static String formatCUSIP(String cusip) {
        String formattedCusip = cusip;

        if (StringUtils.isNotBlank(cusip) && cusip.length() == Constants.LENGTH_CUSIP) {
            formattedCusip = cusip.substring(0, 6) + "-" + cusip.substring(6, 8) + "-" + cusip.substring(8);
        }

        if (StringUtils.isNotBlank(cusip) && cusip.length() == Constants.LENGTH_CUSIIP) {
            formattedCusip = cusip.substring(0, 6) + "-" + cusip.substring(6, 9) + "-" + cusip.substring(9);
        }

        if (StringUtils.isNotBlank(cusip) && cusip.length() <= Constants.LENGTH_CUSIP) {
            String[] splitTimeStamp = formattedCusip.split("");
            Arrays.setAll(splitTimeStamp, index -> splitTimeStamp[index].replaceAll("[\\s\\-()]", ""));
            formattedCusip = cusip.substring(0, 7);
        }

        return formattedCusip;
    }

    public void setOriginalDleaAccountNumberDetails(String originalDleaAccount, TransferItem transferItem) {
        // Remove all '-' characters from the original account number
        String dleAccountNumber = originalDleaAccount.replaceAll("-", "");

        // Extract the first 8 digits of the account number
        String accountNumberPrefix = dleAccountNumber.substring(0, 8);

        // Extract the account type (9th digit)
        String accountType = dleAccountNumber.substring(8, 9);

        // Extract the check digit (10th digit)
        String checkDigit = dleAccountNumber.substring(9, 10);

        transferItem.setOriginalDleaAccountNumber(accountNumberPrefix);
        transferItem.setOriginalDleaAccountType(accountType);
        transferItem.setOriginalDleaAccountCheckDigit(checkDigit);
    }

    public void setOriginalCseAccountNumberDetails(String originalCseAcct, TransferItem transferItem) {
        // split the itemAcct value into three parts using the '-' delimiter
        String cseAccountNumber = originalCseAcct.replaceAll("-", "");

        // Extract the first 8 digits of the account number
        String accountNumberPrefix = cseAccountNumber.substring(0, 8);

        // Extract the account type (9th digit)
        String accountType = cseAccountNumber.substring(8, 9);

        // Extract the check digit (10th digit)
        String checkDigit = cseAccountNumber.substring(9, 10);

        transferItem.setOriginalCseaAccountNumber(accountNumberPrefix);
        transferItem.setOriginalCseaAccountType(accountType);
        transferItem.setOriginalCseaAccountCheckDigit(checkDigit);
    }

    public void setCusIpNumber(String originalCusIpNumber, TransferItem transferItem) {
        String cusIpNumber = originalCusIpNumber.replaceAll("-", "");
        transferItem.setOriginalCusIp(cusIpNumber);
    }

    public static String formatAccountNumber(String accountNumberIn, String accountType, String checkDigit) {
        String accountNumber = accountNumberIn;

        if (accountType != null) {
            accountNumber += accountType;
        } else {
            accountNumber += " ";
        }

        if (checkDigit != null) {
            accountNumber += checkDigit;
        } else {
            accountNumber += " ";
        }

        String formattedAccountNumber = accountNumber;

        if (accountNumber != null) {
            if (accountNumber.length() == 12) {
                // dain format
                formattedAccountNumber = accountNumber.substring(0, 4) + "-" + accountNumber.substring(4, 8) + "-" + accountNumber.substring(8);
            } else if (accountNumber.length() == 10) {
                // adp format
                formattedAccountNumber = accountNumber.substring(0, 3) + "-" + accountNumber.substring(3, 8) + "-" + accountNumber.substring(8, 9) + "-" + accountNumber.substring(9);
            }
        }

        return formattedAccountNumber;
    }

    public List<TransferItemResponse> search(TransferItemDto transferItemDto, String statusCode) {
        Specification<TransferItem> specificationTi = Specification.where(new TransferItemSearchPending(transferItemDto))
                .and((root, query, builder) -> builder.equal(root.get("statusCode"), statusCode));
        List<TransferItem> items = transferItemRepository.findAll(specificationTi);

        List<TransferItemResponse> responsesAll = new ArrayList<>();

        if (statusCode.equalsIgnoreCase(Constants.STATUS_PENDING) || statusCode.equalsIgnoreCase(Constants.STATUS_OUT_TO_TRANSFER)) {
            responsesAll = getTransferItems(items);
        }
        return responsesAll;
    }

    @Override
    public List<TransferItemResponse> advanceCriteriaSearch(TransferItemDto transferItemDto, String statusCode) {
        Specification<TransferItem> specificationTi = Specification.where(new TransferItemSearchPending(transferItemDto))
                .and((root, query, builder) -> builder.equal(root.get("statusCode"), statusCode));
        List<TransferItem> items = transferItemRepository.findAll(specificationTi);

        List<TransferItemResponse> responsesAll = new ArrayList<>();

        for (TransferItem ti : items) {
            if (Constants.STATUS_PENDING.equalsIgnoreCase(ti.getStatusCode())) {
                TransferItemResponse response = new TransferItemResponse();
                response.setTransferItemId(ti.getTransferItemId());
                response.setStatusCode(ti.getStatusCode());
                response.setItemAcct(ti.getAdpAccountNumber());
                response.setItemRecd(convertDate(ti.getReceivedDt()));
                response.setItemRecdDateTo(convertDate(ti.getReceivedDt()));
                response.setTransferDate(convertDate(ti.getTransferredDt()));
                response.setTransferDateTo(convertDate(ti.getTransferredDt()));
                response.setLastModified(convertDate(ti.getLastUpdateDt()));
                response.setLastModifiedTo(convertDate(ti.getLastUpdateDt()));
                response.setConfirmationReceiveDDt(convertDate(ti.getConfirmationReceivedDt()));
                response.setConfirmationReceiveDDtTo(convertDate(ti.getConfirmationReceivedDt()));
                response.setCloseDate(convertDate(ti.getCloseDt()));
                response.setSecDesc(ti.getOriginalSecurityDescr());
                response.setRegistrationDescr(ti.getRegistrationDescr());
                response.setItemAcct(concatItemAcct(ti.getAdpAccountNumber(), ti.getAdpAccountType(), ti.getAdpAccountCheckDigit()));
                response.setSec(ti.getOriginalAdpSecurityNumber());
                response.setOriginalSec(ti.getOriginalAdpSecurityNumber());
                response.setCusIp(ti.getOriginalCusIp());
                response.setOriginalCusIp(ti.getOriginalCusIp());
                response.setItemQty(ti.getOriginalQty());
                response.setOriginalQuantity(ti.getOriginalQty());
                response.setSecDesc(ti.getOriginalSecurityDescr());
                response.setOriginalSecDesc(ti.getOriginalSecurityDescr());
                response.setLastModifiedBy(ti.getLastUpdateName());
                response.setLastModified(convertDate(ti.getLastUpdateDt()));
                response.setIssueType("OI");
                responsesAll.add(response);
            }

            if (ti.getIssue().size() > 0) {
                for (Issue issueCheck : ti.getIssue()) {
                    TransferItemResponse niResponse = new TransferItemResponse();
                    niResponse.setTransferItemId(issueCheck.getTransferItem().getTransferItemId());
                    niResponse.setIssueId(issueCheck.getIssueID());
                    niResponse.setStatusCode(issueCheck.getTransferItem().getStatusCode());
                    niResponse.setItemAcct(issueCheck.getTransferItem().getAdpAccountNumber());
                    niResponse.setItemRecd(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niResponse.setItemRecd(convertDate(issueCheck.getEntryDate()));
                    niResponse.setItemRecd(convertDate(issueCheck.getCertificateDate()));
                    niResponse.setLastModified(convertDate(issueCheck.getLastUpdateDt()));
                    niResponse.setRegistrationDescr(issueCheck.getTransferItem().getRegistrationDescr());
                    niResponse.setItemAcct(concatItemAcct(issueCheck.getTransferItem().getAdpAccountNumber(), issueCheck.getTransferItem().getAdpAccountType(), issueCheck.getTransferItem().getAdpAccountCheckDigit()));
                    niResponse.setIssueType("NI");
                    niResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niResponse.setOriginalSec(issueCheck.getTransferItem().getOriginalAdpSecurityNumber());
                    niResponse.setCusIp(formatCUSIP(issueCheck.getCusip()));
                    niResponse.setOriginalCusIp(formatCUSIP(issueCheck.getTransferItem().getOriginalCusIp()));
                    niResponse.setItemQty(issueCheck.getQuantity());
                    niResponse.setOriginalQuantity(issueCheck.getTransferItem().getOriginalQty());
                    niResponse.setOriginalSecDesc(issueCheck.getTransferItem().getOriginalSecurityDescr());
                    niResponse.setItemValue(insurenceValue(issueCheck.getInsuranceValue()));
                    niResponse.setLastModifiedBy(issueCheck.getTransferItem().getLastUpdateName());
                    responsesAll.add(niResponse);
                }
            }
        }
        List<TransferItemResponse> closeTransferItemResponse = new ArrayList<>();

        for (TransferItem ti : items) {
            if (Constants.STATUS_CLOSED.equalsIgnoreCase(ti.getStatusCode())) {
                TransferHistoryItemDto thDto = new TransferHistoryItemDto();
                TransferHistoryItemDto transferHistoryItemDto = thDto.mapTransferItemToTransferHistoryItemDto(ti);
                closeTransferItemResponse.addAll(searchForCloseTransferItemDetails(transferHistoryItemDto));
            }
        }

        responsesAll.addAll(closeTransferItemResponse);

        List<TransferItemResponse> ottTransferItemResponse = new ArrayList<>();

        for (TransferItem ti : items) {
            if (Constants.STATUS_OUT_TO_TRANSFER.equalsIgnoreCase(ti.getStatusCode())) {
                TransferItemDto ottDto = new TransferItemDto();
                ottTransferItemResponse.addAll(searchForOttTransferItemDetails(ottDto));
            }
        }

        responsesAll.addAll(ottTransferItemResponse);

        return responsesAll;

    }

    //@Cacheable(value = "transferItemCache", key = "#transferItemDto.toString()")
    public List<TransferItemResponse> searchForOttTransferItemDetails(TransferItemDto transferItemDto) {
        Specification<TransferItem> specificationTi = new TransferItemSearchOTT(transferItemDto);
        List<TransferItem> items = transferItemRepository.findAll(specificationTi);

        List<TransferItemResponse> responsesAll = new ArrayList<>();

        for (TransferItem ti : items) {
            if (Constants.STATUS_OUT_TO_TRANSFER.equalsIgnoreCase(ti.getStatusCode())) {
                TransferItemResponse response = new TransferItemResponse();
                response.setTransferItemId(ti.getTransferItemId());
                response.setStatusCode(ti.getStatusCode());
                response.setItemRecd(convertDate(ti.getReceivedDt()));
                response.setItemRecdDateTo(convertDate(ti.getReceivedDt()));
                response.setTransferDate(convertDate(ti.getTransferredDt()));
                response.setTransferDateTo(convertDate(ti.getTransferredDt()));
                response.setLastModified(convertDate(ti.getLastUpdateDt()));
                response.setLastModifiedTo(convertDate(ti.getLastUpdateDt()));
                response.setConfirmationReceiveDDt(convertDate(ti.getConfirmationReceivedDt()));
                response.setConfirmationReceiveDDtTo(convertDate(ti.getConfirmationReceivedDt()));
                response.setCloseDate(convertDate(ti.getCloseDt()));
                response.setItemAcct(concatItemAcct(ti.getAdpAccountNumber(), ti.getAdpAccountType(), ti.getAdpAccountCheckDigit()));
                response.setSec(ti.getOriginalAdpSecurityNumber());
                response.setOriginalSec(ti.getOriginalAdpSecurityNumber());
                response.setCusIp(ti.getOriginalCusIp());
                response.setOriginalCusIp(ti.getOriginalCusIp());
                response.setItemQty(ti.getOriginalQty());
                response.setOriginalQuantity(ti.getOriginalQty());
                response.setSecDesc(ti.getOriginalSecurityDescr());
                response.setOriginalSecDesc(ti.getOriginalSecurityDescr());
                response.setLastModifiedBy(ti.getLastUpdateName());
                response.setLastModified(convertDate(ti.getLastUpdateDt()));
                response.setIssueType("OI");
                responsesAll.add(response);
            }

            if (ti.getIssue().size() > 0) {
                for (Issue issueCheck : ti.getIssue()) {
                    TransferItemResponse niResponse = new TransferItemResponse();
                    niResponse.setTransferItemId(issueCheck.getTransferItem().getTransferItemId());
                    niResponse.setIssueId(issueCheck.getIssueID());
                    niResponse.setStatusCode(issueCheck.getTransferItem().getStatusCode());
                    niResponse.setItemAcct(issueCheck.getTransferItem().getAdpAccountNumber());
                    niResponse.setItemRecd(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niResponse.setItemRecd(convertDate(issueCheck.getEntryDate()));
                    niResponse.setItemRecd(convertDate(issueCheck.getCertificateDate()));
                    niResponse.setLastModified(convertDate(issueCheck.getLastUpdateDt()));
                    niResponse.setRegistrationDescr(issueCheck.getTransferItem().getRegistrationDescr());
                    niResponse.setItemAcct(concatItemAcct(issueCheck.getTransferItem().getAdpAccountNumber(), issueCheck.getTransferItem().getAdpAccountType(), issueCheck.getTransferItem().getAdpAccountCheckDigit()));
                    niResponse.setIssueType("NI");
                    niResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niResponse.setOriginalSec(issueCheck.getTransferItem().getOriginalAdpSecurityNumber());
                    niResponse.setCusIp(formatCUSIP(issueCheck.getCusip()));
                    niResponse.setOriginalCusIp(formatCUSIP(issueCheck.getTransferItem().getOriginalCusIp()));
                    niResponse.setItemQty(issueCheck.getQuantity());
                    niResponse.setOriginalQuantity(issueCheck.getTransferItem().getOriginalQty());
                    niResponse.setOriginalSecDesc(issueCheck.getTransferItem().getOriginalSecurityDescr());
                    niResponse.setItemValue(insurenceValue(issueCheck.getInsuranceValue()));
                    niResponse.setLastModifiedBy(issueCheck.getTransferItem().getLastUpdateName());
                    responsesAll.add(niResponse);
                }
            }
        }

        return responsesAll;
    }

    //@Cacheable(value = "transferItemHistoryCache", key = "#transferItemHistoryDto.toString()")
    public List<TransferItemResponse> searchForCloseTransferItemDetails(TransferHistoryItemDto transferHistoryItemDto) {
        Specification<TransferItemHistory> specificationThi = new TransferItemHistorySearchClose(transferHistoryItemDto);
        List<TransferItemHistory> itemHisPage = transferItemHistoryRepository.findAll(specificationThi);
        List<TransferItemResponse> responsesAll = new ArrayList<>();

        for (TransferItemHistory thid : itemHisPage) {
            TransferItemResponse response = new TransferItemResponse();
            response.setStatusCode(thid.getStatusCode());
            response.setTransferItemId(thid.getTransferItemId());
            response.setItemRecd(convertDate(thid.getReceivedDt()));
            response.setItemRecdDateTo(convertDate(thid.getReceivedDt()));
            response.setTransferDate(convertDate(thid.getTransferredDt()));
            response.setTransferDateTo(convertDate(thid.getTransferredDt()));
            response.setLastModified(convertDate(thid.getLastUpdateDt()));
            response.setLastModifiedTo(convertDate(thid.getLastUpdateDt()));
            response.setConfirmationReceiveDDt(convertDate(thid.getConfirmationReceivedDt()));
            response.setConfirmationReceiveDDtTo(convertDate(thid.getConfirmationReceivedDt()));
            response.setCloseDate(convertDate(thid.getCloseDt()));
            response.setItemAcct(concatItemAcct(thid.getAdpAccountNumber(), thid.getAdpAccountType(), thid.getAdpAccountCheckDigit()));
            response.setSec(thid.getOriginalAdpSecurityNumber());
            response.setOriginalSec(thid.getOriginalAdpSecurityNumber());
            response.setCusIp(formatCUSIP(thid.getOriginalCusIp()));
            response.setOriginalCusIp(formatCUSIP(thid.getOriginalCusIp()));
            response.setItemQty(thid.getOriginalQty());
            response.setOriginalQuantity(thid.getOriginalQty());
            response.setSecDesc(thid.getOriginalSecurityDescr());
            response.setOriginalSecDesc(thid.getOriginalSecurityDescr());
            response.setLastModified(convertDate(thid.getLastUpdateDt()));
            response.setLastModifiedBy(thid.getLastUpdateName());
            response.setIssueType("OI");
            responsesAll.add(response);

            if (thid.getIssueHistory().size() > 0) {
                for (IssueHistory issueCheck : thid.getIssueHistory()) {
                    TransferItemResponse niResponse = new TransferItemResponse();
                    niResponse.setStatusCode(thid.getStatusCode());
                    niResponse.setTransferItemId(thid.getTransferItemId());
                    niResponse.setItemAcct(concatItemAcct(thid.getAdpAccountNumber(), thid.getAdpAccountType(), thid.getAdpAccountCheckDigit()));
                    niResponse.setIssueType("NI");
                    niResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niResponse.setOriginalSec(thid.getOriginalAdpSecurityNumber());
                    niResponse.setCusIp(formatCUSIP(issueCheck.getCusip()));
                    niResponse.setOriginalCusIp(formatCUSIP(thid.getOriginalCusIp()));
                    niResponse.setItemQty(issueCheck.getQuantity());
                    niResponse.setSecDesc(issueCheck.getSecurityDescription());
                    niResponse.setOriginalSecDesc(thid.getOriginalSecurityDescr());
                    niResponse.setItemValue(insurenceValue(issueCheck.getInsuranceValue()));
                    niResponse.setLastModified(convertDate(thid.getLastUpdateDt()));
                    niResponse.setLastModifiedBy(thid.getLastUpdateName());
                    niResponse.setIssueEntryDate(issueCheck.getEntryDate());
                    niResponse.setStatusCode(thid.getStatusCode());
                    responsesAll.add(niResponse);
                }
            }

        }

        return responsesAll;
    }

    protected static class SqlArgs {
        // A simple struct used to contain sql parameter-related fields.
        public int[] sqlType;

        public Object[] args;

        public String whereClause;

        public void add(SqlArgs sqlArg) {
            if (sqlArg == null) {
                return;
            }

            if (this.sqlType == null) {
                this.sqlType = sqlArg.sqlType;
            } else if (sqlArg.sqlType != null) {
                this.sqlType = ArrayUtils.addAll(this.sqlType, sqlArg.sqlType);
            }

            if (this.args == null) {
                this.args = sqlArg.args;
            } else if (sqlArg.args != null) {
                this.args = ArrayUtils.addAll(this.args, sqlArg.args);
            }

            if (this.whereClause == null) {
                this.whereClause = sqlArg.whereClause;
            } else if (sqlArg.whereClause != null) {
                this.whereClause += " AND " + sqlArg.whereClause;
            }
        }

    }

    private void getSqlArgsFromDateRangeCriteria(List<SqlArgs> sqlArgs, String columnName, RangeCriteriaDate dateCriteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntityType> query = cb.createQuery(EntityType.class);
        Root<EntityType> root = query.from(EntityType.class);

        Predicate predicate = null;

        if (dateCriteria.getFrom() != null && dateCriteria.getTo() != null) {
            predicate = cb.between(root.get(columnName), dateCriteria.getFrom(), dateCriteria.getTo());
        } else if (dateCriteria.getFrom() != null) {
            predicate = cb.greaterThanOrEqualTo(root.get(columnName), dateCriteria.getFrom());
        } else if (dateCriteria.getTo() != null) {
            predicate = cb.lessThanOrEqualTo(root.get(columnName), dateCriteria.getTo());
        }

        if (predicate != null) {
            SqlArgs sqlArg = new SqlArgs();
            sqlArg.whereClause = predicate.toString();
            sqlArg.args = predicate.getExpressions().stream().map(Expression::getJavaType).toArray();
            sqlArg.sqlType = predicate.getExpressions().stream().mapToInt(this::getSqlType).toArray();
            sqlArgs.add(sqlArg);
        }
    }

    private int getSqlType(Expression<?> expression) {
        Class<?> javaType = expression.getJavaType();

        if (javaType.equals(LocalDateTime.class)) {
            return Types.TIMESTAMP;
        } else if (javaType.equals(LocalDate.class)) {
            return Types.DATE;
        } else if (javaType.equals(LocalTime.class)) {
            return Types.TIME;
        }

        return Types.VARCHAR;
    }

    private List<TransferItemResponse> getTransferItems(List<TransferItem> transferItems) {
        List<TransferItemResponse> responses = new ArrayList<>();
        for (TransferItem tii : transferItems) {
            TransferItemResponse response = new TransferItemResponse();
            response.setTransferItemId(tii.getTransferItemId());
            response.setStatusCode(tii.getStatusCode());
            response.setItemRecd(convertDate(tii.getReceivedDt()));
            response.setItemRecdDateTo(convertDate(tii.getReceivedDt()));
            response.setTransferDate(convertDate(tii.getTransferredDt()));
            response.setTransferDateTo(convertDate(tii.getTransferredDt()));
            response.setLastModified(convertDate(tii.getLastUpdateDt()));
            response.setLastModifiedTo(convertDate(tii.getLastUpdateDt()));
            response.setConfirmationReceiveDDt(convertDate(tii.getConfirmationReceivedDt()));
            response.setConfirmationReceiveDDtTo(convertDate(tii.getConfirmationReceivedDt()));
            response.setCloseDate(convertDate(tii.getCloseDt()));
            response.setItemAcct(concatItemAcct(tii.getAdpAccountNumber(), tii.getAdpAccountType(), tii.getAdpAccountCheckDigit()));
            response.setSec(tii.getOriginalAdpSecurityNumber());
            response.setOriginalSec(tii.getOriginalAdpSecurityNumber());
            response.setCusIp(tii.getOriginalCusIp());
            response.setOriginalCusIp(tii.getOriginalCusIp());
            response.setItemQty(tii.getOriginalQty());
            response.setOriginalQuantity(tii.getOriginalQty());
            response.setSecDesc(tii.getOriginalSecurityDescr());
            response.setOriginalSecDesc(tii.getOriginalSecurityDescr());
            response.setLastModifiedBy(tii.getLastUpdateName());
            response.setIssueType("OI");
            responses.add(response);

            if (tii.getIssue().size() > 0) {
                for (Issue issueCheck : tii.getIssue()) {
                    TransferItemResponse niiResponse = new TransferItemResponse();
                    niiResponse.setTransferItemId(issueCheck.getTransferItem().getTransferItemId());
                    niiResponse.setStatusCode(issueCheck.getTransferItem().getStatusCode());
                    niiResponse.setItemRecd(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niiResponse.setItemRecdDateTo(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niiResponse.setTransferDate(convertDate(issueCheck.getTransferItem().getTransferredDt()));
                    niiResponse.setTransferDateTo(convertDate(issueCheck.getTransferItem().getTransferredDt()));
                    niiResponse.setLastModified(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setLastModifiedTo(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setConfirmationReceiveDDt(convertDate(issueCheck.getTransferItem().getConfirmationReceivedDt()));
                    niiResponse.setConfirmationReceiveDDtTo(convertDate(issueCheck.getTransferItem().getConfirmationReceivedDt()));
                    niiResponse.setCloseDate(convertDate(issueCheck.getTransferItem().getCloseDt()));
                    niiResponse.setItemAcct(concatItemAcct(issueCheck.getTransferItem().getAdpAccountNumber(), issueCheck.getTransferItem().getAdpAccountType(), issueCheck.getTransferItem().getAdpAccountCheckDigit()));
                    niiResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niiResponse.setOriginalSec(issueCheck.getTransferItem().getOriginalAdpSecurityNumber());
                    niiResponse.setCusIp(issueCheck.getCusip());
                    niiResponse.setItemValue(issueCheck.getInsuranceValue());
                    niiResponse.setOriginalCusIp(issueCheck.getTransferItem().getOriginalCusIp());
                    niiResponse.setItemQty(issueCheck.getQuantity());
                    niiResponse.setOriginalQuantity(issueCheck.getTransferItem().getOriginalQty());
                    niiResponse.setSecDesc(issueCheck.getSecurityDescription());
                    niiResponse.setOriginalSecDesc(issueCheck.getTransferItem().getOriginalSecurityDescr());
                    niiResponse.setLastModifiedBy(issueCheck.getLastUpdateName());
                    niiResponse.setCheckAmount(issueCheck.getCheckAmount());
                    niiResponse.setIssueType("NI");
                    responses.add(niiResponse);
                }
            }
        }
        return responses;
    }

    @Override
    public List<TransferItemResponse> advanceCriteriaSearchForDI(TransferItemDto transferItemDto, String statusCode) {
        TransferItemSearchFilter filter = null;
        List<TransferItemResponse> responses = new ArrayList<>();
        SqlArgs sqlArgs = new SqlArgs();
        Date lastEodRunDate = null;
        RegistrationInformation ri = registrationInformationRepository.findByRegistrationKey("DEF");
        RangeCriteriaDate dateCriteria = null;
        lastEodRunDate = ri.getEodEndDate();

        if (lastEodRunDate == null) {
            dateCriteria = new RangeCriteriaDate(new Date(), new Date());
        } else {
            dateCriteria = new RangeCriteriaDate(lastEodRunDate, new Date(), false);
        }

        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<TransferItem> criteriaQuery = criteriaBuilder.createQuery(TransferItem.class);
        Root<TransferItem> root = criteriaQuery.from(TransferItem.class);
        predicates.add(criteriaBuilder.or(
                criteriaBuilder.between(root.get("originalEntryDt"), dateCriteria.getFrom(), dateCriteria.getTo()),
                criteriaBuilder.between(root.get("issue").get("entryDate"), dateCriteria.getFrom(), dateCriteria.getTo()),
                criteriaBuilder.between(root.get("transferredDt"), dateCriteria.getFrom(), dateCriteria.getTo()),
                criteriaBuilder.between(root.get("closeDt"), dateCriteria.getFrom(), dateCriteria.getTo())
        ));
        if (transferItemDto.getTransferItem() != 0) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("transferItemId"), transferItemDto.getTransferItem())));
        }
        if (transferItemDto.getAccountNumber() != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("adpAccountNumber"), transferItemDto.getAccountNumber())));
        }
        if (transferItemDto.getOrigAdpSecurityNumber() != null) {
            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("originalAdpSecurityNumber"), transferItemDto.getOrigAdpSecurityNumber())));
        }
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<TransferItem> query = em.createQuery(criteriaQuery);
        List<TransferItem> tiis = query.getResultList();
        log.info("count of DI transfer item {}", tiis.size());

        responses = new ArrayList<>();

        for (TransferItem tii : tiis) {
            TransferItemResponse oiiResponse = new TransferItemResponse();
            oiiResponse.setTransferItemId(tii.getTransferItemId());
            oiiResponse.setStatusCode(tii.getStatusCode());
            oiiResponse.setItemRecd(convertDate(tii.getReceivedDt()));
            oiiResponse.setTransferDate(convertDate(tii.getTransferredDt()));
            oiiResponse.setCloseDate(convertDate(tii.getCloseDt()));
            oiiResponse.setItemAcct(concatItemAcct(tii.getAdpAccountNumber(), tii.getAdpAccountType(), tii.getAdpAccountCheckDigit()));
            oiiResponse.setIssueType("OI");
            oiiResponse.setSec(tii.getOriginalAdpSecurityNumber());
            oiiResponse.setOriginalSec(tii.getOriginalAdpSecurityNumber());
            oiiResponse.setCusIp(formatCUSIP(tii.getOriginalCusIp()));
            oiiResponse.setOriginalCusIp(formatCUSIP(tii.getOriginalCusIp()));
            oiiResponse.setItemQty(tii.getOriginalQty());
            oiiResponse.setOriginalQuantity(tii.getOriginalQty());
            oiiResponse.setSecDesc(tii.getOriginalSecurityDescr());
            oiiResponse.setOriginalSecDesc(tii.getOriginalSecurityDescr());
            oiiResponse.setLastModified(convertDate(tii.getLastUpdateDt()));
            oiiResponse.setLastModifiedBy(tii.getLastUpdateName());
            responses.add(oiiResponse);

            if (tii.getIssue().size() > 0) {
                for (Issue issueCheck : tii.getIssue()) {
                    TransferItemResponse niiResponse = new TransferItemResponse();
                    niiResponse.setTransferItemId(issueCheck.getTransferItem().getTransferItemId());
                    niiResponse.setStatusCode(issueCheck.getTransferItem().getStatusCode());
                    niiResponse.setLastModified(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setLastModifiedTo(convertDate(issueCheck.getTransferItem().getLastUpdateDt()));
                    niiResponse.setItemRecd(convertDate(issueCheck.getTransferItem().getReceivedDt()));
                    niiResponse.setItemRecd(convertDate(issueCheck.getEntryDate()));
                    niiResponse.setItemRecd(convertDate(issueCheck.getCertificateDate()));
                    niiResponse.setItemAcct(concatItemAcct(issueCheck.getTransferItem().getAdpAccountNumber(), issueCheck.getTransferItem().getAdpAccountType(), issueCheck.getTransferItem().getAdpAccountCheckDigit()));
                    niiResponse.setSec(issueCheck.getAdpSecurityNumber());
                    niiResponse.setOriginalSec(issueCheck.getTransferItem().getOriginalAdpSecurityNumber());
                    niiResponse.setCusIp(issueCheck.getCusip());
                    niiResponse.setOriginalCusIp(issueCheck.getTransferItem().getOriginalCusIp());
                    niiResponse.setItemQty(issueCheck.getQuantity());
                    niiResponse.setItemValue(issueCheck.getInsuranceValue());
                    niiResponse.setOriginalQuantity(issueCheck.getTransferItem().getOriginalQty());
                    niiResponse.setSecDesc(issueCheck.getSecurityDescription());
                    niiResponse.setOriginalSecDesc(issueCheck.getTransferItem().getOriginalSecurityDescr());
                    niiResponse.setLastModifiedBy(issueCheck.getLastUpdateName());
                    niiResponse.setIssueType("NI");
                    responses.add(niiResponse);
                }
            }
        }

        log.info("count {}", responses.size());
        return responses;
    }

}