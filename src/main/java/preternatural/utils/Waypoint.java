package preternatural.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class Waypoint {

    public DimensionType dim = null;
    public BlockPos pos = null;

    public Waypoint() {}

    public Waypoint(BlockPos pos, DimensionType dim) {
        this.pos = pos;
        this.dim = dim;
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

    public void fromNBT(CompoundTag nbt) {
        pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        dim = DimensionType.byId(new Identifier(nbt.getString("dim")));
    }

}
