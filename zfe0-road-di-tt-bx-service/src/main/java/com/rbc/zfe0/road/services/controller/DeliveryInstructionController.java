package com.rbc.zfe0.road.services.controller;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.deliveryinstruction.DeliveryInstructionRequest;
import com.rbc.zfe0.road.services.dto.ApiResponse;
import com.rbc.zfe0.road.services.entity.DeliveryInstruction;
import com.rbc.zfe0.road.services.service.DeliveryInstructionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/deliveryinstruction")
public class DeliveryInstructionController {

    private final DeliveryInstructionService deliveryInstructionService;

    @Autowired
    public DeliveryInstructionController(DeliveryInstructionService deliveryInstructionService) {
        this.deliveryInstructionService = deliveryInstructionService;
    }

    @GetMapping(path = "/getAllDeliveryInstructions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> readAllDeliveryInstructions() {
        try {
            log.info("Get All Delivery Instructions");
            List<DeliveryInstruction> deliveryInstructions = deliveryInstructionService.getAllDeliveryInstructions();
            return new ResponseEntity<>(deliveryInstructions, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching all the delivery instructions: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to get all delivery instructions.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody DeliveryInstructionRequest deliveryInstructionRequest) {
        try {
            log.info("Add a new delivery instruction request: {}", deliveryInstructionRequest);
            DeliveryInstruction deliveryInstruction = deliveryInstructionService.createDeliveryInstruction(deliveryInstructionRequest);
            if (deliveryInstruction != null) {
                return new ResponseEntity<>(deliveryInstruction, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.builder().message("Failed to createBoxLocation delivery instruction.").build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Unexpected Error occurred while adding a delivery instruction: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to create new delivery instruction.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/update/{deliveryInstructionId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@PathVariable("deliveryInstructionId") Integer deliveryInstructionId,
                                         @RequestBody DeliveryInstructionRequest deliveryInstructionRequest) {
        try {
            log.info("Updating a delivery instruction for id: {} and delivery instruction request: {}", deliveryInstructionId, deliveryInstructionRequest);
            DeliveryInstruction deliveryInstruction = deliveryInstructionService.updateDeliveryInstruction(deliveryInstructionId, deliveryInstructionRequest);
            if (deliveryInstruction != null) {
                return new ResponseEntity<>(deliveryInstruction, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.builder().message("The selected delivery instruction does not exist.").build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Unexpected Error occurred while updating a delivery instruction: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to updateEntryCode a delivery instruction.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/delete/{deliveryInstructionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> delete(@PathVariable("deliveryInstructionId") Integer deliveryInstructionId) {
        try {
            Map<String, String> record = deliveryInstructionService.deleteByDeliveryInstructionId(deliveryInstructionId);
            if (record.containsKey(Constants.Exist)) {
                return new ResponseEntity<>(ApiResponse.builder().message(record.get(Constants.Exist)).build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.builder().message(record.get(Constants.NotExist)).build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Unexpected Error occurred while deleting a delivery instruction: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to delete a delivery instruction.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
