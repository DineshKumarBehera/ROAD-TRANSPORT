package com.rbc.road.auth.service;

import com.rbc.road.auth.entity.SessionInfo;
import com.rbc.road.auth.repository.SessionInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private SessionInfoRepository sessionInfoRepository;

    @Autowired
    public UserService(SessionInfoRepository sessionInfoRepository) {
        this.sessionInfoRepository = sessionInfoRepository;
    }

    public void saveSession(OAuth2AuthorizedClient client, OidcUser principal, HttpServletRequest request, String group) {
        SessionInfo sessionInfo = new SessionInfo();
        Optional<SessionInfo> session = sessionInfoRepository.findById(request.getRequestedSessionId());
        if(!session.isPresent()) {
            sessionInfo.setSessionId(request.getRequestedSessionId());
            sessionInfo.setAccessToken(client.getAccessToken().getTokenValue());
            sessionInfo.setRefreshToken(client.getRefreshToken().getTokenValue());
            sessionInfo.setIdToken(principal.getIdToken().getTokenValue());
            sessionInfo.setUsername(principal.getFullName());
            sessionInfo.setAdGroup(group);
            sessionInfo.setLastLoginTime(new Date());
            sessionInfo.setNetworkId(principal.getFullName());
            sessionInfoRepository.save(sessionInfo);
        }
    }
}
