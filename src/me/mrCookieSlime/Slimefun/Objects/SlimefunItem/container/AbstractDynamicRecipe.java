package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.container;

import org.bukkit.inventory.ItemStack;

/**
 * @author Narcissu14
 */
public abstract class AbstractDynamicRecipe {

    public AbstractDynamicRecipe() {

    }

    public abstract ItemStack[] getInput();

    public abstract ItemStack[] getOutput();

    public abstract void setTicks(int ticks);

    public abstract int getTicks();
}
