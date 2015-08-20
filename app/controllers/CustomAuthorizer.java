package controllers;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.authorization.Authorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.http.profile.HttpProfile;

public class CustomAuthorizer implements Authorizer {

    public boolean isAuthorized(WebContext context, UserProfile profile) {
        if (profile == null) {
            return false;
        }
        if (!(profile instanceof HttpProfile)) {
            return false;
        }
        final HttpProfile httpProfile = (HttpProfile) profile;
        final String username = httpProfile.getUsername();
        return StringUtils.startsWith(username, "jle");
    }
}