package io.github.apace100.calio.util;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Allows registering data resource listeners in a specified order, to prevent problems
 * due to mod loading order and inter-mod data dependencies.
 */
@Deprecated
public final class OrderedResourceListeners {

	private static final Set<Identifier> finalizedRegistrations = new HashSet<>();
	private static final HashMap<Identifier, Registration> registrations = new HashMap<>();

	public static Registration register(IdentifiableResourceReloadListener resourceReloadListener) {
		return new Registration(resourceReloadListener);
	}

	public static class Registration {

		private final IdentifiableResourceReloadListener resourceReloadListener;

		private Registration(IdentifiableResourceReloadListener resourceReloadListener) {
			this.resourceReloadListener = resourceReloadListener;
		}

		public Registration after(Identifier identifier) {
			ResourceLoader.get(ResourceType.SERVER_DATA).addReloaderOrdering(identifier, this.resourceReloadListener.getFabricId());
			return this;
		}

		public Registration before(Identifier identifier) {
			ResourceLoader.get(ResourceType.SERVER_DATA).addReloaderOrdering(this.resourceReloadListener.getFabricId(), identifier);
			return this;
		}

		public void complete() {
			ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(this.resourceReloadListener);
		}
	}
}
