package preternatural.entities;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import preternatural.ModEntities;
import preternatural.utils.Waypoint;

public class EntityRift extends MobEntity {

    public static final int LIFESPAN = 100;
    public static final TrackedData<Boolean> TRACKER_IS_VALID = DataTracker.registerData(EntityRift.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Integer> TRACKER_X = DataTracker.registerData(EntityRift.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> TRACKER_Y = DataTracker.registerData(EntityRift.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> TRACKER_Z = DataTracker.registerData(EntityRift.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<String> TRACKER_DIM = DataTracker.registerData(EntityRift.class, TrackedDataHandlerRegistry.STRING);

    public EntityRift(World world) {
        super(ModEntities.RIFT, world);
    }

    public EntityRift(World world, Waypoint waypoint) {
        super(ModEntities.RIFT, world);
        waypoint.assignToRift(this);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(TRACKER_IS_VALID, false);
        dataTracker.startTracking(TRACKER_DIM, DimensionType.OVERWORLD.toString());
        dataTracker.startTracking(TRACKER_X, 0);
        dataTracker.startTracking(TRACKER_Y, 0);
        dataTracker.startTracking(TRACKER_Z, 0);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag nbt) {
        super.readCustomDataFromTag(nbt);
        Waypoint.fromNBT(nbt).assignToRift(this);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag nbt) {
        super.writeCustomDataToTag(nbt);
        Waypoint waypoint = new Waypoint(this);
        waypoint.toNBT(nbt);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.age > LIFESPAN)
            this.remove();

        if(this.world.isClient)
            return;


    }

    @Override
    public void tickMovement() {
        if(this.world.isClient)
            for(int i = 0; i < 1; ++i)
                this.world.addParticle(ParticleTypes.SMOKE, this.x + (this.random.nextDouble() - 0.5D) * (double)this.getWidth(), this.y + this.random.nextDouble() * (double)this.getHeight(), this.z + (this.random.nextDouble() - 0.5D) * (double)this.getWidth(), 0.0D, 0.0D, 0.0D);
    }

}