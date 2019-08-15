package preternatural.utils;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class WorldUtils {

	public interface IBlockCallback {
		boolean isValid(BlockState state);
	}

	public static HashMap<BlockPos,BlockState> findBlocksInRadius(World world, BlockPos pos, int radius, IBlockCallback cb) {
		HashMap<BlockPos,BlockState> found = new HashMap<>();
		for(double x = pos.getX() - radius; x <= pos.getX() + radius; x++){
			for(double y = pos.getY() - radius; y <= pos.getY() + radius; y++){
				for(double z = pos.getZ() - radius; z <= pos.getZ() + radius; z++){
					BlockPos blockPos = new BlockPos(x, y, z);
					BlockState state = world.getBlockState(blockPos);
					if(cb.isValid(state))
						found.put(blockPos, state);
				}
			}
		}
		return found;
	}

	public static void spawnParticlesForBlockSides(World world, BlockPos pos, Direction[] directions, ParticleEffect particle) {
		Random rnd = world.random;
		for (Direction dir : directions) {
			BlockPos pos2 = pos.offset(dir);
			if (!world.getBlockState(pos2).isFullOpaque(world, pos2)) {
				Direction.Axis axis = dir.getAxis();
				double a = axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) dir.getOffsetX() : (double) rnd.nextFloat();
				double b = axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) dir.getOffsetY() : (double) rnd.nextFloat();
				double c = axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) dir.getOffsetZ() : (double) rnd.nextFloat();
				world.addParticle(particle, (double) pos.getX() + a, (double) pos.getY() + b, (double) pos.getZ() + c, 0.0D, 0.0D, 0.0D);
			}
		}
	}

}