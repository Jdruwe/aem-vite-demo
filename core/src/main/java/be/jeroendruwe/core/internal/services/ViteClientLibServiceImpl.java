package be.jeroendruwe.core.internal.services;

import be.jeroendruwe.core.services.ModuleBasedClientLibService;
import be.jeroendruwe.core.services.ViteDevServerConfig;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.*;
import java.util.stream.Collectors;

@Component(service = ModuleBasedClientLibService.class)
@Designate(ocd = ViteClientLibServiceImpl.Config.class)
public class ViteClientLibServiceImpl implements ModuleBasedClientLibService {

    private static final String IDENTIFIER = "vite";

    private static final String PN_SCRIPTS = "scripts";
    private static final String PN_PRELOADS = "preloads";
    private static final String PN_STYLESHEETS = "stylesheets";

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
    public Set<String> getIncludes(ClientLibrary library, Map<String, Object> props) {
        if (isViteDevServerEnabled()) {
            return getViteDevModules(library);
        } else {
            return getViteModules(props);
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

    private Set<String> getViteModules(Map<String, Object> props) {
        Set<String> libs = new LinkedHashSet<>();
        libs.addAll(getScripts(props));
        libs.addAll(getPreloads(props));
        libs.addAll(getStylesheets(props));
        return libs;
    }

    private Set<String> getScripts(Map<String, Object> props) {
        String[] scripts = (String[]) props.get(PN_SCRIPTS);
        if (scripts != null) {
            return Arrays.stream(scripts).map(script -> "<script type=\"module\" crossorigin src=\"" + script + "\"></script>").collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    private Set<String> getPreloads(Map<String, Object> props) {
        String[] preloads = (String[]) props.get(PN_PRELOADS);
        if (preloads != null) {
            return Arrays.stream(preloads).map(preload -> "<link rel=\"modulepreload\" href=\"" + preload + "\">").collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    private Set<String> getStylesheets(Map<String, Object> props) {
        String[] stylesheets = (String[]) props.get(PN_STYLESHEETS);
        if (stylesheets != null) {
            return Arrays.stream(stylesheets).map(stylesheet -> "<link rel=\"stylesheet\" href=\"" + stylesheet + "\">").collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
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
