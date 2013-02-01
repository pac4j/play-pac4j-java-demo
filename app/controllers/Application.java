package controllers;

import org.pac4j.cas.client.CasClient;
import org.pac4j.core.client.Clients;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.http.client.BasicAuthClient;
import org.pac4j.http.client.FormClient;
import org.pac4j.http.credentials.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.play.Config;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.mvc.Result;

public class Application extends JavaController {
    
    static {
        // OAuth
        final FacebookClient facebookClient = new FacebookClient("132736803558924", "e461422527aeedb32ee6c10834d3e19e");
        final TwitterClient twitterClient = new TwitterClient("HVSQGAw2XmiwcKOTvZFbQ",
                                                              "FSiO9G9VRR4KCuksky0kgGuo8gAVndYymr4Nl7qc8AA");
        // HTTP
        final FormClient formClient = new FormClient("http://localhost:9000/form",
                                                     new SimpleTestUsernamePasswordAuthenticator());
        final BasicAuthClient basicAuthClient = new BasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());
        
        // CAS
        final CasClient casClient = new CasClient();
        casClient.setCasLoginUrl("http://localhost:8080/cas/login");
        final Clients clients = new Clients("http://localhost:9000/callback", facebookClient, twitterClient,
                                            formClient, basicAuthClient, casClient);
        
        Config.setClients(clients);
        // for test purposes : cache timeout = 60 seconds
        // Config.setCacheTimeout(60);
    }
    
    public static Result index() throws TechnicalException {
        // profile (maybe null if not authenticated)
        final CommonProfile profile = profile();
        final String urlFacebook = redirectUrl("FacebookClient");
        final String urlTwitter = redirectUrl("TwitterClient");
        return ok(views.html.index.render(profile, urlFacebook, urlTwitter));
    }
    
    private static Result protectedIndex() {
        // profile
        final CommonProfile profile = profile();
        return ok(views.html.protectedIndex.render(profile));
    }
    
    @RequiresAuthentication(clientName = "FacebookClient")
    public static Result facebookIndex() {
        return protectedIndex();
    }
    
    @RequiresAuthentication(clientName = "TwitterClient")
    public static Result twitterIndex() {
        return protectedIndex();
    }
    
    @RequiresAuthentication(clientName = "FormClient")
    public static Result formIndex() {
        return protectedIndex();
    }
    
    @RequiresAuthentication(clientName = "BasicAuthClient")
    public static Result basicauthIndex() {
        return protectedIndex();
    }
    
    @RequiresAuthentication(clientName = "CasClient")
    public static Result casIndex() {
        return protectedIndex();
    }
    
    public static Result form() throws TechnicalException {
        final FormClient formClient = (FormClient) Config.getClients().findClient("FormClient");
        return ok(views.html.form.render(formClient.getCallbackUrl()));
    }
}
