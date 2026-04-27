package org.bukkit.inventory.meta;

import org.bukkit.inventory.ItemStack;

public interface Damageable {

    boolean hasDamage();

    int getDamage();

    void setDamage(int damage);
}
