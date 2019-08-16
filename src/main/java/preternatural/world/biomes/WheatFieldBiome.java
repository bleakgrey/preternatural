package preternatural.world.biomes;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.function.Function;

public class WheatFieldBiome extends Biome {

	private static final BlockState WHEAT = Blocks.WHEAT.getDefaultState().with(CropBlock.AGE, 7);
	private static final BlockState GRASS = Blocks.GRASS_BLOCK.getDefaultState();
	private static final BlockState TOP_BLOCK = Blocks.DIRT.getDefaultState();
	private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();

	private static final TernarySurfaceConfig CONFIG = new TernarySurfaceConfig(TOP_BLOCK, TOP_BLOCK, GRAVEL);
	private static final Feature<GrassFeatureConfig> WHEAT_FEATURE = new WheatFeature(GrassFeatureConfig::deserialize);

	private static class WheatFeature extends GrassFeature {
		public WheatFeature(Function<Dynamic<?>, ? extends GrassFeatureConfig> function_1) {
			super(function_1);
		}

		@Override
		public boolean method_14080(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random rnd, BlockPos pos1, GrassFeatureConfig cfg) {
			for(BlockState blockState_1 = world.getBlockState(pos1); (blockState_1.isAir() || blockState_1.matches(BlockTags.LEAVES)) && pos1.getY() > 0; blockState_1 = world.getBlockState(pos1))
				pos1 = pos1.down();

			int int_1 = 0;
			for(int int_2 = 0; int_2 < 128; ++int_2) {
				BlockPos pos2 = pos1.add(rnd.nextInt(8) - rnd.nextInt(8), rnd.nextInt(4) - rnd.nextInt(4), rnd.nextInt(8) - rnd.nextInt(8));
				if (world.isAir(pos2) && Blocks.POPPY.canPlaceAt(world.getBlockState(pos2), world, pos2)) {
					world.setBlockState(pos2.down(), Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 7), 0);
					world.setBlockState(pos2, cfg.state, 0);
					++int_1;
				}
			}

			return int_1 > 0;
		}
	}

	public WheatFieldBiome() {
		super(new Settings()
				.configureSurfaceBuilder(SurfaceBuilder.SWAMP, CONFIG)
				.precipitation(Precipitation.RAIN)
				.category(Category.PLAINS)
				.depth(0.125F)
				.scale(0.04F)
				.temperature(0.8F)
				.downfall(0.4F)
				.waterColor(4159204)
				.waterFogColor(329011)
				.parent(null)
		);
		this.addStructureFeature(Feature.SWAMP_HUT, FeatureConfig.DEFAULT);
		this.addStructureFeature(Feature.VILLAGE, new VillageFeatureConfig("village/plains/town_centers", 6));
		this.addStructureFeature(Feature.PILLAGER_OUTPOST, new PillagerOutpostFeatureConfig(0.004D));
		this.addStructureFeature(Feature.MINESHAFT, new MineshaftFeatureConfig(0.004D, MineshaftFeature.Type.NORMAL));
		this.addStructureFeature(Feature.STRONGHOLD, FeatureConfig.DEFAULT);
		DefaultBiomeFeatures.addDungeons(this);
		DefaultBiomeFeatures.addMineables(this);
		DefaultBiomeFeatures.addDefaultOres(this);
		DefaultBiomeFeatures.addLandCarvers(this);
		DefaultBiomeFeatures.addSprings(this);
		DefaultBiomeFeatures.addDefaultLakes(this);

		this.addSpawn(EntityCategory.CREATURE, new SpawnEntry(EntityType.SHEEP, 12, 4, 4));
		this.addSpawn(EntityCategory.CREATURE, new SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
		this.addSpawn(EntityCategory.CREATURE, new SpawnEntry(EntityType.COW, 8, 4, 4));

		this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(WHEAT_FEATURE, new GrassFeatureConfig(WHEAT), Decorator.COUNT_HEIGHTMAP_DOUBLE, new CountDecoratorConfig(12)));
	}

}