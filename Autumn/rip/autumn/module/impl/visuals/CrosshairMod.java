package rip.autumn.module.impl.visuals;

import java.awt.Color;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import rip.autumn.annotations.Label;
import rip.autumn.events.render.RenderCrosshairEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.ColorOption;
import rip.autumn.module.option.impl.DoubleOption;

@Label("Crosshair")
@Category(ModuleCategory.VISUALS)
public final class CrosshairMod extends Module {
   public static final BoolOption dot = new BoolOption("Dot", true);
   public static final DoubleOption thickness = new DoubleOption("Thickness", 1.0D, 0.5D, 4.0D, 0.5D);
   public static final DoubleOption length = new DoubleOption("Length", 3.0D, 0.5D, 10.0D, 0.5D);
   public static final DoubleOption gap = new DoubleOption("Gap", 3.0D, 0.5D, 4.0D, 0.5D);
   public static final BoolOption outline = new BoolOption("Outline", true);
   public static final DoubleOption outlineThickness;
   public static final ColorOption color;

   public CrosshairMod() {
      this.addOptions(new Option[]{dot, thickness, length, gap, outline, outlineThickness, color});
   }

   @Listener(RenderCrosshairEvent.class)
   public final void onRenderCrosshair(RenderCrosshairEvent event) {
      GL11.glPushMatrix();
      event.setCancelled();
      ScaledResolution sr = event.getScaledRes();
      double thickness = (Double)CrosshairMod.thickness.getValue() / 2.0D;
      int width = sr.getScaledWidth();
      int height = sr.getScaledHeight();
      float middleX = (float)width / 2.0F;
      float middleY = (float)height / 2.0F;
      if (dot.getValue()) {
         Gui.drawRect((double)middleX - thickness - (Double)outlineThickness.getValue(), (double)middleY - thickness - (Double)outlineThickness.getValue(), (double)middleX + thickness + (Double)outlineThickness.getValue(), (double)middleY + thickness + (Double)outlineThickness.getValue(), Color.BLACK.getRGB());
         Gui.drawRect((double)middleX - thickness, (double)middleY - thickness, (double)middleX + thickness, (double)middleY + thickness, ((Color)color.getValue()).getRGB());
      }

      Gui.drawRect((double)middleX - thickness - (Double)outlineThickness.getValue(), (double)middleY - (Double)gap.getValue() - (Double)length.getValue() - (Double)outlineThickness.getValue(), (double)middleX + thickness + (Double)outlineThickness.getValue(), (double)middleY - (Double)gap.getValue() + (Double)outlineThickness.getValue(), Color.BLACK.getRGB());
      Gui.drawRect((double)middleX - thickness, (double)middleY - (Double)gap.getValue() - (Double)length.getValue(), (double)middleX + thickness, (double)middleY - (Double)gap.getValue(), ((Color)color.getValue()).getRGB());
      Gui.drawRect((double)middleX - (Double)gap.getValue() - (Double)length.getValue() - (Double)outlineThickness.getValue(), (double)middleY - thickness - (Double)outlineThickness.getValue(), (double)middleX - (Double)gap.getValue() + (Double)outlineThickness.getValue(), (double)middleY + thickness + (Double)outlineThickness.getValue(), Color.BLACK.getRGB());
      Gui.drawRect((double)middleX - (Double)gap.getValue() - (Double)length.getValue(), (double)middleY - thickness, (double)middleX - (Double)gap.getValue(), (double)middleY + thickness, ((Color)color.getValue()).getRGB());
      Gui.drawRect((double)middleX - thickness - (Double)outlineThickness.getValue(), (double)middleY + (Double)gap.getValue() - (Double)outlineThickness.getValue(), (double)middleX + thickness + (Double)outlineThickness.getValue(), (double)middleY + (Double)gap.getValue() + (Double)length.getValue() + (Double)outlineThickness.getValue(), Color.BLACK.getRGB());
      Gui.drawRect((double)middleX - thickness, (double)middleY + (Double)gap.getValue(), (double)middleX + thickness, (double)middleY + (Double)gap.getValue() + (Double)length.getValue(), ((Color)color.getValue()).getRGB());
      Gui.drawRect((double)middleX + (Double)gap.getValue() - (Double)outlineThickness.getValue(), (double)middleY - thickness - (Double)outlineThickness.getValue(), (double)middleX + (Double)gap.getValue() + (Double)length.getValue() + (Double)outlineThickness.getValue(), (double)middleY + thickness + (Double)outlineThickness.getValue(), Color.BLACK.getRGB());
      Gui.drawRect((double)middleX + (Double)gap.getValue(), (double)middleY - thickness, (double)middleX + (Double)gap.getValue() + (Double)length.getValue(), (double)middleY + thickness, ((Color)color.getValue()).getRGB());
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableBlend();
      GL11.glPopMatrix();
   }

   static {
      outlineThickness = new DoubleOption("Outline thickness", 0.5D, outline::getValue, 0.5D, 4.0D, 0.5D);
      color = new ColorOption("Color", new Color(163, 61, 61));
   }
}
