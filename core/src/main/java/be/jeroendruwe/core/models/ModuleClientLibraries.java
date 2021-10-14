package be.jeroendruwe.core.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface ModuleClientLibraries {
    String OPTION_CATEGORIES = "categories";

    default String getIncludes() {
        return null;
    }
}
