package com.liamkbryant.luckyblockaddon.block;

import mod.lucky.fabric.FabricGameAPIKt;
import mod.lucky.fabric.FabricJavaGameAPIKt;
import mod.lucky.fabric.FabricLuckyRegistry;
import mod.lucky.java.game.LuckyBlockEntityData;
import mod.lucky.java.game.LuckyBlockUtilsKt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisguisedGrassLuckyBlock extends Block {
    public static final BooleanProperty SNOWY;

    public DisguisedGrassLuckyBlock(Properties props, Block vanillaBlock) {
        super(props);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(SNOWY, false));
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return direction == Direction.UP ? (BlockState)blockState.setValue(SNOWY, isSnowySetting(blockState2)) : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().above());
        return (BlockState)this.defaultBlockState().setValue(SNOWY, isSnowySetting(blockState));
    }

    private static boolean isSnowySetting(BlockState blockState) {
        return blockState.is(BlockTags.SNOW);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{SNOWY});
    }

    static {
        SNOWY = BlockStateProperties.SNOWY;
    }

    private static boolean canBeGrass(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.above();
        BlockState blockState2 = levelReader.getBlockState(blockPos2);
        if (blockState2.is(Blocks.SNOW) && (Integer)blockState2.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockState2.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(levelReader, blockState, blockPos, blockState2, blockPos2, Direction.UP, blockState2.getLightBlock(levelReader, blockPos2));
            return i < levelReader.getMaxLightLevel();
        }
    }

    private static MapColor getMapColorSafely(Block block) {
        try {
            return block.defaultBlockState().getMapColor(null, null);
        } catch (Exception e) {
            return MapColor.GRASS; // Default lucky block color
        }
    }

    private static SoundType getSoundTypeSafely(Block block) {
        try {
            return block.defaultBlockState().getSoundType();
        } catch (Exception e) {
            return SoundType.STONE; // Default sound
        }
    }

    /**
     * Custom lucky block break logic that replicates the private onBreak method
     */
    private void handleLuckyBlockBreak(Level world, Player player, BlockPos pos, boolean removedByRedstone) {
        // Only process on server side (same logic as original LuckyBlockKt.onBreak)
        if (!FabricJavaGameAPIKt.isClientWorld((LevelAccessor)world)) {
            // Get block entity data if it exists
            BlockEntity blockEntity = world.getBlockEntity(pos);
            LuckyBlockEntityData blockEntityData = FabricLuckyRegistry.luckyBlockEntity.create(pos, defaultBlockState()).getData();

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
//            handleLuckyBlockBreak(world, null, pos, true);
            FabricLuckyRegistry.INSTANCE.getLuckyBlock().neighborChanged(state, world, pos, neighborBlock, neighborPos, notify);
        }
    }

    @Override
    public void playerDestroy(@NotNull Level world, @NotNull Player player, @NotNull BlockPos pos,
                              @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack stack) {
        super.playerDestroy(world, player, pos, state, blockEntity, stack);
        FabricLuckyRegistry.INSTANCE.getLuckyBlock().playerDestroy(world, player, pos, state, blockEntity, stack);
//        handleLuckyBlockBreak(world, player, pos, false);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos pos,
                                 @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        // Delegate to lucky block's right-click behavior
        return FabricLuckyRegistry.INSTANCE.getLuckyBlock().use(blockState, world, pos, player, hand, hitResult);
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity player, @NotNull ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, player, itemStack);

        // Delegate to lucky block's placement logic
        FabricLuckyRegistry.INSTANCE.getLuckyBlock().setPlacedBy(world, pos, state, player, itemStack);

        // Check for immediate redstone activation after placement
        if (world.hasNeighborSignal(pos)) {
//            handleLuckyBlockBreak(world, null, pos, true);
        }
    }

//    @NotNull
//    @Override
//    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
//        return FabricLuckyRegistry.luckyBlockEntity.create();
//    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }
}