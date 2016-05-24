package controllers;

import java.util.Map;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.http.HttpActionAdapter;
import org.pac4j.play.PlayWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http.Response;

import static play.mvc.Results.*;

public class DemoHttpActionAdapter implements HttpActionAdapter{

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object adapt(final int code, final WebContext context) {
        final PlayWebContext webContext = (PlayWebContext) context;
        logger.debug("requires HTTP action: {}", code);
        if (code == HttpConstants.UNAUTHORIZED) {
            final String content = views.html.error401.render().toString();
            return unauthorized(content).as(HttpConstants.HTML_CONTENT_TYPE);
        } else if (code == HttpConstants.FORBIDDEN) {
            final String content = views.html.error403.render().toString();
            return forbidden(content).as(HttpConstants.HTML_CONTENT_TYPE);
        } else if (code == HttpConstants.TEMP_REDIRECT) {
            return redirect(getLocation(webContext));
        } else if (code == HttpConstants.OK) {
            final String content = webContext.getResponseContent();
            logger.debug("render: {}", content);
            return ok(content).as(HttpConstants.HTML_CONTENT_TYPE);
        }

        final String message = "Unsupported HTTP action: " + code;
        logger.error(message);
        throw new TechnicalException(message);
    }

    private String getLocation(final PlayWebContext webContext){
      final Response response = webContext.getJavaContext().response();
      final Map<String, String> headers = response.getHeaders();

      return headers.get(HttpConstants.LOCATION_HEADER);
    }
}
