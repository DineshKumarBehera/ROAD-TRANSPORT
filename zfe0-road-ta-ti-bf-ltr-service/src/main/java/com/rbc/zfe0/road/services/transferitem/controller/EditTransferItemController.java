package com.rbc.zfe0.road.services.transferitem.controller;

import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import com.rbc.zfe0.road.services.transferitem.exception.TransferItemNotFoundException;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import com.rbc.zfe0.road.services.transferitem.repository.TransferItemRepository;
import com.rbc.zfe0.road.services.transferitem.service.TransferItemService;
import com.rbc.zfe0.road.services.transferitem.utils.OperationResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/editTransferItem")
@Schema(name = "Edit Transfer Item")
public class EditTransferItemController {

    @Autowired
    TransferItemService transferItemService;


    @Autowired
    TransferItemRepository transferItemRepository;

    @GetMapping(path = "/getEditTransferItem/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EditTransferItem findTransportItemsByID(@PathVariable int id) throws IOException, TransferItemNotFoundException {
        return transferItemService.findByTransportItemId(id);
    }

    @GetMapping(path = "/getEditItem/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EditTransferItem findEditTransferItm(@PathVariable int id) throws IOException, TransferItemNotFoundException {
        return transferItemService.findByCLOSItemId(id);


    }

    @PostMapping("/saveEditItem/{transferItemId}")
    public ResponseEntity<OperationResult> saveAndEditTransferItem(@PathVariable Integer transferItemId,
                                                                   @RequestBody EditTransferItem editTransferItem) {
        TransferItem transferItem = transferItemService.getTransferItemById(transferItemId);
        // get user details here
        String user = "roadadmin";
        transferItemService.saveTransferItem(transferItem, editTransferItem, user);
        OperationResult result = new OperationResult();
        result.setMessage("Transfer Item update successful");
        result.setSuccess(true);
        return ResponseEntity.ok(result);
    }
}
