package controllers;

import org.pac4j.cas.client.CasClient;
import org.pac4j.core.client.Clients;
import org.pac4j.http.client.BasicAuthClient;
import org.pac4j.http.client.FormClient;
import org.pac4j.http.credentials.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.openid.client.MyOpenIdClient;
import org.pac4j.play.Config;

import play.Application;
import play.GlobalSettings;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

public class Global extends GlobalSettings {
    
    @Override
    public Result onError(final RequestHeader requestHeader, final Throwable t) {
        return play.mvc.Controller.internalServerError(views.html.error500.render());
    }
    
    @Override
    public void onStart(final Application app) {
        Config.setErrorPage401(views.html.error401.render().toString());
        Config.setErrorPage403(views.html.error403.render().toString());
        
        // OAuth
        final FacebookClient facebookClient = new FacebookClient("132736803558924", "e461422527aeedb32ee6c10834d3e19e");
        final TwitterClient twitterClient = new TwitterClient("HVSQGAw2XmiwcKOTvZFbQ",
                                                              "FSiO9G9VRR4KCuksky0kgGuo8gAVndYymr4Nl7qc8AA");
        // HTTP
        final FormClient formClient = new FormClient("http://localhost:9000/theForm",
                                                     new SimpleTestUsernamePasswordAuthenticator());
        final BasicAuthClient basicAuthClient = new BasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());
        
        // CAS
        final CasClient casClient = new CasClient();
        // casClient.setLogoutHandler(new PlayLogoutHandler());
        // casClient.setCasProtocol(CasProtocol.SAML);
        // casClient.setGateway(true);
        /*final CasProxyReceptor casProxyReceptor = new CasProxyReceptor();
        casProxyReceptor.setCallbackUrl("http://localhost:9000/casProxyCallback");
        casClient.setCasProxyReceptor(casProxyReceptor);*/
        casClient.setCasLoginUrl("http://localhost:8080/cas/login");
        
        // OpenID
        final MyOpenIdClient myOpenIdClient = new MyOpenIdClient();
        
        final Clients clients = new Clients("http://localhost:9000/callback", facebookClient, twitterClient,
                                            formClient, basicAuthClient, casClient, myOpenIdClient); // , casProxyReceptor);
        Config.setClients(clients);
        // for test purposes : profile timeout = 60 seconds
        // Config.setProfileTimeout(60);
    }
}
