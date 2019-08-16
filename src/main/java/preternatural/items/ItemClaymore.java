package preternatural.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import preternatural.Mod;
import preternatural.ModEntities;
import preternatural.entities.EntityRift;
import preternatural.utils.Waypoint;

public class ItemClaymore extends SwordItem {

    public ItemClaymore() {
        super(ToolMaterials.IRON, 0, -2.4F, new Item.Settings()
                .group(ItemGroup.TOOLS)
                .rarity(Rarity.RARE));
    }

    @Override
    public Multimap<String, EntityAttributeModifier> getModifiers(EquipmentSlot equipmentSlot_1) {
        return HashMultimap.create();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity entity, LivingEntity player) {
        player.world.createExplosion(entity, DamageSource.MAGIC, entity.x, entity.y, entity.z, 4.0F, false, Explosion.DestructionType.BREAK);
        stack.damage(getMaterial().getDurability(), player, (plr) -> plr.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return super.postHit(stack, entity, player);
    }

    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        ItemStack stack = ctx.getStack();
        BlockPos ctxBlockPos = ctx.getBlockPos();
        Direction dir = ctx.getSide();
        BlockState blockState = world.getBlockState(ctxBlockPos);
        PlayerEntity player = ctx.getPlayer();

        if (world.isClient || player == null)
            return ActionResult.SUCCESS;

        BlockPos pos = ctxBlockPos;
        if (!blockState.getCollisionShape(world, ctxBlockPos).isEmpty())
            pos = ctxBlockPos.offset(dir);

        Waypoint waypoint = new Waypoint(new BlockPos(0,100,0), player.dimension);
        EntityRift rift = ModEntities.RIFT.create(world);
        waypoint.assignToRift(rift);
        rift.setPositionAndAngles(pos, player.yaw, 0);
        rift.headYaw = rift.yaw;
        rift.field_6283 = rift.yaw;
        world.spawnEntity(rift);
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.isInSneakingPose()) {
            Mod.log("Show GUI here...");
            return new TypedActionResult(ActionResult.SUCCESS, stack);
        }
        else
            return super.use(world, player, hand);
    }

}