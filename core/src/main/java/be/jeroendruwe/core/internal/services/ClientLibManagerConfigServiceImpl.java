package be.jeroendruwe.core.internal.services;

import be.jeroendruwe.core.services.ClientLibManagerConfigService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(service = ClientLibManagerConfigService.class)
@Designate(ocd = ClientLibManagerConfigServiceImpl.Config.class)
public class ClientLibManagerConfigServiceImpl implements ClientLibManagerConfigService {

    private Config config;

    @ObjectClassDefinition(name = "Vite integration configuration")
    @interface Config {
        @AttributeDefinition(name = "Enable Vite Integration") boolean viteEnabled() default false;

        @AttributeDefinition(name = "protocol") String protocol() default "http";

        @AttributeDefinition(name = "hostname") String hostname() default "localhost";

        @AttributeDefinition(name = "port") String port() default "3000";
    }

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
    }

    @Override
    public boolean isViteEnabled() {
        return config.viteEnabled();
    }

    @Override
    public String getServerUrl() {
        return String.format("%s://%s:%s/", config.protocol(), config.hostname(), config.port());
    }
}
