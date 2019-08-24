package preternatural.mixin;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.Criterions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("PublicStaticMixinMember")
@Mixin(Criterions.class)
public interface CriterionRegistryAccessor {

	@Invoker("register")
	static <T extends Criterion<?>> T registerCriterion(T criterion) {
		return null;
	}

}