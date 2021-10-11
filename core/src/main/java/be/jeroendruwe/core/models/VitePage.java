package be.jeroendruwe.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.wcm.core.components.models.HtmlPageItem;
import com.adobe.cq.wcm.core.components.models.NavigationItem;
import com.adobe.cq.wcm.core.components.models.Page;
import com.day.cq.wcm.api.designer.Style;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = Page.class,
        resourceType = "aem-vite-demo/components/page"
)
public class VitePage implements Page {

    /**
     * ES module client library style property name.
     */
    protected static final String PN_CLIENTLIBS_ES_MODULE = "esModuleClientlibs";

    private String[] esModuleClientlibs;

    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected Style currentStyle;

    @Self
    @Via(type = ResourceSuperType.class)
    private Page page;

    public String[] getESModuleClientlibCategories() {
        if (esModuleClientlibs == null) {
            esModuleClientlibs = Optional.ofNullable(currentStyle)
                                         .map(style -> style.get(PN_CLIENTLIBS_ES_MODULE, String[].class))
                                         .map(Arrays::stream)
                                         .orElseGet(Stream::empty)
                                         .distinct()
                                         .toArray(String[]::new);
        }
        return Arrays.copyOf(esModuleClientlibs, esModuleClientlibs.length);
    }

    @Override
    public String getLanguage() {
        return Page.super.getLanguage();
    }

    @Override
    public Calendar getLastModifiedDate() {
        return Page.super.getLastModifiedDate();
    }

    @Override
    public String[] getKeywords() {
        return Page.super.getKeywords();
    }

    @Override
    public String getDesignPath() {
        return Page.super.getDesignPath();
    }

    @Override
    public String getStaticDesignPath() {
        return Page.super.getStaticDesignPath();
    }

    @Override
    public Map<String, String> getFavicons() {
        return Page.super.getFavicons();
    }

    @Override
    public String getTitle() {
        return Page.super.getTitle();
    }

    @Override
    public String getBrandSlug() {
        return Page.super.getBrandSlug();
    }

    @Override
    public String[] getClientLibCategories() {
        return Page.super.getClientLibCategories();
    }

    @Override
    public String[] getClientLibCategoriesJsBody() {
        return Page.super.getClientLibCategoriesJsBody();
    }

    @Override
    public String[] getClientLibCategoriesJsHead() {
        return Page.super.getClientLibCategoriesJsHead();
    }

    @Override
    public String getTemplateName() {
        return Page.super.getTemplateName();
    }

    @Override
    public @Nullable String getAppResourcesPath() {
        return Page.super.getAppResourcesPath();
    }

    @Override
    public String getCssClassNames() {
        return Page.super.getCssClassNames();
    }

    @Override
    public @Nullable NavigationItem getRedirectTarget() {
        return Page.super.getRedirectTarget();
    }

    @Override
    public boolean hasCloudconfigSupport() {
        return Page.super.hasCloudconfigSupport();
    }

    @Override
    public @NotNull Set<String> getComponentsResourceTypes() {
        return Page.super.getComponentsResourceTypes();
    }

    @Override
    public @NotNull String[] getExportedItemsOrder() {
        return Page.super.getExportedItemsOrder();
    }

    @Override
    public @NotNull Map<String, ? extends ComponentExporter> getExportedItems() {
        return Page.super.getExportedItems();
    }

    @Override
    public @NotNull String getExportedType() {
        return Page.super.getExportedType();
    }

    @Override
    public @Nullable String getMainContentSelector() {
        return Page.super.getMainContentSelector();
    }

    @Override
    public @Nullable List<HtmlPageItem> getHtmlPageItems() {
        return Page.super.getHtmlPageItems();
    }
}
