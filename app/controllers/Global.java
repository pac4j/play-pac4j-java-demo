package controllers;

import org.pac4j.cas.client.CasClient;
import org.pac4j.core.client.Clients;
import org.pac4j.http.client.BasicAuthClient;
import org.pac4j.http.client.FormClient;
import org.pac4j.http.credentials.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.http.profile.UsernameProfileCreator;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.play.Config;
import org.pac4j.saml.client.Saml2Client;

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
                new SimpleTestUsernamePasswordAuthenticator(), new UsernameProfileCreator());
        final BasicAuthClient basicAuthClient = new BasicAuthClient(new SimpleTestUsernamePasswordAuthenticator(),
                new UsernameProfileCreator());

        // CAS
        final CasClient casClient = new CasClient();
        // casClient.setLogoutHandler(new PlayLogoutHandler());
        // casClient.setCasProtocol(CasProtocol.SAML);
        // casClient.setGateway(true);
        /*final CasProxyReceptor casProxyReceptor = new CasProxyReceptor();
        casProxyReceptor.setCallbackUrl("http://localhost:9000/casProxyCallback");
        casClient.setCasProxyReceptor(casProxyReceptor);*/
        casClient.setCasLoginUrl(casUrl);

        // SAML
        final Saml2Client saml2Client = new Saml2Client();
        saml2Client.setKeystorePath("resource:samlKeystore.jks");
        saml2Client.setKeystorePassword("pac4j-demo-passwd");
        saml2Client.setPrivateKeyPassword("pac4j-demo-passwd");
        saml2Client.setIdpMetadataPath("resource:openidp-feide.xml");

        // OpenID Connect
        final OidcClient oidcClient = new OidcClient();
        oidcClient.setClientID("343992089165-i1es0qvej18asl33mvlbeq750i3ko32k.apps.googleusercontent.com");
        oidcClient.setSecret("unXK_RSCbCXLTic2JACTiAo9");
        oidcClient.setDiscoveryURI("https://accounts.google.com/.well-known/openid-configuration");
        oidcClient.addCustomParam("prompt", "consent");

        final Clients clients = new Clients(baseUrl + "/callback", facebookClient, twitterClient, formClient,
                basicAuthClient, casClient, saml2Client, oidcClient); // , casProxyReceptor);
        Config.setClients(clients);
        // for test purposes : profile timeout = 60 seconds
        // Config.setProfileTimeout(60);
    }
}
