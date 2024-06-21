package com.rbc.zfe0.road.services.transferitem.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.rbc.zfe0.road.services.transferitem.dto.ApiResponse;
import com.rbc.zfe0.road.services.transferitem.dto.TransferItemResponse;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import com.rbc.zfe0.road.services.transferitem.repository.TransferItemRepository;
import com.rbc.zfe0.road.services.transferitem.service.PdfGeneratorService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.rbc.zfe0.road.services.transferitem.service.TransferItemService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/transferitem")
@Schema(name = "Transfer Item")
public class TransferItemController {

    @Autowired
    TransferItemService transferItemService;

    @Autowired
    TransferItemRepository transferItemRepository;

//    @Autowired
//    private PdfGeneratorService pdfGeneratorService;

    @GetMapping(path = "/getTransferItems", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransferItemResponse> findAllItemByStatusCode(@RequestParam("statusCode") String statusCode) {
        log.info("Find all transfer item by status code {}", statusCode);
        return transferItemService.findTransportItem(statusCode);
    }


    @GetMapping(path = "/getTransferAllItems", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllItems() {
        try {
            List<TransferItemResponse> transferResponse = transferItemService.findAllTransportItem();
            return new ResponseEntity(transferResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occured while fetching all the transfer types: {}", e.getMessage());
            return new ResponseEntity(ApiResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/addNewTransferItem", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json", headers = "content-type=application/json")
    public ResponseEntity addTransferItem(@RequestBody Map<String, Object> input) {
        try {
            transferItemService.addNewTransferItem(input);
            return new ResponseEntity("Transfer Item Added", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occured while Adding Transfer Item: {}", e.getMessage());
            return new ResponseEntity(ApiResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
