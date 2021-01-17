package me.slowly.client.ui.scriptmenu.elements;

import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.ui.scriptmenu.system.Script;
import me.slowly.client.ui.scriptmenu.system.object.ScriptMod;
import me.slowly.client.ui.scriptmenu.system.object.ScriptValue;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class UIElementTreeViewScript {
   private int x;
   private int y;
   private int width;
   private int height;
   private int TAB_HEIGHT = 11;
   private Script script;
   private final ResourceLocation ARROW = new ResourceLocation("slowly/icon/arrow_down.png");
   private MouseInputHandler handler = new MouseInputHandler(0);
   private MouseInputHandler handlerRight = new MouseInputHandler(1);
   private ScriptMod hoverMod;
   private int scrollY = 1;
   private int allHeight = 1;
   private int yMouse;
   private boolean dragScrollBar;
   private float scroll;

   public UIElementTreeViewScript(Script script) {
      this.script = script;
   }

   public void draw(float xPosition, float yPosition, float screenWidth, float screenHeight, int mouseX, int mouseY) {
      if (this.script != null) {
         this.hoverMod = null;
         this.x = (int)xPosition;
         this.y = (int)yPosition;
         this.width = (int)screenWidth;
         this.height = (int)screenHeight;
         int i = 0;
         float yAxis = (float)(this.y + 1);
         UnicodeFontRenderer boldFont = Client.getInstance().getFontManager().consolasbold14;
         UnicodeFontRenderer font = Client.getInstance().getFontManager().consolas14;
         float scrollBarSize = (float)this.height * ((float)this.height / (float)this.allHeight);
         float scroll = (float)(this.allHeight - this.y - this.height) * ((float)this.scrollY / ((float)this.height - scrollBarSize));
         this.scroll = scroll;
         GL11.glPushMatrix();
         GL11.glEnable(3089);
         RenderUtil.doGlScissor(this.x, (int)yAxis, this.width, this.height);
         GL11.glTranslated(0.0D, (double)(-scroll), 0.0D);

         ScriptMod scm;
         Iterator var14;
         for(var14 = this.script.getPropertyList().keySet().iterator(); var14.hasNext(); ++i) {
            scm = (ScriptMod)var14.next();
            GL11.glPushMatrix();
            int imgSize = 4;
            int imgX = this.x + 2;
            float xMid = (float)imgX + (float)imgSize / 2.0F;
            float yMid = yAxis + (float)this.TAB_HEIGHT / 2.0F;
            GL11.glTranslatef(xMid, yMid, 0.0F);
            GL11.glRotatef(scm.isGuiOpen() ? -45.0F : -90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-xMid, -yMid, 0.0F);
            RenderUtil.drawImage(this.ARROW, imgX, (int)(yAxis + (float)(this.TAB_HEIGHT - imgSize) / 2.0F), imgSize, imgSize);
            GL11.glPopMatrix();
            boldFont.drawString(scm.getMod().getName(), (float)(this.x + 10), yAxis + (float)(this.TAB_HEIGHT - boldFont.FONT_HEIGHT) / 2.0F, -2894893);
            boolean hover = mouseY > this.y && mouseY <= this.y + this.height && mouseX >= this.x && mouseX <= this.x + this.width / 2 && (float)mouseY + scroll > yAxis && (float)mouseY + scroll <= yAxis + (float)this.TAB_HEIGHT;
            boolean enableAll = false;
            if (hover && this.handlerRight.canExcecute()) {
               scm.setEditable(!scm.isEditable());
               enableAll = scm.isEditable();
            }

            if (!scm.isEditable()) {
               Gui.drawRect((float)this.x, yAxis, (float)(this.x + this.width - 6), yAxis + (float)this.TAB_HEIGHT, ClientUtil.reAlpha(1, 0.5F));
            }

            if (hover) {
               this.hoverMod = scm;
            }

            int next = this.getCurrentOpen();
            Iterator var23 = ((ArrayList)this.script.getPropertyList().get(scm)).iterator();

            while(var23.hasNext()) {
               ScriptValue scv = (ScriptValue)var23.next();
               if (enableAll) {
                  scv.setEditable(true);
               }

               if (scm.isGuiOpen()) {
                  yAxis += (float)this.TAB_HEIGHT + 1.0F;
                  boolean hoverValue = false;
                  if (!scv.getValue().isValueBoolean && !scv.getValue().isValueMode) {
                     hoverValue = next != -1 ? scv.getNumeric().ELEMENT_ID == next : mouseX >= this.x && mouseX <= this.x + this.width && (float)mouseY + scroll > yAxis && (float)mouseY + scroll <= yAxis + (float)this.TAB_HEIGHT;
                  } else {
                     hoverValue = next != -1 ? scv.getComboList().ELEMENT_ID == next : mouseX >= this.x && mouseX <= this.x + this.width && (float)mouseY + scroll > yAxis && (float)mouseY + scroll <= yAxis + (float)this.TAB_HEIGHT + 1.0F;
                  }

                  if (hoverValue) {
                     if (this.handlerRight.canExcecute()) {
                        scv.setEditable(!scv.isEditable());
                     }

                     Gui.drawRect((float)(this.x + 10), yAxis, (float)(this.x + this.width - 82), yAxis + (float)this.TAB_HEIGHT, ClientUtil.reAlpha(-1, 0.25F));
                  }

                  font.drawString(scv.getName(), (float)(this.x + 15), yAxis + (float)(this.TAB_HEIGHT - boldFont.FONT_HEIGHT) / 2.0F, -2894893);
                  if (!scv.getValue().isValueBoolean && !scv.getValue().isValueMode) {
                     if (scv.getValue().isValueDouble) {
                        if (scv.getNumeric().hasChanged()) {
                           scv.setEditable(true);
                        }

                        scv.getNumeric().draw((float)(this.x + this.width - 81), (float)((int)yAxis), mouseX, (int)((float)mouseY + scroll));
                     }
                  } else {
                     if (scv.getComboList().hasChanged()) {
                        scv.setEditable(true);
                     }

                     scv.getComboList().draw((float)(this.x + this.width - 81), yAxis, mouseX, (int)((float)mouseY + scroll));
                  }

                  if (!scm.isEditable()) {
                     scv.setEditable(false);
                  }

                  if (!scv.isEditable()) {
                     Gui.drawRect((float)this.x, yAxis - 1.0F, (float)(this.x + this.width - 6), yAxis + (float)this.TAB_HEIGHT, ClientUtil.reAlpha(1, 0.5F));
                  }
               }
            }

            yAxis += (float)this.TAB_HEIGHT;
            if (scm.isGuiOpen()) {
               Gui.drawRect(xPosition, yAxis - 0.5F, (float)(this.x + this.width - 6), yAxis, ClientUtil.reAlpha(FlatColors.BLUE.c, 0.5F));
            }
         }

         yAxis += (float)(this.TAB_HEIGHT + 1);
         GL11.glDisable(3089);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         var14 = this.script.getPropertyList().keySet().iterator();

         label196:
         while(true) {
            do {
               if (!var14.hasNext()) {
                  GL11.glPopMatrix();
                  Gui.drawRect(this.x + this.width - 6, this.y + 1, this.x + this.width, this.y + this.height + 1, ClientUtil.reAlpha(Colors.WHITE.c, 0.1F));
                  this.allHeight = (int)yAxis;
                  boolean hoverScrollbar = mouseX >= this.x + this.width - 6 && mouseX <= this.x + this.width && mouseY >= this.y + this.scrollY && (float)mouseY <= (float)(this.y + this.scrollY) + scrollBarSize;
                  Gui.drawRect((float)(this.x + this.width - 6), (float)(this.y + this.scrollY), (float)(this.x + this.width - 1), (float)(this.y + this.scrollY) + scrollBarSize, ClientUtil.reAlpha(Colors.WHITE.c, hoverScrollbar ? 0.35F : 0.25F));
                  if (this.handler.canExcecute()) {
                     if (this.hoverMod != null) {
                        this.hoverMod.setGuiOpen(!this.hoverMod.isGuiOpen());
                     }

                     if (hoverScrollbar) {
                        this.dragScrollBar = true;
                        this.yMouse = mouseY - (this.y + this.scrollY);
                     }
                  }

                  if (!Mouse.isButtonDown(0)) {
                     this.dragScrollBar = false;
                  }

                  if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height) {
                     this.scrollY = (int)((float)this.scrollY - (float)Mouse.getDWheel() / 11.0F);
                  }

                  if (this.dragScrollBar) {
                     this.scrollY = mouseY - this.y - this.yMouse;
                  }

                  if (this.scrollY < 0) {
                     this.scrollY = 0;
                  }

                  if ((float)this.scrollY > (float)this.height - scrollBarSize) {
                     this.scrollY = (int)((float)this.height - scrollBarSize);
                  }

                  return;
               }

               scm = (ScriptMod)var14.next();
            } while(!scm.isGuiOpen());

            Iterator var27 = ((ArrayList)this.script.getPropertyList().get(scm)).iterator();

            while(true) {
               ScriptValue scv;
               do {
                  if (!var27.hasNext()) {
                     continue label196;
                  }

                  scv = (ScriptValue)var27.next();
               } while(!scv.getValue().isValueBoolean && !scv.getValue().isValueMode);

               scv.getComboList().drawbox(mouseX, mouseY, (int)(-scroll));
            }
         }
      }
   }

   private int getCurrentOpen() {
      if (this.script == null) {
         return -1;
      } else {
         Iterator var2 = this.script.getPropertyList().keySet().iterator();

         label36:
         while(true) {
            ScriptMod scm;
            do {
               if (!var2.hasNext()) {
                  return -1;
               }

               scm = (ScriptMod)var2.next();
            } while(!scm.isGuiOpen());

            Iterator var4 = ((ArrayList)this.script.getPropertyList().get(scm)).iterator();

            ScriptValue scv;
            do {
               do {
                  if (!var4.hasNext()) {
                     continue label36;
                  }

                  scv = (ScriptValue)var4.next();
               } while(!scv.getValue().isValueBoolean && !scv.getValue().isValueMode);
            } while(!scv.getComboList().open);

            return scv.getComboList().ELEMENT_ID;
         }
      }
   }

   public void mouseClicked(int mouseX, int mouseY) {
      boolean hover = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
      if (this.script != null) {
         int next = this.getCurrentOpen();
         Iterator var6 = this.script.getPropertyList().keySet().iterator();

         label56:
         while(true) {
            ScriptMod scm;
            do {
               if (!var6.hasNext()) {
                  return;
               }

               scm = (ScriptMod)var6.next();
            } while(!scm.isGuiOpen());

            Iterator var8 = ((ArrayList)this.script.getPropertyList().get(scm)).iterator();

            while(true) {
               ScriptValue scv;
               do {
                  do {
                     do {
                        if (!var8.hasNext()) {
                           continue label56;
                        }

                        scv = (ScriptValue)var8.next();
                     } while(!scv.getValue().isValueBoolean && !scv.getValue().isValueMode);
                  } while(next != -1 && next != scv.getComboList().ELEMENT_ID);
               } while(!hover && scv.getComboList().hoveringOption == -1);

               scv.getComboList().mouseClick(mouseX, (int)((float)mouseY - this.scroll));
            }
         }
      }
   }

   public void setScript(Script script) {
      this.script = script;
   }
}
