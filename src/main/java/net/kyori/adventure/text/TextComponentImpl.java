package net.kyori.adventure.text;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

class TextComponentImpl implements Component {

    private final String content;

    TextComponentImpl(String content) {
        this.content = content;
    }

    @Override
    public Component style(Style style) {
        return this;
    }

    @Override
    public Component clickEvent(net.kyori.adventure.text.event.ClickEvent event) {
        return this;
    }

    @Override
    public Component hoverEvent(net.kyori.adventure.text.event.HoverEvent event) {
        return this;
    }

    @Override
    public Component append(Component component) {
        return this;
    }

    @Override
    public String content() {
        return content;
    }
}
