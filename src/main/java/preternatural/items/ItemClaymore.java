package preternatural.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import preternatural.Mod;
import preternatural.ModEntities;
import preternatural.client.gui.GuiWaypointSelect;
import preternatural.entities.EntityRift;
import preternatural.utils.Waypoint;
import preternatural.utils.WorldUtils;

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

        if(blockState.getBlock() instanceof BannerBlock) {
            return onUsedOnBanner(ctx);
        }

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

    protected ActionResult onUsedOnBanner(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BannerBlockEntity entity = (BannerBlockEntity) world.getBlockEntity(pos);
        BlockState blockState = world.getBlockState(pos);
        DyeColor color = ((BannerBlock)blockState.getBlock()).getColor();
        PlayerEntity player = ctx.getPlayer();

        if(entity == null) {
            Mod.log("No BannerBlockEntity entity found!");
            return ActionResult.FAIL;
        }

        if(player.isSneaking()) {
            WorldUtils.spawnBlockParticles(world, pos, Direction.values(), ParticleTypes.POOF);
        }
        else {
            WorldUtils.spawnBlockParticles(world, pos, Direction.values(), ParticleTypes.PORTAL);
        }

        Mod.log(color.toString());
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.isInSneakingPose()) {
            if(world.isClient)
                MinecraftClient.getInstance().openScreen(new GuiWaypointSelect(stack));
            return new TypedActionResult(ActionResult.SUCCESS, stack);
        }
        else
            return super.use(world, player, hand);
    }

}