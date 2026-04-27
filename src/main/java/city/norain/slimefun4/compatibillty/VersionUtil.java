package city.norain.slimefun4.compatibillty;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class VersionUtil {

    private static final int MAJOR;
    private static final int MINOR;

    static {
        String[] parts = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        MAJOR = Integer.parseInt(parts[1]);
        MINOR = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
    }

    public static boolean isAtLeast(int minor) {
        return MAJOR >= minor;
    }

    public static boolean is1_12() {
        return MAJOR == 12;
    }

    public static boolean is1_13_OrAbove() {
        return MAJOR >= 13;
    }

    public static boolean is1_14_OrAbove() {
        return MAJOR >= 14;
    }

    public static boolean is1_16_OrAbove() {
        return MAJOR >= 16;
    }

    public static boolean is1_19_OrAbove() {
        return MAJOR >= 19;
    }

    public static boolean is1_21_OrAbove() {
        return MAJOR >= 21;
    }

    public static boolean is1_20_OrAbove() {
        return MAJOR >= 20;
    }

    public static int getMinorVersion() {
        return MAJOR;
    }
}
