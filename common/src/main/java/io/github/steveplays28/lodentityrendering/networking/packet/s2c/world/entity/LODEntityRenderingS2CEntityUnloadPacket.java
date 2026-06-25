package io.github.steveplays28.lodentityrendering.networking.packet.s2c.world.entity;

import io.github.steveplays28.lodentityrendering.LODEntityRendering;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class LODEntityRenderingS2CEntityUnloadPacket implements CustomPayload {
	public static final @NotNull Id<LODEntityRenderingS2CEntityUnloadPacket> IDENTIFIER = new Id<>(Identifier.of(LODEntityRendering.MOD_ID, "entity_unload_packet"));
	public static final @NotNull PacketCodec<RegistryByteBuf, LODEntityRenderingS2CEntityUnloadPacket> CODEC =
			PacketCodec.tuple(PacketCodecs.INTEGER, LODEntityRenderingS2CEntityUnloadPacket::getEntityId, LODEntityRenderingS2CEntityUnloadPacket::new);

	private final int entityId;

	public LODEntityRenderingS2CEntityUnloadPacket(int entityId) {
		this.entityId = entityId;
	}

	public LODEntityRenderingS2CEntityUnloadPacket(@NotNull PacketByteBuf buf) {
		this.entityId = buf.readInt();
	}

	@Override
	public @NotNull Id<LODEntityRenderingS2CEntityUnloadPacket> getId() {
		return IDENTIFIER;
	}

	public int getEntityId() {
		return entityId;
	}
}
