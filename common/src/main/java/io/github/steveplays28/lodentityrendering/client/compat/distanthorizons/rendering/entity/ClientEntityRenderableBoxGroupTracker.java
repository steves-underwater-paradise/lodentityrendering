package io.github.steveplays28.lodentityrendering.client.compat.distanthorizons.rendering.entity;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.enums.rendering.EDhApiBlockMaterial;
import com.seibel.distanthorizons.api.interfaces.render.IDhApiRenderableBoxGroup;
import com.seibel.distanthorizons.api.objects.math.DhApiVec3d;
import com.seibel.distanthorizons.api.objects.render.DhApiRenderableBox;
import dev.architectury.networking.NetworkManager;
import io.github.steveplays28.lodentityrendering.client.entity.color.EntityAverageColorRegistry;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityLoadPacket;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityTickPacket;
import io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity.LODEntityRenderingS2CEntityUnloadPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Map;

import static io.github.steveplays28.lodentityrendering.LODEntityRendering.MOD_ID;

@Environment(EnvType.CLIENT)
public class ClientEntityRenderableBoxGroupTracker {
	/**
	 * Prefix for {@link IDhApiRenderableBoxGroup} identifiers.
	 */
	private static final @NotNull String RENDERABLE_BOX_GROUP_IDENTIFIER_PREFIX = String.format("%s:entity", MOD_ID);
	/**
	 * Stores {@link Entity} IDs->{@link IDhApiRenderableBoxGroup}s.
	 */
	private static final @NotNull Map<Integer, IDhApiRenderableBoxGroup> RENDERABLE_BOX_GROUPS = new Object2ObjectOpenHashMap<>(50);

	public static void initialize() {
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, LODEntityRenderingS2CEntityLoadPacket.getId(), (buf, context) -> {
			var entityLoadPacket = new LODEntityRenderingS2CEntityLoadPacket(buf);
			startTrackingEntity(
					entityLoadPacket.getEntityId(), entityLoadPacket.getEntityTextureId(), entityLoadPacket.getEntityPosition(),
					entityLoadPacket.getEntityBoundingBoxMin(), entityLoadPacket.getEntityBoundingBoxMax()
			);
		});
		NetworkManager.registerReceiver(
				NetworkManager.Side.S2C, LODEntityRenderingS2CEntityUnloadPacket.getId(),
				(buf, context) -> stopTrackingEntity(new LODEntityRenderingS2CEntityUnloadPacket(buf).getEntityId())
		);
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, LODEntityRenderingS2CEntityTickPacket.getId(), (buf, context) -> {
			var entityTickPacket = new LODEntityRenderingS2CEntityTickPacket(buf);
			updateTrackingEntityPosition(entityTickPacket.getEntityId(), entityTickPacket.getEntityPosition());
		});
	}

	private static void startTrackingEntity(int entityId, @NotNull Identifier entityTextureIdentifier, @NotNull Vector3f entityPosition, @NotNull Vector3f entityBoundingBoxMin, @NotNull Vector3f entityBoundingBoxMax) {
		@Nullable var existingRenderableBoxGroup = RENDERABLE_BOX_GROUPS.get(entityId);
		if (existingRenderableBoxGroup != null) {
			existingRenderableBoxGroup.setOriginBlockPos(new DhApiVec3d(entityPosition.x(), entityPosition.y(), entityPosition.z()));
			return;
		}

		@Nullable var entityAverageTextureColor = EntityAverageColorRegistry.ENTITY_AVERAGE_COLOR_REGISTRY.get(entityTextureIdentifier);
		if (entityAverageTextureColor == null) {
			entityAverageTextureColor = Color.BLACK;
		}

		@NotNull final var renderableBoxGroup = DhApi.Delayed.customRenderObjectFactory.createForSingleBox(
				String.format(
						"%s/%s/%s", RENDERABLE_BOX_GROUP_IDENTIFIER_PREFIX, entityTextureIdentifier.getNamespace(),
						entityTextureIdentifier.getPath()
				),
				new DhApiRenderableBox(
						new DhApiVec3d(entityBoundingBoxMin.x(), entityBoundingBoxMin.y(), entityBoundingBoxMin.z()),
						new DhApiVec3d(entityBoundingBoxMax.x(), entityBoundingBoxMax.y(), entityBoundingBoxMax.z()),
						entityAverageTextureColor, EDhApiBlockMaterial.UNKNOWN
				)
		);
		renderableBoxGroup.setOriginBlockPos(new DhApiVec3d(entityPosition.x(), entityPosition.y(), entityPosition.z()));
		RENDERABLE_BOX_GROUPS.put(entityId, renderableBoxGroup);

		@Nullable final var renderRegister = DhApi.Delayed.worldProxy.getSinglePlayerLevel().getRenderRegister();
		if (renderRegister == null) {
			return;
		}

		renderRegister.add(renderableBoxGroup);
	}

	private static void stopTrackingEntity(int entityId) {
		@Nullable final var renderableBoxGroup = RENDERABLE_BOX_GROUPS.get(entityId);
		if (renderableBoxGroup == null) {
			return;
		}

		@Nullable final var renderRegister = DhApi.Delayed.worldProxy.getSinglePlayerLevel().getRenderRegister();
		if (renderRegister == null) {
			return;
		}

		renderRegister.remove(renderableBoxGroup.getId());
		RENDERABLE_BOX_GROUPS.remove(entityId);
	}

	private static void updateTrackingEntityPosition(int entityId, @NotNull Vector3f entityPosition) {
		@Nullable final var renderableBoxGroup = RENDERABLE_BOX_GROUPS.get(entityId);
		if (renderableBoxGroup == null) {
			return;
		}

		RENDERABLE_BOX_GROUPS.get(entityId).setOriginBlockPos(new DhApiVec3d(entityPosition.x(), entityPosition.y(), entityPosition.z()));
	}
}
