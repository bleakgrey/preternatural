package preternatural.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import preternatural.utils.Waypoint;
import preternatural.utils.WorldUtils;

public class EntityRift extends MobEntity {

    public static final int LIFESPAN = 100;
    public static final TrackedData<CompoundTag> TRACKER_WAYPOINT = DataTracker.registerData(EntityRift.class, TrackedDataHandlerRegistry.TAG_COMPOUND);

	public EntityRift(EntityType<? extends EntityRift> entityType, World world) {
		super(entityType, world);
	}

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(TRACKER_WAYPOINT, new CompoundTag());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag nbt) {
        super.readCustomDataFromTag(nbt);
        Waypoint.fromNBT(nbt).assignToRift(this);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag nbt) {
        super.writeCustomDataToTag(nbt);
        Waypoint.fromNBT(this.dataTracker.get(TRACKER_WAYPOINT)).toNBT(nbt);
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > LIFESPAN)
            this.remove();
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);
	    Waypoint waypoint = Waypoint.fromNBT(this.dataTracker.get(TRACKER_WAYPOINT));
	    WorldUtils.teleport(player, waypoint);
    }

    @Override
    public void tickMovement() {
        if (this.world.isClient)
            for (int i = 0; i < 1; ++i)
                this.world.addParticle(ParticleTypes.MYCELIUM, this.x + (this.random.nextDouble() - 0.5D) * (double)this.getWidth(), this.y + this.random.nextDouble() * (double)this.getHeight(), this.z + (this.random.nextDouble() - 0.5D) * (double)this.getWidth(), 0.0D, 0.0D, 0.0D);
    }

}