package me.slowly.client.ui.hudcustomizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import me.slowly.client.Client;
import me.slowly.client.ui.hudcustomizer.options.UICustomColorPicker;
import me.slowly.client.ui.hudcustomizer.options.UICustomDropDown;
import me.slowly.client.ui.hudcustomizer.options.UICustomSlider;
import me.slowly.client.ui.hudcustomizer.options.UICustomToggleButton;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class CustomHUD {
   public int x;
   public int y;
   public int width;
   public int height;
   public int windowWidth;
   public int windowHeight;
   public String owner;
   public ArrayList options;
   private MouseInputHandler mouseHandler;
   private Map sliders = new LinkedHashMap();
   private Map toggleButtons = new LinkedHashMap();
   private Map dropDownButtons = new LinkedHashMap();
   private Map colorPickers = new LinkedHashMap();
   private int currentCustomValue = 0;

   public CustomHUD(String ownerName, MouseInputHandler mouseHandler, ArrayList options) {
      this.owner = ownerName;
      this.mouseHandler = mouseHandler;
      this.options = options;
      if (options != null) {
         this.addValues();
      }

   }

   public void draw(int mouseX, int mouseY) {
      if (this.options != null) {
         float x = 0.0F;
         float buttonWidth = (float)this.windowWidth / (float)this.options.size();
         int buttonHeight = 30;

         for(int i = 0; i < this.options.size(); ++i) {
            CustomHUDOptions option = (CustomHUDOptions)this.options.get(i);
            Gui.drawRect(x, 0.0F, x + buttonWidth, (float)buttonHeight, FlatColors.ORANGE.c);
            if (i == this.currentCustomValue) {
               Gui.drawRect(x, (float)(buttonHeight - 2), x + buttonWidth, (float)buttonHeight, ClientUtil.reAlpha(Colors.BLACK.c, 0.5F));
            }

            UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton15;
            font.drawString(((CustomHUDOptions)this.options.get(i)).name, x + (buttonWidth - (float)font.getStringWidth(((CustomHUDOptions)this.options.get(i)).name)) / 2.0F, (float)((this.height - font.FONT_HEIGHT) / 2), -1);
            boolean hoverButton = (float)mouseX >= x && (float)mouseX < x + buttonWidth && mouseY >= 0 && mouseY <= buttonHeight;
            if (hoverButton && this.mouseHandler.canExcecute()) {
               this.currentCustomValue = i;
            }

            if (i == this.currentCustomValue) {
               GL11.glPushMatrix();
               GL11.glTranslated(0.0D, (double)buttonHeight, 0.0D);
               this.drawValues(option, mouseX, mouseY);
               GL11.glPopMatrix();
            }

            x += buttonWidth;
         }

      }
   }

   private void drawValues(CustomHUDOptions option, int mouseX, int mouseY) {
       int y = 10;
       y += this.drawSliders(y, option, mouseX, mouseY);
       y += this.drawToggleButtons(y, option, mouseX, mouseY);
       y += this.drawColorPickers(y, option, mouseX, mouseY);
   }

   private int drawDropDowns(int y, CustomHUDOptions option, int mouseX, int mouseY) {
       int oldY = y;
       int x = 10;
       boolean addY = false;
       return y - oldY + (addY ? 30 : 0);
   }

   private int drawColorPickers(int y, CustomHUDOptions option, int mouseX, int mouseY) {
      int x = 10;
      boolean addY = false;
      Iterator var9 = this.colorPickers.keySet().iterator();

      CustomValue val;
      CustomValue optionValue;
      Iterator var11;
      while(var9.hasNext()) {
         val = (CustomValue)var9.next();
         var11 = option.values.iterator();

         while(var11.hasNext()) {
            optionValue = (CustomValue)var11.next();
            if (val == optionValue) {
               addY = true;
               ((UICustomColorPicker)this.colorPickers.get(val)).draw(mouseX, mouseY - 30, x, y, ((Double)val.alphaValue.getValueState()).doubleValue());
               x += 155;
            }
         }
      }

      var9 = this.dropDownButtons.keySet().iterator();

      while(var9.hasNext()) {
         val = (CustomValue)var9.next();
         var11 = option.values.iterator();

         while(var11.hasNext()) {
            optionValue = (CustomValue)var11.next();
            if (val == optionValue) {
               addY = true;
               ((UICustomDropDown)this.dropDownButtons.get(val)).draw(mouseX, mouseY - 30, x, y);
               x += 110;
            }
         }
      }

      return y - y + (addY ? 100 : 0);
   }

   private int drawToggleButtons(int y, CustomHUDOptions option, int mouseX, int mouseY) {
      int x = 10;
      boolean addY = false;
      Iterator var9 = this.toggleButtons.keySet().iterator();

      while(var9.hasNext()) {
         CustomValue val = (CustomValue)var9.next();
         Iterator var11 = option.values.iterator();

         while(var11.hasNext()) {
            CustomValue optionValue = (CustomValue)var11.next();
            if (val == optionValue) {
               addY = true;
               ((UICustomToggleButton)this.toggleButtons.get(val)).draw(mouseX, mouseY - 30, x, y, 65, 15);
               x += 110;
            }
         }
      }

      return y - y + (addY ? 30 : 0);
   }

   private int drawSliders(int y, CustomHUDOptions option, int mouseX, int mouseY) {
      int oldY = y;
      int x = 10;
      boolean addY = false;
      Iterator var9 = this.sliders.keySet().iterator();

      while(var9.hasNext()) {
         CustomValue val = (CustomValue)var9.next();
         Iterator var11 = option.values.iterator();

         while(var11.hasNext()) {
            CustomValue optionValue = (CustomValue)var11.next();
            if (val == optionValue) {
               addY = true;
               double newVal = ((UICustomSlider)this.sliders.get(val)).draw(val.prefix, (float)((Double)val.getValueState()).doubleValue(), mouseX, mouseY, x, oldY);
               val.setValueState(newVal);
               x += ((UICustomSlider)this.sliders.get(val)).width + 10;
            }
         }
      }

      return y - oldY + (addY ? 30 : 0);
   }

   private void addValues() {
      Iterator var2 = this.options.iterator();

      while(true) {
         CustomHUDOptions hudOption;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               hudOption = (CustomHUDOptions)var2.next();
            } while(hudOption == null);
         } while(hudOption.values == null);

         Iterator var4 = hudOption.values.iterator();

         while(var4.hasNext()) {
            CustomValue val = (CustomValue)var4.next();
            if (val.isValueDouble) {
               this.sliders.put(val, new UICustomSlider(val.getValueName(), ((Double)val.getValueMin()).doubleValue(), ((Double)val.getValueMax()).doubleValue(), val.getSteps()));
            }

            if (val.isValueBoolean) {
               this.toggleButtons.put(val, new UICustomToggleButton(val, this.mouseHandler));
            }

            if (val.isValueColor) {
               this.colorPickers.put(val, new UICustomColorPicker(val));
            }

            if (val.isValueMode) {
               this.dropDownButtons.put(val, new UICustomDropDown(val));
            }
         }
      }
   }

   public void mouseClick(int mouseX, int mouseY) {
      Iterator var4 = this.sliders.keySet().iterator();

      CustomValue val;
      CustomValue optionValue;
      Iterator var6;
      while(var4.hasNext()) {
         val = (CustomValue)var4.next();
         var6 = ((CustomHUDOptions)this.options.get(this.currentCustomValue)).values.iterator();

         while(var6.hasNext()) {
            optionValue = (CustomValue)var6.next();
            if (val == optionValue) {
               ((UICustomSlider)this.sliders.get(val)).mouseClick(mouseX, mouseY - 30);
            }
         }
      }

      var4 = this.colorPickers.keySet().iterator();

      while(var4.hasNext()) {
         val = (CustomValue)var4.next();
         var6 = ((CustomHUDOptions)this.options.get(this.currentCustomValue)).values.iterator();

         while(var6.hasNext()) {
            optionValue = (CustomValue)var6.next();
            if (val == optionValue) {
               ((UICustomColorPicker)this.colorPickers.get(val)).mouseClick(mouseX, mouseY - 30);
            }
         }
      }

      var4 = this.dropDownButtons.keySet().iterator();

      while(var4.hasNext()) {
         val = (CustomValue)var4.next();
         var6 = ((CustomHUDOptions)this.options.get(this.currentCustomValue)).values.iterator();

         while(var6.hasNext()) {
            optionValue = (CustomValue)var6.next();
            if (val == optionValue) {
               ((UICustomDropDown)this.dropDownButtons.get(val)).mouseClick(mouseX, mouseY - 30);
            }
         }
      }

   }

   public void mouseRelease() {
      Iterator var2 = this.sliders.keySet().iterator();

      CustomValue val;
      while(var2.hasNext()) {
         val = (CustomValue)var2.next();
         ((UICustomSlider)this.sliders.get(val)).mouseRelease();
      }

      var2 = this.colorPickers.keySet().iterator();

      while(var2.hasNext()) {
         val = (CustomValue)var2.next();
         ((UICustomColorPicker)this.colorPickers.get(val)).mouseRelease();
      }

   }
}
