package info.sigmaclient.gui.click.components;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ui.UI;

/**
 * Created by cool1 on 2/9/2017.
 */
public class RGBSlider {

    public float x;
    public float y;
    public boolean dragging;
    public double dragX;
    public ColorPreview colorPreview;
    public double lastDragX;
    public Colors rgba;

    public RGBSlider(float x, float y, ColorPreview colorPreview, RGBSlider.Colors colors) {
        this.x = x;
        this.y = y;
        this.colorPreview = colorPreview;
        int colorShit = 0;
        switch (colors) {
            case RED:
                colorShit = colorPreview.colorObject.getRed();
                break;
            case GREEN:
                colorShit = colorPreview.colorObject.getGreen();
                break;
            case BLUE:
                colorShit = colorPreview.colorObject.getBlue();
                break;
            case ALPHA:
                colorShit = colorPreview.colorObject.getAlpha();
                break;
        }
        rgba = colors;
        this.dragX = colorShit * 90 / 255;
    }

    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderDraw(this, x, y);
        }
    }

    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderClick(this, x, y, button);
        }
    }

    public void mouseReleased(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.rgbSliderMovedOrUp(this, x, y, button);
        }
    }

    public enum Colors {
        RED, GREEN, BLUE, ALPHA
    }

}
