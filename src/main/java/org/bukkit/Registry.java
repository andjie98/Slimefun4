package org.bukkit;

import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Registry<T> extends Iterable<T> {

    @Nullable
    T get(@Nonnull NamespacedKey key);

    @Nonnull
    Iterator<T> iterator();
}
