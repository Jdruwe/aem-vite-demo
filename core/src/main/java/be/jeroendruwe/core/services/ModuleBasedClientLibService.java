package be.jeroendruwe.core.services;

import com.adobe.granite.ui.clientlibs.ClientLibrary;

import java.util.Map;
import java.util.Set;

public interface ModuleBasedClientLibService {
    String PN_MODULE_IDENTIFIER = "moduleIdentifier";

    boolean isApplicable(Map<String, Object> props);

    Set<String> getIncludes(ClientLibrary clientLibrary, Map<String, Object> props);
}
