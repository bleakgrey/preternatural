package preternatural.mixin;

import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import preternatural.Mod;

@Mixin(TickCriterion.class)
public class TickCriterionMixin {

	@Inject(at = @At("RETURN"), method = "handle")
	public void handle(ServerPlayerEntity player, CallbackInfo info) {
		Mod.CRITERION_IS_INCOMPLETE.handle(player);
	}

}