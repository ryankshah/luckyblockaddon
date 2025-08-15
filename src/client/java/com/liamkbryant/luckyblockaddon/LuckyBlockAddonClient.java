package com.liamkbryant.luckyblockaddon;

import com.liamkbryant.luckyblockaddon.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.GrassColor;

public class LuckyBlockAddonClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
			world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor(), ModBlocks.DISGUISED_GRASS_BLOCK);

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DISGUISED_GRASS_BLOCK, RenderType.cutout());
	}
}