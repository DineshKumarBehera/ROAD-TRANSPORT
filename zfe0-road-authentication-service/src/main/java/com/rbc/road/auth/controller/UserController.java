package com.rbc.road.auth.controller;

import com.rbc.road.auth.service.UserService;
import com.rbc.road.auth.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@CrossOrigin("${rbc.road.cors-uri}")
@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    private UserService userService;


    @Value("${rbc.road.cors-uri}")
    private String successHomeUri;

    @GetMapping(path = "/login")
    public void login(@RegisteredOAuth2AuthorizedClient("azure") OAuth2AuthorizedClient client,
                      @AuthenticationPrincipal OidcUser principal,
                      HttpServletResponse response,
                      HttpServletRequest request) throws IOException {

        String group = AuthenticationUtils.getGroup(principal);

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            userService.saveSession(client, principal, request, group);
            response.setHeader(HttpHeaders.LOCATION, successHomeUri);
            response.sendRedirect(successHomeUri + "/" + principal.getFullName() + "/" + group);
        }
    }

}
