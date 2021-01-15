package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import saint.Saint;
import saint.utilities.RenderHelper;

public class GuiChat extends GuiScreen {
   private static final Logger logger = LogManager.getLogger();
   private String historyBuffer = "";
   private int sentHistoryCursor = -1;
   private boolean playerNamesFound;
   private boolean waitingOnAutocomplete;
   private int autocompleteIndex;
   private List foundPlayerNames = Lists.newArrayList();
   public static GuiTextField inputField;
   private String defaultInputFieldText = "";
   private static final String __OBFID = "CL_00000682";

   public GuiChat() {
   }

   public GuiChat(String p_i1024_1_) {
      this.defaultInputFieldText = p_i1024_1_;
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
      inputField.setMaxStringLength(100);
      inputField.setEnableBackgroundDrawing(false);
      inputField.setFocused(true);
      inputField.setText(this.defaultInputFieldText);
      inputField.setCanLoseFocus(false);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.ingameGUI.getChatGUI().resetScroll();
   }

   public void updateScreen() {
      inputField.updateCursorCounter();
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.waitingOnAutocomplete = false;
      if (keyCode == 15) {
         this.autocompletePlayerNames();
      } else {
         this.playerNamesFound = false;
      }

      if (keyCode == 1) {
         this.mc.displayGuiScreen((GuiScreen)null);
      } else if (keyCode != 28 && keyCode != 156) {
         if (keyCode == 200) {
            this.getSentHistory(-1);
         } else if (keyCode == 208) {
            this.getSentHistory(1);
         } else if (keyCode == 201) {
            this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
         } else if (keyCode == 209) {
            this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
         } else {
            inputField.textboxKeyTyped(typedChar, keyCode);
         }
      } else {
         String var3 = inputField.getText().trim();
         if (var3.length() > 0) {
            this.func_175275_f(var3);
         }

         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      int var1 = Mouse.getEventDWheel();
      if (var1 != 0) {
         if (var1 > 1) {
            var1 = 1;
         }

         if (var1 < -1) {
            var1 = -1;
         }

         if (!isShiftKeyDown()) {
            var1 *= 7;
         }

         this.mc.ingameGUI.getChatGUI().scroll(var1);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.mc.ingameGUI.persistantChatGUI.mouseClicked(mouseX, mouseY, mouseButton);
      if (mouseButton == 0) {
         IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
         if (this.func_175276_a(var4)) {
            return;
         }
      }

      inputField.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void func_175274_a(String p_175274_1_, boolean p_175274_2_) {
      if (p_175274_2_) {
         inputField.setText(p_175274_1_);
      } else {
         inputField.writeText(p_175274_1_);
      }

   }

   public void autocompletePlayerNames() {
      String var3;
      if (this.playerNamesFound) {
         inputField.deleteFromCursor(inputField.func_146197_a(-1, inputField.getCursorPosition(), false) - inputField.getCursorPosition());
         if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
            this.autocompleteIndex = 0;
         }
      } else {
         int var1 = inputField.func_146197_a(-1, inputField.getCursorPosition(), false);
         this.foundPlayerNames.clear();
         this.autocompleteIndex = 0;
         String var2 = inputField.getText().substring(var1).toLowerCase();
         var3 = inputField.getText().substring(0, inputField.getCursorPosition());
         this.sendAutocompleteRequest(var3, var2);
         if (this.foundPlayerNames.isEmpty()) {
            return;
         }

         this.playerNamesFound = true;
         inputField.deleteFromCursor(var1 - inputField.getCursorPosition());
      }

      if (this.foundPlayerNames.size() > 1) {
         StringBuilder var4 = new StringBuilder();

         for(Iterator var5 = this.foundPlayerNames.iterator(); var5.hasNext(); var4.append(var3)) {
            var3 = (String)var5.next();
            if (var4.length() > 0) {
               var4.append(", ");
            }
         }

         this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(var4.toString()), 1);
      }

      inputField.writeText((String)this.foundPlayerNames.get(this.autocompleteIndex++));
   }

   private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_) {
      if (p_146405_1_.length() >= 1) {
         BlockPos var3 = null;
         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            var3 = this.mc.objectMouseOver.func_178782_a();
         }

         this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, var3));
         this.waitingOnAutocomplete = true;
      }

   }

   public void getSentHistory(int p_146402_1_) {
      int var2 = this.sentHistoryCursor + p_146402_1_;
      int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      var2 = MathHelper.clamp_int(var2, 0, var3);
      if (var2 != this.sentHistoryCursor) {
         if (var2 == var3) {
            this.sentHistoryCursor = var3;
            inputField.setText(this.historyBuffer);
         } else {
            if (this.sentHistoryCursor == var3) {
               this.historyBuffer = inputField.getText();
            }

            inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
            this.sentHistoryCursor = var2;
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      if (this.mc.ingameGUI.persistantChatGUI.drag) {
         this.mc.ingameGUI.persistantChatGUI.drag(mouseX, mouseY);
      }

      if (Saint.getModuleManager().getModuleUsingName("saintchat").isEnabled()) {
         RenderHelper.drawBorderedRect(2.0F, (float)(this.height - 14), (float)this.mc.fontRendererObj.getStringWidth(inputField.getText() + 3), (float)(this.height - 2), 2.0F, 1073741824, Integer.MIN_VALUE);
      } else {
         RenderHelper.drawRect(2.0F, (float)(this.height - 14), (float)(this.width - 1), (float)(this.height - 2), Integer.MIN_VALUE);
      }

      inputField.drawTextBox();
      IChatComponent var4 = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
      if (var4 != null && var4.getChatStyle().getChatHoverEvent() != null) {
         this.func_175272_a(var4, mouseX, mouseY);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void onAutocompleteResponse(String[] p_146406_1_) {
      if (this.waitingOnAutocomplete) {
         this.playerNamesFound = false;
         this.foundPlayerNames.clear();
         String[] var2 = p_146406_1_;
         int var3 = p_146406_1_.length;

         String var7;
         for(int var4 = 0; var4 < var3; ++var4) {
            var7 = var2[var4];
            if (var7.length() > 0) {
               this.foundPlayerNames.add(var7);
            }
         }

         String var6 = inputField.getText().substring(inputField.func_146197_a(-1, inputField.getCursorPosition(), false));
         var7 = StringUtils.getCommonPrefix(p_146406_1_);
         if (var7.length() > 0 && !var6.equalsIgnoreCase(var7)) {
            inputField.deleteFromCursor(inputField.func_146197_a(-1, inputField.getCursorPosition(), false) - inputField.getCursorPosition());
            inputField.writeText(var7);
         } else if (this.foundPlayerNames.size() > 0) {
            this.playerNamesFound = true;
            this.autocompletePlayerNames();
         }
      }

   }

   protected void mouseReleased(int mouseX, int mouseY, int partialTicks) {
      this.mc.ingameGUI.persistantChatGUI.mouseReleased(mouseX, mouseY, partialTicks);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
