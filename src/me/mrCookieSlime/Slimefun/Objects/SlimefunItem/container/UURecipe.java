package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.container;

import org.bukkit.inventory.ItemStack;

public class UURecipe {

    int ticks;
    ItemStack[] input;
    int uu;

    public UURecipe(int seconds, ItemStack[] input, int uuAmount) {
        this.ticks = seconds * 2;
        this.input = input;
        this.uu = uuAmount;
    }

    public ItemStack[] getInput() {
        return this.input;
    }

    public int getUUAmount() {
        return this.uu;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getTicks() {
        return ticks;
    }
}
