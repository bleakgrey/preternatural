package preternatural.world;

import net.fabricmc.fabric.api.biomes.v1.FabricBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldBiomes;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;
import preternatural.Mod;
import preternatural.world.biomes.WheatFieldBiome;
import preternatural.world.features.MyFeature;
import preternatural.world.features.MyGenerator;

public class ModGeneration {

	public static final Identifier BONEGRINDER_ID = new Identifier(Mod.DOMAIN, "bonegrinder");

	public static final StructurePieceType myStructurePieceType = Registry.register(Registry.STRUCTURE_PIECE, "my_piece", MyGenerator.Piece::new);
	public static final StructureFeature<DefaultFeatureConfig> myFeature = Registry.register(Registry.FEATURE, BONEGRINDER_ID, new MyFeature(new Identifier("pillager_outpost/watchtower")));
//	public static final StructureFeature<DefaultFeatureConfig> myFeature = Registry.register(Registry.FEATURE, "my_feature", new MyFeature(BONEGRINDER_ID));
	public static final StructureFeature<?> myStructure = Registry.register(Registry.STRUCTURE_FEATURE, BONEGRINDER_ID.toString(), myFeature);

	public static final Biome WHEAT_FIELD_BIOME = Registry.register(Registry.BIOME, new Identifier(Mod.DOMAIN, "wheat_field"), new WheatFieldBiome());

	public static void register() {
		Mod.log("REGISTER GENERATION");

		Feature.STRUCTURES.put(BONEGRINDER_ID.toString(), myStructure);

		//OverworldBiomes.addContinentalBiome(WHEAT_FIELD_BIOME, OverworldClimate.TEMPERATE, 2D);
		OverworldBiomes.addBiomeVariant(Biomes.PLAINS, WHEAT_FIELD_BIOME, 0.5);
		FabricBiomes.addSpawnBiome(WHEAT_FIELD_BIOME);
	}

}