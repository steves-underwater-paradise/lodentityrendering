package io.github.steveplays28.lodentityrendering.mixin.server.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.steveplays28.lodentityrendering.server.event.world.entity.LODEntityRenderingServerWorldEntityEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract @NotNull ServerWorld getServerWorld();

	protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@WrapOperation(method = "changeGameMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;changeGameMode(Lnet/minecraft/world/GameMode;)Z"))
	private boolean lodentityrendering$stopRenderingSpectatorModePlayers(ServerPlayerInteractionManager instance, @NotNull GameMode gameMode, @NotNull Operation<Boolean> original) {
		final var wasPreviouslyPartOfGame = this.isPartOfGame();
		final var result = original.call(instance, gameMode);
		if (wasPreviouslyPartOfGame && gameMode == GameMode.SPECTATOR) {
			LODEntityRenderingServerWorldEntityEvent.ENTITY_UNLOAD.invoker().onUnload(
					this.getServerWorld(), (ServerPlayerEntity) (Object) this);
		} else if (!wasPreviouslyPartOfGame && gameMode != GameMode.SPECTATOR) {
			LODEntityRenderingServerWorldEntityEvent.ENTITY_LOAD.invoker().onLoad(
					this.getServerWorld(), (ServerPlayerEntity) (Object) this);
		}
		return result;
	}
}
