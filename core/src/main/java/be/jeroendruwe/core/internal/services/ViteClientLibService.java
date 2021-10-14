package be.jeroendruwe.core.internal.services;

import be.jeroendruwe.core.services.ModuleBasedClientLibService;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component(service = ModuleBasedClientLibService.class)
public class ViteClientLibService implements ModuleBasedClientLibService {

    private static final Logger LOG = LoggerFactory.getLogger(ViteClientLibService.class);

    private static final String IDENTIFIER = "vite";
    private static final String PN_VITE_TARGET = "viteTarget";

    private Config config;

    @Override
    public boolean isApplicable(Map<String, Object> props) {
        String moduleIdentifier = (String) props.get(PN_MODULE_IDENTIFIER);
        return moduleIdentifier != null && moduleIdentifier.equals(IDENTIFIER);
    }

    @Override
    public Set<String> getIncludes(ClientLibrary library, Map<String, Object> props) {
        if (isViteDevServerEnabled()) {
            Optional<String> viteTarget = getViteTarget(props);
            if (viteTarget.isPresent()) {
                return getViteDevModules(viteTarget.get());
            } else {
                LOG.error("Vite dev server is enabled but no '{}' was set for: '{}'", PN_VITE_TARGET, library.getPath());
            }
        } else {
            //TODO: serve clientlibs using assets withing the library.
        }
        return Collections.emptySet();
    }

    @ObjectClassDefinition(name = "Vite Dev Server Integration Configuration")
    @interface Config {
        @AttributeDefinition(name = "Enable Vite Integration") boolean viteDevServerEnabled() default false;

        @AttributeDefinition(name = "protocol") String protocol() default "http";

        @AttributeDefinition(name = "hostname") String hostname() default "localhost";

        @AttributeDefinition(name = "port") String port() default "3000";
    }

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
    }

    private boolean isViteDevServerEnabled() {
        return config.viteDevServerEnabled();
    }

    private Optional<String> getViteTarget(Map<String, Object> props) {
        String viteTarget = (String) props.get(PN_VITE_TARGET);
        if (StringUtils.isNotBlank(viteTarget)) {
            return Optional.of(viteTarget);
        }
        return Optional.empty();
    }

    private Set<String> getViteDevModules(@NotNull String viteTarget) {
        Set<String> libs = new HashSet<>();
        libs.add("<script type=\"module\" src=\"" + getViteDevServerUrl() + "@vite/client\"></script>");
        libs.add("<script type=\"module\" src=\"" + getViteDevServerUrl() + viteTarget + "\"></script>");
        return libs;
    }

    private String getViteDevServerUrl() {
        return String.format("%s://%s:%s/", config.protocol(), config.hostname(), config.port());
    }
}
