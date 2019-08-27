package preternatural.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum ModArmorMaterial implements ArmorMaterial {

	SHULKER("shulker", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, 2.0F, () -> {
		return Ingredient.ofItems(ModItems.SHULKER_INGOT);
	});

	private static final int[] BASE_DURABILITY = {13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] armorValues;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final Lazy<Ingredient> repairIngredient;

	ModArmorMaterial(String name, int durabilityMultiplier, int[] armorValueArr, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.armorValues = armorValueArr;
		this.enchantability = enchantability;
		this.equipSound = soundEvent;
		this.toughness = toughness;
		this.repairIngredient = new Lazy(repairIngredient);
	}

	public int getDurability(EquipmentSlot equipmentSlot_1) {
		return BASE_DURABILITY[equipmentSlot_1.getEntitySlotId()] * this.durabilityMultiplier;
	}

	public int getProtectionAmount(EquipmentSlot equipmentSlot_1) {
		return this.armorValues[equipmentSlot_1.getEntitySlotId()];
	}

	public int getEnchantability() {
		return this.enchantability;
	}

	public SoundEvent getEquipSound() {
		return this.equipSound;
	}

	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@Environment(EnvType.CLIENT)
	public String getName() {
		return this.name;
	}

	public float getToughness() {
		return this.toughness;
	}

}