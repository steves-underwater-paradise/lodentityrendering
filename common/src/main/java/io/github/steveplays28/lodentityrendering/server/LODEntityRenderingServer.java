package io.github.steveplays28.lodentityrendering.server;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.networking.NetworkManager;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityLoadPacket;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityTickPacket;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityUnloadPacket;
import io.github.steveplays28.lodentityrendering.server.entity.LODEntityRenderingServerEntityTracker;

public class LODEntityRenderingServer {
	@SuppressWarnings("unused") private static LODEntityRenderingServerEntityTracker serverEntityTracker;

	public static void initialize() {
		registerS2CPayloadTypes();
		LifecycleEvent.SERVER_STARTING.register(instance -> serverEntityTracker = new LODEntityRenderingServerEntityTracker());
		LifecycleEvent.SERVER_STOPPED.register(instance -> serverEntityTracker = null);
	}

	private static void registerS2CPayloadTypes() {
		NetworkManager.registerS2CPayloadType(LODEntityRenderingS2CEntityLoadPacket.IDENTIFIER, LODEntityRenderingS2CEntityLoadPacket.CODEC);
		NetworkManager.registerS2CPayloadType(LODEntityRenderingS2CEntityUnloadPacket.IDENTIFIER, LODEntityRenderingS2CEntityUnloadPacket.CODEC);
		NetworkManager.registerS2CPayloadType(LODEntityRenderingS2CEntityTickPacket.IDENTIFIER, LODEntityRenderingS2CEntityTickPacket.CODEC);
	}
}
