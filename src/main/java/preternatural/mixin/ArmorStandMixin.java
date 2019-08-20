package preternatural.mixin;

import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ArmorStandEntity.class)
public class ArmorStandMixin {

	@Overwrite
	public boolean shouldShowArms() {
		return true;
	}

}