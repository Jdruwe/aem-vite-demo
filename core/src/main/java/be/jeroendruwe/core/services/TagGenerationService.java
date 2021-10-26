package be.jeroendruwe.core.services;

import java.util.Map;
import java.util.Set;

public interface TagGenerationService {
    String PN_SCRIPTS = "scripts";
    String PN_PRELOADS = "preloads";
    String PN_STYLESHEETS = "stylesheets";

    Set<String> generateTags(Map<String, Object> props);
}
