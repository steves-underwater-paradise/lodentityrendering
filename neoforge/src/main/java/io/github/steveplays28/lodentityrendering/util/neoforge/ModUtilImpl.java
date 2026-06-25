package io.github.steveplays28.lodentityrendering.util.neoforge;

import io.github.steveplays28.lodentityrendering.util.ModUtil;
import net.neoforged.fml.loading.LoadingModList;

/**
 * Implements {@link ModUtil}.
 */
@SuppressWarnings("unused")
public class ModUtilImpl {
	/**
	 * Checks if a mod is present during loading.
	 */
	public static boolean isModPresent(String id) {
		return LoadingModList.get().getModFileById(id) != null;
	}
}
