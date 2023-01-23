package controllers;

import com.google.inject.Inject;
import lombok.val;
import model.JsonContent;
import modules.SecurityModule;
import org.pac4j.cas.profile.CasProxyProfile;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.context.PlayFrameworkParameters;
import org.pac4j.play.java.Secure;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Content;
import util.Utils;

import java.util.List;

public class Application extends Controller {

    @Inject
    private Config config;

    private List<UserProfile> getProfiles(Http.Request request) {
        val parameters = new PlayFrameworkParameters(request);
        val context = config.getWebContextFactory().newContext(parameters);
        val sessionStore = config.getSessionStoreFactory().newSessionStore(parameters);
        val profileManager = config.getProfileManagerFactory().apply(context, sessionStore);
        return profileManager.getProfiles();
    }

    @Secure(clients = "AnonymousClient", matchers = "+csrfToken")
    public Result index(Http.Request request) throws Exception {
        val parameters = new PlayFrameworkParameters(request);
        val context = (PlayWebContext) config.getWebContextFactory().newContext(parameters);
        val sessionStore = config.getSessionStoreFactory().newSessionStore(parameters);
        val sessionId = sessionStore.getSessionId(context, false).orElse("nosession");
        val token = (String) context.getRequestAttribute(Pac4jConstants.CSRF_TOKEN).orElse(null);
        // profiles (maybe be empty if not authenticated)
        return ok(views.html.index.render(getProfiles(request), token, sessionId));
    }

    private Result protectedIndexView(Http.Request request) {
        // profiles
        return ok(views.html.protectedIndex.render(getProfiles(request)));
    }

    @Secure(clients = "FacebookClient", matchers = "excludedPath")
    public Result facebookIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    private Result notProtectedIndexView(Http.Request request) {
        // profiles
        return ok(views.html.notprotectedIndex.render(getProfiles(request)));
    }

    public Result facebookNotProtectedIndex(Http.Request request) {
        return notProtectedIndexView(request);
    }

    @Secure(clients = "FacebookClient", authorizers = "admin")
    public Result facebookAdminIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure(clients = "FacebookClient", authorizers = "custom")
    public Result facebookCustomIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure(clients = "TwitterClient,FacebookClient")
    public Result twitterIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure
    public Result protectedIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure(clients = "FormClient")
    public Result formIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    // Setting the isAjax parameter is no longer necessary as AJAX requests are automatically detected:
    // a 401 error response will be returned instead of a redirection to the login url.
    @Secure(clients = "FormClient")
    public Result formIndexJson(Http.Request request) {
        Content content = views.html.protectedIndex.render(getProfiles(request));
        JsonContent jsonContent = new JsonContent(content.body());
        return ok(jsonContent);
    }

    @Secure(clients = "IndirectBasicAuthClient")
    public Result basicauthIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure(clients = "DirectBasicAuthClient,ParameterClient,DirectFormClient")
    public Result dbaIndex(Http.Request request) {

        Utils.block();

        return protectedIndexView(request);
    }

    @Secure(clients = "CasClient")
    public Result casIndex(Http.Request request) {
        final UserProfile profile = getProfiles(request).get(0);
        final String service = "http://localhost:8080/proxiedService";
        String proxyTicket = null;
        if (profile instanceof CasProxyProfile) {
            final CasProxyProfile proxyProfile = (CasProxyProfile) profile;
            proxyTicket = proxyProfile.getProxyTicketFor(service);
        }
        return ok(views.html.casProtectedIndex.render(profile, service, proxyTicket));
    }

    @Secure(clients = "SAML2Client")
    public Result samlIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure(clients = "OidcClient")
    public Result oidcIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    @Secure(clients = "ParameterClient")
    public Result restJwtIndex(Http.Request request) {
        return protectedIndexView(request);
    }

    //@Secure(clients = "AnonymousClient", authorizers = "csrfCheck")
    public Result csrfIndex(Http.Request request) {
        return ok(views.html.csrf.render(getProfiles(request)));
    }

    public Result loginForm() throws TechnicalException {
        final FormClient formClient = (FormClient) config.getClients().findClient("FormClient").get();
        return ok(views.html.loginForm.render(formClient.getCallbackUrl()));
    }

    public Result jwt(Http.Request request) {
        final List<UserProfile> profiles = getProfiles(request);
        final JwtGenerator generator = new JwtGenerator(new SecretSignatureConfiguration(SecurityModule.JWT_SALT));
        String token = "";
        if (CommonHelper.isNotEmpty(profiles)) {
            token = generator.generate(profiles.get(0));
        }
        return ok(views.html.jwt.render(token));
    }

    public Result forceLogin(Http.Request request) {
        val parameters = new PlayFrameworkParameters(request);
        val context = config.getWebContextFactory().newContext(parameters);
        val sessionStore = config.getSessionStoreFactory().newSessionStore(parameters);
        val client = config.getClients().findClient(context.getRequestParameter(Pac4jConstants.DEFAULT_CLIENT_NAME_PARAMETER).get()).get();
        try {
            val action = client.getRedirectionAction(new CallContext(context, sessionStore, config.getProfileManagerFactory())).get();
            return (Result) config.getHttpActionAdapter().adapt(action, context);
        } catch (final HttpAction e) {
            throw new TechnicalException(e);
        }
    }
}
