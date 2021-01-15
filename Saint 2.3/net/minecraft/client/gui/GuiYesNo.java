package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.I18n;

public class GuiYesNo extends GuiScreen {
   protected GuiYesNoCallback parentScreen;
   protected String messageLine1;
   private String messageLine2;
   private final List field_175298_s = Lists.newArrayList();
   protected String confirmButtonText;
   protected String cancelButtonText;
   protected int parentButtonClickedId;
   private int ticksUntilEnable;
   private static final String __OBFID = "CL_00000684";

   public GuiYesNo(GuiYesNoCallback p_i1082_1_, String p_i1082_2_, String p_i1082_3_, int p_i1082_4_) {
      this.parentScreen = p_i1082_1_;
      this.messageLine1 = p_i1082_2_;
      this.messageLine2 = p_i1082_3_;
      this.parentButtonClickedId = p_i1082_4_;
      this.confirmButtonText = I18n.format("gui.yes");
      this.cancelButtonText = I18n.format("gui.no");
   }

   public GuiYesNo(GuiYesNoCallback p_i1083_1_, String p_i1083_2_, String p_i1083_3_, String p_i1083_4_, String p_i1083_5_, int p_i1083_6_) {
      this.parentScreen = p_i1083_1_;
      this.messageLine1 = p_i1083_2_;
      this.messageLine2 = p_i1083_3_;
      this.confirmButtonText = p_i1083_4_;
      this.cancelButtonText = p_i1083_5_;
      this.parentButtonClickedId = p_i1083_6_;
   }

   public void initGui() {
      this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
      this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
      this.field_175298_s.clear();
      this.field_175298_s.addAll(this.fontRendererObj.listFormattedStringToWidth(this.messageLine2, this.width - 50));
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      this.parentScreen.confirmClicked(button.id == 0, this.parentButtonClickedId);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.messageLine1, this.width / 2, 70, 16777215);
      int var4 = 90;

      for(Iterator var5 = this.field_175298_s.iterator(); var5.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT) {
         String var6 = (String)var5.next();
         this.drawCenteredString(this.fontRendererObj, var6, this.width / 2, var4, 16777215);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void setButtonDelay(int p_146350_1_) {
      this.ticksUntilEnable = p_146350_1_;

      GuiButton var3;
      for(Iterator var2 = this.buttonList.iterator(); var2.hasNext(); var3.enabled = false) {
         var3 = (GuiButton)var2.next();
      }

   }

   public void updateScreen() {
      super.updateScreen();
      GuiButton var2;
      if (--this.ticksUntilEnable == 0) {
         for(Iterator var1 = this.buttonList.iterator(); var1.hasNext(); var2.enabled = true) {
            var2 = (GuiButton)var1.next();
         }
      }

   }
}
