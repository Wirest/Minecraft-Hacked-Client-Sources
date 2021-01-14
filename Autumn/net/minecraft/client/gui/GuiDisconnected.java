package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen {
   private String reason;
   private IChatComponent message;
   private List multilineMessage;
   private final GuiScreen parentScreen;
   private int field_175353_i;

   public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
      this.parentScreen = screen;
      this.reason = I18n.format(reasonLocalizationKey);
      this.message = chatComp;
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   public void initGui() {
      this.buttonList.clear();
      this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
      int var10001 = this.multilineMessage.size();
      FontRenderer var10002 = this.fontRendererObj;
      this.field_175353_i = var10001 * 9;
      int var10004 = this.width / 2 - 100;
      int var10005 = this.height / 2 + this.field_175353_i / 2;
      FontRenderer var10006 = this.fontRendererObj;
      this.buttonList.add(new GuiButton(0, var10004, var10005 + 9, I18n.format("gui.toMenu")));
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 0) {
         this.mc.displayGuiScreen(this.parentScreen);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      int var10003 = this.width / 2;
      int var10004 = this.height / 2 - this.field_175353_i / 2;
      FontRenderer var10005 = this.fontRendererObj;
      this.drawCenteredString(this.fontRendererObj, this.reason, var10003, var10004 - 9 * 2, 11184810);
      int i = this.height / 2 - this.field_175353_i / 2;
      if (this.multilineMessage != null) {
         for(Iterator var5 = this.multilineMessage.iterator(); var5.hasNext(); i += 9) {
            String s = (String)var5.next();
            this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
            FontRenderer var10001 = this.fontRendererObj;
         }
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
