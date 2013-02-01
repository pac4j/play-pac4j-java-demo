/*
  Copyright 2012 Jerome Leleu

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.play.java;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.Config;
import org.pac4j.play.Constants;
import org.pac4j.play.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.Cache;

/**
 * This controller is the Java controller to retrieve the user profile or the redirect url to start the authentication process.
 * 
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class JavaController extends Controller {
    
    protected static final Logger logger = LoggerFactory.getLogger(JavaController.class);
    
    /**
     * This method returns the url of the provider where the user must be redirected for authentication.<br />
     * The current requested url is saved into session to be restored after authentication.
     * 
     * @param clientType
     * @return the url of the provider where to redirect the user
     * @throws TechnicalException
     */
    protected static String redirectUrl(final String clientType) throws TechnicalException {
        return redirectUrl(clientType, null);
    }
    
    /**
     * This method returns the url of the provider where the user must be redirected for authentication.<br />
     * The input <code>targetUrl</code> (or the current requested url if <code>null</code>) is saved into session to be restored after
     * authentication.
     * 
     * @param clientType
     * @param targetUrl
     * @return the url of the provider where to redirect the user
     * @throws TechnicalException
     */
    protected static String redirectUrl(final String clientType, final String targetUrl) throws TechnicalException {
        // save requested url to session
        final String savedRequestUrl = Controller.getRedirectUrl(targetUrl, request().uri());
        logger.debug("savedRequestUrl : {}", savedRequestUrl);
        session(Constants.REQUESTED_URL, savedRequestUrl);
        // redirect to the provider for authentication
        final String redirectUrl = Config.getClients().findClient(clientType)
            .getRedirectionUrl(new JavaWebContext(request(), session()));
        logger.debug("redirectUrl : {}", redirectUrl);
        return redirectUrl;
    }
    
    /**
     * This method returns the user profile if the user is authenticated or <code>null</code> otherwise.
     * 
     * @return the user profile if the user is authenticated or <code>null</code> otherwise
     */
    protected static CommonProfile profile() {
        // get the session id
        final String sessionId = session(Constants.SESSION_ID);
        logger.debug("sessionId for profile : {}", sessionId);
        if (StringUtils.isNotBlank(sessionId)) {
            // get the user profile in cache
            final CommonProfile profile = (CommonProfile) Cache.get(sessionId);
            logger.debug("profile : {}", profile);
            return profile;
        }
        return null;
    }
}
