package be.jeroendruwe.core.internal.services;

import be.jeroendruwe.core.services.ClientLibManagerService;
import be.jeroendruwe.core.services.ModuleBasedClientLibService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.ArrayList;
import java.util.List;

@Component(service = ClientLibManagerService.class)
public class ClientLibManagerServiceImpl implements ClientLibManagerService {

    private List<ModuleBasedClientLibService> moduleBasedClientLibServices;

    @Reference(
            service = ModuleBasedClientLibService.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC
    )
    protected void bindModuleBasedClientLibService(ModuleBasedClientLibService moduleBasedClientLibService) {
        if (moduleBasedClientLibServices == null) {
            moduleBasedClientLibServices = new ArrayList<>();
        }
        moduleBasedClientLibServices.add(moduleBasedClientLibService);
    }

    protected synchronized void unbindModuleBasedClientLibService(ModuleBasedClientLibService moduleBasedClientLibService) {
        moduleBasedClientLibServices.remove(moduleBasedClientLibService);
    }

    @Override
    public List<ModuleBasedClientLibService> getModuleBasedClientLibServices() {
        return moduleBasedClientLibServices;
    }
}
