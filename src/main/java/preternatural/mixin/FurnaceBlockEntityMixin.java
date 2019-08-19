package preternatural.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import preternatural.Mod;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends LockableContainerBlockEntity {

	private static final Identifier KABOOM_RECIPE = new Identifier(Mod.DOMAIN, "furnace_kaboom");

	protected FurnaceBlockEntityMixin(BlockEntityType<?> type) { super(type); }

	@Inject(method = "craftRecipe", at = @At("RETURN"))
	private void craftRecipe(Recipe<?> recipe, CallbackInfo cb) {
		if (recipe.getId().equals(KABOOM_RECIPE)) {
			this.setInvStack(2, ItemStack.EMPTY);
			this.world.createExplosion(null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 4.0F, Explosion.DestructionType.BREAK);
		}
	}

}