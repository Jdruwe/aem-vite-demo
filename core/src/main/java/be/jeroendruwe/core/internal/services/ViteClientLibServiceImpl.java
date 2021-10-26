package be.jeroendruwe.core.internal.services;

import be.jeroendruwe.core.services.ModuleBasedClientLibService;
import be.jeroendruwe.core.services.TagGenerationService;
import be.jeroendruwe.core.services.ViteDevServerConfig;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.*;

@Component(service = ModuleBasedClientLibService.class)
@Designate(ocd = ViteClientLibServiceImpl.Config.class)
public class ViteClientLibServiceImpl implements ModuleBasedClientLibService {

    private static final String IDENTIFIER = "vite";

    private final List<ViteDevServerConfig> devServerConfigurations = new ArrayList<>();
    private Config config;

    @Reference
    private TagGenerationService tagGenerationService;

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
    public Set<String> getIncludes(ClientLibrary library, Map<String, Object> props) {
        if (isViteDevServerEnabled()) {
            return getViteDevModules(library);
        } else {
            return tagGenerationService.generateTags(props);
        }
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

    private Set<String> getViteDevModules(ClientLibrary library) {
        Set<String> libs = new LinkedHashSet<>();
        for (String category : library.getCategories()) {
            getDevServerConfig(category).ifPresent(c -> {
                String viteDevServerUrl = getViteDevServerUrl(c);
                libs.add("<script type=\"module\" src=\"" + viteDevServerUrl + "@vite/client\"></script>");
                libs.add("<script type=\"module\" src=\"" + viteDevServerUrl + c.getEntry() + "\"></script>");
            });
        }
        return libs;
    }

    private Optional<ViteDevServerConfig> getDevServerConfig(String category) {
        return devServerConfigurations.stream()
                                      .filter(c -> StringUtils.equals(c.getCategory(), category))
                                      .filter(this::isValidDevServerConfig)
                                      .findFirst();
    }

    private boolean isValidDevServerConfig(ViteDevServerConfig c) {
        return StringUtils.isNoneBlank(c.getEntry(), c.getProtocol(), c.getPort(), c.getHostname());
    }

    private String getViteDevServerUrl(ViteDevServerConfig config) {
        return String.format("%s://%s:%s/", config.getProtocol(), config.getHostname(), config.getPort());
    }
}
