package preternatural.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import preternatural.Mod;

import java.util.*;

public class IsIncompleteCriterion implements Criterion<IsIncompleteCriterion.Conditions> {

	public static final Identifier ID = new Identifier(Mod.DOMAIN, "is_incomplete");
	private final Map<PlayerAdvancementTracker, IsIncompleteCriterion.Handler> handlers = Maps.newHashMap();

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public void beginTrackingCondition(PlayerAdvancementTracker tracker, ConditionsContainer<IsIncompleteCriterion.Conditions> conditions) {
		IsIncompleteCriterion.Handler handler = this.handlers.get(tracker);
		if (handler == null) {
			handler = new IsIncompleteCriterion.Handler(tracker);
			this.handlers.put(tracker, handler);
		}
		handler.addCondition(conditions);
	}

	@Override
	public void endTrackingCondition(PlayerAdvancementTracker tracker, ConditionsContainer<IsIncompleteCriterion.Conditions> conditions) {
		IsIncompleteCriterion.Handler handler = this.handlers.get(tracker);
		if (handler != null) {
			handler.removeCondition(conditions);
			if (handler.isEmpty())
				this.handlers.remove(tracker);
		}

	}

	@Override
	public void endTracking(PlayerAdvancementTracker playerAdvancementTracker_1) {
		this.handlers.remove(playerAdvancementTracker_1);
	}

	@Override
	public IsIncompleteCriterion.Conditions conditionsFromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
		String id = jsonObject.get("id").getAsString();
		return new IsIncompleteCriterion.Conditions(id);
	}

	public void handle(ServerPlayerEntity entity) {
		IsIncompleteCriterion.Handler handler = this.handlers.get(entity.getAdvancementManager());
		if (handler != null)
			handler.handle(entity.getCommandSource().getMinecraftServer().getAdvancementManager().getAdvancements());
	}



	static class Handler {
		private final PlayerAdvancementTracker manager;
		private final Set<ConditionsContainer<IsIncompleteCriterion.Conditions>> conditions = Sets.newHashSet();

		public Handler(PlayerAdvancementTracker tracker) {
			this.manager = tracker;
		}

		public boolean isEmpty() {
			return this.conditions.isEmpty();
		}

		public void addCondition(ConditionsContainer<IsIncompleteCriterion.Conditions> conditions) {
			this.conditions.add(conditions);
		}

		public void removeCondition(ConditionsContainer<IsIncompleteCriterion.Conditions> conditions) {
			this.conditions.remove(conditions);
		}

		public void handle(Collection<Advancement> advancements) {
			List<ConditionsContainer<IsIncompleteCriterion.Conditions>> list_1 = null;
			Iterator var3 = this.conditions.iterator();

			ConditionsContainer conditions;
			while(var3.hasNext()) {
				conditions = (ConditionsContainer)var3.next();
				if (((IsIncompleteCriterion.Conditions)conditions.getConditions()).test(advancements, manager)) {
					if (list_1 == null)
						list_1 = Lists.newArrayList();
					list_1.add(conditions);
				}
			}

			if (list_1 != null) {
				var3 = list_1.iterator();

				while(var3.hasNext()) {
					conditions = (ConditionsContainer)var3.next();
					conditions.apply(this.manager);
				}
			}

		}
	}



	public static class Conditions extends AbstractCriterionConditions {
		private final String advancementId;

		public Conditions(String id) {
			super(ID);
			this.advancementId = id;
		}

		public static IsIncompleteCriterion.Conditions create(String id) {
			return new IsIncompleteCriterion.Conditions(id);
		}

		public boolean test(Collection<Advancement> advancements, PlayerAdvancementTracker manager) {
			return advancements.stream()
					.filter(i -> i.getId().toString().equals(this.advancementId))
					.anyMatch(i -> !manager.getProgress(i).isDone());
		}

		public JsonElement toJson() {
			JsonObject obj = new JsonObject();
			obj.addProperty("id", this.advancementId);
			return obj;
		}
	}

}
