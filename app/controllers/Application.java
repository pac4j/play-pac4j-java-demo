package controllers;

import model.JsonContent;

import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.http.client.FormClient;
import org.pac4j.play.Config;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.mvc.Result;
import play.twirl.api.Content;

public class Application extends JavaController {

    public Result index() throws TechnicalException {
        // profile (maybe null if not authenticated)
        final CommonProfile profile = getUserProfile();
        final String urlFacebook = getRedirectAction("FacebookClient", "/?0").getLocation();
        final String urlTwitter = getRedirectAction("TwitterClient", "/?1").getLocation();
        final String urlForm = getRedirectAction("FormClient", "/?2").getLocation();
        final String urlBasicAuth = getRedirectAction("BasicAuthClient", "/?3").getLocation();
        final String urlCas = getRedirectAction("CasClient", "/?4").getLocation();
        final String urlOidc = getRedirectAction("OidcClient", "/?5").getLocation();
        final String urlSaml = getRedirectAction("Saml2Client", "/?6").getLocation();
        return ok(views.html.index.render(profile, urlFacebook, urlTwitter, urlForm, urlBasicAuth, urlCas, urlOidc,
                urlSaml));
    }

    private Result protectedIndex() {
        // profile
        final CommonProfile profile = getUserProfile();
        return ok(views.html.protectedIndex.render(profile));
    }

    @RequiresAuthentication(clientName = "FacebookClient")
    public Result facebookIndex() {
        return protectedIndex();
    }

    @RequiresAuthentication(clientName = "TwitterClient")
    public Result twitterIndex() {
        return protectedIndex();
    }

    @RequiresAuthentication(clientName = "FormClient")
    public Result formIndex() {
        return protectedIndex();
    }

    // Setting the isAjax parameter to true will result in a 401 error response
    // instead of redirecting to the login url.
    @RequiresAuthentication(clientName = "FormClient", isAjax = true)
    public Result formIndexJson() {
        final CommonProfile profile = getUserProfile();
        Content content = views.html.protectedIndex.render(profile);
        JsonContent jsonContent = new JsonContent(content.body());
        return ok(jsonContent);
    }

    @RequiresAuthentication(clientName = "BasicAuthClient")
    public Result basicauthIndex() {
        return protectedIndex();
    }

    @RequiresAuthentication(clientName = "CasClient")
    public Result casIndex() {
        /*final CommonProfile profile = getUserProfile();
        final String service = "http://localhost:8080/proxiedService";
        String proxyTicket = null;
        if (profile instanceof CasProxyProfile) {
            final CasProxyProfile proxyProfile = (CasProxyProfile) profile;
            proxyTicket = proxyProfile.getProxyTicketFor(service);
        }
        return ok(views.html.casProtectedIndex.render(profile, service, proxyTicket));*/
        return protectedIndex();
    }

    @RequiresAuthentication(clientName = "Saml2Client")
    public Result samlIndex() {
        return protectedIndex();
    }

    @RequiresAuthentication(clientName = "OidcClient")
    public Result oidcIndex() {
        return protectedIndex();
    }

    public Result theForm() throws TechnicalException {
        final FormClient formClient = (FormClient) Config.getClients().findClient("FormClient");
        return ok(views.html.theForm.render(formClient.getCallbackUrl()));
    }

    @RequiresAuthentication(clientName = "BasicAuthClient", stateless = true)
    public Result statelessIndex() {
        return protectedIndex();
    }
}
