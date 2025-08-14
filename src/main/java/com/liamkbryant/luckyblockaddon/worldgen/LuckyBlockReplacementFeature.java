package com.liamkbryant.luckyblockaddon.worldgen;

import com.liamkbryant.luckyblockaddon.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Set;

public class LuckyBlockReplacementFeature extends Feature<NoneFeatureConfiguration> {
    // Define the vanilla blocks we want to replace
    private static final Set<Block> STONE_DIRT_BLOCKS = Set.of(
            Blocks.STONE, Blocks.DIRT, Blocks.GRASS_BLOCK,
            Blocks.COARSE_DIRT, Blocks.GRAVEL
    );

    private static final Set<Block> WOOD_BLOCKS = Set.of(
            Blocks.BIRCH_LOG, Blocks.OAK_LOG, Blocks.SPRUCE_LOG,
            Blocks.ACACIA_LOG, Blocks.CHERRY_LOG, Blocks.DARK_OAK_LOG,
            Blocks.JUNGLE_LOG, Blocks.MANGROVE_LOG
    );

    private static final Set<Block> ORE_BLOCKS = Set.of(
            Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.DIAMOND_ORE,
            Blocks.GOLD_ORE, Blocks.EMERALD_ORE, Blocks.COPPER_ORE,
            Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE,
            Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE
    );

    public LuckyBlockReplacementFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        boolean replacedAny = false;
        int blocksChecked = 0;
        int blocksReplaced = 0;

        // Process a reasonable area around the origin
        int radius = 8; // Smaller radius for debugging
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -60; y <= 80; y++) { // Focus on common Y levels
                    BlockPos pos = origin.offset(x, y, z);

                    if (!level.isOutsideBuildHeight(pos)) {
                        BlockState currentState = level.getBlockState(pos);
                        Block currentBlock = currentState.getBlock();
                        blocksChecked++;

                        // Get replacement chance and disguised block
                        float replacementChance = ModBlocks.getReplacementChance(currentBlock);

                        if (replacementChance > 0) {
                            if (random.nextFloat() < replacementChance) {
                                Block disguisedBlock = Blocks.SEA_LANTERN; //ModBlocks.getDisguisedVersion(currentBlock);

                                if (disguisedBlock != null) {
                                    BlockState replacementState = disguisedBlock.defaultBlockState();
                                    level.setBlock(pos, replacementState, 3);
                                    blocksReplaced++;
                                    replacedAny = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return replacedAny;
    }
}