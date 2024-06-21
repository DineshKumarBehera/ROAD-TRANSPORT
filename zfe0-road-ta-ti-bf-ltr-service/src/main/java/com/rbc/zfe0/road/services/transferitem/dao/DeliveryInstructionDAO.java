package com.rbc.zfe0.road.services.transferitem.dao;

import com.rbc.zfe0.road.services.transferitem.entity.DeliveryInstruction;
import com.rbc.zfe0.road.services.transferitem.exception.ResourceNotFoundException;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import com.rbc.zfe0.road.services.transferitem.repository.DeliveryInstructionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class DeliveryInstructionDAO {

    @Autowired
    private DeliveryInstructionRepository deliveryInstructionRepository;

    public DeliveryInstruction saveDeliveryInstruction(DeliveryInstruction deliveryInstruction) {
        return deliveryInstructionRepository.save(deliveryInstruction);
    }


    public DeliveryInstruction getDeliveryInstructionDetails(DeliveryInstruction deliveryInstruction, EditTransferItem editTransferItem) {
        Date locTime = new Date();
        if (deliveryInstruction != null) {
            try {
                deliveryInstruction = deliveryInstructionRepository.findById(editTransferItem.getDeliveryInstructionResponse().getDeliveryInstructionId())
                        .orElseThrow(() -> new ResourceNotFoundException("DeliveryInstruction not found with id " + editTransferItem.getDeliveryInstructionResponse().getDeliveryInstructionId()));

                deliveryInstruction.setDeliveryInstructionName(editTransferItem.getDeliveryInstructionResponse().getDeliveryInstructionName());
                deliveryInstruction.setAddressBox(editTransferItem.getDeliveryInstructionResponse().getAddressBox());
                deliveryInstruction.setLastUpdateName(editTransferItem.getDeliveryInstructionResponse().getLastUpdateName());
                deliveryInstruction.setLastUpdateDt(locTime);

            } catch (Exception e) {
                throw new RuntimeException("An error occurred while saving the delivery details", e);
            }
        }
        log.info("set delivey instruction" + deliveryInstruction);
        return saveDeliveryInstruction(deliveryInstruction);

    }

    public DeliveryInstruction getTransferDeliveryInstById(int deliveryInstructionId) {
        return deliveryInstructionRepository.findById(deliveryInstructionId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryInstruction By", "id", deliveryInstructionId));
    }
}
