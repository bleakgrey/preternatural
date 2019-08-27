package preternatural.blocks;

import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.BlockView;

public class CursingTableBlock extends EnchantingTableBlock {

	public CursingTableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new Entity();
	}

	public static class Entity extends EnchantingTableBlockEntity {

		@Override
		public BlockEntityType<Entity> getType() {
			return ModBlocks.CURSING_TABLE_ENTITY;
		}

	}

}