package preternatural.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.BannerBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import preternatural.items.ItemClaymore;
import preternatural.network.ModPackets;
import preternatural.utils.Waypoint;

import java.util.ArrayList;

import static net.minecraft.client.render.VertexFormats.POSITION_COLOR;

public class GuiWaypointSelect extends Screen {

	protected int ticks = 0;
	protected int selection = -1;
	protected ItemStack tool;
	protected Hand hand;

	protected ArrayList<Waypoint> waypoints = new ArrayList<>();
	protected ArrayList<ItemStack> displayStacks = new ArrayList<>();

	public GuiWaypointSelect(ItemStack stack, Hand hand) {
		super(NarratorManager.EMPTY);
		this.tool = stack;
		this.hand = hand;
	}

	@Override
	protected void init() {
		CompoundTag tag = tool.getOrCreateSubTag(ItemClaymore.SUBTAG);
		ListTag list = tag.getList(ItemClaymore.TAG_RECORDS, 10);
		list.forEach(tag1 -> {
			CompoundTag record = (CompoundTag) tag1;
			Waypoint waypoint = Waypoint.fromNBT(record);
			DyeColor color = DyeColor.byName(record.getString("color"), DyeColor.WHITE);
			ItemStack stack = new ItemStack(BannerBlock.getForColor(color).asItem());
			stack.setTag(record);

			if (record.containsKey("CustomName")) {
				Text name = Text.Serializer.fromJson(record.getString("CustomName"));
				stack.setCustomName(name);
			}

			waypoints.add(waypoint);
			displayStacks.add(stack);
		});
	}

	@Override
	public void render(int mx, int my, float partialTicks) {
		this.renderBackground();

		int x = width / 2;
		int y = height / 2;
		int maxRadius = 80;
		double angle = mouseAngle(x, y, mx, my);
		int segments = waypoints.size();
		float step = (float) Math.PI / 180;
		float degPer = (float) Math.PI * 2 / segments;

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buf = tess.getBufferBuilder();
		GlStateManager.disableCull();
		GlStateManager.disableTexture();
		GlStateManager.enableBlend();
		//GlStateManager.shadeModel(GL11.GL_SMOOTH);
		buf.begin(GL11.GL_TRIANGLE_FAN, POSITION_COLOR);

		for(int seg = 0; seg < segments; seg++) {
			boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
			float radius = Math.max(0F, Math.min((ticks + partialTicks - seg * 6F / segments) * 40F, maxRadius));
			if (mouseInSector)
				radius *= 1.025f;

			int gs = 0x50;//0x40;
//			if(seg % 2 == 0)
//				gs += 0x19;
			int r = gs;
			int g = gs;
			int b = gs;
			int a = 0x30;

			if(seg == 0)
				buf.vertex(x, y, 0).color(r, g, b, a).next();

			if(mouseInSector) {
				selection = seg;
				r = g = b = 0xFF;
			}

			for(float i = 0; i < degPer + step / 2; i += step) {
				float rad = i + seg * degPer;
				float xp = x + MathHelper.cos(rad) * radius;
				float yp = y + MathHelper.sin(rad) * radius;

				if (i == 0)
					buf.vertex(xp, yp, 0).color(r, g, b, a).next();
				buf.vertex(xp, yp, 0).color(r, g, b, a).next();
			}
		}
		tess.draw();

		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.enableTexture();

		for(int seg = 0; seg < segments; seg++) {
			boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
			float radius = Math.max(0F, Math.min((ticks + partialTicks - seg * 6F / segments) * 40F, maxRadius));

			float rad = (seg + 0.5f) * degPer;
			float xp = x + MathHelper.cos(rad) * radius;
			float yp = y + MathHelper.sin(rad) * radius;

			ItemStack stack = displayStacks.get(seg);
			if(!stack.isEmpty()) {
				float xsp = xp - 4;
				float ysp = yp;

				String waypointName = (mouseInSector ? ChatFormatting.RESET : ChatFormatting.GRAY) + stack.getName().asString();
				int width = font.getStringWidth(waypointName);

				double mod = 0.6;
				int xdp = (int) ((xp - x) * mod + x);
				int ydp = (int) ((yp - y) * mod + y);

				MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, xdp - 8, ydp - 8);

				if(xsp < x)
					xsp -= width - 8;
				if(ysp < y)
					ysp -= 9;

				if (mouseInSector)
					font.drawWithShadow(waypointName, xsp, ysp, 0xFFFFFF);
			}
		}

		float shift = Math.min(5, ticks + partialTicks) / 5;
		float scale = 2 * shift;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GuiLighting.enable();

		GlStateManager.pushMatrix();
		GlStateManager.scalef(scale, scale, scale);

		MinecraftClient.getInstance().getItemRenderer().renderGuiItem(tool, (int) (x / scale) - 8, (int) (y / scale) - 8);
		GlStateManager.popMatrix();

		GuiLighting.disable();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		ticks++;
		if(!hasShiftDown()) {
			if (selection <= -1)
				return;

			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			CompoundTag tag = new CompoundTag();
			Waypoint waypoint = waypoints.get(selection);
			waypoint.name = displayStacks.get(selection).getName().asString();
			waypoint.toNBT(tag);
			buf.writeCompoundTag(tag);
			buf.writeString(this.hand.toString());

			ClientSidePacketRegistry.INSTANCE.sendToServer(ModPackets.CLAYMORE_SELECTION, buf);
			ItemClaymore.writeSelectedDestination(tool, Waypoint.fromNBT(tag));
			MinecraftClient.getInstance().openScreen(null);
		}
	}

	private static double mouseAngle(int x, int y, int mx, int my) {
		return (MathHelper.atan2(my - y, mx - x) + Math.PI * 2) % (Math.PI * 2);
	}

}
