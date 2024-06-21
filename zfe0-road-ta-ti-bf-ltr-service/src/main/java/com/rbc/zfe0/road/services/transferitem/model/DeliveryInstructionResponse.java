package com.rbc.zfe0.road.services.transferitem.model;

import com.rbc.zfe0.road.services.transferitem.entity.DeliveryInstruction;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryInstructionResponse {
    private int  deliveryInstructionId;
    private String  deliveryInstructionName;
    private String  addressBox;
    private String  lastUpdateName;
    private Date  lastUpdateDt;

    public DeliveryInstruction toDeliveryInstruction() {
        DeliveryInstruction deliveryInstruction = new DeliveryInstruction();
        deliveryInstruction.setDeliveryInstructionName(this.deliveryInstructionName);
        deliveryInstruction.setAddressBox(this.addressBox);
        deliveryInstruction.setLastUpdateName(this.lastUpdateName);
        deliveryInstruction.setLastUpdateDt(this.lastUpdateDt);
        return deliveryInstruction;
    }
}
