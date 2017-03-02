package modules;

import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.AbstractModule;
import controllers.CustomAuthorizer;
import controllers.DemoHttpActionAdapter;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.client.CasProxyReceptor;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.direct.AnonymousClient;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.client.indirect.IndirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.play.CallbackController;
import org.pac4j.play.LogoutController;
import org.pac4j.play.deadbolt2.Pac4jHandlerCache;
import org.pac4j.play.deadbolt2.Pac4jRoleHandler;
import org.pac4j.play.store.PlayCacheSessionStore;
import org.pac4j.play.store.PlaySessionStore;
import org.pac4j.saml.client.SAML2Client;
import org.pac4j.saml.client.SAML2ClientConfiguration;
import play.Configuration;
import play.Environment;
import play.cache.CacheApi;

import java.io.File;

public class SecurityModule extends AbstractModule {

    public final static String JWT_SALT = "12345678901234567890123456789012";

    private final Configuration configuration;

    private static class MyPac4jRoleHandler implements Pac4jRoleHandler { }

    public SecurityModule(final Environment environment, final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {

        bind(HandlerCache.class).to(Pac4jHandlerCache.class);

        bind(Pac4jRoleHandler.class).to(MyPac4jRoleHandler.class);
        final PlayCacheSessionStore playCacheSessionStore = new PlayCacheSessionStore(getProvider(CacheApi.class));
        bind(PlaySessionStore.class).toInstance(playCacheSessionStore);
        //bind(PlaySessionStore.class).to(PlayCacheSessionStore.class);

        final String fbId = configuration.getString("fbId");
        final String fbSecret = configuration.getString("fbSecret");
        final String baseUrl = configuration.getString("baseUrl");

        // OAuth
        final FacebookClient facebookClient = new FacebookClient(fbId, fbSecret);
        final TwitterClient twitterClient = new TwitterClient("HVSQGAw2XmiwcKOTvZFbQ", "FSiO9G9VRR4KCuksky0kgGuo8gAVndYymr4Nl7qc8AA");
        // HTTP
        final FormClient formClient = new FormClient(baseUrl + "/loginForm", new SimpleTestUsernamePasswordAuthenticator());
        final IndirectBasicAuthClient indirectBasicAuthClient = new IndirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());

        // CAS
        // final CasOAuthWrapperClient casClient = new CasOAuthWrapperClient("this_is_the_key2", "this_is_the_secret2", "http://localhost:8080/cas2/oauth2.0");
        // casClient.setName("CasClient");
        //final CasConfiguration casConfiguration = new CasConfiguration("https://casserverpac4j.herokuapp.com/login");
        final CasConfiguration casConfiguration = new CasConfiguration("http://localhost:8888/cas/login");
        final CasProxyReceptor casProxyReceptor = new CasProxyReceptor();
        casConfiguration.setProxyReceptor(casProxyReceptor);
        final CasClient casClient = new CasClient(casConfiguration);

        // SAML
        final SAML2ClientConfiguration cfg = new SAML2ClientConfiguration("resource:samlKeystore.jks",
                "pac4j-demo-passwd", "pac4j-demo-passwd", "resource:openidp-feide.xml");
        cfg.setMaximumAuthenticationLifetime(3600);
        cfg.setServiceProviderEntityId("urn:mace:saml:pac4j.org");
        cfg.setServiceProviderMetadataPath(new File("target", "sp-metadata.xml").getAbsolutePath());
        final SAML2Client saml2Client = new SAML2Client(cfg);

        // OpenID Connect
        final OidcConfiguration oidcConfiguration = new OidcConfiguration();
        oidcConfiguration.setClientId("343992089165-i1es0qvej18asl33mvlbeq750i3ko32k.apps.googleusercontent.com");
        oidcConfiguration.setSecret("unXK_RSCbCXLTic2JACTiAo9");
        oidcConfiguration.setDiscoveryURI("https://accounts.google.com/.well-known/openid-configuration");
        oidcConfiguration.addCustomParam("prompt", "consent");
        final OidcClient oidcClient = new OidcClient(oidcConfiguration);
        oidcClient.addAuthorizationGenerator((ctx, profile) -> { profile.addRole("ROLE_ADMIN"); return profile; });

        // REST authent with JWT for a token passed in the url as the token parameter
        final ParameterClient parameterClient = new ParameterClient("token",
                new JwtAuthenticator(new SecretSignatureConfiguration(JWT_SALT)));
        parameterClient.setSupportGetRequest(true);
        parameterClient.setSupportPostRequest(false);

        // basic auth
        final DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());

        final Clients clients = new Clients(baseUrl + "/callback", facebookClient, twitterClient, formClient,
                indirectBasicAuthClient, casClient, saml2Client, oidcClient, parameterClient, directBasicAuthClient,
                new AnonymousClient(), casProxyReceptor);

        final Config config = new Config(clients);
        config.addAuthorizer("admin", new RequireAnyRoleAuthorizer<>("ROLE_ADMIN"));
        config.addAuthorizer("custom", new CustomAuthorizer());
        config.setHttpActionAdapter(new DemoHttpActionAdapter());
        bind(Config.class).toInstance(config);

        // callback
        final CallbackController callbackController = new CallbackController();
        callbackController.setDefaultUrl("/");
        callbackController.setMultiProfile(true);
        bind(CallbackController.class).toInstance(callbackController);

        // logout
        final LogoutController logoutController = new LogoutController();
        logoutController.setDefaultUrl("/?defaulturlafterlogout");
        //logoutController.setDestroySession(true);
        bind(LogoutController.class).toInstance(logoutController);
    }
}
