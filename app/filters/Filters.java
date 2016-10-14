package filters;

import org.pac4j.play.filters.SecurityFilter;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;

public class Filters implements HttpFilters {

    private final SecurityFilter securityFilter;

    @Inject
    public Filters(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] { securityFilter.asJava() };
    }
}
