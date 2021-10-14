package be.jeroendruwe.core.internal.services;

import be.jeroendruwe.core.services.ViteDevServerConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        immediate = true,
        service = ViteDevServerConfig.class
)
@Designate(factory = true, ocd = ViteDevServerConfigImpl.Config.class)
public class ViteDevServerConfigImpl implements ViteDevServerConfig {

    private Config config;

    @Override
    public String getProtocol() {
        return config.protocol();
    }

    @Override
    public String getHostname() {
        return config.hostname();
    }

    @Override
    public String getPort() {
        return config.port();
    }

    @Override
    public String getCategory() {
        return config.category();
    }

    @Override
    public String getEntry() {
        return config.entry();
    }

    @ObjectClassDefinition(name = "Vite Dev Server Configuration")
    @interface Config {
        @AttributeDefinition(name = "protocol") String protocol() default "http";

        @AttributeDefinition(name = "hostname") String hostname() default "localhost";

        @AttributeDefinition(name = "port") String port();

        @AttributeDefinition(name = "port") String entry();

        @AttributeDefinition(name = "port") String category();
    }

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
    }
}
