package preternatural.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import preternatural.ModBlocks;
import preternatural.utils.WorldUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BedrockPortalBlock extends EndPortalFrameBlock {

	public static final int RADIUS = 4;
	public static final int FRAMES = 4;

	public BedrockPortalBlock(Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rnd) {
		if (state.get(EYE)) {
			Direction[] dirs = {Direction.UP};
			WorldUtils.spawnParticlesForBlockSides(world, pos, dirs, ParticleTypes.MYCELIUM);
		}
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if(state.get(EYE) || stack.getItem() != Items.WITHER_SKELETON_SKULL)
			return super.activate(state, world, pos, player, hand, hit);

		stack.decrement(1);
		world.setBlockState(pos, state.with(EYE, true));
		return tryUnlock(world, pos);
	}

	private boolean tryUnlock(World world, BlockPos origin) {
		int activeFrames = WorldUtils.findBlocksInRadius(world, origin, RADIUS, new WorldUtils.IBlockCallback() {
			@Override public boolean isValid(BlockState state) {
				return state.getBlock() == ModBlocks.BEDROCK_PORTAL && state.get(EYE);
			}
		}).size();

		if(activeFrames >= FRAMES){
			HashMap<BlockPos,BlockState> blocks = WorldUtils.findBlocksInRadius(world, origin, RADIUS, new WorldUtils.IBlockCallback() {
				@Override public boolean isValid(BlockState state) {
					return state.getBlock() == ModBlocks.BEDROCK_FORCEFIELD;
				}
			});
			for(Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
				BlockPos pos = entry.getKey();
				Direction[] dirs = Direction.values();
				WorldUtils.spawnParticlesForBlockSides(world, pos, dirs, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
		return true;
	}

}