package io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity;

import io.github.steveplays28.lodentityrendering.LODEntityRendering;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class LODEntityRenderingS2CEntityLoadPacket implements CustomPayload {
	public static final @NotNull Id<LODEntityRenderingS2CEntityLoadPacket> IDENTIFIER = new Id<>(Identifier.of(LODEntityRendering.MOD_ID, "entity_load_packet"));
	public static final @NotNull PacketCodec<RegistryByteBuf, LODEntityRenderingS2CEntityLoadPacket> CODEC =
			PacketCodec.tuple(PacketCodecs.INTEGER, LODEntityRenderingS2CEntityLoadPacket::getEntityId, Identifier.PACKET_CODEC, LODEntityRenderingS2CEntityLoadPacket::getEntityTextureId,
					PacketCodecs.VECTOR3F, LODEntityRenderingS2CEntityLoadPacket::getEntityPosition, PacketCodecs.VECTOR3F, LODEntityRenderingS2CEntityLoadPacket::getEntityBoundingBoxMin,
					PacketCodecs.VECTOR3F, LODEntityRenderingS2CEntityLoadPacket::getEntityBoundingBoxMax, LODEntityRenderingS2CEntityLoadPacket::new);

	private final int entityId;
	private final @NotNull Identifier entityTextureId;
	private final @NotNull Vector3f entityPosition;
	private final @NotNull Vector3f entityBoundingBoxMin;
	private final @NotNull Vector3f entityBoundingBoxMax;

	public LODEntityRenderingS2CEntityLoadPacket(int entityId, @NotNull Identifier entityTextureId, @NotNull Vector3f entityPosition, @NotNull Vector3f entityBoundingBoxMin,
			@NotNull Vector3f entityBoundingBoxMax) {
		this.entityId = entityId;
		this.entityTextureId = entityTextureId;
		this.entityPosition = entityPosition;
		this.entityBoundingBoxMin = entityBoundingBoxMin;
		this.entityBoundingBoxMax = entityBoundingBoxMax;
	}

	public LODEntityRenderingS2CEntityLoadPacket(@NotNull PacketByteBuf buf) {
		this.entityId = buf.readInt();
		this.entityTextureId = buf.readIdentifier();
		this.entityPosition = buf.readVector3f();
		this.entityBoundingBoxMin = buf.readVector3f();
		this.entityBoundingBoxMax = buf.readVector3f();
	}

	@Override
	public @NotNull Id<LODEntityRenderingS2CEntityLoadPacket> getId() {
		return IDENTIFIER;
	}

	public int getEntityId() {
		return entityId;
	}

	public @NotNull Identifier getEntityTextureId() {
		return entityTextureId;
	}

	public @NotNull Vector3f getEntityPosition() {
		return entityPosition;
	}

	public @NotNull Vector3f getEntityBoundingBoxMin() {
		return entityBoundingBoxMin;
	}

	public @NotNull Vector3f getEntityBoundingBoxMax() {
		return entityBoundingBoxMax;
	}
}
