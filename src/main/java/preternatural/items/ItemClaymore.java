package preternatural.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import preternatural.Mod;
import preternatural.client.gui.GuiWaypointSelect;
import preternatural.entities.EntityRift;
import preternatural.entities.ModEntities;
import preternatural.utils.Waypoint;
import preternatural.utils.WorldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemClaymore extends SwordItem {

	public static final String SUBTAG = "waypoints";
	public static final String TAG_RECORDS = "v1";
	public static final String TAG_DESTINATION = "destination";

    public ItemClaymore() {
        super(ToolMaterials.IRON, 0, -2.4F, new Item.Settings()
                .group(ItemGroup.TOOLS)
                .rarity(Rarity.RARE)
		        .maxDamage(32));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity entity, LivingEntity player) {
	    CompoundTag destination = stack.getSubTag(SUBTAG).getCompound(TAG_DESTINATION);
	    Waypoint waypoint = Waypoint.fromNBT(destination);
	    if (waypoint.isEmpty()) {
		    Mod.log("Empty destination!");
		    return false;
	    }

        stack.damage(8, player, (plr) -> plr.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
	    entity.world.createExplosion(entity, entity.x, entity.y, entity.z, 2.0F, Explosion.DestructionType.NONE);
        WorldUtils.teleport(entity, waypoint);
	    entity.world.createExplosion(entity, entity.x, entity.y, entity.z, 2.0F, Explosion.DestructionType.NONE);

	    if (player.world.isClient) {
	    	Random rnd = player.world.random;
		    for (int i = 0; i < 10; ++i)
			    player.world.addParticle(ParticleTypes.HEART, entity.x + (rnd.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.y + rnd.nextDouble() * (double) entity.getHeight(), entity.z + (rnd.nextDouble() - 0.5D) * (double) entity.getWidth(), 0.0D, 0.0D, 0.0D);
	    }

        return super.postHit(stack, entity, player);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        ItemStack stack = ctx.getStack();
        BlockPos ctxBlockPos = ctx.getBlockPos();
        Direction dir = ctx.getSide();
        BlockState blockState = world.getBlockState(ctxBlockPos);
        PlayerEntity player = ctx.getPlayer();
        Hand hand = ctx.getHand();

        if (player.isSneaking())
        	return ActionResult.PASS;

	    if (blockState.getBlock() instanceof BannerBlock)
		    return onUsedOnBanner(ctx, new Waypoint(player.getBlockPos(), player.dimension));

        CompoundTag destination = stack.getSubTag(SUBTAG).getCompound(TAG_DESTINATION);
	    Waypoint waypoint = Waypoint.fromNBT(destination);
	    if (waypoint.isEmpty()) {
	    	Mod.log("Empty destination!");
		    return ActionResult.FAIL;
	    }

	    if (world.isClient)
		    return ActionResult.SUCCESS;

        BlockPos pos = ctxBlockPos;
        if (!blockState.getCollisionShape(world, ctxBlockPos).isEmpty())
            pos = ctxBlockPos.offset(dir);

		if (spawnRift(world, waypoint, pos, player)) {
			player.getItemCooldownManager().set(this, EntityRift.LIFESPAN);
			stack.damage(1, player, entity -> {
				entity.sendEquipmentBreakStatus(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
			});
			return ActionResult.SUCCESS;
		}
        return ActionResult.FAIL;
    }

    public boolean spawnRift(World world, Waypoint waypoint, BlockPos pos, Entity entity) {
	    EntityRift rift = ModEntities.RIFT.create(world);
	    waypoint.assignToRift(rift);
	    rift.setPositionAndAngles(pos, entity.yaw, 0);
	    rift.headYaw = rift.yaw;
	    rift.field_6283 = rift.yaw;
	    return world.spawnEntity(rift);
    }

    protected ActionResult onUsedOnBanner(ItemUsageContext ctx, Waypoint waypoint) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BannerBlockEntity entity = (BannerBlockEntity) world.getBlockEntity(pos);
        BlockState blockState = world.getBlockState(pos);
        DyeColor color = ((BannerBlock)blockState.getBlock()).getColor();
        PlayerEntity player = ctx.getPlayer();
        ItemStack stack = ctx.getStack();

        if (entity == null) {
            Mod.log("No BannerBlockEntity entity found!");
            return ActionResult.FAIL;
        }

	    CompoundTag tag = new CompoundTag();
        tag.putString("color", color.toString());
        entity.toTag(tag);
        waypoint.toNBT(tag);

        Waypoint blockWaypoint = new Waypoint(pos, player.dimension);
        CompoundTag blockTag = new CompoundTag();
        blockWaypoint.toNBT(blockTag);
        tag.put("block", blockTag);

        boolean alreadySaved = containsRecord(stack, tag);
        WorldUtils.spawnBlockParticles(world, pos, Direction.values(), alreadySaved ? ParticleTypes.POOF : ParticleTypes.PORTAL);
        WorldUtils.spawnBlockParticles(world, pos.up(), Direction.values(), alreadySaved ? ParticleTypes.POOF : ParticleTypes.PORTAL);
	    manipulateRecord(stack, tag, alreadySaved);

        return ActionResult.SUCCESS;
    }

    public static boolean areRecordsEqual(CompoundTag tag1, CompoundTag tag2) {
		CompoundTag blockTag1 = tag1.getCompound("block");
		CompoundTag blockTag2 = tag2.getCompound("block");
		Waypoint waypoint1 = Waypoint.fromNBT(blockTag1);
		Waypoint waypoint2 = Waypoint.fromNBT(blockTag2);
		return waypoint1.equals(waypoint2);
    }

    public static boolean containsRecord(ItemStack stack, CompoundTag tag) {
	    ListTag list = stack.getOrCreateSubTag(SUBTAG).getList(TAG_RECORDS, 10);
	    for (Tag item : list) {
		    CompoundTag listTag = (CompoundTag) item;
		    if (areRecordsEqual(listTag, tag))
		    	return true;
	    }
	    return false;
    }

    public static void manipulateRecord(ItemStack stack, CompoundTag tag, boolean removeMode) {
	    CompoundTag nbt = stack.getOrCreateSubTag(SUBTAG);
    	ListTag list = nbt.getList(TAG_RECORDS, 10);

	    if (removeMode) {
		    ArrayList<CompoundTag> toRemove = new ArrayList<>();
		    for (int i = 0; i < list.size(); i++) {
			    CompoundTag listTag = list.getCompoundTag(i);
			    if (areRecordsEqual(listTag, tag)) {
			    	toRemove.add(listTag);
			    }
		    }
		    toRemove.forEach(list::remove);
	    }
	    else
	        list.add(tag);

	    nbt.put(TAG_RECORDS, list);
	    stack.putSubTag(SUBTAG, nbt);
	    //Mod.log("Result stack NBT: "+stack.getTag().toString());
    }

    public static void writeSelectedDestination(ItemStack stack, Waypoint waypoint) {
    	CompoundTag nbt = stack.getOrCreateSubTag(SUBTAG);
    	CompoundTag tag = new CompoundTag();
	    waypoint.toNBT(tag);
    	nbt.put(TAG_DESTINATION, tag);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.isInSneakingPose()) {
            if (world.isClient)
                MinecraftClient.getInstance().openScreen(new GuiWaypointSelect(stack, hand));
            return new TypedActionResult(ActionResult.SUCCESS, stack);
        }
        else
            return super.use(world, player, hand);
    }

    @Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> lines, TooltipContext ctx) {
		CompoundTag nbt = stack.getOrCreateSubTag(SUBTAG);
		if (nbt.containsKey(TAG_DESTINATION)){
			CompoundTag destination = nbt.getCompound(TAG_DESTINATION);
			Waypoint waypoint = Waypoint.fromNBT(destination);
			lines.add(new LiteralText("Destination: ").append(waypoint.name));
		}

//	    ListTag list = nbt.getList(TAG_RECORDS, 10);
//	    if (list.size() > 0)
//		    lines.add(new LiteralText("Saved waypoints: "+list.size()));
	}

}