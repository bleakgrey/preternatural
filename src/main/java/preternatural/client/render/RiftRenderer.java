package preternatural.client.render;

import preternatural.entities.EntityRift;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class RiftRenderer extends MobEntityRenderer<EntityRift, RiftModel<EntityRift>> {

    private static final Identifier SKIN = new Identifier("textures/entity/snow_golem.png");

    public RiftRenderer(EntityRenderDispatcher disp) {
        super(disp, new RiftModel<>(), 0F);
    }

    @Override
    protected Identifier getTexture(EntityRift entity) {
        return SKIN;
    }

    @Override
    protected void render(EntityRift entity, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6) {
        GlStateManager.disableLighting();
        GlStateManager.color3f(0,0,0);

        float t = (float)entity.age / EntityRift.LIFESPAN;
        float scale_x = MathHelper.lerp(t, 1.2f, 0f);
        float scale_y = MathHelper.lerp(t, 1f, 0.7f);
        GlStateManager.scalef(scale_x, scale_y, 0.1f);
        super.render(entity, float_1, float_2, float_3, float_4, float_5, float_6);
    }

}