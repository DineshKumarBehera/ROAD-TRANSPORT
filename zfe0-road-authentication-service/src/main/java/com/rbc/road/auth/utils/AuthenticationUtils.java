package com.rbc.road.auth.utils;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationUtils {


    public static String getGroup(OidcUser principal) {
        String grpName = null;
        if(principal.getClaims().containsKey("groups")) {
            List<String> groups = (ArrayList) principal.getClaims().get("groups");
            for (String group : groups) {
                if(group.contains("ROAD_ADMIN")) {
                    grpName = "ADMIN";
                    break;
                } else if(group.contains("ROAD_ASSOCIATE")) {
                    grpName = "ASSOCIATE";
                    break;
                } else if(group.contains("ROAD_TL")) {
                    grpName = "TL";
                    break;
                } else if(group.contains("ROAD_GUEST")) {
                    grpName = "GUEST";
                    break;
                }
            }
        }
        return grpName;
    }
}
