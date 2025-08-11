package com.liamkbryant.luckyblockaddon.registry;

import com.liamkbryant.luckyblockaddon.LuckyBlockAddon;
import com.liamkbryant.luckyblockaddon.worldgen.LuckyBlockReplacementFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ModFeatures {
    public static final Feature<NoneFeatureConfiguration> LUCKY_BLOCK_REPLACEMENT =
        Registry.register(
            BuiltInRegistries.FEATURE,
            new ResourceLocation(LuckyBlockAddon.MOD_ID, "lucky_block_replacement"),
            new LuckyBlockReplacementFeature(NoneFeatureConfiguration.CODEC)
        );

    public static void registerFeatures() {
        // Features are registered when this class is loaded
    }
}