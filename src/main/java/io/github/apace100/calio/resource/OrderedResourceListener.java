package io.github.apace100.calio.resource;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.HashSet;
import java.util.Set;

public class OrderedResourceListener implements ModInitializer {

	public static final String ENTRYPOINT_KEY = "calio:ordered-resource-listener";

	@Override
	public void onInitialize(ModContainer mod) {
		OrderedResourceListenerManager manager = new OrderedResourceListenerManager();
		QuiltLoader.getEntrypoints(ENTRYPOINT_KEY, OrderedResourceListenerInitializer.class).forEach(
				entrypoint -> {
					entrypoint.registerResourceListeners(manager);
				}
		);
	}

	public static class Registration {

		private final ResourceLoader resourceLoader;
		final Identifier id;
		final IdentifiableResourceReloadListener resourceReloadListener;
		final Set<Identifier> dependencies = new HashSet<>();

		Registration(ResourceLoader resourceLoader, IdentifiableResourceReloadListener listener) {
			this.id = listener.getFabricId();
			this.resourceLoader = resourceLoader;
			this.resourceReloadListener = listener;
		}

		public Registration after(String identifier) {
			return after(new Identifier(identifier));
		}

		public Registration after(Identifier identifier) {
			this.resourceLoader.addReloaderOrdering(identifier, this.id);
			dependencies.add(identifier);
			return this;
		}

		public Registration before(String identifier) {
			return before(new Identifier(identifier));
		}

		public Registration before(Identifier identifier) {
			this.resourceLoader.addReloaderOrdering(this.id, identifier);
			return this;
		}

		public void complete() {
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder(id.toString());
			builder.append("{depends_on=[");
			boolean first = true;
			for (Identifier afterId : dependencies) {
				builder.append(afterId);
				if (!first) {
					builder.append(',');
				}
				first = false;
			}
			builder.append("]}");
			return builder.toString();
		}
	}
}
