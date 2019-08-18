package preternatural.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import preternatural.entities.EntityRift;

import static preternatural.entities.EntityRift.TRACKER_WAYPOINT;

public class Waypoint {

    public DimensionType dim = null;
    public BlockPos pos = null;
    public String name = "Unknown";

    public Waypoint(BlockPos pos, DimensionType dim) {
        this.pos = pos;
        this.dim = dim;
    }

    public boolean isEmpty() {
        return this.dim == null || this.pos == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else {
            if (obj instanceof Waypoint) {
                Waypoint w = (Waypoint) obj;
                return
                        this.pos.getX() == w.pos.getX() &&
                        this.pos.getY() == w.pos.getY() &&
                        this.pos.getZ() == w.pos.getZ() &&
                        this.dim.toString().equals(w.dim.toString());
            }
            return false;
        }
    }

    public void toNBT(CompoundTag nbt) {
        if (isEmpty())
            return;
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putString("dim", DimensionType.getId(dim).toString());
        nbt.putString("name", this.name);
    }

    public static Waypoint fromNBT(CompoundTag nbt) {
        BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        DimensionType dim = DimensionType.byId(new Identifier(nbt.getString("dim")));
        Waypoint waypoint = new Waypoint(pos, dim);
        if (nbt.containsKey("name"))
            waypoint.name = nbt.getString("name");
        return waypoint;
    }

    public void assignToRift(EntityRift rift) {
        CompoundTag tag = new CompoundTag();
        this.toNBT(tag);
        if (!this.isEmpty())
            rift.getDataTracker().set(TRACKER_WAYPOINT, tag);
    }

}