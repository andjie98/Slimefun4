package org.bukkit.block.data;

public interface Orientable extends BlockData {

    org.bukkit.Axis getAxis();

    void setAxis(org.bukkit.Axis axis);
}
