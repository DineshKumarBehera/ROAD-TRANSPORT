package com.rbc.zfe0.road.deliveryinstruction.service;

import com.rbc.zfe0.road.deliveryinstruction.constants.Constants;
import com.rbc.zfe0.road.deliveryinstruction.domain.DeliveryInstructionRequest;
import com.rbc.zfe0.road.deliveryinstruction.model.DeliveryInstruction;
import com.rbc.zfe0.road.deliveryinstruction.repository.DeliveryInstructionRepository;
import com.rbc.zfe0.road.deliveryinstruction.repository.TransferItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class DeliveryInstructionService {

    @Autowired
    DeliveryInstructionRepository deliveryInstructionRepository;

    @Autowired
    TransferItemRepository transferItemRepository;

    /**
     * Get all the delivery instructions from database
     *
     * @return List<DeliveryInstruction>
     */
    public List<DeliveryInstruction> getAllDeliveryInstructions() {
        return deliveryInstructionRepository.findAll();
    }

    /**
     * Update a delivery instruction in database
     *
     * @param deliveryInstructionId
     * @param deliveryInstructionRequest
     * @return DeliveryInstruction
     */
    public DeliveryInstruction updateDeliveryInstruction(Integer deliveryInstructionId, DeliveryInstructionRequest deliveryInstructionRequest) {
        DeliveryInstruction savedInstruction = null;
        Optional<DeliveryInstruction> deliveryInstruction = deliveryInstructionRepository.findById(deliveryInstructionId);
        if (deliveryInstruction.isPresent()) {
            deliveryInstruction.get().setDeliveryInstructionName(deliveryInstructionRequest.getDeliveryInstructionName());
            deliveryInstruction.get().setAddressBox(deliveryInstructionRequest.getAddressBox());
            deliveryInstruction.get().setLastUpdateName(deliveryInstructionRequest.getLastUpdateName());
            deliveryInstruction.get().setLastUpdateDt(LocalDateTime.now());
            savedInstruction = deliveryInstructionRepository.save(deliveryInstruction.get());
            log.info("Updated delivery instruction: {}", savedInstruction);
        } else {
            log.info("Delivery Instruction Id: {} does not exist.", deliveryInstructionId);
        }
        return savedInstruction;
    }

    /**
     * Delete delivery instruction by deliveryInstructionId
     * Check whether the delivery instruction is linked to any transfer items.If not linked then delete.
     *
     * @param deliveryInstructionId
     * @return message
     */
    public Map<String, String> deleteByDeliveryInstructionId(Integer deliveryInstructionId) {
        Map<String, String> message = new HashMap<>();
        Optional<DeliveryInstruction> deliveryInstruction = deliveryInstructionRepository.findById(deliveryInstructionId);
        if (deliveryInstruction.isPresent()) {
            if (!checkDeliveryInstructionIsLinkedToTransferItems(deliveryInstruction.get().getDeliveryInstructionId())) {
                deliveryInstructionRepository.deleteById(deliveryInstruction.get().getDeliveryInstructionId());
                message.put(Constants.Exist, "The selected delivery instruction is deleted successfully.");
            } else {
                message.put(Constants.Exist, "The selected delivery instruction cannot be deleted. It is assigned to open transfer items.");
            }
        } else {
            message.put(Constants.NotExist, "The selected delivery instruction does not exist.");
        }
        return message;
    }

    /**
     * Insert a delivery instruction
     *
     * @param deliveryInstructionRequest
     * @return
     */
    public DeliveryInstruction insertDeliveryInstruction(DeliveryInstructionRequest deliveryInstructionRequest) {
        DeliveryInstruction deliveryInstruction = new DeliveryInstruction();
        deliveryInstruction.setDeliveryInstructionName(deliveryInstructionRequest.getDeliveryInstructionName());
        deliveryInstruction.setAddressBox(deliveryInstructionRequest.getAddressBox());
        deliveryInstruction.setLastUpdateName(deliveryInstructionRequest.getLastUpdateName());
        deliveryInstruction.setLastUpdateDt(LocalDateTime.now());
        DeliveryInstruction savedInstruction = deliveryInstructionRepository.save(deliveryInstruction);
        log.info("Added delivery instruction: {}", savedInstruction);
        return savedInstruction;
    }

    /**
     * Check whether the delivery instruction is linked to any transfer items.
     *
     * @param deliveryInstructionId
     * @return boolean
     */
    private boolean checkDeliveryInstructionIsLinkedToTransferItems(Integer deliveryInstructionId) {
        Long count = transferItemRepository.countByDeliveryInstructionId(deliveryInstructionId);
        return count > 0 ? true : false;
    }

}
