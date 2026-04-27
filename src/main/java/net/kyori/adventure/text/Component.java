package net.kyori.adventure.text;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

public interface Component {

    static Component text(String content) {
        return new TextComponentImpl(content);
    }

    static Component text(String content, TextColor color) {
        return new TextComponentImpl(content);
    }

    Component style(Style style);

    Component clickEvent(ClickEvent event);

    Component hoverEvent(HoverEvent event);

    Component append(Component component);

    String content();
}
