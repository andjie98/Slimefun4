package city.norain.slimefun4.compatibillty;

import java.lang.reflect.Method;
import lombok.experimental.UtilityClass;
import org.bukkit.attribute.Attribute;

@UtilityClass
public class LegacyRegistryCompat {

    private static boolean initialized = false;
    private static boolean useRegistry = false;
    private static Method registryGet;

    private static synchronized void init() {
        if (initialized) return;
        initialized = true;
        try {
            Class<?> registryClass = Class.forName("org.bukkit.Registry");
            Object attributeRegistry = registryClass.getField("ATTRIBUTE").get(null);
            registryGet = registryClass.getMethod("get", org.bukkit.NamespacedKey.class);
            useRegistry = true;
        } catch (Exception e) {
            useRegistry = false;
        }
    }

    public static Attribute getMaxHealthAttribute() {
        init();
        if (useRegistry) {
            try {
                Object key = org.bukkit.NamespacedKey.fromString("max_health");
                return (Attribute) registryGet.invoke(
                        Class.forName("org.bukkit.Registry").getField("ATTRIBUTE").get(null), key);
            } catch (Exception e) {
                return Attribute.GENERIC_MAX_HEALTH;
            }
        }
        try {
            return Attribute.valueOf("GENERIC_MAX_HEALTH");
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
