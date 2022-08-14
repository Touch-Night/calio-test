package io.github.apace100.calio.network;

import io.github.apace100.calio.registry.DataObjectRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class CalioNetworkingClient {

    public static void registerReceivers() {
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(
                CalioNetworking.SYNC_DATA_OBJECT_REGISTRY,
                CalioNetworkingClient::onDataObjectRegistrySync
            );
        }));
    }

    private static void onDataObjectRegistrySync(
        MinecraftClient minecraftClient,
        ClientPlayNetworkHandler clientPlayNetworkHandler,
        PacketByteBuf packetByteBuf,
        PacketSender packetSender) {
        Identifier registryId = packetByteBuf.readIdentifier();
        DataObjectRegistry.getRegistry(registryId).receive(packetByteBuf,
            minecraftClient.isIntegratedServerRunning() ? r -> {} : minecraftClient::execute);
        /*minecraftClient.execute(() -> {
            DataObjectRegistry.getRegistry(registryId).receive(packetByteBuf);
        });*/
    }
}
