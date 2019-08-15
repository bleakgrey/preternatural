package preternatural.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
import java.util.Random;

public class BedrockPortalBlock extends EndPortalFrameBlock {

	public static final int SEARCH_RADIUS = 4;
	public static final int FRAMES = 4;
	public static final Item KEY = Items.WITHER_SKELETON_SKULL;

	public BedrockPortalBlock(Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rnd) {
		if (state.get(EYE)) {
			Direction[] dirs = {Direction.UP};
			WorldUtils.spawnBlockParticles(world, pos, dirs, ParticleTypes.MYCELIUM);
		}
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if(state.get(EYE) || stack.getItem() != KEY)
			return super.activate(state, world, pos, player, hand, hit);

		stack.decrement(1);
		world.setBlockState(pos, state.with(EYE, true));
		return tryUnlock(world, pos);
	}

	private boolean tryUnlock(World world, BlockPos origin) {
		int activatedFrames = WorldUtils.findBlocksInRadius(world, origin, SEARCH_RADIUS, new WorldUtils.IBlockCallback() {
			@Override public boolean isValid(BlockState state) {
				return state.getBlock() == ModBlocks.BEDROCK_PORTAL && state.get(EYE);
			}
		}).size();

		if(activatedFrames >= FRAMES){
			HashMap<BlockPos,BlockState> found = WorldUtils.findBlocksInRadius(world, origin, SEARCH_RADIUS, new WorldUtils.IBlockCallback() {
				@Override public boolean isValid(BlockState state) {
					return state.getBlock() == ModBlocks.BEDROCK_FORCEFIELD;
				}
			});
			found.keySet().forEach(pos -> {
				Direction[] dirs = Direction.values();
				WorldUtils.spawnBlockParticles(world, pos, dirs, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			});
		}
		return true;
	}

}