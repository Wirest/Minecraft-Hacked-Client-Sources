package com.ihl.client.comparator;

import net.minecraft.client.gui.FontRenderer;

import java.util.Comparator;

public class StringLengthComparator implements Comparator<String> {

    public static enum Direction {
        SMALLTOLARGE,
        LARGETOSMALL
    }

    private Direction direction;
    private FontRenderer fontRenderer;

    public StringLengthComparator(Direction direction, FontRenderer fontRenderer) {
        this.direction = direction;
        this.fontRenderer = fontRenderer;
    }

    @Override
    public int compare(String a, String b) {
        double c = fontRenderer.getStringWidth(a);
        double d = fontRenderer.getStringWidth(b);
        return c < d ? direction == Direction.LARGETOSMALL ? 1 : -1 : c == d ? 0 : direction == Direction.LARGETOSMALL ? -1 : 1;
    }

}
