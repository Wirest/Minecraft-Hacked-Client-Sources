package cedo.ui.changelog;

import java.awt.*;

public enum ChangeType {
    NEW(new Color(17, 255, 85).getRGB()),
    UPDATE(new Color(0, 255, 255).getRGB()),
    FIX(new Color(255, 255, 69).getRGB()),
    DELETE(Color.RED.getRGB());

    private final int color;

    ChangeType(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

}
