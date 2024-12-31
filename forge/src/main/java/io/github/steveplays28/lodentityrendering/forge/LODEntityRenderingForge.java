package io.github.steveplays28.lodentityrendering.forge;

import io.github.steveplays28.lodentityrendering.LODEntityRendering;
import io.github.steveplays28.lodentityrendering.client.LODEntityRenderingClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(LODEntityRendering.MOD_ID)
public class LODEntityRenderingForge {
	public LODEntityRenderingForge() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			LODEntityRenderingClient.initialize();
		}

		LODEntityRendering.initialize();
	}
}
