package be.jeroendruwe.core.internal.services;

import be.jeroendruwe.core.services.ModuleBasedClientLibService;
import be.jeroendruwe.core.services.ViteDevServerConfig;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component(service = ModuleBasedClientLibService.class)
@Designate(ocd = ViteClientLibServiceImpl.Config.class)
public class ViteClientLibServiceImpl implements ModuleBasedClientLibService {

    private static final Logger LOG = LoggerFactory.getLogger(ViteClientLibServiceImpl.class);

    private static final String IDENTIFIER = "vite";
    private static final String PN_VITE_TARGET = "viteTarget";

    private final List<ViteDevServerConfig> devServerConfigurations = new ArrayList<>();
    private Config config;

    @Reference(
            service = ViteDevServerConfig.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    protected synchronized void bindConfigurationFactory(final ViteDevServerConfig config) {
        devServerConfigurations.add(config);
    }

    protected synchronized void unbindConfigurationFactory(final ViteDevServerConfig config) {
        devServerConfigurations.remove(config);
    }

    @Override
    public boolean isApplicable(Map<String, Object> props) {
        String moduleIdentifier = (String) props.get(PN_MODULE_IDENTIFIER);
        return moduleIdentifier != null && moduleIdentifier.equals(IDENTIFIER);
    }

    @Override
    public Set<String> getIncludes(ClientLibrary library) {
        if (isViteDevServerEnabled()) {
            //TODO: fix if we use multiple categories
            Optional<ViteDevServerConfig> devServerConfig = getDevServerConfig(library.getCategories()[0]);
            if (devServerConfig.isPresent()) {
                return getViteDevModules(devServerConfig.get());
            } else {
                LOG.error("Vite dev server is enabled but no '{}' was set for: '{}'", PN_VITE_TARGET, library.getPath());
            }
        } else {
            //TODO: serve clientlibs using assets withing the library.
        }
        return Collections.emptySet();
    }

    private Optional<ViteDevServerConfig> getDevServerConfig(String category) {
        return devServerConfigurations.stream()
                                      .filter(c -> c.getCategory().equals(category))
                                      .findFirst();
    }

    @ObjectClassDefinition(name = "Vite Dev Server Integration Configuration")
    @interface Config {
        @AttributeDefinition(name = "Enable Vite Integration") boolean viteDevServerEnabled() default false;
    }

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
    }

    private boolean isViteDevServerEnabled() {
        return config.viteDevServerEnabled();
    }

    private Set<String> getViteDevModules(@NotNull ViteDevServerConfig config) {
        Set<String> libs = new LinkedHashSet<>();
        String viteDevServerUrl = getViteDevServerUrl(config);
        libs.add("<script type=\"module\" src=\"" + viteDevServerUrl + "@vite/client\"></script>");
        libs.add("<script type=\"module\" src=\"" + viteDevServerUrl + config.getEntry() + "\"></script>");
        return libs;
    }

    private String getViteDevServerUrl(ViteDevServerConfig config) {
        return String.format("%s://%s:%s/", config.getProtocol(), config.getHostname(), config.getPort());
    }
}
