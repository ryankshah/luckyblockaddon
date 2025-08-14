package com.liamkbryant.luckyblockaddon;

import com.liamkbryant.luckyblockaddon.config.LuckyBlockConfig;
import com.liamkbryant.luckyblockaddon.registry.ModBlocks;
import com.liamkbryant.luckyblockaddon.registry.ModFeatures;
import com.liamkbryant.luckyblockaddon.registry.ModPlacedFeatures;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuckyBlockAddon implements ModInitializer {
	public static final String MOD_ID = "luckyblockaddon";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static LuckyBlockConfig CONFIG;

	@Override
	public void onInitialize() {
		LOGGER.info("=== LuckyBlockAddon initializing ===");

		// Initialize config FIRST
		AutoConfig.register(LuckyBlockConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(LuckyBlockConfig.class).getConfig();
		LOGGER.info("Config loaded successfully");

		// Register blocks
		ModBlocks.init();

		// Register world generation features
		ModFeatures.registerFeatures();
		LOGGER.info("Features registered");

		// Add world generation to biomes
		BiomeModifications.addFeature(
				BiomeSelectors.all(),
				GenerationStep.Decoration.UNDERGROUND_DECORATION,
				ModPlacedFeatures.LUCKY_BLOCK_REPLACEMENT_UNDERGROUND_KEY
		);
		BiomeModifications.addFeature(
				BiomeSelectors.all(),
				GenerationStep.Decoration.VEGETAL_DECORATION,
				ModPlacedFeatures.LUCKY_BLOCK_REPLACEMENT_VEGETAL_KEY
		);
		LOGGER.info("Biome modifications added");

		LOGGER.info("=== LuckyBlockAddon initialized successfully ===");
	}

	/**
	 * Get the current config instance
	 */
	public static LuckyBlockConfig getConfig() {
		return CONFIG;
	}

	/**
	 * Reload config from disk
	 */
	public static void reloadConfig() {
		AutoConfig.getConfigHolder(LuckyBlockConfig.class).load();
		CONFIG = AutoConfig.getConfigHolder(LuckyBlockConfig.class).getConfig();
		LOGGER.info("Config reloaded");
	}
}