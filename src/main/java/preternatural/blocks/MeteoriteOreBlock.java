package preternatural.blocks;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class MeteoriteOreBlock extends OreBlock {

	public MeteoriteOreBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected int getExperienceWhenMined(Random rnd) {
		return MathHelper.nextInt(rnd, 2, 5);
	}

}