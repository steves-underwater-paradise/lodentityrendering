package io.github.steveplays28.lodentityrendering.fabric.client;

import io.github.steveplays28.lodentityrendering.client.LODEntityRenderingClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import static io.github.steveplays28.lodentityrendering.LODEntityRendering.MOD_ID;
import static io.github.steveplays28.lodentityrendering.LODEntityRendering.MOD_NAME;
import static io.github.steveplays28.lodentityrendering.client.LODEntityRenderingClient.BUILT_IN_RESOURCE_PACK_ID;

public class LODEntityRenderingClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		var modContainer = FabricLoader.getInstance().getModContainer(MOD_ID);
		if (modContainer.isEmpty()) {
			throw new IllegalStateException(
					String.format("%s's mod container (mod ID: %s) is empty. %s is unable to register a built-in default resource pack.",
							MOD_NAME, MOD_ID, MOD_NAME
					));
		}

		// Register a built-in default resource pack
		ResourceManagerHelper.registerBuiltinResourcePack(
				Identifier.of(MOD_ID, BUILT_IN_RESOURCE_PACK_ID), modContainer.get(), ResourcePackActivationType.DEFAULT_ENABLED);
		LODEntityRenderingClient.initialize();
	}
}
