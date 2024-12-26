package io.github.steveplays28.lodentityrendering.server.entity;

import dev.architectury.networking.NetworkManager;
import io.github.steveplays28.lodentityrendering.LODEntityRendering;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityTickPacket;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityUnloadPacket;
import io.github.steveplays28.lodentityrendering.server.event.world.entity.LODEntityRenderingServerWorldEntityEvent;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityLoadPacket;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class LODEntityRenderingServerEntityTracker {
	private static final @NotNull Identifier FALLBACK_ENTITY_TEXTURE_ID = new Identifier(LODEntityRendering.MOD_ID, "fallback_entity_texture_id");

	static {
		LODEntityRenderingServerWorldEntityEvent.ENTITY_LOAD.register(LODEntityRenderingServerEntityTracker::onEntityLoad);
		LODEntityRenderingServerWorldEntityEvent.ENTITY_UNLOAD.register(LODEntityRenderingServerEntityTracker::onEntityUnload);
		LODEntityRenderingServerWorldEntityEvent.ENTITY_TICK.register(LODEntityRenderingServerEntityTracker::onEntityTick);
	}

	private static void onEntityLoad(@NotNull ServerWorld serverWorld, @NotNull Entity entity) {
		if (entity instanceof ServerPlayerEntity serverPlayerEntity && !serverPlayerEntity.isPartOfGame()) {
			return;
		}

		@Nullable var entityTextureId = entity.getType().arch$registryName();
		if (entityTextureId == null) {
			entityTextureId = FALLBACK_ENTITY_TEXTURE_ID;
		}

		@NotNull final var entityPosition = entity.getPos().toVector3f();
		final var entityPositionX = entityPosition.x();
		final var entityPositionY = entityPosition.y();
		final var entityPositionZ = entityPosition.z();
		@NotNull final var entityBoundingBox = entity.getBoundingBox();
		NetworkManager.sendToPlayers(
				serverWorld.getPlayers(), LODEntityRenderingS2CEntityLoadPacket.getId(),
				new LODEntityRenderingS2CEntityLoadPacket(
						entity.getId(), entityTextureId, entity.getPos().toVector3f(),
						new Vector3f(
								(float) entityBoundingBox.minX - entityPositionX,
								(float) entityBoundingBox.minY - entityPositionY,
								(float) entityBoundingBox.minZ - entityPositionZ
						),
						new Vector3f(
								(float) entityBoundingBox.maxX - entityPositionX,
								(float) entityBoundingBox.maxY - entityPositionY,
								(float) entityBoundingBox.maxZ - entityPositionZ
						)
				).writeBuf()
		);
	}

	private static void onEntityUnload(@NotNull ServerWorld serverWorld, @NotNull Entity entity) {
		NetworkManager.sendToPlayers(
				serverWorld.getPlayers(), LODEntityRenderingS2CEntityUnloadPacket.getId(), new LODEntityRenderingS2CEntityUnloadPacket(entity.getId()).writeBuf());
	}

	private static void onEntityTick(@NotNull ServerWorld serverWorld, @NotNull Entity entity) {
		NetworkManager.sendToPlayers(
				serverWorld.getPlayers(), LODEntityRenderingS2CEntityTickPacket.getId(),
				new LODEntityRenderingS2CEntityTickPacket(entity.getId(), entity.getPos().toVector3f()).writeBuf()
		);
	}
}
