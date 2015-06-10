package modules;

import com.google.inject.AbstractModule;
import security.SecurityConfig;
import security.impl.SecurityConfigAll;

/**
 * Created by hv01016 on 10-6-2015.
 */
public class SecurityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SecurityConfig.class).to(SecurityConfigAll.class).asEagerSingleton();
    }
}
