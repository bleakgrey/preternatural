package preternatural.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import preternatural.Mod;
import preternatural.entities.ModEntities;

public class ModItems {

    public static final Item DEBUG_SHTICK = new ItemDebugShtick();
    public static final Item VILE_REMANT = new Item(new Item.Settings().group(ItemGroup.BREWING));

    private static void item (Item item, String name) {
        Registry.register(Registry.ITEM, new Identifier(Mod.DOMAIN, name), item);
    }

    public static final Item CLAYMORE = new ItemClaymore();

    public static void register() {
        Mod.log("REGISTER ITEMS");

        item(VILE_REMANT, "vile_remnant");

        item(DEBUG_SHTICK, "debug_shtick");
        item(CLAYMORE, "claymore");

        item(new SpawnEggItem(ModEntities.RIFT, 0,0, new Item.Settings().group(ItemGroup.MISC)), "spawner_rift");
        item(new SpawnEggItem(ModEntities.SCARECROW, 0,0, new Item.Settings().group(ItemGroup.MISC)), "spawner_scarecrow");
    }

}