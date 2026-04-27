package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;

public interface PistonHead extends Directional {

    boolean isShort();

    void setShort(boolean isShort);
}
