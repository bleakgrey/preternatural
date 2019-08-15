package preternatural.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class BedrockPortalBlock extends EndPortalFrameBlock {

	public BedrockPortalBlock(Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState blockState_1, World world_1, BlockPos blockPos_1, Random random_1) {
		if (blockState_1.get(EYE))
			spawnParticles(world_1, blockPos_1);
	}

	private static void spawnParticles(World world, BlockPos pos) {
		Random random_1 = world.random;
		Direction[] directions = {Direction.UP}; //Direction.values();
		for (Direction dir : directions) {
			BlockPos blockPos_2 = pos.offset(dir);
			if (!world.getBlockState(blockPos_2).isFullOpaque(world, blockPos_2)) {
				Direction.Axis direction$Axis_1 = dir.getAxis();
				double double_2 = direction$Axis_1 == Direction.Axis.X ? 0.5D + 0.5625D * (double) dir.getOffsetX() : (double) random_1.nextFloat();
				double double_3 = direction$Axis_1 == Direction.Axis.Y ? 0.5D + 0.5625D * (double) dir.getOffsetY() : (double) random_1.nextFloat();
				double double_4 = direction$Axis_1 == Direction.Axis.Z ? 0.5D + 0.5625D * (double) dir.getOffsetZ() : (double) random_1.nextFloat();
				world.addParticle(ParticleTypes.MYCELIUM, (double) pos.getX() + double_2, (double) pos.getY() + double_3, (double) pos.getZ() + double_4, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if(world.isClient || state.get(EYE) || stack.getItem() != Items.WITHER_SKELETON_SKULL)
			return super.activate(state, world, pos, player, hand, hit);

		stack.decrement(1);
		world.setBlockState(pos, state.with(EYE, true));
		return tryUnlock();
	}

	private boolean tryUnlock() {
		return true;
	}

}