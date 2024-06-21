package com.rbc.zfe0.road.services.transferitem.service;


import com.rbc.zfe0.road.services.transferitem.dto.TransferHistoryItemDto;
import com.rbc.zfe0.road.services.transferitem.dto.TransferItemDto;
import com.rbc.zfe0.road.services.transferitem.dto.TransferItemResponse;

import com.rbc.zfe0.road.services.transferitem.entity.Issue;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import com.rbc.zfe0.road.services.transferitem.exception.TransferItemNotFoundException;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Transactional
@Service
public interface TransferItemService {


    public List<TransferItemResponse> findTransportItem(String statusCode);

    public EditTransferItem findByTransportItemId(int id) throws TransferItemNotFoundException;

    public void addIssueGeneratedNote(EditTransferItem itemIn, Issue newIssueIn, String userIn);

    public List<TransferItemResponse> findAllTransportItem();

    public EditTransferItem findByCLOSItemId(int id) throws TransferItemNotFoundException;

    public TransferItem getTransferItemById(Integer transferItemId);

    public void saveTransferItem(TransferItem transferItem, EditTransferItem editTransferItem, String user);


    public List<TransferItemResponse> advanceCriteriaSearch(TransferItemDto transferItemDto, String statusCode);

    public List<TransferItemResponse> search(TransferItemDto transferItemDto, String statusCode);
    public List<TransferItemResponse> advanceCriteriaSearchForClos(TransferHistoryItemDto transferHistoryItemDto, String statusCode);

    public void addNewTransferItem(Map<String, Object> requestParams) throws Exception;

    List<TransferItemResponse> advanceCriteriaSearchForDI(TransferItemDto transferItemDto, String statusCode);

}
