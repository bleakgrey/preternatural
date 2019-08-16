package preternatural.utils;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import preternatural.entities.EntityRift;

import static preternatural.entities.EntityRift.*;

public class Waypoint {

    public DimensionType dim = null;
    public BlockPos pos = null;

    public Waypoint() {}

    public Waypoint(BlockPos pos, DimensionType dim) {
        this.pos = pos;
        this.dim = dim;
    }

    public Waypoint(EntityRift rift) {
        this.pos = new BlockPos(rift.getDataTracker().get(TRACKER_X), rift.getDataTracker().get(TRACKER_Y), rift.getDataTracker().get(TRACKER_Z));
        this.dim = DimensionType.byId(new Identifier(rift.getDataTracker().get(TRACKER_DIM)));
    }

    public boolean isEmpty() {
        return this.dim == null || this.pos == null;
    }

    public void toNBT(CompoundTag nbt) {
        if (isEmpty())
            return;
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putString("dim", DimensionType.getId(dim).toString());
    }

    public static Waypoint fromNBT(CompoundTag nbt) {
        BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        DimensionType dim = DimensionType.byId(new Identifier(nbt.getString("dim")));
        return new Waypoint(pos, dim);
    }

    public void assignToRift(EntityRift rift) {
        DataTracker dataTracker = rift.getDataTracker();
        if (!this.isEmpty()) {
            dataTracker.set(TRACKER_DIM, this.dim.toString());
            dataTracker.set(TRACKER_X, this.pos.getX());
            dataTracker.set(TRACKER_Y, this.pos.getY());
            dataTracker.set(TRACKER_Z, this.pos.getZ());
        }
        dataTracker.set(TRACKER_IS_VALID, !this.isEmpty());
    }

}