package preternatural;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import preternatural.blocks.BedrockPortalBlock;
import preternatural.blocks.BlockStairs;

public class ModBlocks {

    public static final Block BEDROCK_BRICKS = new Block(FabricBlockSettings.copy(Blocks.BEDROCK).build());
    public static final Block CHISELED_BEDROCK_BRICKS = new Block(FabricBlockSettings.copy(Blocks.BEDROCK).build());
    public static final Block BEDROCK_FORCEFIELD = new StainedGlassBlock(DyeColor.GRAY, FabricBlockSettings.copy(Blocks.BEDROCK).build());
    public static final Block BEDROCK_BRICK_STAIRS = new BlockStairs(Blocks.BEDROCK.getDefaultState(), FabricBlockSettings.copy(Blocks.BEDROCK).build());
    public static final Block BEDROCK_PORTAL = new BedrockPortalBlock(FabricBlockSettings.copy(Blocks.BEDROCK).build());

    static void register() {
        Mod.log("REGISTER BLOCKS");
        withItem("bedrock_bricks", BEDROCK_BRICKS);
        withItem("bedrock_bricks_chiseled", CHISELED_BEDROCK_BRICKS);
        withItem("bedrock_brick_stairs", BEDROCK_BRICK_STAIRS);
        withItem("bedrock_forcefield", BEDROCK_FORCEFIELD);
        withItem("bedrock_portal", BEDROCK_PORTAL);
    }

    private static void withItem(String id, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(Mod.DOMAIN, id), block);
        Registry.register(Registry.ITEM, new Identifier(Mod.DOMAIN, id), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
    }

}