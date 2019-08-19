package preternatural.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ScarecrowEntity extends ArmorStandEntity {

	public static final TrackedData<Boolean> TRACKER_EXCITEMENT = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public ScarecrowEntity(EntityType<? extends ArmorStandEntity> entityType, World world) {
		super(entityType, world);
		setEquipment(world.random);
	}

	public ScarecrowEntity(World world, double x, double y, double z) {
		super(world, x, y, z);
		setEquipment(world.random);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(TRACKER_EXCITEMENT, false);
	}

	private void setEquipment (Random rnd) {
		this.setEquippedStack(EquipmentSlot.HEAD, new ItemStack(Items.CARVED_PUMPKIN));
		ItemStack chest = new ItemStack(Items.LEATHER_CHESTPLATE);
		chest.setDamage(Items.LEATHER_CHESTPLATE.getMaxDamage());
		this.setEquippedStack(EquipmentSlot.CHEST, chest);
		if (rnd.nextInt(3) == 0)
			this.setEquippedStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STICK));
	}

	@Override
	public ActionResult interactAt(PlayerEntity playerEntity_1, Vec3d vec3d_1, Hand hand_1) {
		return ActionResult.PASS;
	}

	@Override
	public boolean shouldShowArms() {
		return true;
	}

	@Override
	protected void onEquipStack(ItemStack stack) { }

	@Override
	public void tick() {
		super.tick();
		if(this.world.isClient) {
			this.setEquippedStack(EquipmentSlot.HEAD, new ItemStack(isExcited() ? Items.JACK_O_LANTERN : Items.CARVED_PUMPKIN));
			if (this.world.random.nextInt(getMovementTickRate()) > 0)
				return;

			this.setHeadRotation(new EulerAngle(this.world.random.nextFloat() * 5f, this.world.random.nextFloat() * 5f, 0F));
			this.setLeftArmRotation(new EulerAngle(-10F * this.world.random.nextFloat(), 0F, -10.0F));
			if (isExcited() && this.getEquippedStack(EquipmentSlot.MAINHAND) != ItemStack.EMPTY)
				this.setRightArmRotation(new EulerAngle(-15F * this.world.random.nextFloat(), 90F, 100f));
			else
				this.setRightArmRotation(new EulerAngle(-15F * this.world.random.nextFloat(), 0F, 10f));
			this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_ARMOR_STAND_HIT, this.getSoundCategory(), 0.5F, 0.5F, false);
		}
		else {
			Entity closest = this.world.getClosestPlayer(this, 8);
			if (closest != null)
				this.dataTracker.set(TRACKER_EXCITEMENT, true);
			else
				this.dataTracker.set(TRACKER_EXCITEMENT, false);
		}
	}

	protected boolean isExcited() {
		return this.dataTracker.get(TRACKER_EXCITEMENT);
	}

	protected int getMovementTickRate() {
		return this.dataTracker.get(TRACKER_EXCITEMENT) ? 2 : 100;
	}

}