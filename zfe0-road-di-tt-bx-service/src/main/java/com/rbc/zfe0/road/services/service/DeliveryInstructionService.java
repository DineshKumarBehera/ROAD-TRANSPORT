package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.deliveryinstruction.DeliveryInstructionRequest;
import com.rbc.zfe0.road.services.entity.DeliveryInstruction;
import com.rbc.zfe0.road.services.repository.DeliveryInstructionRepository;
import com.rbc.zfe0.road.services.repository.TransferItemRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryInstructionService {

    @Autowired
    private DeliveryInstructionRepository deliveryInstructionRepository;
    @Autowired
    private TransferItemRepository transferItemRepository;


    public List<DeliveryInstruction> getAllDeliveryInstructions() {
        List<DeliveryInstruction> deliveryInstructions = deliveryInstructionRepository.findAll();
        return deliveryInstructions;

    }

    public DeliveryInstruction updateDeliveryInstruction(Integer deliveryInstructionId, DeliveryInstructionRequest deliveryInstructionRequest) {
        DeliveryInstruction savedInstruction = null;
        Optional<DeliveryInstruction> deliveryInstruction = deliveryInstructionRepository.findById(deliveryInstructionId);
        if (deliveryInstruction.isPresent()) {
            deliveryInstruction.get().setDeliveryInstructionName(deliveryInstructionRequest.getDeliveryInstructionName());
            deliveryInstruction.get().setAddressBox(deliveryInstructionRequest.getAddressBox());
            deliveryInstruction.get().setLastUpdateName(deliveryInstructionRequest.getLastUpdateName());
            deliveryInstruction.get().setLastUpdateDt(new Date());
            savedInstruction = deliveryInstructionRepository.save(deliveryInstruction.get());
            log.info("Updated delivery instruction: {}", savedInstruction);
        } else {
            log.info("Delivery Instruction Id: {} does not exist.", deliveryInstructionId);
        }
        return savedInstruction;
    }


    public Map<String, String> deleteByDeliveryInstructionId(Integer deliveryInstructionId) {
        Map<String, String> message = new HashMap<>();
        Optional<DeliveryInstruction> deliveryInstruction = deliveryInstructionRepository.findById(deliveryInstructionId);
        if (deliveryInstruction.isPresent()) {
            if (!checkDeliveryInstructionIsLinkedToTransferItems(deliveryInstruction.get().getDeliveryInstructionId())) {
                deleteDeliveryInstruction(deliveryInstruction.get());
                message.put(Constants.Exist, "The selected delivery instruction is deleted successfully.");
            } else {
                message.put(Constants.Exist, "The selected delivery instruction cannot be deleted. It is assigned to open transfer items.");
            }
        } else {
            message.put(Constants.NotExist, "The selected delivery instruction does not exist.");
        }
        return message;
    }


    public void deleteDeliveryInstruction(DeliveryInstruction deliveryInstruction) {
        deliveryInstructionRepository.deleteById(deliveryInstruction.getDeliveryInstructionId());
    }


    public DeliveryInstruction createDeliveryInstruction(DeliveryInstructionRequest deliveryInstructionRequest) {
        DeliveryInstruction deliveryInstruction = new DeliveryInstruction();
        deliveryInstruction.setDeliveryInstructionName(deliveryInstructionRequest.getDeliveryInstructionName());
        deliveryInstruction.setAddressBox(deliveryInstructionRequest.getAddressBox());
        deliveryInstruction.setLastUpdateName(deliveryInstructionRequest.getLastUpdateName());
        deliveryInstruction.setLastUpdateDt(new Date());
        DeliveryInstruction savedInstruction = deliveryInstructionRepository.save(deliveryInstruction);
        log.info("Added delivery instruction: {}", savedInstruction);
        return savedInstruction;
    }

    private boolean checkDeliveryInstructionIsLinkedToTransferItems(Integer deliveryInstructionId) {
        Long count = transferItemRepository.countByDeliveryInstructionId(deliveryInstructionId);
        return count > 0;
    }
}
