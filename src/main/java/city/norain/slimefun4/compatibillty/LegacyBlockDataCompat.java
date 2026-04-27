package city.norain.slimefun4.compatibillty;

import java.lang.reflect.Method;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.block.Block;

@UtilityClass
public class LegacyBlockDataCompat {

    private static Method getBlockData;
    private static Method setBlockData;
    private static Method getAge;
    private static Method setAge;
    private static Method getMaximumAge;
    private static Method getLevel;
    private static Method setLevel;
    private static Method isPowered;
    private static boolean initialized = false;
    private static boolean useModernApi = false;

    private static synchronized void init() {
        if (initialized) return;
        initialized = true;
        try {
            Class<?> blockDataClass = Class.forName("org.bukkit.block.data.BlockData");
            getBlockData = Block.class.getMethod("getBlockData");
            setBlockData = Block.class.getMethod("setBlockData", blockDataClass);

            Class<?> ageableClass = Class.forName("org.bukkit.block.data.Ageable");
            getAge = ageableClass.getMethod("getAge");
            setAge = ageableClass.getMethod("setAge", int.class);
            getMaximumAge = ageableClass.getMethod("getMaximumAge");

            Class<?> levelledClass = Class.forName("org.bukkit.block.data.Levelled");
            getLevel = levelledClass.getMethod("getLevel");
            setLevel = levelledClass.getMethod("setLevel", int.class);

            useModernApi = true;
        } catch (Exception e) {
            useModernApi = false;
        }
    }

    public static boolean isAgeable(Block block) {
        init();
        if (useModernApi) {
            try {
                Object bd = getBlockData.invoke(block);
                return Class.forName("org.bukkit.block.data.Ageable").isInstance(bd);
            } catch (Exception e) {
                return false;
            }
        }
        Material mat = block.getType();
        return mat == Material.CROPS || mat == Material.CARROT || mat == Material.POTATO
                || mat == Material.BEETROOT_BLOCK || mat == Material.NETHER_WARTS;
    }

    public static int getAge(Block block) {
        init();
        if (useModernApi) {
            try {
                Object bd = getBlockData.invoke(block);
                return (int) getAge.invoke(bd);
            } catch (Exception e) {
                return 0;
            }
        }
        if (VersionUtil.is1_12()) {
            try {
                Object blockData = block.getState().getData();
                if (blockData instanceof org.bukkit.material.Crops) {
                    return ((org.bukkit.material.Crops) blockData).getState().ordinal();
                }
                if (blockData instanceof org.bukkit.material.NetherWarts) {
                    return ((org.bukkit.material.NetherWarts) blockData).getState().ordinal();
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static void setAge(Block block, int age) {
        init();
        if (useModernApi) {
            try {
                Object bd = getBlockData.invoke(block);
                setAge.invoke(bd, age);
                setBlockData.invoke(block, bd);
            } catch (Exception e) {
                // ignore
            }
        }
        // 1.12.2: no-op for now, crop growth handled differently
    }

    public static int getMaximumAge(Block block) {
        init();
        if (useModernApi) {
            try {
                Object bd = getBlockData.invoke(block);
                return (int) getMaximumAge.invoke(bd);
            } catch (Exception e) {
                return 7;
            }
        }
        return 7;
    }

    public static int getLevel(Block block) {
        init();
        if (useModernApi) {
            try {
                Object bd = getBlockData.invoke(block);
                return (int) getLevel.invoke(bd);
            } catch (Exception e) {
                return 0;
            }
        }
        if (VersionUtil.is1_12()) {
            try {
                Object blockData = block.getState().getData();
                if (blockData instanceof org.bukkit.material.Cauldron) {
                    return ((org.bukkit.material.Cauldron) blockData).getData();
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static void setLevel(Block block, int level) {
        init();
        if (useModernApi) {
            try {
                Object bd = getBlockData.invoke(block);
                setLevel.invoke(bd, level);
                setBlockData.invoke(block, bd);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public static boolean isFullyGrown(Block block) {
        return getAge(block) >= getMaximumAge(block);
    }
}
