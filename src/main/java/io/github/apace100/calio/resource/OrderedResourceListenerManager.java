package io.github.apace100.calio.resource;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class OrderedResourceListenerManager {

	OrderedResourceListenerManager() {
	}

	public OrderedResourceListener.Registration register(ResourceType resourceType, IdentifiableResourceReloadListener resourceReloadListener) {
		ResourceManagerHelper.get(resourceType).registerReloadListener(resourceReloadListener);

		return new OrderedResourceListener.Registration(ResourceLoader.get(resourceType), resourceReloadListener);
	}
}
