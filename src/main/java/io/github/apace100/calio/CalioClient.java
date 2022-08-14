package io.github.apace100.calio;

import io.github.apace100.calio.network.CalioNetworkingClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class CalioClient implements ClientModInitializer {

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient(ModContainer mod) {
        CalioNetworkingClient.registerReceivers();
    }
}
