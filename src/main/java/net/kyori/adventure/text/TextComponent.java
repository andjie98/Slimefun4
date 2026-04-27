package net.kyori.adventure.text;

public interface TextComponent extends Component {

    static TextComponent of(String content) {
        return new TextComponentImpl(content)::content;
    }

    @Override
    String content();
}
