/*
  Copyright 2012 - 2013 Jerome Leleu

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

import java.util.HashMap;
import java.util.Map;

import org.pac4j.core.context.BaseResponseContext;
import org.pac4j.play.Constants;
import org.scribe.model.Token;

import play.mvc.Http.Request;
import play.mvc.Http.Session;

/**
 * This class is the Java web context for Play.
 * <p />
 * For session management, it only handles String or Token objects as the Play session only stores String and thus requires a mapping from
 * Object to String.
 * 
 * @author Jerome Leleu
 * @since 1.1.0
 */
public class JavaWebContext extends BaseResponseContext {
    
    private final Request request;
    
    private final Session session;
    
    public JavaWebContext(final Request request, final Session session) {
        this.request = request;
        this.session = session;
    }
    
    @Override
    public String getRequestHeader(final String name) {
        return this.request.getHeader(name);
    }
    
    @Override
    public String getRequestMethod() {
        return this.request.method();
    }
    
    @Override
    public String getRequestParameter(final String name) {
        final Map<String, String[]> parameters = getRequestParameters();
        final String[] values = parameters.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }
    
    @Override
    public Map<String, String[]> getRequestParameters() {
        final Map<String, String[]> formParameters = this.request.body().asFormUrlEncoded();
        final Map<String, String[]> urlParameters = this.request.queryString();
        final Map<String, String[]> parameters = new HashMap<String, String[]>();
        if (formParameters != null) {
            parameters.putAll(formParameters);
        }
        if (urlParameters != null) {
            parameters.putAll(urlParameters);
        }
        return parameters;
    }
    
    @Override
    public Object getSessionAttribute(final String key) {
        final Object object = this.session.get(key);
        if (object != null)
            return object;
        
        final String secret = this.session.get(key + Constants.SECRET_SUFFIX_SESSION_PARAMETER);
        final String token = this.session.get(key + Constants.TOKEN_SUFFIX_SESSION_PARAMETER);
        if (secret != null || token != null)
            return new Token(token, secret);
        
        return null;
    }
    
    @Override
    public void setSessionAttribute(final String key, final Object value) {
        if (value instanceof String) {
            this.session.put(key, (String) value);
        } else if (value instanceof Token) {
            final Token scribeToken = (Token) value;
            final String secret = scribeToken.getSecret();
            this.session.put(key + Constants.SECRET_SUFFIX_SESSION_PARAMETER, secret);
            final String token = scribeToken.getToken();
            this.session.put(key + Constants.TOKEN_SUFFIX_SESSION_PARAMETER, token);
        } else
            throw new IllegalArgumentException("String and Token only supported in Play session");
    }
}
