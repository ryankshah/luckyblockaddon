package com.liamkbryant.luckyblockaddon.block;

import mod.lucky.fabric.FabricGameAPIKt;
import mod.lucky.fabric.FabricJavaGameAPIKt;
import mod.lucky.fabric.game.LuckyBlock;
import mod.lucky.fabric.game.LuckyBlockEntity;
import mod.lucky.java.game.LuckyBlockEntityData;
import mod.lucky.java.game.LuckyBlockUtilsKt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisguisedLuckyBlock extends BaseEntityBlock {

    private final Block vanillaBlock;
    private final LuckyBlock luckyBlockDelegate;

    public DisguisedLuckyBlock(BlockBehaviour.Properties props, Block vanillaBlock) {
        // Use safe properties that work for all blocks
        super(props);

        this.vanillaBlock = vanillaBlock;
        this.luckyBlockDelegate = new LuckyBlock();
    }

    private static MapColor getMapColorSafely(Block block) {
        try {
            return block.defaultBlockState().getMapColor(null, null);
        } catch (Exception e) {
            return MapColor.COLOR_YELLOW; // Default lucky block color
        }
    }

    private static SoundType getSoundTypeSafely(Block block) {
        try {
            return block.defaultBlockState().getSoundType();
        } catch (Exception e) {
            return SoundType.STONE; // Default sound
        }
    }

    public Block getVanillaBlock() {
        return vanillaBlock;
    }

    /**
     * Custom lucky block break logic that replicates the private onBreak method
     */
    private void handleLuckyBlockBreak(Level world, Player player, BlockPos pos, boolean removedByRedstone) {
        // Only process on server side (same logic as original LuckyBlockKt.onBreak)
        if (!FabricJavaGameAPIKt.isClientWorld((LevelAccessor)world)) {
            // Get block entity data if it exists
            BlockEntity blockEntity = world.getBlockEntity(pos);
            LuckyBlockEntityData blockEntityData = null;

            if (blockEntity instanceof LuckyBlockEntity luckyEntity) {
                blockEntityData = luckyEntity.getData();
            }

            // Remove block and block entity (same as original)
            world.removeBlock(pos, false);
            world.removeBlockEntity(pos);

            // Convert BlockPos to Vec3 and trigger lucky block logic
            mod.lucky.common.Vec3 vec3Pos = FabricGameAPIKt.toVec3i((Vec3i)pos);
            LuckyBlockUtilsKt.onLuckyBlockBreak(this, world, vec3Pos, player, blockEntityData, removedByRedstone);
        }
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos,
                                @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean notify) {
        super.neighborChanged(state, world, pos, neighborBlock, neighborPos, notify);

        // Handle redstone activation (same logic as LuckyBlock)
        if (world.hasNeighborSignal(pos)) {
            handleLuckyBlockBreak(world, null, pos, true);
        }
    }

    @Override
    public void playerDestroy(@NotNull Level world, @NotNull Player player, @NotNull BlockPos pos,
                              @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack stack) {
        super.playerDestroy(world, player, pos, state, blockEntity, stack);

        // Handle player breaking the block - this is where lucky drops happen
        handleLuckyBlockBreak(world, player, pos, false);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos pos,
                                 @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        // Delegate to lucky block's right-click behavior
        return luckyBlockDelegate.use(blockState, world, pos, player, hand, hitResult);
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity player, @NotNull ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, player, itemStack);

        // Delegate to lucky block's placement logic
        luckyBlockDelegate.setPlacedBy(world, pos, state, player, itemStack);

        // Check for immediate redstone activation after placement
        if (world.hasNeighborSignal(pos)) {
            handleLuckyBlockBreak(world, null, pos, true);
        }
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        // Create a lucky block entity (same as lucky block)
        return luckyBlockDelegate.newBlockEntity(pos, state);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }
}