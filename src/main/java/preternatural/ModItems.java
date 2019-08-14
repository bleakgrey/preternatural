package preternatural;

import preternatural.items.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    private static void item (Item item, String name) {
        Registry.register(Registry.ITEM, new Identifier(Mod.DOMAIN, name), item);
    }

    public static final Item CLAYMORE = new ItemClaymore();

    static void register() {
        Mod.log("REGISTER ITEMS");
        item(CLAYMORE, "claymore");

        item(new SpawnEggItem(ModEntities.RIFT, 0,0, new Item.Settings().group(ItemGroup.MISC)), "spawner_rift");
    }

}