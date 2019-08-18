package preternatural.world.features;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class MyFeature extends AbstractTempleFeature<DefaultFeatureConfig> {

	private Identifier id;

	public MyFeature(Identifier structureID) {
		super(DefaultFeatureConfig::deserialize);
		this.id = structureID;
	}

	@Override
	protected int getSeedModifier() {
		return 14357618;
	}

	@Override
	public StructureStartFactory getStructureStartFactory() {
		return MyStructureStart::new;
	}

	@Override
	public String getName() {
		return id.toString();
	}

	@Override
	public int getRadius() {
		return 10;
	}

	@Override
	public boolean shouldStartAt(ChunkGenerator<?> chunkGenerator_1, Random random_1, int int_1, int int_2) {
		return true;
	}

	public class MyStructureStart extends StructureStart {

		public MyStructureStart(StructureFeature<?> structureFeature_1, int int_1, int int_2, Biome biome_1, MutableIntBoundingBox mutableIntBoundingBox_1, int int_3, long long_1) {
			super(structureFeature_1, int_1, int_2, biome_1, mutableIntBoundingBox_1, int_3, long_1);
		}

		@Override
		public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int chunkX, int chunkZ, Biome biome) {
			//DefaultFeatureConfig defaultFeatureConfig = chunkGenerator.getStructureConfig(biome, ModGeneration.myFeature);
			FeatureConfig cfg = chunkGenerator.getStructureConfig(biome, Registry.STRUCTURE_FEATURE.get(id));
			int x = chunkX * 16;
			int z = chunkZ * 16;
			BlockPos startingPos = new BlockPos(x, 0, z);
			BlockRotation rotation = BlockRotation.values()[this.random.nextInt(BlockRotation.values().length)];
			this.children.add(new MyGenerator.Piece(structureManager, id, startingPos, rotation));
			this.setBoundingBoxFromChildren();
		}
	}
}
