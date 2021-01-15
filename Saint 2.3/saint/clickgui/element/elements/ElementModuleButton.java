package saint.clickgui.element.elements;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import saint.clickgui.element.Element;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class ElementModuleButton extends Element {
   private Module module;
   private ModManager.Category category;
   private boolean values;
   private float stringWidth;
   private float stringHeight;
   private final TimeHelper time = new TimeHelper();

   public ElementModuleButton(Module module, ModManager.Category category) {
      this.setModule(module);
      this.setCategory(category);
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
         this.getModule().toggle();
         Minecraft.getMinecraft().thePlayer.playSound("random.click", 1.0F, 0.75F);
      }

      if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
         this.values = !this.values;
         Minecraft.getMinecraft().thePlayer.playSound("random.click", 1.0F, 0.25F);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float button) {
      float width = RenderHelper.getNahrFont().getStringWidth(this.getText());
      RenderHelper.drawBorderedRect((float)this.getX(), (float)(this.getY() + 4), (float)(this.getX() + this.getWidth()), (float)(this.getY() + 17), 1.0F, -16777216, this.getEnabled() ? -2142711348 : -2145049307);
      RenderHelper.getNahrFont().drawString(this.getText(), (float)this.getX() - (width - 100.0F) / 2.0F, (float)(this.getY() + 2), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      if (this.values) {
         int yPos = this.getY() + 2;
         int count = 0;
         Iterator var8 = Value.list.iterator();

         while(var8.hasNext()) {
            Value value = (Value)var8.next();
            if (value != null && value.getValueName().startsWith(this.module.getName().toLowerCase() + "_") && value.isValueBoolean) {
               String text = value.getValueName().substring(this.module.getName().length() + 1).substring(0, 1).toUpperCase() + value.getValueName().substring(this.module.getName().length() + 1).substring(1).toLowerCase();
               float stringWidth = RenderHelper.getNahrFont().getStringWidth(text);
               if (this.stringWidth < stringWidth) {
                  this.stringWidth = stringWidth;
               }

               if (this.stringHeight < (float)yPos) {
                  this.stringHeight = (float)yPos;
               }

               RenderHelper.drawRect((float)(this.getX() + this.width + 5), (float)(yPos + 2), (float)(this.getX() + this.width + 9) + this.stringWidth + 2.0F, (float)(yPos + 14), (Boolean)value.getValueState() ? -2142711348 : Integer.MIN_VALUE);
               if (mouseX >= this.getX() + this.width + 6 && (float)mouseX <= (float)(this.getX() + this.width + 8) + this.stringWidth + 2.0F && mouseY >= yPos + 2 && mouseY <= yPos + 14) {
                  RenderHelper.drawRect((float)(this.getX() + this.width + 5), (float)(yPos + 2), (float)(this.getX() + this.width + 9) + this.stringWidth + 2.0F, (float)(yPos + 14), -2130706433);
                  if (Mouse.isButtonDown(0) && this.time.hasReached(500L)) {
                     value.setValueState(!(Boolean)value.getValueState());
                     Minecraft.getMinecraft().thePlayer.playSound("random.click", 1.0F, 0.75F);
                     this.time.reset();
                  }
               }

               RenderHelper.getNahrFont().drawString(text, (float)(this.getX() + this.width + 8), (float)yPos, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
               yPos += 12;
               ++count;
            }
         }

         if (this.stringWidth > 0.0F && this.stringHeight > 0.0F) {
            RenderHelper.drawBorderedRect((float)(this.getX() + this.width + 5), (float)(this.getY() + 4), (float)(this.getX() + this.width) + this.stringWidth + 11.0F, (float)(this.getY() + 4 + count * 12), 1.0F, -587202560, 0);
         }
      }

      if (this.isHovering(mouseX, mouseY)) {
         RenderHelper.drawRect((float)this.getX(), (float)(this.getY() + 4), (float)(this.getX() + this.getWidth()), (float)(this.getY() + 17), 1627389951);
      }

   }

   public int getHeight() {
      return 16;
   }

   public Module getModule() {
      return this.module;
   }

   public String getText() {
      return this.getModule().getName();
   }

   public ModManager.Category getCategory() {
      return this.category;
   }

   public boolean getEnabled() {
      return this.getModule().isEnabled();
   }

   public boolean isHovering(int mouseX, int mouseY) {
      return mouseX >= this.getX() && mouseX <= this.getX() + this.width && mouseY >= this.getY() && mouseY <= this.getY() + 16;
   }

   public void setModule(Module module) {
      this.module = module;
   }

   public void setCategory(ModManager.Category category) {
      this.category = category;
   }
}
