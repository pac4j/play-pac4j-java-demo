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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.client.BaseClient;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.CallbackController;
import org.pac4j.play.Config;
import org.pac4j.play.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.cache.Cache;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;

/**
 * This action checks if the user is not authenticated and starts the authentication process if necessary. *
 * 
 * @author Jerome Leleu
 * @since 1.0.0
 */
public final class RequiresAuthenticationAction extends Action<Result> {
    
    private static final Logger logger = LoggerFactory.getLogger(RequiresAuthenticationAction.class);
    
    private static final Method clientNameMethod;
    
    private static final Method targetUrlMethod;
    
    static {
        try {
            clientNameMethod = RequiresAuthentication.class.getDeclaredMethod(Constants.CLIENT_NAME);
            targetUrlMethod = RequiresAuthentication.class.getDeclaredMethod(Constants.TARGET_URL);
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public Result call(final Context context) throws Throwable {
        final InvocationHandler invocationHandler = Proxy.getInvocationHandler(this.configuration);
        final String clientName = (String) invocationHandler.invoke(this.configuration, clientNameMethod, null);
        logger.debug("clientName : {}", clientName);
        final String targetUrl = (String) invocationHandler.invoke(this.configuration, targetUrlMethod, null);
        logger.debug("targetUrl : {}", targetUrl);
        final String sessionId = context.session().get(Constants.SESSION_ID);
        logger.debug("sessionId : {}", sessionId);
        if (StringUtils.isNotBlank(sessionId)) {
            final CommonProfile profile = (CommonProfile) Cache.get(sessionId);
            logger.debug("profile : {}", profile);
            if (profile != null) {
                return this.delegate.call(context);
            }
        }
        // generate session id if necessary
        CallbackController.generateSessionId(context.session());
        // save requested url to session
        final String savedRequestUrl = CallbackController.getRedirectUrl(targetUrl, context.request().uri());
        logger.debug("savedRequestUrl : {}", savedRequestUrl);
        context.session().put(Constants.REQUESTED_URL, savedRequestUrl);
        // get client
        final BaseClient client = (BaseClient) Config.getClients().findClient(clientName);
        logger.debug("client : {}", client);
        // and compute redirection url
        final String redirectUrl = client.getRedirectionUrl(new JavaWebContext(context.request(), context.response(),
                                                                               context.session()), true);
        logger.debug("redirectUrl : {}", redirectUrl);
        return redirect(redirectUrl);
    }
}
