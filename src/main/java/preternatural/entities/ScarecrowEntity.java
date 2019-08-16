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

public class ScarecrowEntity extends ArmorStandEntity {

	public static final TrackedData<Boolean> TRACKER_EXCITEMENT = DataTracker.registerData(ScarecrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public ScarecrowEntity(EntityType<? extends ArmorStandEntity> entityType, World world) {
		super(entityType, world);
		setEquipment();
	}

	public ScarecrowEntity(World world, double x, double y, double z) {
		super(world, x, y, z);
		setEquipment();
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(TRACKER_EXCITEMENT, false);
	}

	private void setEquipment () {
		this.setEquippedStack(EquipmentSlot.HEAD, new ItemStack(Items.CARVED_PUMPKIN));
		this.setEquippedStack(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
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
		}
		else {
			if(this.world.random.nextInt(getMovementTickRate()) == 0) {
				this.playSound(SoundEvents.ENTITY_ITEM_FRAME_BREAK, 0.5f, 0.5f);
				this.dataTracker.set(TRACKER_LEFT_ARM_ROTATION, new EulerAngle(-10.0F * this.world.random.nextFloat(), 0.0F, -10.0F));
				this.dataTracker.set(TRACKER_RIGHT_ARM_ROTATION, new EulerAngle(-15.0F * this.world.random.nextFloat(), 0.0F, 10.0F));
				this.dataTracker.set(TRACKER_HEAD_ROTATION, new EulerAngle(this.world.random.nextFloat() * 5f, this.world.random.nextFloat() * 5f, 0.0F));

			}

			Entity closest = this.world.getClosestPlayer(this, 30);
			if (closest != null)
				this.dataTracker.set(TRACKER_EXCITEMENT, closest.distanceTo(this) <= 10f);
			else
				this.dataTracker.set(TRACKER_EXCITEMENT, false);
		}
	}

	protected boolean isExcited() {
		return this.dataTracker.get(TRACKER_EXCITEMENT);
	}

	protected int getMovementTickRate() {
		return isExcited() ? 2 : 50;
	}

}