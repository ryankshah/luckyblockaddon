package com.liamkbryant.luckyblockaddon.registry;

import com.liamkbryant.luckyblockaddon.block.DisguisedLuckyBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModBlocks {
    public static final String MOD_ID = "luckyblockaddon";

    private static final Map<Block, Block> RUNTIME_DISGUISE_MAP = new HashMap<>();

    // Stone and Dirt blocks (3% replacement chance)
    public static final Block DISGUISED_STONE = registerDisguisedLuckyBlock("stone", Blocks.STONE);
    public static final Block DISGUISED_DIRT = registerDisguisedLuckyBlock("dirt", Blocks.DIRT);
    public static final Block DISGUISED_GRASS_BLOCK = registerDisguisedLuckyBlock("grass_block", Blocks.GRASS_BLOCK);
    public static final Block DISGUISED_COARSE_DIRT = registerDisguisedLuckyBlock("coarse_dirt", Blocks.COARSE_DIRT);
    public static final Block DISGUISED_GRAVEL = registerDisguisedLuckyBlock("gravel", Blocks.GRAVEL);

    // Wood blocks (5% replacement chance)
    public static final Block DISGUISED_BIRCH_LOG = registerDisguisedLuckyBlock("birch_log", Blocks.BIRCH_LOG);
    public static final Block DISGUISED_OAK_LOG = registerDisguisedLuckyBlock("oak_log", Blocks.OAK_LOG);
    public static final Block DISGUISED_SPRUCE_LOG = registerDisguisedLuckyBlock("spruce_log", Blocks.SPRUCE_LOG);
    public static final Block DISGUISED_ACACIA_LOG = registerDisguisedLuckyBlock("acacia_log", Blocks.ACACIA_LOG);
    public static final Block DISGUISED_CHERRY_LOG = registerDisguisedLuckyBlock("cherry_log", Blocks.CHERRY_LOG);
    public static final Block DISGUISED_DARK_OAK_LOG = registerDisguisedLuckyBlock("dark_oak_log", Blocks.DARK_OAK_LOG);
    public static final Block DISGUISED_JUNGLE_LOG = registerDisguisedLuckyBlock("jungle_log", Blocks.JUNGLE_LOG);
    public static final Block DISGUISED_MANGROVE_LOG = registerDisguisedLuckyBlock("mangrove_log", Blocks.MANGROVE_LOG);

    // Ore blocks (5% replacement chance)
    public static final Block DISGUISED_COAL_ORE = registerDisguisedLuckyBlock("coal_ore", Blocks.COAL_ORE);
    public static final Block DISGUISED_IRON_ORE = registerDisguisedLuckyBlock("iron_ore", Blocks.IRON_ORE);
    public static final Block DISGUISED_DIAMOND_ORE = registerDisguisedLuckyBlock("diamond_ore", Blocks.DIAMOND_ORE);
    public static final Block DISGUISED_GOLD_ORE = registerDisguisedLuckyBlock("gold_ore", Blocks.GOLD_ORE);
    public static final Block DISGUISED_EMERALD_ORE = registerDisguisedLuckyBlock("emerald_ore", Blocks.EMERALD_ORE);
    public static final Block DISGUISED_COPPER_ORE = registerDisguisedLuckyBlock("copper_ore", Blocks.COPPER_ORE);
    public static final Block DISGUISED_LAPIS_ORE = registerDisguisedLuckyBlock("lapis_ore", Blocks.LAPIS_ORE);
    public static final Block DISGUISED_REDSTONE_ORE = registerDisguisedLuckyBlock("redstone_ore", Blocks.REDSTONE_ORE);

    // Deepslate ores (5% replacement chance)
    public static final Block DISGUISED_DEEPSLATE_COAL_ORE = registerDisguisedLuckyBlock("deepslate_coal_ore", Blocks.DEEPSLATE_COAL_ORE);
    public static final Block DISGUISED_DEEPSLATE_IRON_ORE = registerDisguisedLuckyBlock("deepslate_iron_ore", Blocks.DEEPSLATE_IRON_ORE);
    public static final Block DISGUISED_DEEPSLATE_DIAMOND_ORE = registerDisguisedLuckyBlock("deepslate_diamond_ore", Blocks.DEEPSLATE_DIAMOND_ORE);
    public static final Block DISGUISED_DEEPSLATE_GOLD_ORE = registerDisguisedLuckyBlock("deepslate_gold_ore", Blocks.DEEPSLATE_GOLD_ORE);
    public static final Block DISGUISED_DEEPSLATE_EMERALD_ORE = registerDisguisedLuckyBlock("deepslate_emerald_ore", Blocks.DEEPSLATE_EMERALD_ORE);
    public static final Block DISGUISED_DEEPSLATE_COPPER_ORE = registerDisguisedLuckyBlock("deepslate_copper_ore", Blocks.DEEPSLATE_COPPER_ORE);
    public static final Block DISGUISED_DEEPSLATE_LAPIS_ORE = registerDisguisedLuckyBlock("deepslate_lapis_ore", Blocks.DEEPSLATE_LAPIS_ORE);
    public static final Block DISGUISED_DEEPSLATE_REDSTONE_ORE = registerDisguisedLuckyBlock("deepslate_redstone_ore", Blocks.DEEPSLATE_REDSTONE_ORE);

    // Hay block (10% replacement chance)
    public static final Block DISGUISED_HAY_BLOCK = registerDisguisedLuckyBlock("hay_block", Blocks.HAY_BLOCK);

    /**
     * Register a disguised lucky block and its corresponding item
     */
    private static Block registerDisguisedLuckyBlock(String name, Block vanillaBlock) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(vanillaBlock.defaultMapColor())
                .strength(vanillaBlock.defaultDestroyTime(), vanillaBlock.getExplosionResistance())
                .sound(vanillaBlock.getSoundType(vanillaBlock.defaultBlockState()));

        if (vanillaBlock.defaultBlockState().requiresCorrectToolForDrops()) {
            properties.requiresCorrectToolForDrops();
        }

        // Create the disguised block
        Block disguisedBlock = new DisguisedLuckyBlock(properties, vanillaBlock);

        // Register the block
        Block registeredBlock = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(MOD_ID, "disguised_" + name),
                disguisedBlock
        );

        // Register the corresponding block item
        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(MOD_ID, "disguised_" + name),
                new BlockItem(registeredBlock, new Item.Properties())
        );

        // Store the runtime mapping for reference
        RUNTIME_DISGUISE_MAP.put(registeredBlock, vanillaBlock);

        return registeredBlock;
    }

    /**
     * Initialize the registry
     */
    public static void init() {
        System.out.println("ModBlocks initialized - " + RUNTIME_DISGUISE_MAP.size() + " disguised lucky blocks registered");
    }

    /**
     * Get the vanilla block that this disguised block is imitating
     */
    public static Block getVanillaCounterpart(Block disguisedBlock) {
        return RUNTIME_DISGUISE_MAP.get(disguisedBlock);
    }

    /**
     * Get the disguised version of a vanilla block
     */
    public static Block getDisguisedVersion(Block vanillaBlock) {
        for (Map.Entry<Block, Block> entry : RUNTIME_DISGUISE_MAP.entrySet()) {
            if (entry.getValue() == vanillaBlock) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Check if a block is a disguised lucky block
     */
    public static boolean isDisguisedLuckyBlock(Block block) {
        return RUNTIME_DISGUISE_MAP.containsKey(block);
    }

    /**
     * Get replacement chance for a vanilla block type
     */
    public static float getReplacementChance(Block vanillaBlock) {
        if (vanillaBlock == Blocks.HAY_BLOCK) {
            return 0.1f; // 10%
        } else if (Set.of(Blocks.STONE, Blocks.DIRT, Blocks.GRASS_BLOCK,
                Blocks.COARSE_DIRT, Blocks.GRAVEL).contains(vanillaBlock)) {
            return 0.03f; // 3%
        } else if (Set.of(Blocks.BIRCH_LOG, Blocks.OAK_LOG, Blocks.SPRUCE_LOG,
                Blocks.ACACIA_LOG, Blocks.CHERRY_LOG, Blocks.DARK_OAK_LOG,
                Blocks.JUNGLE_LOG, Blocks.MANGROVE_LOG,
                Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.DIAMOND_ORE,
                Blocks.GOLD_ORE, Blocks.EMERALD_ORE, Blocks.COPPER_ORE,
                Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE,
                Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_IRON_ORE,
                Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_COPPER_ORE,
                Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE).contains(vanillaBlock)) {
            return 0.05f; // 5%
        }
        return 0f; // No replacement
    }

    /**
     * Get the number of registered disguised blocks
     */
    public static int getRegisteredBlockCount() {
        return RUNTIME_DISGUISE_MAP.size();
    }
}