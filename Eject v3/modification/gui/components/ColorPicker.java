package modification.gui.components;

import modification.files.ColorFile;
import modification.gui.Component;
import modification.main.Modification;
import modification.modules.misc.GUI;

import java.awt.*;
import java.util.Objects;

public class ColorPicker
        extends Component {
    private Robot robot;
    private Color currentRGB;
    private Point rgbPoint;
    private Point colorPoint;
    private float xPosRGB;
    private float yPosRGB;
    private float xPos;
    private float yPos;
    private boolean rgbDragging;
    private boolean dragging;

    public ColorPicker() {
        super(null);
        try {
            this.robot = new Robot();
        } catch (AWTException localAWTException) {
            Modification.LOG_UTIL.sendConsoleMessage("Error: Couldn't create robot");
        }
        this.currentRGB = Modification.color;
    }

    public void draw(int paramInt1, int paramInt2) {
        GUI localGUI = (GUI) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("GUI"));
        if (((String) localGUI.theme.value).equals("Abraxas")) {
            Modification.RENDER_UTIL.renderColorPicker(this.x, this.y, 100.0F, 100.0F, this.currentRGB.getRGB());
            if ((this.rgbDragging) && (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y + 130.0F, 100.0F, 15.0F))) {
                this.rgbPoint = MouseInfo.getPointerInfo().getLocation();
                this.xPosRGB = paramInt1;
                this.yPosRGB = paramInt2;
                this.currentRGB = this.robot.getPixelColor(this.rgbPoint.x, this.rgbPoint.y);
            }
            if ((this.dragging) && (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y, 100.0F, 100.0F))) {
                this.colorPoint = MouseInfo.getPointerInfo().getLocation();
                this.xPos = paramInt1;
                this.yPos = paramInt2;
                Modification.color = this.robot.getPixelColor(this.colorPoint.x, this.colorPoint.y);
                Modification.FILE_MANAGER.update(ColorFile.class);
            }
            if (this.dragging) {
                if (this.rgbPoint != null) {
                    Modification.RENDER_UTIL.drawCircle(this.xPosRGB, this.yPosRGB, 3.0F, 83886080, Color.BLACK.getRGB());
                }
                if (this.colorPoint != null) {
                    Modification.RENDER_UTIL.drawCircle(this.xPos, this.yPos, 3.0F, 83886080, Color.BLACK.getRGB());
                }
            }
            return;
        }
        Modification.RENDER_UTIL.renderColorPicker(this.x, this.y, 120.0F, 120.0F, this.currentRGB.getRGB());
        if ((this.rgbDragging) && (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y + 130.0F, 120.0F, 15.0F))) {
            this.rgbPoint = MouseInfo.getPointerInfo().getLocation();
            this.xPosRGB = paramInt1;
            this.yPosRGB = paramInt2;
            this.currentRGB = this.robot.getPixelColor(this.rgbPoint.x, this.rgbPoint.y);
        }
        if ((this.dragging) && (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y, 120.0F, 120.0F))) {
            this.colorPoint = MouseInfo.getPointerInfo().getLocation();
            this.xPos = paramInt1;
            this.yPos = paramInt2;
            Modification.color = this.robot.getPixelColor(this.colorPoint.x, this.colorPoint.y);
            Modification.FILE_MANAGER.update(ColorFile.class);
        }
        if (this.dragging) {
            if (this.rgbPoint != null) {
                Modification.RENDER_UTIL.drawCircle(this.xPosRGB, this.yPosRGB, 3.0F, 83886080, Color.BLACK.getRGB());
            }
            if (this.colorPoint != null) {
                Modification.RENDER_UTIL.drawCircle(this.xPos, this.yPos, 3.0F, 83886080, Color.BLACK.getRGB());
            }
        }
    }

    public void click(int paramInt1, int paramInt2, int paramInt3) {
        if (paramInt3 == 0) {
            if (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y + 130.0F, 120.0F, 15.0F)) {
                this.rgbDragging = true;
            }
            if (Modification.RENDER_UTIL.mouseHovered(paramInt1, paramInt2, this.x, this.y, 120.0F, 120.0F)) {
                this.dragging = true;
            }
        }
    }

    public void release(int paramInt) {
        if (paramInt == 0) {
            this.dragging = false;
            this.rgbDragging = false;
        }
    }
}




