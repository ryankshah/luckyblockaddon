package com.liamkbryant.luckyblockaddon.registry;

import com.liamkbryant.luckyblockaddon.LuckyBlockAddon;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUCKY_BLOCK_REPLACEMENT_KEY = 
        ResourceKey.create(Registries.CONFIGURED_FEATURE, 
            new ResourceLocation(LuckyBlockAddon.MOD_ID, "lucky_block_replacement"));

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        context.register(LUCKY_BLOCK_REPLACEMENT_KEY, 
            new ConfiguredFeature<>(ModFeatures.LUCKY_BLOCK_REPLACEMENT, 
                NoneFeatureConfiguration.INSTANCE));
    }
}