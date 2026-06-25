package io.github.steveplays28.lodentityrendering.neoforge;

import io.github.steveplays28.lodentityrendering.LODEntityRendering;
import io.github.steveplays28.lodentityrendering.client.LODEntityRenderingClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(LODEntityRendering.MOD_ID)
public class LODEntityRenderingNeoForge {
	public LODEntityRenderingNeoForge() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			LODEntityRenderingClient.initialize();
		}

		LODEntityRendering.initialize();
	}
}
