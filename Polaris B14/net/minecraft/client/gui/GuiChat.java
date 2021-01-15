/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiChat extends GuiScreen
/*     */ {
/*  20 */   private static final Logger logger = ;
/*  21 */   private String historyBuffer = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  27 */   private int sentHistoryCursor = -1;
/*     */   private boolean playerNamesFound;
/*     */   private boolean waitingOnAutocomplete;
/*     */   private int autocompleteIndex;
/*  31 */   private List<String> foundPlayerNames = com.google.common.collect.Lists.newArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   protected GuiTextField inputField;
/*     */   
/*     */ 
/*     */ 
/*  39 */   private String defaultInputFieldText = "";
/*     */   
/*     */ 
/*     */   public GuiChat() {}
/*     */   
/*     */ 
/*     */   public GuiChat(String defaultText)
/*     */   {
/*  47 */     this.defaultInputFieldText = defaultText;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  56 */     Keyboard.enableRepeatEvents(true);
/*  57 */     this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
/*  58 */     this.inputField = new GuiTextField(0, this.fontRendererObj, 4, height - 12, width - 4, 12);
/*  59 */     this.inputField.setMaxStringLength(100);
/*  60 */     this.inputField.setEnableBackgroundDrawing(false);
/*  61 */     this.inputField.setFocused(true);
/*  62 */     this.inputField.setText(this.defaultInputFieldText);
/*  63 */     this.inputField.setCanLoseFocus(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onGuiClosed()
/*     */   {
/*  71 */     Keyboard.enableRepeatEvents(false);
/*  72 */     this.mc.ingameGUI.getChatGUI().resetScroll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/*  80 */     this.inputField.updateCursorCounter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/*  89 */     this.waitingOnAutocomplete = false;
/*     */     
/*  91 */     if (keyCode == 15)
/*     */     {
/*  93 */       autocompletePlayerNames();
/*     */     }
/*     */     else
/*     */     {
/*  97 */       this.playerNamesFound = false;
/*     */     }
/*     */     
/* 100 */     if (keyCode == 1)
/*     */     {
/* 102 */       this.mc.displayGuiScreen(null);
/*     */     }
/* 104 */     else if ((keyCode != 28) && (keyCode != 156))
/*     */     {
/* 106 */       if (keyCode == 200)
/*     */       {
/* 108 */         getSentHistory(-1);
/*     */       }
/* 110 */       else if (keyCode == 208)
/*     */       {
/* 112 */         getSentHistory(1);
/*     */       }
/* 114 */       else if (keyCode == 201)
/*     */       {
/* 116 */         this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
/*     */       }
/* 118 */       else if (keyCode == 209)
/*     */       {
/* 120 */         this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
/*     */       }
/*     */       else
/*     */       {
/* 124 */         this.inputField.textboxKeyTyped(typedChar, keyCode);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 129 */       String s = this.inputField.getText().trim();
/*     */       
/* 131 */       if (s.length() > 0)
/*     */       {
/* 133 */         sendChatMessage(s);
/*     */       }
/*     */       
/* 136 */       this.mc.displayGuiScreen(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleMouseInput()
/*     */     throws IOException
/*     */   {
/* 145 */     super.handleMouseInput();
/* 146 */     int i = Mouse.getEventDWheel();
/*     */     
/* 148 */     if (i != 0)
/*     */     {
/* 150 */       if (i > 1)
/*     */       {
/* 152 */         i = 1;
/*     */       }
/*     */       
/* 155 */       if (i < -1)
/*     */       {
/* 157 */         i = -1;
/*     */       }
/*     */       
/* 160 */       if (!isShiftKeyDown())
/*     */       {
/* 162 */         i *= 7;
/*     */       }
/*     */       
/* 165 */       this.mc.ingameGUI.getChatGUI().scroll(i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/* 174 */     if (mouseButton == 0)
/*     */     {
/* 176 */       IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
/*     */       
/* 178 */       if (handleComponentClick(ichatcomponent))
/*     */       {
/* 180 */         return;
/*     */       }
/*     */     }
/*     */     
/* 184 */     this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
/* 185 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setText(String newChatText, boolean shouldOverwrite)
/*     */   {
/* 193 */     if (shouldOverwrite)
/*     */     {
/* 195 */       this.inputField.setText(newChatText);
/*     */     }
/*     */     else
/*     */     {
/* 199 */       this.inputField.writeText(newChatText);
/*     */     }
/*     */   }
/*     */   
/*     */   public void autocompletePlayerNames() {
/*     */     String s1;
/* 205 */     if (this.playerNamesFound)
/*     */     {
/* 207 */       this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
/*     */       
/* 209 */       if (this.autocompleteIndex >= this.foundPlayerNames.size())
/*     */       {
/* 211 */         this.autocompleteIndex = 0;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 216 */       int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
/* 217 */       this.foundPlayerNames.clear();
/* 218 */       this.autocompleteIndex = 0;
/* 219 */       String s = this.inputField.getText().substring(i).toLowerCase();
/* 220 */       s1 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
/* 221 */       sendAutocompleteRequest(s1, s);
/*     */       
/* 223 */       if (this.foundPlayerNames.isEmpty())
/*     */       {
/* 225 */         return;
/*     */       }
/*     */       
/* 228 */       this.playerNamesFound = true;
/* 229 */       this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
/*     */     }
/*     */     
/* 232 */     if (this.foundPlayerNames.size() > 1)
/*     */     {
/* 234 */       StringBuilder stringbuilder = new StringBuilder();
/*     */       
/* 236 */       for (String s2 : this.foundPlayerNames)
/*     */       {
/* 238 */         if (stringbuilder.length() > 0)
/*     */         {
/* 240 */           stringbuilder.append(", ");
/*     */         }
/*     */         
/* 243 */         stringbuilder.append(s2);
/*     */       }
/*     */       
/* 246 */       this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
/*     */     }
/*     */     
/* 249 */     this.inputField.writeText((String)this.foundPlayerNames.get(this.autocompleteIndex++));
/*     */   }
/*     */   
/*     */   private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_)
/*     */   {
/* 254 */     if (p_146405_1_.length() >= 1)
/*     */     {
/* 256 */       net.minecraft.util.BlockPos blockpos = null;
/*     */       
/* 258 */       if ((this.mc.objectMouseOver != null) && (this.mc.objectMouseOver.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK))
/*     */       {
/* 260 */         blockpos = this.mc.objectMouseOver.getBlockPos();
/*     */       }
/*     */       
/* 263 */       this.mc.thePlayer.sendQueue.addToSendQueue(new net.minecraft.network.play.client.C14PacketTabComplete(p_146405_1_, blockpos));
/* 264 */       this.waitingOnAutocomplete = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSentHistory(int msgPos)
/*     */   {
/* 274 */     int i = this.sentHistoryCursor + msgPos;
/* 275 */     int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
/* 276 */     i = MathHelper.clamp_int(i, 0, j);
/*     */     
/* 278 */     if (i != this.sentHistoryCursor)
/*     */     {
/* 280 */       if (i == j)
/*     */       {
/* 282 */         this.sentHistoryCursor = j;
/* 283 */         this.inputField.setText(this.historyBuffer);
/*     */       }
/*     */       else
/*     */       {
/* 287 */         if (this.sentHistoryCursor == j)
/*     */         {
/* 289 */           this.historyBuffer = this.inputField.getText();
/*     */         }
/*     */         
/* 292 */         this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
/* 293 */         this.sentHistoryCursor = i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 303 */     drawRect(2.0D, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
/* 304 */     this.inputField.drawTextBox();
/* 305 */     IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
/*     */     
/* 307 */     if ((ichatcomponent != null) && (ichatcomponent.getChatStyle().getChatHoverEvent() != null))
/*     */     {
/* 309 */       handleComponentHover(ichatcomponent, mouseX, mouseY);
/*     */     }
/*     */     
/* 312 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public void onAutocompleteResponse(String[] p_146406_1_)
/*     */   {
/* 317 */     if (this.waitingOnAutocomplete)
/*     */     {
/* 319 */       this.playerNamesFound = false;
/* 320 */       this.foundPlayerNames.clear();
/*     */       String[] arrayOfString;
/* 322 */       int j = (arrayOfString = p_146406_1_).length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
/*     */         
/* 324 */         if (s.length() > 0)
/*     */         {
/* 326 */           this.foundPlayerNames.add(s);
/*     */         }
/*     */       }
/*     */       
/* 330 */       String s1 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
/* 331 */       String s2 = org.apache.commons.lang3.StringUtils.getCommonPrefix(p_146406_1_);
/*     */       
/* 333 */       if ((s2.length() > 0) && (!s1.equalsIgnoreCase(s2)))
/*     */       {
/* 335 */         this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
/* 336 */         this.inputField.writeText(s2);
/*     */       }
/* 338 */       else if (this.foundPlayerNames.size() > 0)
/*     */       {
/* 340 */         this.playerNamesFound = true;
/* 341 */         autocompletePlayerNames();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean doesGuiPauseGame()
/*     */   {
/* 351 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */