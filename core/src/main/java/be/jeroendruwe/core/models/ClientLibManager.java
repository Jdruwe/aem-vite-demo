package be.jeroendruwe.core.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface ClientLibManager {
    String OPTION_CATEGORIES = "categories";

    default String getJsIncludes() {
        return null;
    }
}
