package preternatural.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class ItemDebugShtick extends Item {

	public ItemDebugShtick() {
		super(new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
	}

	public ActionResult useOnBlock(ItemUsageContext ctx) {
		return ActionResult.PASS;
	}

}
