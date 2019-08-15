package preternatural;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class Mod implements ModInitializer, ClientModInitializer {

	public static final String DOMAIN = "preternatural";

	public static void log(String text) {
		System.out.println(DOMAIN + ": " + text);
	}

	@Override
	public void onInitialize() {
		ModItems.register();
		ModBlocks.register();
	}

	@Override
	public void onInitializeClient() {
		ModEntities.registerRenderers();
	}

}