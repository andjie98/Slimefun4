package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;

import org.bukkit.inventory.ItemStack;

public class EnhancedFurnace extends SlimefunItem {
	
	int speed, efficiency, output;
	
	public EnhancedFurnace(int speed, int efficiency, int output, ItemStack item, String id, ItemStack[] recipe) {
		super(Categories.MACHINES_1, item, id, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
		
		this.speed = speed;
		this.efficiency = efficiency;
		this.output = output;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getFuelEfficiency() {
		return efficiency;
	}
	
	public int getOutput() {
		return output;
	}
}
