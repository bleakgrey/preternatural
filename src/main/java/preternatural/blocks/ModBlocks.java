package preternatural.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import preternatural.Mod;

public class ModBlocks {

    public static final Block BEDROCK_BRICKS = new Block(FabricBlockSettings.copy(Blocks.BEDROCK).build());
    public static final Block CHISELED_BEDROCK_BRICKS = new Block(FabricBlockSettings.copy(Blocks.BEDROCK).build());
    public static final Block BEDROCK_FORCEFIELD = new StainedGlassBlock(DyeColor.GRAY, FabricBlockSettings.copy(Blocks.BEDROCK).build());
    public static final Block BEDROCK_BRICK_STAIRS = new ModStairsBlock(Blocks.BEDROCK.getDefaultState(), FabricBlockSettings.copy(Blocks.BEDROCK).build());
    public static final Block BEDROCK_PORTAL = new BedrockPortalBlock(FabricBlockSettings.copy(Blocks.BEDROCK).build());

    public static final Block METEORITE = new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(15.0F, 15.0F).build());
    public static final Block METEORITE_ORE = new MeteoriteOreBlock(FabricBlockSettings.copy(METEORITE).build());
    public static final Block BLIGHT = new BlightBlock(FabricBlockSettings.copy(Blocks.MYCELIUM).build());

    public static void register() {
        Mod.log("REGISTER BLOCKS");
        withItem("bedrock_bricks", BEDROCK_BRICKS);
        withItem("bedrock_bricks_chiseled", CHISELED_BEDROCK_BRICKS);
        withItem("bedrock_brick_stairs", BEDROCK_BRICK_STAIRS);
        withItem("bedrock_forcefield", BEDROCK_FORCEFIELD);
        withItem("bedrock_portal", BEDROCK_PORTAL);

        withItem("meteorite", METEORITE);
        withItem("meteorite_ore", METEORITE_ORE);
        withItem("blight", BLIGHT);
    }

    private static void withItem(String id, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(Mod.DOMAIN, id), block);
        Registry.register(Registry.ITEM, new Identifier(Mod.DOMAIN, id), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
    }

}