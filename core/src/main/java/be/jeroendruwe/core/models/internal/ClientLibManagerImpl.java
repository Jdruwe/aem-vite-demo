package be.jeroendruwe.core.models.internal;

import be.jeroendruwe.core.models.ClientLibManager;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import com.adobe.granite.ui.clientlibs.HtmlLibraryManager;
import com.adobe.granite.ui.clientlibs.LibraryType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.jetbrains.annotations.Nullable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = ClientLibManager.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ClientLibManagerImpl implements ClientLibManager {

    @Inject
    @Named(OPTION_CATEGORIES)
    private Object categories;

    @OSGiService
    private HtmlLibraryManager htmlLibraryManager;

    private String[] categoriesArray;

    //TODO: adapt to the model if needed!
//    private ClientLibraries clientLibraries;

    @PostConstruct
    protected void initModel() {
        Set<String> categoriesSet = getStrings(categories);
        categoriesArray = categoriesSet.toArray(new String[0]);
        System.out.println();
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
        Collection<ClientLibrary> libraries = htmlLibraryManager.getLibraries(categoriesArray, LibraryType.JS, true, false);

        return null;
    }

    //TODO: use resourceResolverFactory and custom user with the correct rights
    private boolean clientlibHasProperty(ClientLibrary lib, String property) {
        Resource libResource = resourceResolver.resolve(lib.getPath());

        if (!ResourceUtil.isNonExistingResource(libResource)) {
            ValueMap libProps = libResource.getValueMap();

            return Boolean.TRUE.equals(libProps.get(property, Boolean.class));
        }

        return false;
    }
}
