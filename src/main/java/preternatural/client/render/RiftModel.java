package preternatural.client.render;

import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;

public class RiftModel<T extends Entity> extends EntityModel<T> {

    private final Cuboid field_3567;
    private final Cuboid field_3569;
    private final Cuboid field_3568;

    public RiftModel() {
        this.field_3568 = (new Cuboid(this, 0, 0)).setTextureSize(64, 64);
        this.field_3568.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
        this.field_3568.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.field_3567 = (new Cuboid(this, 0, 16)).setTextureSize(64, 64);
        this.field_3567.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, -0.5F);
        this.field_3567.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.field_3569 = (new Cuboid(this, 0, 36)).setTextureSize(64, 64);
        this.field_3569.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, -0.5F);
        this.field_3569.setRotationPoint(0.0F, 24.0F, 0.0F);
    }

    public void setAngles(T entity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6) {
        super.setAngles(entity_1, float_1, float_2, float_3, float_4, float_5, float_6);
        this.field_3568.yaw = float_4 * 0.017453292F;
        this.field_3568.pitch = float_5 * 0.017453292F;
        this.field_3567.yaw = float_4 * 0.017453292F * 0.25F;
    }

    public void render(T entity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6) {
        this.setAngles(entity_1, float_1, float_2, float_3, float_4, float_5, float_6);
        this.field_3567.render(float_6);
        this.field_3569.render(float_6);
        this.field_3568.render(float_6);
    }

}