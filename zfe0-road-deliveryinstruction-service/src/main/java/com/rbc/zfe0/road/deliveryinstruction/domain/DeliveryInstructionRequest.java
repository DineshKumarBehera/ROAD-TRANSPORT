package com.rbc.zfe0.road.deliveryinstruction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pojo class for Delivery Instruction Request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryInstructionRequest {
    private String deliveryInstructionName;
    private String addressBox;
    private String lastUpdateName;
}
