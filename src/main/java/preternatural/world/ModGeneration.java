package preternatural.world;

import net.fabricmc.fabric.api.biomes.v1.FabricBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldBiomes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import preternatural.Mod;
import preternatural.world.biomes.WheatFieldBiome;

public class ModGeneration {

	public static final Biome WHEAT_FIELD_BIOME = Registry.register(Registry.BIOME, new Identifier(Mod.DOMAIN, "wheat_field"), new WheatFieldBiome());

	public static void register() {
		Mod.log("REGISTER GENERATION");
		//OverworldBiomes.addContinentalBiome(WHEAT_FIELD_BIOME, OverworldClimate.TEMPERATE, 2D);
		OverworldBiomes.addBiomeVariant(Biomes.PLAINS, WHEAT_FIELD_BIOME, 0.5);
		FabricBiomes.addSpawnBiome(WHEAT_FIELD_BIOME);
	}

}