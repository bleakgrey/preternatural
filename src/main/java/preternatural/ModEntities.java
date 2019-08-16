package preternatural;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import preternatural.client.render.RiftRenderer;
import preternatural.entities.EntityRift;
import preternatural.entities.ScarecrowEntity;

public class ModEntities {

    public static EntityType<EntityRift> RIFT = Registry.register(Registry.ENTITY_TYPE, new Identifier(Mod.DOMAIN, "rift"),
            FabricEntityTypeBuilder.<EntityRift>create(EntityCategory.MISC, (entityType, world) -> new EntityRift(world, null))
                    .size(new EntityDimensions(1,2,true))
                    .build()
    );
    public static EntityType<ScarecrowEntity> SCARECROW = Registry.register(Registry.ENTITY_TYPE, new Identifier(Mod.DOMAIN, "scarecrow"),
            FabricEntityTypeBuilder.<ScarecrowEntity>create(EntityCategory.MISC, ScarecrowEntity::new)
                    .size(new EntityDimensions(1,2,true))
                    .build()
    );

    static void register() {
        Mod.log("REGISTER ENTITIES");
    }

    @Environment(EnvType.CLIENT)
    static void registerRenderers() {
        Mod.log("REGISTER ENTITY RENDERERS");
        EntityRendererRegistry.INSTANCE.register(EntityRift.class, (disp, ctx) -> new RiftRenderer(disp));
    }

}