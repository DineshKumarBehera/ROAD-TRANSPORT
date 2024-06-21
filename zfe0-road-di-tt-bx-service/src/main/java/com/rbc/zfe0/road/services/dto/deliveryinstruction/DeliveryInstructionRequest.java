package com.rbc.zfe0.road.services.dto.deliveryinstruction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Pojo class for Delivery Instruction Request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryInstructionRequest implements Serializable {
    private Integer deliveryInstructionId;
    private String deliveryInstructionName;
    private String addressBox;
    private String lastUpdateName;
}
