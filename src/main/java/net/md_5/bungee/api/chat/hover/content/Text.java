package net.md_5.bungee.api.chat.hover.content;

import net.md_5.bungee.api.chat.BaseComponent;

public class Text extends Content {

    private final BaseComponent[] value;

    public Text(BaseComponent[] value) {
        this.value = value;
    }

    public Text(String value) {
        this.value = new BaseComponent[0];
    }

    public BaseComponent[] getValue() {
        return value;
    }
}
