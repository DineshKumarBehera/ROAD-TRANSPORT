package com.rbc.road.auth.entity;

import javax.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
@ToString
@Table(name="T_SSO_SESSION_INFO")
public class SessionInfo {

    @Id
    @Column(name = "SESSION_ID")
    private String sessionId;

    @Column(name = "NTWRK_ID")
    private String networkId;

    @Column(name = "AD_GRP")
    private String adGroup;

    @Column(name = "USR_NM")
    private String username;

    @Column(name = "CLNT_ID")
    private String clientId;

    @Column(name = "CLNT_SECRT")
    private String clientSecret;

    @Column(name = "ID_TKN")
    private String idToken;

    @Column(name = "ACCESS_TKN")
    private String accessToken;

    @Column(name = "RFRSH_TKN")
    private String refreshToken;

    @Column(name = "TENANT_ID")
    private String tenantId;

    @Column(name = "REDRT_URI")
    private String redirectUri;

    @Column(name = "APP_URI")
    private String appUri;

    @Column(name = "TKN_URI")
    private String tokenUri;

    @Column(name="LST_LGN_TM")
    private Date lastLoginTime;
}
