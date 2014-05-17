package controllers;

import org.pac4j.cas.client.CasClient;
import org.pac4j.core.client.Clients;
import org.pac4j.http.client.BasicAuthClient;
import org.pac4j.http.client.FormClient;
import org.pac4j.http.credentials.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.play.Config;
import org.pac4j.saml.client.Saml2Client;

import play.Application;
import play.GlobalSettings;
//import play.mvc.Http.RequestHeader;
import play.Play;

public class Global extends GlobalSettings {

    /*@Override
    public Result onError(final RequestHeader requestHeader, final Throwable t) {
        return play.mvc.Controller.internalServerError(views.html.error500.render());
    }*/

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
        casClient.setCasLoginUrl("https://freeuse1.casinthecloud.com/leleujgithub/login");

        // SAML
        final Saml2Client saml2Client = new Saml2Client();
        saml2Client.setKeystorePath(Play.application().resource("samlKeystore.jks").getFile());
        saml2Client.setKeystorePassword("pac4j-demo-passwd");
        saml2Client.setPrivateKeyPassword("pac4j-demo-passwd");
        saml2Client.setIdpMetadataPath(Play.application().resource("openidp-feide.xml").getFile());

        final Clients clients = new Clients("http://localhost:9000/callback", facebookClient, twitterClient,
                formClient, basicAuthClient, casClient, saml2Client); // , casProxyReceptor);
        Config.setClients(clients);
        // for test purposes : profile timeout = 60 seconds
        // Config.setProfileTimeout(60);
    }
}
