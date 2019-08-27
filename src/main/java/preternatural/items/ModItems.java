package preternatural.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import preternatural.Mod;
import preternatural.entities.ModEntities;

public class ModItems {

    public static final Item DEBUG_SHTICK = new ItemDebugShtick();
    public static final Item VILE_REMNANT = new Item(new Item.Settings().group(ItemGroup.BREWING));
    public static final Item SHULKER_INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item SHULKER_NUGGET = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item SHULKER_HEAD = new ArmorItem(ModArmorMaterial.SHULKER, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item SHULKER_CHEST = new ArmorItem(ModArmorMaterial.SHULKER, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item SHULKER_LEGS = new ArmorItem(ModArmorMaterial.SHULKER, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item SHULKER_FEET = new ArmorItem(ModArmorMaterial.SHULKER, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT));

    private static void item (Item item, String name) {
        Registry.register(Registry.ITEM, new Identifier(Mod.DOMAIN, name), item);
    }

    public static final Item CLAYMORE = new ItemClaymore();

    public static void register() {
        Mod.log("REGISTER ITEMS");

        item(VILE_REMNANT, "vile_remnant");
        item(SHULKER_INGOT, "shulker_ingot");
        item(SHULKER_NUGGET, "shulker_nugget");
        item(SHULKER_HEAD, "shulker_head");
        item(SHULKER_CHEST, "shulker_chest");
        item(SHULKER_LEGS, "shulker_legs");
        item(SHULKER_FEET, "shulker_feet");

        item(DEBUG_SHTICK, "debug_shtick");
        item(CLAYMORE, "claymore");

        item(new SpawnEggItem(ModEntities.RIFT, 0,0, new Item.Settings().group(ItemGroup.MISC)), "spawner_rift");
        item(new SpawnEggItem(ModEntities.SCARECROW, 0,0, new Item.Settings().group(ItemGroup.MISC)), "spawner_scarecrow");
    }

}