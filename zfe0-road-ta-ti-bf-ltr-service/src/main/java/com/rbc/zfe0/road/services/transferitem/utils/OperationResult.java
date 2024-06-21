package com.rbc.zfe0.road.services.transferitem.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@ToString
public class OperationResult {
    private boolean success;
    private String message;


}
