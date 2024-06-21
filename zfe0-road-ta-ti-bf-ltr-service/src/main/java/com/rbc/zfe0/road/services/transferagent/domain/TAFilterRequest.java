package com.rbc.zfe0.road.services.transferagent.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TAFilterRequest {
    private String name;
    private boolean strict;
}
