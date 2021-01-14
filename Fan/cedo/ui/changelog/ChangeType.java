package cedo.ui.changelog;

import java.awt.*;

public enum ChangeType {
    NEW(0xff11ff55),
    UPDATE(0xff00ffff),
    DELETE(Color.RED.getRGB());

    private final int color;

    ChangeType(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

}
