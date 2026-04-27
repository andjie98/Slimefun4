package city.norain.slimefun4.compatibillty;

import com.cryptomorin.xseries.XMaterial;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class LegacyPersistentDataCompat {

    private static final Map<String, Map<String, String>> STRING_DATA = new HashMap<>();
    private static final Map<String, Map<String, Integer>> INT_DATA = new HashMap<>();

    private static Method metaGetPersistentDataContainer;
    private static Method containerHas;
    private static Method containerGet;
    private static Method containerSet;
    private static Object persistentDataTypeString;
    private static Object persistentDataTypeInteger;
    private static boolean initialized = false;
    private static boolean useModernApi = false;

    private static synchronized void init() {
        if (initialized) return;
        initialized = true;
        try {
            Class<?> pdcClass = Class.forName("org.bukkit.persistence.PersistentDataContainer");
            Class<?> pdtClass = Class.forName("org.bukkit.persistence.PersistentDataType");
            metaGetPersistentDataContainer = ItemMeta.class.getMethod("getPersistentDataContainer");
            containerHas = pdcClass.getMethod("has", NamespacedKey.class, pdtClass);
            containerGet = pdcClass.getMethod("get", NamespacedKey.class, pdtClass);
            containerSet = pdcClass.getMethod("set", NamespacedKey.class, pdtClass, Object.class);
            persistentDataTypeString = pdtClass.getField("STRING").get(null);
            persistentDataTypeInteger = pdtClass.getField("INTEGER").get(null);
            useModernApi = true;
        } catch (Exception e) {
            useModernApi = false;
        }
    }

    public static boolean has(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull Object type) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                return (boolean) containerHas.invoke(container, key, resolveType(type));
            } catch (Exception e) {
                return false;
            }
        }
        return hasLegacy(meta, key);
    }

    public static <T> T get(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull Object type) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                return (T) containerGet.invoke(container, key, resolveType(type));
            } catch (Exception e) {
                return null;
            }
        }
        return getLegacy(meta, key);
    }

    public static void set(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull Object type, @Nonnull Object value) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                containerSet.invoke(container, key, resolveType(type), value);
            } catch (Exception e) {
                setLegacy(meta, key, value);
            }
        } else {
            setLegacy(meta, key, value);
        }
    }

    private static Object resolveType(Object type) {
        if (type == null) return persistentDataTypeString;
        String typeName = type.getClass().getName();
        if (typeName.contains("STRING") || type.toString().equals("STRING")) return persistentDataTypeString;
        if (typeName.contains("INTEGER") || type.toString().equals("INTEGER")) return persistentDataTypeInteger;
        return persistentDataTypeString;
    }

    private static String itemKey(ItemMeta meta) {
        return Integer.toHexString(System.identityHashCode(meta));
    }

    private static boolean hasLegacy(ItemMeta meta, NamespacedKey key) {
        Map<String, String> data = STRING_DATA.get(itemKey(meta));
        return data != null && data.containsKey(key.toString());
    }

    private static <T> T getLegacy(ItemMeta meta, NamespacedKey key) {
        Map<String, String> data = STRING_DATA.get(itemKey(meta));
        if (data == null) return null;
        return (T) data.get(key.toString());
    }

    private static void setLegacy(ItemMeta meta, NamespacedKey key, Object value) {
        String ik = itemKey(meta);
        STRING_DATA.computeIfAbsent(ik, k -> new HashMap<>()).put(key.toString(), String.valueOf(value));
    }

    public static boolean hasString(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                return (boolean) containerHas.invoke(container, key, persistentDataTypeString);
            } catch (Exception e) {
                return false;
            }
        }
        return hasLegacy(meta, key);
    }

    @Nullable
    public static String getString(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                return (String) containerGet.invoke(container, key, persistentDataTypeString);
            } catch (Exception e) {
                return null;
            }
        }
        return getLegacy(meta, key);
    }

    public static void setString(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull String value) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                containerSet.invoke(container, key, persistentDataTypeString, value);
            } catch (Exception e) {
                setLegacy(meta, key, value);
            }
        } else {
            setLegacy(meta, key, value);
        }
    }

    public static boolean hasInteger(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                return (boolean) containerHas.invoke(container, key, persistentDataTypeInteger);
            } catch (Exception e) {
                return false;
            }
        }
        Map<String, Integer> data = INT_DATA.get(itemKey(meta));
        return data != null && data.containsKey(key.toString());
    }

    @Nullable
    public static Integer getInteger(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                return (Integer) containerGet.invoke(container, key, persistentDataTypeInteger);
            } catch (Exception e) {
                return null;
            }
        }
        Map<String, Integer> data = INT_DATA.get(itemKey(meta));
        return data != null ? data.get(key.toString()) : null;
    }

    public static void setInteger(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, int value) {
        init();
        if (useModernApi) {
            try {
                Object container = metaGetPersistentDataContainer.invoke(meta);
                containerSet.invoke(container, key, persistentDataTypeInteger, value);
            } catch (Exception e) {
                INT_DATA.computeIfAbsent(itemKey(meta), k -> new HashMap<>()).put(key.toString(), value);
            }
        } else {
            INT_DATA.computeIfAbsent(itemKey(meta), k -> new HashMap<>()).put(key.toString(), value);
        }
    }
}
