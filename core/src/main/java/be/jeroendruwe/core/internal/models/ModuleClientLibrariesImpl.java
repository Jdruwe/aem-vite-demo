package be.jeroendruwe.core.internal.models;

import be.jeroendruwe.core.models.ModuleClientLibraries;
import be.jeroendruwe.core.services.ClientLibManagerConfigService;
import be.jeroendruwe.core.services.ClientLibManagerService;
import be.jeroendruwe.core.services.ModuleBasedClientLibService;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import com.adobe.granite.ui.clientlibs.HtmlLibraryManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = ModuleClientLibraries.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ModuleClientLibrariesImpl implements ModuleClientLibraries {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleClientLibrariesImpl.class);

    private static final String CLIENT_LIB_MANAGER_SERVICE = "client-lib-manager";
    private static final String PN_MODULE_IDENTIFIER = "moduleIdentifier";
    private static final String PN_VITE_TARGET = "viteTarget";

    @Inject
    @Named(OPTION_CATEGORIES)
    private Object categories;

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private HtmlLibraryManager htmlLibraryManager;

    @OSGiService
    private ResourceResolverFactory resolverFactory;

    @OSGiService
    private ClientLibManagerConfigService configService;

    @OSGiService
    private ClientLibManagerService clientLibManagerService;

    private Set<String> includes;

    @PostConstruct
    protected void initModel() {
        includes = new HashSet<>();
        List<ModuleBasedClientLibService> moduleBasedClientLibServices = clientLibManagerService.getModuleBasedClientLibServices();
        if (!moduleBasedClientLibServices.isEmpty()) {
            Collection<ClientLibrary> libraries = htmlLibraryManager.getLibraries(getCategoryNames(), null, true, false);
            for (ClientLibrary library : libraries) {
                Map<String, Object> properties = geClientLibraryProperties(library);
                for (ModuleBasedClientLibService moduleBasedClientLibService : moduleBasedClientLibServices) {
                    if (moduleBasedClientLibService.isApplicable(properties)) {
                        includes.addAll(moduleBasedClientLibService.getIncludes());
                        break;
                    }
                }
            }
        }
    }

    private String[] getCategoryNames() {
        Set<String> categoriesSet = getStrings(categories);
        return categoriesSet.toArray(new String[0]);
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

    private Map<String, Object> geClientLibraryProperties(ClientLibrary lib) {
        try (ResourceResolver resourceResolver = resolverFactory.getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, CLIENT_LIB_MANAGER_SERVICE))) {
            Resource clientLibResource = resourceResolver.getResource(lib.getPath());
            if (clientLibResource != null) {
                return new HashMap<>(clientLibResource.getValueMap());
            }
        } catch (LoginException e) {
            LOG.error("Cannot login as a service user", e);
        }
        return Collections.emptyMap();
    }

    @Override
    public String getIncludes() {
        return StringUtils.join(includes, StringUtils.EMPTY);
    }
}
