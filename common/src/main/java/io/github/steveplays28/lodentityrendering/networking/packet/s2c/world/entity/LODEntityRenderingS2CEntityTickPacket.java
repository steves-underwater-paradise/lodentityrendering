package io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import static io.github.steveplays28.lodentityrendering.LODEntityRendering.MOD_ID;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class LODEntityRenderingS2CEntityTickPacket implements CustomPayload {
	public static final @NotNull Id<LODEntityRenderingS2CEntityTickPacket> IDENTIFIER = new Id<>(Identifier.of(MOD_ID, "entity_tick_packet"));
	public static final @NotNull PacketCodec<RegistryByteBuf, LODEntityRenderingS2CEntityTickPacket> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, LODEntityRenderingS2CEntityTickPacket::getEntityId,
			PacketCodecs.VECTOR3F, LODEntityRenderingS2CEntityTickPacket::getEntityPosition, LODEntityRenderingS2CEntityTickPacket::new);

	private final int entityId;
	private final @NotNull Vector3f entityPosition;

	public LODEntityRenderingS2CEntityTickPacket(int entityId, @NotNull Vector3f entityPosition) {
		this.entityId = entityId;
		this.entityPosition = entityPosition;
	}

	public LODEntityRenderingS2CEntityTickPacket(@NotNull PacketByteBuf buf) {
		this.entityId = buf.readInt();
		this.entityPosition = buf.readVector3f();
	}

	@Override
	public @NotNull Id<LODEntityRenderingS2CEntityTickPacket> getId() {
		return IDENTIFIER;
	}

	public int getEntityId() {
		return entityId;
	}

	public @NotNull Vector3f getEntityPosition() {
		return entityPosition;
	}
}
