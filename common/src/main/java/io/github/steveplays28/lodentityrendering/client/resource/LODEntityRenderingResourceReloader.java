package io.github.steveplays28.lodentityrendering.client.resource;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import io.github.steveplays28.lodentityrendering.LODEntityRendering;
import io.github.steveplays28.lodentityrendering.client.entity.color.EntityAverageColorRegistry;
import io.github.steveplays28.lodentityrendering.client.resource.json.EntityAverageColor;
import io.github.steveplays28.lodentityrendering.client.util.image.BufferedImageUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class LODEntityRenderingResourceReloader extends SinglePreparationResourceReloader<Void> {
	private static final @NotNull String JSON_FILE_SUFFIX = ".json";
	private static final @NotNull String PNG_FILE_SUFFIX = ".png";
	private static final @NotNull String ENTITY_TEXTURES_FOLDER_NAME = "textures/entity";
	private static final @NotNull String ENTITY_AVERAGE_COLORS_FOLDER_NAME = "average_colors/entity";

	/**
	 * The preparation stage, ran on worker threads.
	 */
	@Override
	protected @Nullable Void prepare(ResourceManager resourceManager, Profiler profiler) {
		// NO-OP
		return null;
	}

	/**
	 * The apply stage, ran on the main thread.
	 */
	@Override
	protected void apply(@Nullable Void prepared, ResourceManager resourceManager, Profiler profiler) {
		EntityAverageColorRegistry.ENTITY_AVERAGE_COLOR_REGISTRY.clear();
		loadAndRegisterAverageEntityTextureColorsFromJson(resourceManager);
		sampleAndRegisterAverageEntityTextureColorsFromEntityTextures(resourceManager);
	}

	private void loadAndRegisterAverageEntityTextureColorsFromJson(@NotNull ResourceManager resourceManager) {
		@NotNull final var averageEntityTextureColorJsonFiles = resourceManager.findResources(
				ENTITY_AVERAGE_COLORS_FOLDER_NAME, identifier -> identifier.toString().endsWith(JSON_FILE_SUFFIX));
		for (@NotNull final var averageEntityTextureColorJsonFilePath : averageEntityTextureColorJsonFiles.keySet()) {
			@NotNull final var entityTexturePathSplit = averageEntityTextureColorJsonFilePath.getPath().replace(JSON_FILE_SUFFIX, "").split(
					"/");
			@NotNull final var entityIdentifier = new Identifier(
					averageEntityTextureColorJsonFilePath.getNamespace(), entityTexturePathSplit[entityTexturePathSplit.length - 1]
			);
			if (EntityAverageColorRegistry.ENTITY_AVERAGE_COLOR_REGISTRY.containsKey(entityIdentifier)) {
				continue;
			}

			try {
				@NotNull final var gson = new Gson().newBuilder().setFieldNamingPolicy(
						FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
				EntityAverageColorRegistry.ENTITY_AVERAGE_COLOR_REGISTRY.put(entityIdentifier, gson.fromJson(
						averageEntityTextureColorJsonFiles.get(averageEntityTextureColorJsonFilePath).getReader(),
						EntityAverageColor.class
				).getAverageColor());
			} catch (IOException e) {
				LODEntityRendering.LOGGER.error(
						"Exception thrown while trying to load an entity's average color from JSON ({}):\n{}", entityIdentifier, e);
			}
		}
	}

	private void sampleAndRegisterAverageEntityTextureColorsFromEntityTextures(@NotNull ResourceManager resourceManager) {
		@NotNull final var entityTextures = resourceManager.findResources(
				ENTITY_TEXTURES_FOLDER_NAME, identifier -> identifier.toString().endsWith(PNG_FILE_SUFFIX));
		for (@NotNull final var entityTexturePath : entityTextures.keySet()) {
			@NotNull final var entityTexturePathSplit = entityTexturePath.getPath().replace(PNG_FILE_SUFFIX, "").split("/");
			String entityName;
			if (entityTexturePathSplit.length > 3) {
				entityName = entityTexturePathSplit[entityTexturePathSplit.length - 2];
			} else {
				entityName = entityTexturePathSplit[entityTexturePathSplit.length - 1];
			}

			@NotNull final var entityIdentifier = new Identifier(
					entityTexturePath.getNamespace(), entityName
			);
			if (EntityAverageColorRegistry.ENTITY_AVERAGE_COLOR_REGISTRY.containsKey(entityIdentifier)) {
				continue;
			}

			try {
				EntityAverageColorRegistry.ENTITY_AVERAGE_COLOR_REGISTRY.put(
						entityIdentifier,
						BufferedImageUtil.getAverageColor(ImageIO.read(entityTextures.get(entityTexturePath).getInputStream()))
				);
			} catch (IOException e) {
				LODEntityRendering.LOGGER.error(
						"Exception thrown while trying to load entity texture ({}):\n{}", entityIdentifier, e);
			}
		}
	}
}
