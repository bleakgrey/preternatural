package preternatural.world.features;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import preternatural.world.ModGeneration;

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
		return 3;
	}

	@Override
	public boolean shouldStartAt(ChunkGenerator<?> chunkGenerator_1, Random random_1, int int_1, int int_2) {
		return random_1.nextInt(90) == 0;
	}

	public class MyStructureStart extends StructureStart {

		public MyStructureStart(StructureFeature<?> structureFeature_1, int int_1, int int_2, Biome biome_1, MutableIntBoundingBox mutableIntBoundingBox_1, int int_3, long long_1) {
			super(structureFeature_1, int_1, int_2, biome_1, mutableIntBoundingBox_1, int_3, long_1);
		}

		@Override
		public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int chunkX, int chunkZ, Biome biome) {
			int x = chunkX * 16;
			int z = chunkZ * 16;
			BlockPos startingPos = new BlockPos(x, 0, z);
			BlockRotation rotation = BlockRotation.values()[this.random.nextInt(BlockRotation.values().length)];
			this.children.add(new Piece(structureManager, id, startingPos, rotation));
			this.setBoundingBoxFromChildren();
		}
	}

	public static class Piece extends SimpleStructurePiece {
		private final BlockRotation pieceRotation;
		private final Identifier pieceId;

		public Piece(StructureManager mgr, CompoundTag tag) {
			super(ModGeneration.myStructurePieceType, tag);
			this.pieceId = new Identifier(tag.getString("Template"));
			this.pieceRotation = BlockRotation.NONE; //tag.getString("Rot")
			this.setStructureData(mgr);
		}

		public Piece(StructureManager mgr, Identifier id, BlockPos pos, BlockRotation rot) {
			super(ModGeneration.myStructurePieceType, 0);
			this.pieceRotation = rot;
			this.pieceId = id;
			this.pos = pos;
			this.setStructureData(mgr);
		}

		public void setStructureData(StructureManager structureManager) {
			Structure structure = structureManager.getStructureOrBlank(this.pieceId);
			StructurePlacementData data = new StructurePlacementData()
					.setRotation(this.pieceRotation)
					.setMirrored(BlockMirror.NONE)
					.setPosition(pos)
					.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			this.setStructureData(structure, this.pos, data);
		}

		@Override
		protected void toNbt(CompoundTag tag) {
			super.toNbt(tag);
			tag.putString("Template", this.pieceId.toString());
			tag.putString("Rot", this.pieceRotation.name());
		}

		@Override
		protected void handleMetadata(String s, BlockPos pos, IWorld iWorld, Random rnd, MutableIntBoundingBox bb) {
			modifyDataBlock (s, pos, iWorld, rnd, bb);
		}

		@Override
		public boolean generate(IWorld iWorld, Random rnd, MutableIntBoundingBox bb, ChunkPos chunkPos) {
			int yHeight = iWorld.getTop(Heightmap.Type.WORLD_SURFACE_WG, this.pos.getX() + 8, this.pos.getZ() + 8);
			this.pos = this.pos.add(0, yHeight - 1, 0);
//			Mod.log("GEN AT: "+this.pos.toString());

			//this.placementData.setBoundingBox(bb);
			this.structure.method_15172(iWorld, this.pos, this.placementData, 0);
			return false;
//			return super.generate(iWorld, rnd, bb, chunkPos);
		}
	}

	protected static void modifyDataBlock(String s, BlockPos pos, IWorld iWorld, Random rnd, MutableIntBoundingBox bb) {
//		switch (s) {
//				case "wall":
//					while (pos.getY() > -1) {
//						iWorld.setBlockState(pos, ModBlocks.BEDROCK_BRICKS.getDefaultState(), 0);
//						pos = pos.down();
//					}
//					break;
//				default:
//					while (pos.getY() > -1) {
//						iWorld.setBlockState(pos, Blocks.AIR.getDefaultState(), 0);
//						pos = pos.down();
//					}
//					break;
//			}
	}


}
