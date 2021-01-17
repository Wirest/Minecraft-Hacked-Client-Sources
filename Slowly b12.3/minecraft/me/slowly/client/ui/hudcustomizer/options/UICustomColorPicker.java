package me.slowly.client.ui.hudcustomizer.options;

import java.awt.Color;
import java.math.BigDecimal;
import me.slowly.client.Client;
import me.slowly.client.ui.hudcustomizer.CustomValue;
import me.slowly.client.util.Colors;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

public class UICustomColorPicker {
   private CustomValue value;
   private float currentHue = -1.0F;
   private float hueNow = 0.0F;
   private float cursorX = 0.0F;
   private float cursorY = 0.0F;
   private boolean drag;
   private boolean drag2;
   private boolean drag3;
   private boolean loaded = false;
   private Color loadColor = null;
   private boolean setColor = false;
   int x;
   int y;
   int width;
   int height;

   public UICustomColorPicker(CustomValue value) {
      this.value = value;
   }

   public void draw(int mouseX, int mouseY, int x, int y, double alpha) {
      float width = 75.0F;
      UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton10;
      font.drawString(this.value.getValueName(), (float)x + (width - (float)font.getStringWidth(this.value.getValueName())) / 2.0F, (float)(y - font.FONT_HEIGHT), -1);
      Color c = (Color)this.value.getValueState();
      if (!this.setColor) {
         this.loadColor = c;
      }

      this.setColor = true;
      int pixel = 28;
      Color newCol;
      if (!this.loaded) {
         float[] hsbValues = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), (float[])null);
         BigDecimal hue = BigDecimal.valueOf((double)(hsbValues[0] * 360.0F));
         BigDecimal saturation_ = BigDecimal.valueOf((double)hsbValues[1]);
         BigDecimal brightness = BigDecimal.valueOf((double)hsbValues[2]);
         newCol = new Color(this.getHue(hue.floatValue(), saturation_.floatValue(), brightness.floatValue()));
         this.hueNow = hue.floatValue();
         this.cursorX = (float)x + width * saturation_.floatValue();
         this.cursorY = (float)y + width * (1.0F - brightness.floatValue());
         if (this.loadColor.getRed() == newCol.getRed() && this.loadColor.getGreen() == newCol.getGreen() && this.loadColor.getBlue() == newCol.getBlue()) {
            this.loaded = true;
         }
      }

      for(int pixelX = x; (float)pixelX < (float)x + width; pixelX = (int)((float)pixelX + width / (float)pixel)) {
         for(int pixelY = y; (float)pixelY < (float)y + width; pixelY = (int)((float)pixelY + width / (float)pixel)) {
            float min = (float)x;
            float max = (float)x + width;
            float satuation = (float)(pixelX - x) / width;
            float darkstep = 1.0F - (float)(pixelY - y) / width;
            Gui.drawRect((float)pixelX, (float)pixelY, (float)pixelX + width / (float)pixel + 1.0F, (float)pixelY + width / (float)pixel + 1.0F, this.getHue(this.hueNow, satuation, darkstep));
         }
      }

      float saturation = (this.cursorX - (float)x) / width;
      float darkstep = 1.0F - (this.cursorY - (float)y) / width;
      this.drawCursor(mouseX, mouseY, x, y, (int)width, (int)width);
      y = (int)((float)y + width + 5.0F);

      int i;
      for(i = 0; i < 359; ++i) {
         Gui.drawRect((float)x + width * ((float)i / 360.0F), (float)y, (float)x + width * ((float)i / 360.0F) + 1.0F, (float)(y + 10), this.getHue((float)i));
      }

      for(i = 0; i < 360; ++i) {
         if (this.getHue((float)i) == this.getHue(this.hueNow)) {
            Gui.drawRect((float)x + width * ((float)i / 360.0F), (float)y, (float)x + width * ((float)i / 360.0F) + 1.0F, (float)(y + 10), Colors.BLACK.c);
            this.currentHue = (float)this.getHue((float)i);
            this.hueNow = (float)i;
         }
      }

      String hex = "Zero";

      try {
         hex = Integer.toHexString(c.getRGB()).substring(2);
      } catch (Exception var20) {
         ;
      }

      font.drawString("RGB: " + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue(), (float)x + width + 2.0F, (float)y, -1);
      font.drawString("HEX: " + hex, (float)x + width + 2.0F, (float)(y + font.FONT_HEIGHT), -1);
      boolean hoverHueBar = mouseX >= x && (float)mouseX <= (float)x + width && mouseY >= y && mouseY <= y + 10;
      if (Mouse.isButtonDown(0) && hoverHueBar && !this.drag) {
         int var10000 = (int)((float)x + width);
         int newMouseX = mouseX - x;
         int color = this.getHue((float)((int)(359.0F * ((float)newMouseX / width))));
         this.hueNow = (float)((int)(359.0F * ((float)newMouseX / width)));
         Color newCol1 = new Color(this.getHue((float)color, saturation, darkstep));
         this.value.setValueState(new Color((float)newCol1.getRed() / 255.0F, (float)newCol1.getGreen() / 255.0F, (float)newCol1.getBlue() / 255.0F, (float)alpha));
      }

      newCol = new Color(this.getHue(this.hueNow, saturation, darkstep));
      this.value.setValueState(new Color((float)newCol.getRed() / 255.0F, (float)newCol.getGreen() / 255.0F, (float)newCol.getBlue() / 255.0F, (float)alpha));
   }

   private void drawCursor(int mouseX, int mouseY, int x, int y, int width, int height) {
      boolean var10000;
      if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
         var10000 = true;
      } else {
         var10000 = false;
      }

      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      float radius = 2.5F;
      if (this.drag) {
         this.cursorX = (float)mouseX;
         this.cursorY = (float)mouseY;
         if (this.cursorX > (float)(x + width)) {
            this.cursorX = (float)(x + width);
         }

         if (this.cursorX < (float)x) {
            this.cursorX = (float)x;
         }

         if (this.cursorY > (float)(y + width)) {
            this.cursorY = (float)(y + width);
         }

         if (this.cursorY < (float)y) {
            this.cursorY = (float)y;
         }
      }

      int color = this.cursorX <= (float)(x + width / 2) && (double)this.cursorY <= (double)y + (double)height * 0.75D ? Colors.BLACK.c : Colors.WHITE.c;
      Gui.drawCircle(this.cursorX, this.cursorY, radius, new Color(color));
   }

   private int getHue(float value, float saturation, float brightness) {
       float hue = 1.0f - value / 360.0f;
       int color = (int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, saturation, brightness)), 16);
       return color;
   }
   public void mouseClick(int mouseX, int mouseY) {
      boolean hovering = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
      if (hovering) {
         this.drag = true;
      }

   }

   public void mouseRelease() {
      this.drag = false;
   }

   private int getHue(float value) {
       float hue = 1.0f - value / 360.0f;
       int color = (int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
       return color;
   }
}
