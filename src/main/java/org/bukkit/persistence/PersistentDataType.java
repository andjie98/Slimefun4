package org.bukkit.persistence;

import org.bukkit.NamespacedKey;

public final class PersistentDataType<T, Z> {

    public static final PersistentDataType<String, String> STRING = new PersistentDataType<>(String.class, String.class);
    public static final PersistentDataType<Integer, Integer> INTEGER = new PersistentDataType<>(Integer.class, Integer.class);
    public static final PersistentDataType<Double, Double> DOUBLE = new PersistentDataType<>(Double.class, Double.class);
    public static final PersistentDataType<Float, Float> FLOAT = new PersistentDataType<>(Float.class, Float.class);
    public static final PersistentDataType<Long, Long> LONG = new PersistentDataType<>(Long.class, Long.class);
    public static final PersistentDataType<Short, Short> SHORT = new PersistentDataType<>(Short.class, Short.class);
    public static final PersistentDataType<Byte, Byte> BYTE = new PersistentDataType<>(Byte.class, Byte.class);
    public static final PersistentDataType<byte[], byte[]> BYTE_ARRAY = new PersistentDataType<>(byte[].class, byte[].class);
    public static final PersistentDataType<int[], int[]> INTEGER_ARRAY = new PersistentDataType<>(int[].class, int[].class);
    public static final PersistentDataType<long[], long[]> LONG_ARRAY = new PersistentDataType<>(long[].class, long[].class);
    public static final PersistentDataType<PersistentDataContainer, PersistentDataContainer> TAG_CONTAINER =
            new PersistentDataType<>(PersistentDataContainer.class, PersistentDataContainer.class);

    private final Class<T> primitiveType;
    private final Class<Z> complexType;

    public PersistentDataType(Class<T> primitiveType, Class<Z> complexType) {
        this.primitiveType = primitiveType;
        this.complexType = complexType;
    }

    public Class<T> getPrimitiveType() {
        return primitiveType;
    }

    public Class<Z> getComplexType() {
        return complexType;
    }
}
