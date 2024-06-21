package com.rbc.zfe0.road.services.transferitem.model;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewIssueRegistration {
    private int issueId;
    private String dainInfo;
    private String registrationTax;
    private String registrationDetails;

}
