package be.jeroendruwe.core.internal.models;

import be.jeroendruwe.core.models.ClientLibManager;
import be.jeroendruwe.core.services.ClientLibManagerConfigService;
import com.adobe.cq.wcm.core.components.models.ClientLibraries;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import com.adobe.granite.ui.clientlibs.HtmlLibraryManager;
import com.adobe.granite.ui.clientlibs.LibraryType;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = ClientLibManager.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ClientLibManagerImpl implements ClientLibManager {

    private static final Logger LOG = LoggerFactory.getLogger(ClientLibManagerImpl.class);

    private static final String CLIENT_LIB_MANAGER_SERVICE = "client-lib-manager";
    private static final String PN_VITE_TARGET = "viteTarget";

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private HtmlLibraryManager htmlLibraryManager;

    @OSGiService
    private ResourceResolverFactory resolverFactory;

    @OSGiService
    private ClientLibManagerConfigService configService;

    private String includes;

    @PostConstruct
    protected void initModel() {
        this.includes = buildIncludes();
    }

    /**
     * Vite enabled -> load clientlibs based on categories using viteTarget, if no target is set, the clientLib will be ignored.
     * Vite disabled -> load clientlibs based on categories using ClientLibraries provided by Core Components.
     */
    private String buildIncludes() {
        if (configService.isViteEnabled()) {
            return getViteModules();
        } else {
            return getClientLibIncludes();
        }
    }

    private Set<String> getStrings(@Nullable final Object input) {
        Set<String> strings = new LinkedHashSet<>();
        if (input != null) {
            Class<?> aClass = input.getClass();
            if (Object[].class.isAssignableFrom(aClass)) {
                for (Object obj : (Object[]) input) {
                    if (obj != null) {
                        strings.add(obj.toString());
                    }
                }
            }
        }
        return strings;
    }

    @Override
    public String getIncludes() {
        return includes;
    }

    private String getClientLibIncludes() {
        ClientLibraries clientLibraries = request.adaptTo(ClientLibraries.class);
        if (clientLibraries != null) {
            return clientLibraries.getJsAndCssIncludes();
        } else {
            LOG.error("Could not adapt request to ClientLibraries");
            return null;
        }
    }

    private String getViteModules() {
        Set<String> categoriesSet = getStrings(request.getAttribute(OPTION_CATEGORIES));
        String[] categoriesArray = categoriesSet.toArray(new String[0]);

        List<String> viteClientLibs = new ArrayList<>();

        Collection<ClientLibrary> libraries = htmlLibraryManager.getLibraries(categoriesArray, LibraryType.JS, true, false);
        for (ClientLibrary library : libraries) {
            Optional<String> viteTarget = getViteTarget(library);
            viteTarget.ifPresent(s -> viteClientLibs.add("<script type=\"module\" src=\"" + configService.getServerUrl() + s + "\"></script>"));
        }

        if (!viteClientLibs.isEmpty()) {
            viteClientLibs.add("<script type=\"module\" src=\"" + configService.getServerUrl() + "@vite/client\"></script>");
            Collections.reverse(viteClientLibs);
        }

        return StringUtils.join(viteClientLibs, StringUtils.EMPTY);
    }

    private Optional<String> getViteTarget(ClientLibrary lib) {
        try (ResourceResolver resourceResolver = resolverFactory.getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, CLIENT_LIB_MANAGER_SERVICE))) {
            Resource clientLibResource = resourceResolver.getResource(lib.getPath());
            if (clientLibResource != null) {
                ValueMap valueMap = clientLibResource.getValueMap();
                String viteTarget = valueMap.get(PN_VITE_TARGET, String.class);
                if (StringUtils.isNotBlank(viteTarget)) {
                    return Optional.of(viteTarget);
                }
            }
        } catch (LoginException e) {
            LOG.error("Cannot login as a service user", e);
        }
        return Optional.empty();
    }
}
