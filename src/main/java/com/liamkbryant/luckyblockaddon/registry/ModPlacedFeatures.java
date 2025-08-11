package com.liamkbryant.luckyblockaddon.registry;

import com.liamkbryant.luckyblockaddon.LuckyBlockAddon;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> LUCKY_BLOCK_REPLACEMENT_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    new ResourceLocation(LuckyBlockAddon.MOD_ID, "lucky_block_replacement"));

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures =
                context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(LUCKY_BLOCK_REPLACEMENT_KEY,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModConfiguredFeatures.LUCKY_BLOCK_REPLACEMENT_KEY),
                        List.of(
                                RarityFilter.onAverageOnceEvery(5), // Place every 1 chunks on average
                                InSquarePlacement.spread(), // Spread randomly within chunk
                                BiomeFilter.biome() // Only place in valid biomes
                        )
                ));
    }
}