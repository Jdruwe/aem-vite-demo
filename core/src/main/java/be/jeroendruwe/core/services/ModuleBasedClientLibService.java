package be.jeroendruwe.core.services;

import java.util.Map;
import java.util.Set;

public interface ModuleBasedClientLibService {
    String PN_MODULE_IDENTIFIER = "moduleIdentifier";

    boolean isApplicable(Map<String, Object> props);

    Set<String> getIncludes();
}
