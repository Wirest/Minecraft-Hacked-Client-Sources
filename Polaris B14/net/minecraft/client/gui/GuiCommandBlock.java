/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiCommandBlock extends GuiScreen
/*     */ {
/*  16 */   private static final Logger field_146488_a = ;
/*     */   
/*     */   private GuiTextField commandTextField;
/*     */   
/*     */   private GuiTextField previousOutputTextField;
/*     */   
/*     */   private final CommandBlockLogic localCommandBlock;
/*     */   
/*     */   private GuiButton doneBtn;
/*     */   
/*     */   private GuiButton cancelBtn;
/*     */   
/*     */   private GuiButton field_175390_s;
/*     */   private boolean field_175389_t;
/*     */   
/*     */   public GuiCommandBlock(CommandBlockLogic p_i45032_1_)
/*     */   {
/*  33 */     this.localCommandBlock = p_i45032_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/*  41 */     this.commandTextField.updateCursorCounter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  50 */     Keyboard.enableRepeatEvents(true);
/*  51 */     this.buttonList.clear();
/*  52 */     this.buttonList.add(this.doneBtn = new GuiButton(0, width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
/*  53 */     this.buttonList.add(this.cancelBtn = new GuiButton(1, width / 2 + 4, height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  54 */     this.buttonList.add(this.field_175390_s = new GuiButton(4, width / 2 + 150 - 20, 150, 20, 20, "O"));
/*  55 */     this.commandTextField = new GuiTextField(2, this.fontRendererObj, width / 2 - 150, 50, 300, 20);
/*  56 */     this.commandTextField.setMaxStringLength(32767);
/*  57 */     this.commandTextField.setFocused(true);
/*  58 */     this.commandTextField.setText(this.localCommandBlock.getCommand());
/*  59 */     this.previousOutputTextField = new GuiTextField(3, this.fontRendererObj, width / 2 - 150, 150, 276, 20);
/*  60 */     this.previousOutputTextField.setMaxStringLength(32767);
/*  61 */     this.previousOutputTextField.setEnabled(false);
/*  62 */     this.previousOutputTextField.setText("-");
/*  63 */     this.field_175389_t = this.localCommandBlock.shouldTrackOutput();
/*  64 */     func_175388_a();
/*  65 */     this.doneBtn.enabled = (this.commandTextField.getText().trim().length() > 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onGuiClosed()
/*     */   {
/*  73 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws IOException
/*     */   {
/*  81 */     if (button.enabled)
/*     */     {
/*  83 */       if (button.id == 1)
/*     */       {
/*  85 */         this.localCommandBlock.setTrackOutput(this.field_175389_t);
/*  86 */         this.mc.displayGuiScreen(null);
/*     */       }
/*  88 */       else if (button.id == 0)
/*     */       {
/*  90 */         PacketBuffer packetbuffer = new PacketBuffer(io.netty.buffer.Unpooled.buffer());
/*  91 */         packetbuffer.writeByte(this.localCommandBlock.func_145751_f());
/*  92 */         this.localCommandBlock.func_145757_a(packetbuffer);
/*  93 */         packetbuffer.writeString(this.commandTextField.getText());
/*  94 */         packetbuffer.writeBoolean(this.localCommandBlock.shouldTrackOutput());
/*  95 */         this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|AdvCdm", packetbuffer));
/*     */         
/*  97 */         if (!this.localCommandBlock.shouldTrackOutput())
/*     */         {
/*  99 */           this.localCommandBlock.setLastOutput(null);
/*     */         }
/*     */         
/* 102 */         this.mc.displayGuiScreen(null);
/*     */       }
/* 104 */       else if (button.id == 4)
/*     */       {
/* 106 */         this.localCommandBlock.setTrackOutput(!this.localCommandBlock.shouldTrackOutput());
/* 107 */         func_175388_a();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/* 118 */     this.commandTextField.textboxKeyTyped(typedChar, keyCode);
/* 119 */     this.previousOutputTextField.textboxKeyTyped(typedChar, keyCode);
/* 120 */     this.doneBtn.enabled = (this.commandTextField.getText().trim().length() > 0);
/*     */     
/* 122 */     if ((keyCode != 28) && (keyCode != 156))
/*     */     {
/* 124 */       if (keyCode == 1)
/*     */       {
/* 126 */         actionPerformed(this.cancelBtn);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 131 */       actionPerformed(this.doneBtn);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/* 140 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 141 */     this.commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
/* 142 */     this.previousOutputTextField.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 150 */     drawDefaultBackground();
/* 151 */     drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), width / 2, 20, 16777215);
/* 152 */     drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), width / 2 - 150, 37, 10526880);
/* 153 */     this.commandTextField.drawTextBox();
/* 154 */     int i = 75;
/* 155 */     int j = 0;
/* 156 */     drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/* 157 */     drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/* 158 */     drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/* 159 */     drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/* 160 */     drawString(this.fontRendererObj, "", width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/*     */     
/* 162 */     if (this.previousOutputTextField.getText().length() > 0)
/*     */     {
/* 164 */       i = i + j * this.fontRendererObj.FONT_HEIGHT + 16;
/* 165 */       drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), width / 2 - 150, i, 10526880);
/* 166 */       this.previousOutputTextField.drawTextBox();
/*     */     }
/*     */     
/* 169 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   private void func_175388_a()
/*     */   {
/* 174 */     if (this.localCommandBlock.shouldTrackOutput())
/*     */     {
/* 176 */       this.field_175390_s.displayString = "O";
/*     */       
/* 178 */       if (this.localCommandBlock.getLastOutput() != null)
/*     */       {
/* 180 */         this.previousOutputTextField.setText(this.localCommandBlock.getLastOutput().getUnformattedText());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 185 */       this.field_175390_s.displayString = "X";
/* 186 */       this.previousOutputTextField.setText("-");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */