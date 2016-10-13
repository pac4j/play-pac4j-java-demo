package filters;

import org.pac4j.play.filters.SecurityFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class Filters extends DefaultHttpFilters {

    @Inject
    public Filters(SecurityFilter securityFilter) {
        super(securityFilter);
    }
}
