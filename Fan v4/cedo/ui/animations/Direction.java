package cedo.ui.animations;

public enum Direction {
    FORWARDS(new int[]{0, 0}),
    BACKWARDS(new int[]{0, 0}),
    UP(new int[]{0, -1}),
    DOWN(new int[]{0, 1}),
    LEFT(new int[]{-1, 0}),
    RIGHT(new int[]{1, 0});

    private final int[] xy;

    Direction(int[] xy) {
        this.xy = xy;
    }

    public int[] getXy() {
        return xy;
    }

}
