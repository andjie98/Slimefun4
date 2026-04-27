package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

public interface Farmland extends BlockData {

    int getMoisture();

    void setMoisture(int moisture);

    int getMaximumMoisture();
}
