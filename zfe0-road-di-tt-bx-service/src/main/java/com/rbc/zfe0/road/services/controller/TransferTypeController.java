package com.rbc.zfe0.road.services.controller;


import com.rbc.zfe0.road.services.dto.ApiResponse;
import com.rbc.zfe0.road.services.dto.transfertype.TransferTypeDTO;
import com.rbc.zfe0.road.services.entity.TransferType;
import com.rbc.zfe0.road.services.exception.TransferTypeException;
import com.rbc.zfe0.road.services.service.TransferTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/transfertype")
public class TransferTypeController {

    private final TransferTypeService transferTypeService;

    @Autowired
    public TransferTypeController(TransferTypeService transferTypeService) {
        this.transferTypeService = transferTypeService;
    }

    @GetMapping(path = "/getAllTransferTypes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> readAllTransferTypes() {
        try {
            List<TransferType> transferTypes = transferTypeService.getAllTransferTypes();
            return new ResponseEntity<Object>(transferTypes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching all the transfer types: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getActiveTransferTypes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> readActiveTransferTypes() {
        try {
            List<TransferType> transferTypes = transferTypeService.getActiveTransferTypes();
            return new ResponseEntity<>(transferTypes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching active transfer types: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{transferTypeCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> readByTransferTypeCode(@PathVariable("transferTypeCode") String transferTypeCode) {
        try {
            TransferTypeDTO tt = transferTypeService.getByTransferTypeCode(transferTypeCode);
            return new ResponseEntity<>(tt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching transfer type details: {}", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody TransferTypeDTO transferTypeRequest) {
        try {
            transferTypeService.createTransferType(transferTypeRequest);
            return new ResponseEntity<>(ApiResponse.builder().message("New transfer type is created successfully.").build(), HttpStatus.OK);
        } catch (TransferTypeException tte) {
            log.error("Unexpected error occurred while creating new transfer type: {}", tte.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().message(tte.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating new transfer type: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/update/{transferTypeCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("transferTypeCode") String transferTypeCode,
                                              @RequestBody TransferTypeDTO transferTypeRequest) {
        try {
            transferTypeService.updateTransferType(transferTypeCode, transferTypeRequest);
            return new ResponseEntity<>(ApiResponse.builder().message("Transfer type is updated successfully.").build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating a transfer type: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/delete/{transferTypeCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> delete(@PathVariable("transferTypeCode") String transferTypeCode,
                                              @RequestBody TransferTypeDTO transferTypeRequest) {
        try {
            transferTypeService.deleteByTransferTypeCode(transferTypeCode, transferTypeRequest);
            return new ResponseEntity<>(ApiResponse.builder().message("Transfer type is deleted successfully.").build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting a transfer type: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
