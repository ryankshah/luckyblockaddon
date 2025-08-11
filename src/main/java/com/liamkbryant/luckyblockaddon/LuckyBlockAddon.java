package com.liamkbryant.luckyblockaddon;

import com.liamkbryant.luckyblockaddon.registry.ModBlocks;
import com.liamkbryant.luckyblockaddon.registry.ModFeatures;
import com.liamkbryant.luckyblockaddon.registry.ModPlacedFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuckyBlockAddon implements ModInitializer {
	public static final String MOD_ID = "luckyblockaddon";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("=== LuckyBlockAddon initializing ===");

		// Register blocks
		ModBlocks.init();

		// Register world generation features
		ModFeatures.registerFeatures();
		LOGGER.info("Features registered");

		// Add world generation to biomes
		BiomeModifications.addFeature(
				BiomeSelectors.all(),
				GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
				ModPlacedFeatures.LUCKY_BLOCK_REPLACEMENT_KEY
		);
		LOGGER.info("Biome modifications added");

		LOGGER.info("=== LuckyBlockAddon initialized successfully ===");
	}
}