package com.rbc.zfe0.road.deliveryinstruction.controller;

import com.rbc.zfe0.road.deliveryinstruction.constants.Constants;
import com.rbc.zfe0.road.deliveryinstruction.domain.DeliveryInstructionRequest;
import com.rbc.zfe0.road.deliveryinstruction.dto.ApiResponse;
import com.rbc.zfe0.road.deliveryinstruction.model.DeliveryInstruction;
import com.rbc.zfe0.road.deliveryinstruction.service.DeliveryInstructionService;
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

    @Autowired
    DeliveryInstructionService deliveryInstructionService;

    /**
     * Get all the delivery instructions
     *
     * @return list of deliveryInstructions
     */
    @GetMapping(path = "/getAllDeliveryInstructions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllDeliveryInstructions() {
        try {
            //TODO: After developing Authentication Service, Implement the Filter logic to read the session object from RequestHeader and check in the database whether session is available as well as user_role is authorized to perform the action.
            log.info("Get All Delivery Instructions");
            List<DeliveryInstruction> deliveryInstructions = deliveryInstructionService.getAllDeliveryInstructions();
            return new ResponseEntity(deliveryInstructions, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occured while fetching all the delivery instructions: {}", e.getMessage());
            return new ResponseEntity(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add a new delivery instruction
     *
     * @param deliveryInstructionRequest
     * @return ResponseEntity
     */
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insert(@RequestBody DeliveryInstructionRequest deliveryInstructionRequest) {
        try {
            log.info("Add a new delivery instruction request: {}", deliveryInstructionRequest);
            DeliveryInstruction deliveryInstruction = deliveryInstructionService.insertDeliveryInstruction(deliveryInstructionRequest);
            if (deliveryInstruction != null) {
                return new ResponseEntity(deliveryInstruction, HttpStatus.OK);
            } else {
                return new ResponseEntity(ApiResponse.builder().message("Failed to add delivery instruction.").build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Unexpected Error occured while adding a delivery instruction: {}", e.getMessage());
            return new ResponseEntity(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a delivery Instruction
     *
     * @param deliveryInstructionId
     * @param deliveryInstructionRequest
     * @return ResponseEntity
     */
    @PostMapping(path = "/update/{deliveryInstructionId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable("deliveryInstructionId") Integer deliveryInstructionId,
                                 @RequestBody DeliveryInstructionRequest deliveryInstructionRequest) {
        try {
            //TODO: After developing Authentication Service, Implement the Filter logic to read the session object from RequestHeader and check in the database whether session is available as well as user_role is authorized to perform the action.
            log.info("Updating a delivery instruction for id: {} and delivery instruction request: {}", deliveryInstructionId, deliveryInstructionRequest);
            DeliveryInstruction deliveryInstruction = deliveryInstructionService.updateDeliveryInstruction(deliveryInstructionId, deliveryInstructionRequest);
            if (deliveryInstruction != null) {
                return new ResponseEntity(deliveryInstruction, HttpStatus.OK);
            } else {
                return new ResponseEntity(ApiResponse.builder().message("The selected delivery instruction does not exist.").build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Unexpected Error occured while updating a delivery instruction: {}", e.getMessage());
            return new ResponseEntity(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a delivery Instruction
     *
     * @param deliveryInstructionId
     * @return ApiResponse
     */
    @DeleteMapping(path = "/delete/{deliveryInstructionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable("deliveryInstructionId") Integer deliveryInstructionId) {
        try {
            Map<String, String> record = deliveryInstructionService.deleteByDeliveryInstructionId(deliveryInstructionId);
            if (record.containsKey(Constants.Exist)) {
                return new ResponseEntity(ApiResponse.builder().message(record.get(Constants.Exist)).build(), HttpStatus.OK);
            } else {
                return new ResponseEntity(ApiResponse.builder().message(record.get(Constants.NotExist)).build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Unexpected Error occured while deleting a delivery instruction: {}", e.getMessage());
            return new ResponseEntity(ApiResponse.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}