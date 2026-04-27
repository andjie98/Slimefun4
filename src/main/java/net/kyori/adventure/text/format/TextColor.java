package net.kyori.adventure.text.format;

public interface TextColor {

    int red();

    int green();

    int blue();

    static TextColor color(int rgb) {
        return new TextColor() {
            @Override
            public int red() {
                return (rgb >> 16) & 0xFF;
            }

            @Override
            public int green() {
                return (rgb >> 8) & 0xFF;
            }

            @Override
            public int blue() {
                return rgb & 0xFF;
            }
        };
    }
}
