package com.rbc.zfe0.road.services.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Mapping DeliveryInstruction entity to T_DELIVERYINSTRUCTION table in database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "T_DELVRY_INSTRTN")
public class DeliveryInstruction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DELVRY_INSTRTN_ID")
    private Integer deliveryInstructionId;

    @Column(name = "DELVRY_INSTRTN_NM")
    private String deliveryInstructionName;

    @Column(name = "ADDR_BOX")
    private String addressBox;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @UpdateTimestamp
    @Column(name = "LST_UPDT_DT_TM")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDt;

}
