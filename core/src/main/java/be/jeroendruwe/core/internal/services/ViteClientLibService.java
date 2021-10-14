package be.jeroendruwe.core.internal.services;

import be.jeroendruwe.core.services.ModuleBasedClientLibService;
import org.osgi.service.component.annotations.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Component(service = ModuleBasedClientLibService.class)
public class ViteClientLibService implements ModuleBasedClientLibService {

    private static final String IDENTIFIER = "vite";

    @Override
    public boolean isApplicable(Map<String, Object> props) {
        String moduleIdentifier = (String) props.get(PN_MODULE_IDENTIFIER);
        return moduleIdentifier != null && moduleIdentifier.equals(IDENTIFIER);
    }

    @Override
    public Set<String> getIncludes() {
        return Collections.emptySet();
    }

    //    String viteTarget = valueMap.get(PN_MODULE_IDENTIFIER, String.class);
//                if (StringUtils.isNotBlank(viteTarget)) {
//        return Optional.of(viteTarget);
//    }

//    private String getViteModules() {
//        Set<String> categoriesSet = getStrings(categories);
//        String[] categoriesArray = categoriesSet.toArray(new String[0]);
//
//        List<String> viteClientLibs = new ArrayList<>();
//
//        Collection<ClientLibrary> libraries = htmlLibraryManager.getLibraries(categoriesArray, LibraryType.JS, true, false);
//        for (ClientLibrary library : libraries) {
//            Optional<String> viteTarget = getViteTarget(library);
//            viteTarget.ifPresent(s -> viteClientLibs.add("<script type=\"module\" src=\"" + configService.getServerUrl() + s + "\"></script>"));
//        }
//
//        if (!viteClientLibs.isEmpty()) {
//            viteClientLibs.add("<script type=\"module\" src=\"" + configService.getServerUrl() + "@vite/client\"></script>");
//            Collections.reverse(viteClientLibs);
//        }
//
//        return StringUtils.join(viteClientLibs, StringUtils.EMPTY);
//    }
//
//    private Optional<String> getViteTarget(ClientLibrary lib) {
//        try (ResourceResolver resourceResolver = resolverFactory.getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, CLIENT_LIB_MANAGER_SERVICE))) {
//            Resource clientLibResource = resourceResolver.getResource(lib.getPath());
//            if (clientLibResource != null) {
//                ValueMap valueMap = clientLibResource.getValueMap();
//                String viteTarget = valueMap.get(PN_VITE_TARGET, String.class);
//                if (StringUtils.isNotBlank(viteTarget)) {
//                    return Optional.of(viteTarget);
//                }
//            }
//        } catch (LoginException e) {
//            LOG.error("Cannot login as a service user", e);
//        }
//        return Optional.empty();
//    }
}
