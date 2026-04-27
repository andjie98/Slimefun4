package net.kyori.adventure.text.event;

import net.kyori.adventure.text.Component;

public final class ClickEvent {

    public enum Action {
        OPEN_URL,
        RUN_COMMAND,
        SUGGEST_COMMAND,
        COPY_TO_CLIPBOARD
    }

    private final Action action;
    private final String value;

    public ClickEvent(Action action, String value) {
        this.action = action;
        this.value = value;
    }

    public static ClickEvent openUrl(String url) {
        return new ClickEvent(Action.OPEN_URL, url);
    }

    public static ClickEvent runCommand(String command) {
        return new ClickEvent(Action.RUN_COMMAND, command);
    }

    public static ClickEvent suggestCommand(String command) {
        return new ClickEvent(Action.SUGGEST_COMMAND, command);
    }

    public static ClickEvent copyToClipboard(String value) {
        return new ClickEvent(Action.COPY_TO_CLIPBOARD, value);
    }

    public Action action() {
        return action;
    }

    public String value() {
        return value;
    }
}
