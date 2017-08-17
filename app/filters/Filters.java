package filters;

import org.pac4j.play.filters.SecurityFilter;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class Filters implements HttpFilters {

    private final SecurityFilter securityFilter;

    @Inject
    public Filters(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Override
    public List<EssentialFilter> getFilters() {
        return Arrays.asList(securityFilter.asJava());
    }
}
