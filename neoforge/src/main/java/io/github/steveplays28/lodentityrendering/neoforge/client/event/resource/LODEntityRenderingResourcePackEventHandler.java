package io.github.steveplays28.lodentityrendering.neoforge.client.event.resource;

import io.github.steveplays28.lodentityrendering.LODEntityRendering;
import io.github.steveplays28.lodentityrendering.client.LODEntityRenderingClient;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = LODEntityRendering.MOD_ID, value = Dist.CLIENT)
public class LODEntityRenderingResourcePackEventHandler {
	private static final @NotNull String RESOURCE_PACKS_FOLDER = "resourcepacks";
	private static final @NotNull Identifier BUILT_IN_RESOURCE_PACK_ID =
			Identifier.of(LODEntityRendering.MOD_ID, String.format("%s/%s", RESOURCE_PACKS_FOLDER, LODEntityRenderingClient.BUILT_IN_RESOURCE_PACK_ID));

	@SubscribeEvent
	public static void onAddPackFinders(@NotNull AddPackFindersEvent event) {
		if (event.getPackType() != ResourceType.CLIENT_RESOURCES) {
			return;
		}

		// Register a built-in default resource pack
		// TODO: Enable the built-in default resource pack by default
		event.addPackFinders(BUILT_IN_RESOURCE_PACK_ID, ResourceType.CLIENT_RESOURCES, Text.literal(BUILT_IN_RESOURCE_PACK_ID.toString()), ResourcePackSource.BUILTIN, false,
				ResourcePackProfile.InsertionPosition.BOTTOM);
	}
}
