package cedo.util;

import java.awt.*;
import java.util.function.Supplier;

public enum ColorManager {
    BLUE(() -> new Color(116, 202, 255)),
    NICE_BLUE(() -> new Color(116, 202, 255)),
    DARK_PURPLE(() -> new Color(133, 46, 215)),
    GREEN(() -> new Color(0, 255, 138)),
    PURPLE(() -> new Color(198, 139, 255)),
    WHITE(() -> Color.WHITE);

    private final Supplier<Color> colorSupplier;

    ColorManager(Supplier<Color> colorSupplier) {
        this.colorSupplier = colorSupplier;
    }

    public static Color fade(final Color color) {
        return fade(color, 2, 100);
    }

    public static Color flash(final Color color) {
        return flash(color, 2, 10);
    }

    public static Color fade(final Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((((System.currentTimeMillis() % 2000) / 1000f + (index / (float) count) * 2F) % 2F) - 1);
        brightness = 0.5f + (0.5f * brightness);
        hsb[2] = brightness % 2F;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color flash(final Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((((System.currentTimeMillis() % 200) / 500F + (index / (float) count) * 2F) % 2F) - 1);
        brightness = 0.5f + (0.5f * brightness);
        hsb[2] = brightness % 2F;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color rainbow(int index, double speed) {
        int angle = (int) ((System.currentTimeMillis() / speed + index) % 360);
        float hue = angle / 360f;
        int color = Color.HSBtoRGB(hue, 1, 1);
        return new Color(color);
    }

    public static Color rainbow(int index, int speed, float saturation, float brightness, float opacity) {
        int angle = (int) ((System.currentTimeMillis() / speed + index) % 360);
        float hue = angle / 360f;
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        Color obj = new Color(color);
        return new Color(obj.getRed(), obj.getGreen(), obj.getBlue(), Math.max(0, Math.min(255, (int) (opacity * 255))));
    }

    public static Color astolfo(int index, int speed, float saturation, float brightness, float opacity) {
        int angle = (int) ((System.currentTimeMillis() / speed + index) % 360);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        float hue = angle / 360f;

        int color = Color.HSBtoRGB(brightness, saturation, hue);
        Color obj = new Color(color);
        return new Color(obj.getRed(), obj.getGreen(), obj.getBlue(), Math.max(0, Math.min(255, (int) (opacity * 255))));
    }


    public Color getColor() {
        return colorSupplier.get();
    }
}
