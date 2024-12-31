package io.github.steveplays28.lodentityrendering.forge.client.event.resource;

import io.github.steveplays28.lodentityrendering.LODEntityRendering;
import io.github.steveplays28.lodentityrendering.client.LODEntityRenderingClient;
import io.github.steveplays28.lodentityrendering.forge.client.util.resource.ResourcePackUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.steveplays28.lodentityrendering.LODEntityRendering.*;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = LODEntityRendering.MOD_ID, value = Dist.CLIENT)
public class LODEntityRenderingResourcePackEventHandler {
	private static final @NotNull String RESOURCE_PACKS_FOLDER = "resourcepacks";
	private static final @NotNull Identifier BUILT_IN_RESOURCE_PACK_ID = new Identifier(LODEntityRendering.MOD_ID, LODEntityRenderingClient.BUILT_IN_RESOURCE_PACK_ID);

	@SubscribeEvent
	public static void onAddPackFinders(@NotNull AddPackFindersEvent event) {
		LODEntityRendering.LOGGER.info("IT DOES SOMETHING - LODENTITYRENDERING");
		if (event.getPackType() != ResourceType.CLIENT_RESOURCES) {
			return;
		}

		// Register a built-in default resource pack
		LODEntityRendering.LOGGER.info("LOADING RESOURCE PACK FROM LODENTITYRENDERING");
		@NotNull final ResourcePackProfile.PackFactory resourcePackFactory = id -> new PathPackResources(id, true,
				ResourcePackUtil.getResourcePackInformation(BUILT_IN_RESOURCE_PACK_ID).getFile().findResource(RESOURCE_PACKS_FOLDER, LODEntityRenderingClient.BUILT_IN_RESOURCE_PACK_ID));
		@Nullable final var resourcePackMetadata = ResourcePackProfile.loadMetadata(LODEntityRenderingClient.BUILT_IN_RESOURCE_PACK_ID, resourcePackFactory);
		if (resourcePackMetadata == null) {
			LODEntityRendering.LOGGER.error("Error occurred while trying to register {}'s default built-in resource pack ({}): resourcePackMetadata == null\n{}", MOD_NAME, BUILT_IN_RESOURCE_PACK_ID,
					new Throwable().getStackTrace());
			return;
		}

		// TODO: Enable the built-in default resource pack by default
		MinecraftClient.getInstance().getResourcePackManager()
				.addPackFinder(profileAdder -> profileAdder.accept(ResourcePackProfile.of(LODEntityRendering.MOD_ID, Text.literal(BUILT_IN_RESOURCE_PACK_ID.toString()), false, resourcePackFactory,
						resourcePackMetadata, ResourceType.CLIENT_RESOURCES, ResourcePackProfile.InsertionPosition.BOTTOM, false, ResourcePackSource.BUILTIN)));
	}
}
