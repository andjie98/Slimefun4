package org.bukkit;

import java.util.Set;

public interface Tag<T> {

    boolean isTagged(T item);

    Set<T> getValues();

    NamespacedKey getKey();
}
