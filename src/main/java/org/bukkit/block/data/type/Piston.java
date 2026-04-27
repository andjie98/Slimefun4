package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

public interface Piston extends BlockData {

    boolean isExtended();

    void setExtended(boolean extended);
}
