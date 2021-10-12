package be.jeroendruwe.core.models.internal;

import be.jeroendruwe.core.models.ClientLibManager;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import com.adobe.granite.ui.clientlibs.HtmlLibraryManager;
import com.adobe.granite.ui.clientlibs.LibraryType;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = ClientLibManager.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ClientLibManagerImpl implements ClientLibManager {

    //TODO: we should use some kind of config mechanism to toggle between vite in dev mode and clientlib everywhere else.

    private static final Logger LOG = LoggerFactory.getLogger(ClientLibManagerImpl.class);

    private static final String CLIENT_LIB_MANAGER_SERVICE = "client-lib-manager";
    private static final String PN_VITE_TARGET = "viteTarget";

    @Inject
    @Named(OPTION_CATEGORIES)
    private Object categories;

    @OSGiService
    private HtmlLibraryManager htmlLibraryManager;

    @OSGiService
    private ResourceResolverFactory resolverFactory;

    private String[] categoriesArray;

    @PostConstruct
    protected void initModel() {
        Set<String> categoriesSet = getStrings(categories);
        categoriesArray = categoriesSet.toArray(new String[0]);
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
    public String getJsIncludes() {

        List<String> viteClientLibs = new ArrayList<>();

        Collection<ClientLibrary> libraries = htmlLibraryManager.getLibraries(categoriesArray, LibraryType.JS, true, false);
        for (ClientLibrary library : libraries) {
            Optional<String> viteTarget = getViteTarget(library);
            //TODO: move this dev server info into a config? + enabled state based conf .cfg.json env variable
            viteTarget.ifPresent(s -> viteClientLibs.add("<script type=\"module\" src=\"http://localhost:3000" + s + "\"></script>"));
        }

        if (!viteClientLibs.isEmpty()) {
            viteClientLibs.add("<script type=\"module\" src=\"http://localhost:3000/@vite/client\"></script>");
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
