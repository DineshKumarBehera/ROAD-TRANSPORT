package com.rbc.zfe0.road.deliveryinstruction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Mapping DeliveryInstruction entity to T_DELIVERYINSTRUCTION table in database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "T_DELIVERYINSTRUCTION")
public class DeliveryInstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DELIVERYINSTRUCTION_ID")
    private Integer deliveryInstructionId;

    @Column(name = "DELIVERYINSTRUCTIONNAME")
    private String deliveryInstructionName;

    @Column(name = "ADDRESSBOX")
    private String addressBox;

    @Column(name = "LASTUPDATENAME")
    private String lastUpdateName;

    @Column(name = "LASTUPDATEDT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDt;

}
