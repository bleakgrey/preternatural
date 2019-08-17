package preternatural;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import preternatural.blocks.ModBlocks;
import preternatural.entities.ModEntities;
import preternatural.items.ModItems;
import preternatural.network.ModPackets;
import preternatural.world.ModGeneration;

public class Mod implements ModInitializer, ClientModInitializer {

	public static final String DOMAIN = "preternatural";

	public static void log(String text) {
		System.out.println(DOMAIN + ": " + text);
	}

	@Override
	public void onInitialize() {
		ModItems.register();
		ModBlocks.register();
		ModGeneration.register();
		ModPackets.register();
	}

	@Override
	public void onInitializeClient() {
		ModEntities.registerRenderers();
		ModPackets.registerClient();
	}

}