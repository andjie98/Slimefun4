package net.kyori.adventure.text.format;

import java.util.Set;

public interface Style {

    static Style style() {
        return new Style() {
            @Override
            public TextColor color() {
                return null;
            }

            @Override
            public Set<TextDecoration> decorations() {
                return java.util.Collections.emptySet();
            }
        };
    }

    static Style style(TextColor color) {
        return style();
    }

    static Style style(TextColor color, TextDecoration... decorations) {
        return style();
    }

    TextColor color();

    Set<TextDecoration> decorations();
}
