package net.kyori.adventure.text.event;

import net.kyori.adventure.text.Component;

public final class HoverEvent {

    public enum Action {
        SHOW_TEXT,
        SHOW_ITEM,
        SHOW_ENTITY
    }

    private final Action action;
    private final Component value;

    public HoverEvent(Action action, Component value) {
        this.action = action;
        this.value = value;
    }

    public static HoverEvent showText(Component text) {
        return new HoverEvent(Action.SHOW_TEXT, text);
    }

    public Action action() {
        return action;
    }

    public Component value() {
        return value;
    }
}
