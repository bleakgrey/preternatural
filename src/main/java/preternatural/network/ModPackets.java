package preternatural.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import preternatural.Mod;
import preternatural.items.ItemClaymore;
import preternatural.utils.Waypoint;

public class ModPackets {

	public static final Identifier C2S_CLAYMORE_SELECTION = new Identifier(Mod.DOMAIN, "claymore_selection");

	public static void register() {
		Mod.log("REGISTER SERVER PACKETS");

		ServerSidePacketRegistry.INSTANCE.register(C2S_CLAYMORE_SELECTION, new PacketConsumer() {
			@Override public void accept(PacketContext ctx, PacketByteBuf buf) {
				CompoundTag tag = buf.readCompoundTag();
				Hand hand = Hand.valueOf(buf.readString());

				PlayerEntity player = ctx.getPlayer();
				ItemStack stack = player.getStackInHand(hand);
				Waypoint waypoint = Waypoint.fromNBT(tag);
				ItemClaymore.writeSelectedDestination(stack, waypoint);
			}
		});
	}

	@Environment(EnvType.CLIENT)
	public static void registerClient() { }

}
