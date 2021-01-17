package me.slowly.client.ui.clickgui.newclickgui.uis;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.clickgui.newclickgui.ClickGui;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import org.lwjgl.input.Mouse;

public class ModulePanel {
   private Mod.Category cat;
   private int x;
   private int y;
   private int width;
   private int height;
   private int panelY;
   private boolean valueClicked;
   private boolean valueClickedMode;
   float yAdd;
   public int yAxis;
   private boolean isOpen;
   private float maxY;

   public ModulePanel(Mod.Category cat, int x, int y, int width, int height, int panelY, int yAdd) {
      this.yAdd = (float)(-this.width);
      this.yAxis = 0;
      this.maxY = 0.0F;
      this.cat = cat;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.panelY = panelY;
      this.yAdd = (float)yAdd;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks, float delta) {
      this.yAxis = 0;
      if (this.y + this.yAxis >= this.panelY) {
         this.doDropShadow();
      }

      Iterator var6 = ModManager.getModList().iterator();

      while(true) {
         Mod m;
         do {
            if (!var6.hasNext()) {
               return;
            }

            m = (Mod)var6.next();
         } while(m.getCategory() != this.cat);

         float stringWidth = (float)Client.getInstance().getFontManager().Bebas25.getStringWidth(m.getName());
         float stringHeight = (float)Client.getInstance().getFontManager().Bebas25.getStringHeight(m.getName());
         if (this.y + this.yAxis >= this.panelY) {
            Gui.drawRect(this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height, m.isEnabled() ? -16165503 : -15785172);
            int xOld = this.x;
            int yOld = this.y;
            int yAxisOld = this.yAxis;
            boolean hover = false;
            if (this.isOpen) {
               if (this.isHovering(mouseX, mouseY, this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height)) {
                  ClickGui.currentMod = m;
                  hover = true;
               }

               if (ClickGui.currentMod == m && this.yAdd == 0.0F) {
                  Gui.drawRect(this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height, -13877680);
               }
            }

            this.isOpen = this.yAdd == 0.0F;
            this.drawEffectToButton(mouseX, mouseY, this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height, hover, m, delta);
            float halfWidth = (float)this.width / 2.0F;
            float halfHeight = (float)this.height / 2.0F;
            Client.getInstance().getFontManager().Bebas25.drawStringWithShadow(m.getName(), (float)this.x + halfWidth - stringWidth / 2.0F, (float)this.y + halfHeight - stringHeight / 2.0F + (float)this.yAxis - 2.0F, -1);
            Client.getInstance().getFontManager().Bebas25.drawString(m.hasValues() ? (m.openValues ? "-" : "+") : "", (float)(this.x + this.width - 8), (float)this.y + halfHeight - stringHeight / 2.0F + (float)this.yAxis - 2.0F, -1);
            if (m.openValues) {
               Iterator var16 = Value.list.iterator();

               label162:
               while(true) {
                  Value value;
                  String valueMod;
                  String valueName;
                  do {
                     if (!var16.hasNext()) {
                        Client.getInstance().getFileUtil().saveValues();
                        break label162;
                     }

                     value = (Value)var16.next();
                     valueMod = value.getValueName().split("_")[0];
                     valueName = value.getValueName().split("_")[1];
                  } while(!valueMod.equalsIgnoreCase(m.getName()));

                  this.yAxis += this.height;
                  float valueStringWidth = (float)Client.getInstance().getFontManager().Bebas20.getStringWidth(valueName);
                  float valueStringHeight = (float)Client.getInstance().getFontManager().Bebas20.getStringHeight(valueName);
                  float var10002;
                  if (value.isValueMode) {
                     this.yAxis -= this.height;

                     for(int i = 0; i < value.listModes().size(); ++i) {
                        this.yAxis += this.height;
                        float currentRadius;
                        if (value.getCurrentMode() == i) {
                           currentRadius = value.currentRadius;
                        } else {
                           value.getClass();
                           currentRadius = 4.0F;
                        }

                        Gui.drawRect(this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height, -15785172);
                        int n = this.x + this.width;
                        value.getClass();
                        Gui.drawCircle(n - 4 - 4, this.y + this.yAxis + this.height / 2, (int)currentRadius, new Color(Colors.AQUA.c));
                        if (value.getCurrentMode() == i) {
                           int n2 = this.x + this.width;
                           value.getClass();
                           Gui.drawFilledCircle(n2 - 8, this.y + this.yAxis + this.height / 2, currentRadius, -14057287);
                        }

                        String name = value.getModeAt(i);
                        int stringWidthMode = Client.getInstance().getFontManager().Bebas20.getStringWidth(name);
                        int stringHeightMode = Client.getInstance().getFontManager().Bebas20.getStringHeight(name);
                        var10002 = (float)this.x + (float)this.width / 2.0F - (float)stringWidthMode / 2.0F;
                        Client.getInstance().getFontManager().Bebas20.drawStringWithShadow(name, var10002, (float)(this.y + this.yAxis) + (float)this.height / 2.0F - (float)stringHeightMode / 2.0F - 2.0F, -1);
                        int[] array = new int[4];
                        int n3 = 0;
                        int n4 = this.x + this.width;
                        value.getClass();
                        array[n3] = n4 - 4 - 10;
                        int n5 = 1;
                        int n6 = this.y + this.yAxis + this.height / 2;
                        value.getClass();
                        array[n5] = n6 - 4;
                        int n7 = 2;
                        int n8 = this.x + this.width;
                        value.getClass();
                        array[n7] = n8 + 4 - 6;
                        int n9 = 3;
                        int n10 = this.y + this.yAxis + this.height / 2;
                        value.getClass();
                        array[n9] = n10 + 4;
                        if (Mouse.isButtonDown(0) && !value.disabled) {
                           if (!this.valueClickedMode && this.isHovering(mouseX, mouseY, array[0], array[1], array[2], array[3])) {
                              value.setCurrentMode(i);
                              value.currentRadius = 0.0F;
                              this.valueClickedMode = true;
                           }
                        } else {
                           this.valueClickedMode = false;
                        }

                        if (value.currentRadius < 4.0F) {
                           value.currentRadius += 3.0F * delta;
                        }

                        if (value.currentRadius > 4.0F) {
                           value.currentRadius = 4.0F;
                        }
                     }
                  }

                  if (value.isValueBoolean) {
                     Gui.drawRect(this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height, -15785172);
                     if (((Boolean)value.getValueState()).booleanValue()) {
                        Gui.drawRect(this.x + 1, this.y + this.yAxis, this.x + 2, this.y + this.yAxis + this.height, -14057287);
                     }

                     var10002 = (float)this.x + halfWidth - valueStringWidth / 2.0F;
                     float var10003 = (float)this.y + halfHeight - valueStringHeight / 2.0F;
                     Client.getInstance().getFontManager().Bebas20.drawStringWithShadow(valueName, (float)this.x + halfWidth - valueStringWidth / 2.0f, (float)this.y + halfHeight - valueStringHeight / 2.0f + (float)this.yAxis - 2.0f, Boolean.valueOf((Boolean)value.getValueState()) != false ? -14057287 : -1);
                     if (Mouse.isButtonDown(0) && !value.disabled) {
                        if (this.isHovering(mouseX, mouseY, this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height) && !this.valueClicked) {
                           this.valueClicked = true;
                           value.setValueState(!((Boolean)value.getValueState()).booleanValue());
                        }
                     } else {
                        this.valueClicked = false;
                     }

                     if (value.disabled) {
                        Gui.drawRect(this.x + 1, this.y + this.yAxis, this.x + this.width, this.y + this.yAxis + this.height, -15785172);
                     }
                  }

                  if (value.isValueDouble) {
                     double min = ((Double)value.getValueMin()).doubleValue();
                     double max = ((Double)value.getValueMax()).doubleValue();
                     double step = value.getSteps();
                     double valAbs = (double)(mouseX - (this.x + 1));
                     double perc = valAbs / (double)(this.width - 2);
                     perc = Math.min(Math.max(0.0D, perc), 1.0D);
                     double valRel = (max - min) * perc;
                     double valuu = min + valRel;
                     double percSlider = (((Double)value.getValueState()).doubleValue() - min) / (max - min);
                     double val = (double)(this.x + 1) + (double)(this.width - 2) * percSlider;
                     float valueStringWidthDouble = (float)Client.getInstance().getFontManager().Bebas20.getStringWidth(String.valueOf(valueName) + " " + value.getValueState());
                     Gui.drawRect(this.x + 1, this.y + this.yAxis, this.x + this.width - 1, this.y + this.yAxis + this.height, -15785172);
                     GuiIngame var10000 = Minecraft.getMinecraft().ingameGUI;
                     GuiIngame.drawRect((float)(this.x + 1), (float)(this.y + this.yAxis), (float)val, (float)(this.y + this.yAxis + this.height - 1), -14057287);
                     if (Mouse.isButtonDown(0) && !value.disabled && this.isHovering(mouseX, mouseY, this.x, this.y + (int)this.yAdd + this.yAxis, this.x + this.width, this.y + (int)this.yAdd + this.yAxis + this.height)) {
                        value.sliderX = (double)(mouseX - this.x);
                        valuu = (double)Math.round(valuu * (1.0D / step)) / (1.0D / step);
                        value.setValueState(valuu);
                     }

                     Client.getInstance().getFontManager().Bebas20.drawStringWithShadow(String.valueOf(valueName) + " " + value.getValueState(), (float)this.x + halfWidth - valueStringWidthDouble / 2.0F, (float)this.y + halfHeight - valueStringHeight / 2.0F + (float)this.yAxis - 2.0F, -1);
                     if (value.disabled) {
                        Gui.drawRect(this.x + 1, this.y + this.yAxis, this.x + this.width, this.y + this.yAxis + this.height - 1, Colors.BLACK.c);
                     }
                  }
               }
            }

            if (m.openValues) {
               Gui.drawRect(this.x + 1, this.y + this.yAxis + this.height - 1, this.x + this.width - 1, this.y + this.yAxis + this.height, Colors.WHITE.c);
               Gui.drawRect(xOld + 1, yOld + yAxisOld + this.height, xOld + this.width - 1, yOld + yAxisOld + this.height + 1, Colors.WHITE.c);
            }
         }

         this.yAxis += this.height;
         if (this.yAdd == 0.0F) {
            this.maxY = (float)(this.y + this.yAxis);
         } else {
            this.maxY = -1.0F;
         }
      }
   }

   private void doDropShadow() {
   }

   private void drawEffectToButton(int mouseX, int mouseY, int xLeft, int yUp, int xRight, int yDown, boolean hovered, Mod mod, float delta) {
      if (this.isOpen) {
         if (Mouse.isButtonDown(0)) {
            if (!mod.clickedCircle) {
               if (hovered) {
                  mod.circleValue = 0.0F;
                  mod.canSeeCircle = true;
                  mod.circleCoords[0] = mouseX;
                  mod.circleCoords[1] = mouseY;
               }

               mod.clickedCircle = true;
            }
         } else {
            mod.clickedCircle = false;
         }

         if (mod.canSeeCircle) {
            Gui.drawFilledCircle(mod.circleCoords[0], mod.circleCoords[1], mod.circleValue, Integer.MAX_VALUE, xLeft, yUp, xRight, yDown);
         }

         if (mod.circleValue < 88.0F) {
            mod.circleValue += 200.0F * delta;
         } else {
            mod.canSeeCircle = false;
         }
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
   }

   private boolean isHovering(int mouseX, int mouseY, int xLeft, int yUp, int xRight, int yBottom) {
      return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
   }

   public void update(int x, int y, int width, int height, int panelY, float yAdd) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.panelY = panelY;
      this.yAdd = yAdd;
   }
}
