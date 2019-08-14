package preternatural.entities;

import preternatural.ModEntities;
import preternatural.utils.Waypoint;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class EntityRift extends MobEntity {

    public static final int LIFESPAN = 100;
    public Waypoint waypoint = new Waypoint();

    public EntityRift(World world) {
        super(ModEntities.RIFT, world);
    }

    public EntityRift(World world, Waypoint waypoint) {
        super(ModEntities.RIFT, world);
        this.waypoint = waypoint;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public void readCustomDataFromTag(CompoundTag nbt) {
        super.readCustomDataFromTag(nbt);
        waypoint.fromNBT(nbt);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag nbt) {
        super.writeCustomDataToTag(nbt);
        waypoint.toNBT(nbt);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.age > LIFESPAN)
            this.remove();
    }

    @Override
    public void tickMovement() {
        if(this.world.isClient)
            for(int i = 0; i < 1; ++i)
                this.world.addParticle(ParticleTypes.SMOKE, this.x + (this.random.nextDouble() - 0.5D) * (double)this.getWidth(), this.y + this.random.nextDouble() * (double)this.getHeight(), this.z + (this.random.nextDouble() - 0.5D) * (double)this.getWidth(), 0.0D, 0.0D, 0.0D);
    }

}
