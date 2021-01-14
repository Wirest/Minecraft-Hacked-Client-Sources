package moonx.ohare.client.module.impl.visuals;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.render.CrosshairEvent;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.ColorValue;
import moonx.ohare.client.utils.value.impl.NumberValue;

import java.awt.*;

public class Crosshair extends Module {
    private BooleanValue dynamic = new BooleanValue("Dynamic", true);
    private NumberValue<Double> width = new NumberValue<>("Width", 1.0D, 0.5D, 10.0D, 0.5D);
    private NumberValue<Double> gap = new NumberValue<>("Gap", 3.0D, 0.5D, 10.0D, 0.5D);
    private NumberValue<Double> length = new NumberValue<>("Length", 7.0D, 0.5D, 100.0D, 0.5D);
    private NumberValue<Double> dynamicgap = new NumberValue<>("DynamicGap", 1.5D, 0.5D, 10.0D, 0.5D);
    private ColorValue colorValue = new ColorValue("Color",new Color(255,0,0).getRGB());
    private BooleanValue rainbow = new BooleanValue("Rainbow", false);
    public BooleanValue staticRainbow = new BooleanValue("Static Rainbow", false);

    public Crosshair() {
        super("Crosshair", Category.VISUALS, 0);
        setHidden(true);
    }

    @Handler
    public void onCrosshair(CrosshairEvent event) {
        event.setCanceled(true);
    }

    @Handler
    public void onRender2D(Render2DEvent event) {
        Color rai = new Color(RenderUtil.getRainbow(6000, -15,0.85f));
        int color = staticRainbow.isEnabled() ? color(2, 100) : (rainbow.isEnabled() ? new Color(rai.getRed(), rai.getGreen(), rai.getBlue(), new Color(colorValue.getValue()).getAlpha()).getRGB() : colorValue.getValue());

        final double middlex = event.getScaledResolution().getScaledWidth() / 2;
        final double middley = event.getScaledResolution().getScaledHeight() / 2;
        // top box
        RenderUtil.drawBordered(middlex - (width.getValue()), middley - (gap.getValue() + length.getValue()) - ((getMc().thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middlex + (width.getValue()), middley - (gap.getValue()) - ((getMc().thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), 0.5, color, 0xff000000);
        // bottom box
        RenderUtil.drawBordered(middlex - (width.getValue()), middley + (gap.getValue()) + ((getMc().thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middlex + (width.getValue()), middley + (gap.getValue() + length.getValue()) + ((getMc().thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), 0.5, color, 0xff000000);
        // left box
        RenderUtil.drawBordered(middlex - (gap.getValue() + length.getValue()) - ((getMc().thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley - (width.getValue()), middlex - (gap.getValue()) - ((getMc().thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley + (width.getValue()), 0.5, color, 0xff000000);
        // right box
        RenderUtil.drawBordered(middlex + (gap.getValue()) + ((getMc().thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley - (width.getValue()), middlex + (gap.getValue() + length.getValue()) + ((getMc().thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley + (width.getValue()), 0.5, color, 0xff000000);
    }
    public int color(int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(new Color(colorValue.getValue()).getRed(),new Color(colorValue.getValue()).getGreen(),new Color(colorValue.getValue()).getBlue(), hsb);

        float brightness = Math.abs(((getOffset() + (index / (float) count) * 2) % 2) - 1);

        brightness = 0.4f + (0.4f * brightness);
        hsb[2] = brightness % 1f;
        Color clr = new Color(Color.HSBtoRGB(hsb[0],hsb[1], hsb[2]));
        return new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),new Color(colorValue.getValue()).getAlpha()).getRGB();
    }

    private float getOffset() {
        return (System.currentTimeMillis() % 2000) / 1000f;
    }
}
