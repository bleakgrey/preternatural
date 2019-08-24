package preternatural;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.advancement.criterion.Criterions;
import preternatural.advancements.IsIncompleteCriterion;
import preternatural.blocks.ModBlocks;
import preternatural.entities.ModEntities;
import preternatural.items.ModItems;
import preternatural.mixin.CriterionRegistryAccessor;
import preternatural.network.ModPackets;
import preternatural.world.ModGeneration;

public class Mod implements ModInitializer, ClientModInitializer {

	public static final String DOMAIN = "preternatural";

	public static void log(String text) {
		System.out.println(DOMAIN + ": " + text);
	}

	public static IsIncompleteCriterion CRITERION_IS_INCOMPLETE;

	@Override
	public void onInitialize() {
		ModItems.register();
		ModBlocks.register();
		ModGeneration.register();
		ModPackets.register();

		Criterions.INVENTORY_CHANGED.getId();
		CRITERION_IS_INCOMPLETE = CriterionRegistryAccessor.registerCriterion(new IsIncompleteCriterion());
	}

	@Override
	public void onInitializeClient() {
		ModEntities.registerRenderers();
		ModPackets.registerClient();
	}

}