package com.rbc.zfe0.road.services.dto.transfertype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookkeepingDTO implements Serializable {
    private List<Cashbk> cashbk;
    private List<Secbk> secbk;
}
