package preternatural.world.features;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import preternatural.blocks.ModBlocks;
import preternatural.world.ModGeneration;

import java.util.Random;

public class MyGenerator {

	public static class Piece extends SimpleStructurePiece {
		private final BlockRotation rotation;
		private final Identifier identifier;

		public Piece(StructureManager mgr, CompoundTag tag) {
			super(ModGeneration.myStructurePieceType, tag);
			this.identifier = new Identifier(tag.getString("Template"));
			this.rotation = BlockRotation.NONE; //tag.getString("Rot")
			this.setStructureData(mgr);
		}

		public Piece(StructureManager mgr, Identifier identifier, BlockPos pos, BlockRotation rotation) {
			super(ModGeneration.myStructurePieceType, 0);
			this.rotation = rotation;
			this.identifier = identifier;
			this.pos = pos;
			this.setStructureData(mgr);
		}

		public void setStructureData(StructureManager structureManager) {
			Structure structure = structureManager.getStructureOrBlank(this.identifier);
			StructurePlacementData data = new StructurePlacementData()
					.setRotation(this.rotation)
					.setMirrored(BlockMirror.NONE)
					.setPosition(pos)
					.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			this.setStructureData(structure, this.pos, data);
		}

		@Override
		protected void toNbt(CompoundTag tag) {
			super.toNbt(tag);
			tag.putString("Template", this.identifier.toString());
			tag.putString("Rot", this.rotation.name());
		}

		@Override
		protected void handleMetadata(String s, BlockPos pos, IWorld iWorld, Random rnd, MutableIntBoundingBox bb) {
			switch (s) {
				case "wall":
					while (pos.getY() > -1) {
						iWorld.setBlockState(pos, ModBlocks.BEDROCK_BRICKS.getDefaultState(), 0);
						pos = pos.down();
					}
					break;
				default:
					while (pos.getY() > -1) {
						iWorld.setBlockState(pos, Blocks.AIR.getDefaultState(), 0);
						pos = pos.down();
					}
					break;
			}
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
}
