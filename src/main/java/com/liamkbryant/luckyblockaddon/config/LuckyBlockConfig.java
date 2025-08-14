package com.liamkbryant.luckyblockaddon.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "luckyblockaddon")
public class LuckyBlockConfig implements ConfigData {

    @ConfigEntry.Category("replacement_chances")
    @ConfigEntry.Gui.TransitiveObject
    public ReplacementChances replacementChances = new ReplacementChances();

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralSettings general = new GeneralSettings();

    public static class ReplacementChances {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int stoneAndDirt = 12; // 12% - Stone, Dirt, Grass Block, Coarse Dirt, Gravel

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int woodLogs = 20; // 20% - All wood log types

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int ores = 20; // 20% - All ore types (including deepslate variants)

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hayBlock = 10; // 10% - Hay Block
    }

    public static class GeneralSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean enableReplacementLogging = false;

        @ConfigEntry.Gui.Tooltip
        public boolean enableConfigReloading = true;
    }

    /**
     * Get replacement chance as a float (0.0 to 1.0) for a specific block type
     */
    public float getReplacementChance(String blockType) {
        switch (blockType.toLowerCase()) {
            // Stone and dirt category
            case "stone":
            case "dirt":
            case "grass_block":
            case "coarse_dirt":
            case "gravel":
                return replacementChances.stoneAndDirt / 100.0f;

            // Wood logs category
            case "birch_log":
            case "oak_log":
            case "spruce_log":
            case "acacia_log":
            case "cherry_log":
            case "dark_oak_log":
            case "jungle_log":
            case "mangrove_log":
                return replacementChances.woodLogs / 100.0f;

            // Ore category (including deepslate variants)
            case "coal_ore":
            case "iron_ore":
            case "diamond_ore":
            case "gold_ore":
            case "emerald_ore":
            case "copper_ore":
            case "lapis_ore":
            case "redstone_ore":
            case "deepslate_coal_ore":
            case "deepslate_iron_ore":
            case "deepslate_diamond_ore":
            case "deepslate_gold_ore":
            case "deepslate_emerald_ore":
            case "deepslate_copper_ore":
            case "deepslate_lapis_ore":
            case "deepslate_redstone_ore":
                return replacementChances.ores / 100.0f;

            // Special blocks
            case "hay_block":
                return replacementChances.hayBlock / 100.0f;

            default:
                return 0.0f;
        }
    }
}
