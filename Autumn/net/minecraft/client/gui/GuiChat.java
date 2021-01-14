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

public class GuiChat extends GuiScreen {
   private static final Logger logger = LogManager.getLogger();
   private String historyBuffer = "";
   private int sentHistoryCursor = -1;
   private boolean playerNamesFound;
   private boolean waitingOnAutocomplete;
   private int autocompleteIndex;
   private List foundPlayerNames = Lists.newArrayList();
   protected GuiTextField inputField;
   private String defaultInputFieldText = "";

   public GuiChat() {
   }

   public GuiChat(String defaultText) {
      this.defaultInputFieldText = defaultText;
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
      this.inputField.setMaxStringLength(100);
      this.inputField.setEnableBackgroundDrawing(false);
      this.inputField.setFocused(true);
      this.inputField.setText(this.defaultInputFieldText);
      this.inputField.setCanLoseFocus(false);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.ingameGUI.getChatGUI().resetScroll();
   }

   public void updateScreen() {
      this.inputField.updateCursorCounter();
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
            this.inputField.textboxKeyTyped(typedChar, keyCode);
         }
      } else {
         String s = this.inputField.getText().trim();
         if (s.length() > 0) {
            this.sendChatMessage(s);
         }

         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      int i = Mouse.getEventDWheel();
      if (i != 0) {
         if (i > 1) {
            i = 1;
         }

         if (i < -1) {
            i = -1;
         }

         if (!isShiftKeyDown()) {
            i *= 7;
         }

         this.mc.ingameGUI.getChatGUI().scroll(i);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (mouseButton == 0) {
         IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
         if (this.handleComponentClick(ichatcomponent)) {
            return;
         }
      }

      this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void setText(String newChatText, boolean shouldOverwrite) {
      if (shouldOverwrite) {
         this.inputField.setText(newChatText);
      } else {
         this.inputField.writeText(newChatText);
      }

   }

   public void autocompletePlayerNames() {
      String s2;
      if (this.playerNamesFound) {
         this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
         if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
            this.autocompleteIndex = 0;
         }
      } else {
         int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
         this.foundPlayerNames.clear();
         this.autocompleteIndex = 0;
         String s = this.inputField.getText().substring(i).toLowerCase();
         s2 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
         this.sendAutocompleteRequest(s2, s);
         if (this.foundPlayerNames.isEmpty()) {
            return;
         }

         this.playerNamesFound = true;
         this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
      }

      if (this.foundPlayerNames.size() > 1) {
         StringBuilder stringbuilder = new StringBuilder();

         for(Iterator var5 = this.foundPlayerNames.iterator(); var5.hasNext(); stringbuilder.append(s2)) {
            s2 = (String)var5.next();
            if (stringbuilder.length() > 0) {
               stringbuilder.append(", ");
            }
         }

         this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
      }

      this.inputField.writeText((String)this.foundPlayerNames.get(this.autocompleteIndex++));
   }

   private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_) {
      if (p_146405_1_.length() >= 1) {
         BlockPos blockpos = null;
         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            blockpos = this.mc.objectMouseOver.getBlockPos();
         }

         this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, blockpos));
         this.waitingOnAutocomplete = true;
      }

   }

   public void getSentHistory(int msgPos) {
      int i = this.sentHistoryCursor + msgPos;
      int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
      i = MathHelper.clamp_int(i, 0, j);
      if (i != this.sentHistoryCursor) {
         if (i == j) {
            this.sentHistoryCursor = j;
            this.inputField.setText(this.historyBuffer);
         } else {
            if (this.sentHistoryCursor == j) {
               this.historyBuffer = this.inputField.getText();
            }

            this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
            this.sentHistoryCursor = i;
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      drawRect(2.0D, (double)(this.height - 14), (double)(this.width - 2), (double)(this.height - 2), Integer.MIN_VALUE);
      this.inputField.drawTextBox();
      IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
      if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
         this.handleComponentHover(ichatcomponent, mouseX, mouseY);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void onAutocompleteResponse(String[] p_146406_1_) {
      if (this.waitingOnAutocomplete) {
         this.playerNamesFound = false;
         this.foundPlayerNames.clear();
         String[] var2 = p_146406_1_;
         int var3 = p_146406_1_.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            if (s.length() > 0) {
               this.foundPlayerNames.add(s);
            }
         }

         String s1 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
         String s2 = StringUtils.getCommonPrefix(p_146406_1_);
         if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
            this.inputField.writeText(s2);
         } else if (this.foundPlayerNames.size() > 0) {
            this.playerNamesFound = true;
            this.autocompletePlayerNames();
         }
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
