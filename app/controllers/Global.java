package controllers;

import org.pac4j.cas.client.CasClient;
import org.pac4j.core.client.Clients;
import org.pac4j.http.client.BasicAuthClient;
import org.pac4j.http.client.FormClient;
import org.pac4j.http.credentials.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.openid.client.GoogleOpenIdClient;
import org.pac4j.play.Config;

import play.Application;
import play.GlobalSettings;
import play.Play;
//import play.mvc.Http.RequestHeader;

public class Global extends GlobalSettings {
    
    /*@Override
    public Result onError(final RequestHeader requestHeader, final Throwable t) {
        return play.mvc.Controller.internalServerError(views.html.error500.render());
    }*/
    
    @Override
    public void onStart(final Application app) {
        Config.setErrorPage401(views.html.error401.render().toString());
        Config.setErrorPage403(views.html.error403.render().toString());
        
        final String fbId = Play.application().configuration().getString("fbId");
        final String fbSecret = Play.application().configuration().getString("fbSecret");
        final String baseUrl = Play.application().configuration().getString("baseUrl");
        final String casUrl = Play.application().configuration().getString("casUrl");
        
        // OAuth
        final FacebookClient facebookClient = new FacebookClient(fbId, fbSecret);
        final TwitterClient twitterClient = new TwitterClient("HVSQGAw2XmiwcKOTvZFbQ",
                                                              "FSiO9G9VRR4KCuksky0kgGuo8gAVndYymr4Nl7qc8AA");
        // HTTP
        final FormClient formClient = new FormClient(baseUrl + "/theForm",
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
        casClient.setCasLoginUrl(casUrl);
        
        // OpenID
        final GoogleOpenIdClient googleOpenIdClient = new GoogleOpenIdClient();
        
        final Clients clients = new Clients(baseUrl + "/callback", facebookClient, twitterClient,
                                            formClient, basicAuthClient, casClient, googleOpenIdClient); // , casProxyReceptor);
        Config.setClients(clients);
        // for test purposes : profile timeout = 60 seconds
        // Config.setProfileTimeout(60);
    }
}
