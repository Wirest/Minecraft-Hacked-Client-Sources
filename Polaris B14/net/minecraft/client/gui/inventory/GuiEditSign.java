/*     */ package net.minecraft.client.gui.inventory;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.play.client.C12PacketUpdateSign;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiEditSign
/*     */   extends GuiScreen
/*     */ {
/*     */   private TileEntitySign tileSign;
/*     */   private int updateCounter;
/*     */   private int editLine;
/*     */   private GuiButton doneBtn;
/*     */   
/*     */   public GuiEditSign(TileEntitySign teSign)
/*     */   {
/*  34 */     this.tileSign = teSign;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  43 */     this.buttonList.clear();
/*  44 */     Keyboard.enableRepeatEvents(true);
/*  45 */     this.buttonList.add(this.doneBtn = new GuiButton(0, width / 2 - 100, height / 4 + 120, I18n.format("gui.done", new Object[0])));
/*  46 */     this.tileSign.setEditable(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onGuiClosed()
/*     */   {
/*  54 */     Keyboard.enableRepeatEvents(false);
/*  55 */     NetHandlerPlayClient nethandlerplayclient = this.mc.getNetHandler();
/*     */     
/*  57 */     if (nethandlerplayclient != null)
/*     */     {
/*  59 */       nethandlerplayclient.addToSendQueue(new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
/*     */     }
/*     */     
/*  62 */     this.tileSign.setEditable(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/*  70 */     this.updateCounter += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws IOException
/*     */   {
/*  78 */     if (button.enabled)
/*     */     {
/*  80 */       if (button.id == 0)
/*     */       {
/*  82 */         this.tileSign.markDirty();
/*  83 */         this.mc.displayGuiScreen(null);
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
/*  94 */     if (keyCode == 200)
/*     */     {
/*  96 */       this.editLine = (this.editLine - 1 & 0x3);
/*     */     }
/*     */     
/*  99 */     if ((keyCode == 208) || (keyCode == 28) || (keyCode == 156))
/*     */     {
/* 101 */       this.editLine = (this.editLine + 1 & 0x3);
/*     */     }
/*     */     
/* 104 */     String s = this.tileSign.signText[this.editLine].getUnformattedText();
/*     */     
/* 106 */     if ((keyCode == 14) && (s.length() > 0))
/*     */     {
/* 108 */       s = s.substring(0, s.length() - 1);
/*     */     }
/*     */     
/* 111 */     if ((ChatAllowedCharacters.isAllowedCharacter(typedChar)) && (this.fontRendererObj.getStringWidth(s + typedChar) <= 90))
/*     */     {
/* 113 */       s = s + typedChar;
/*     */     }
/*     */     
/* 116 */     this.tileSign.signText[this.editLine] = new ChatComponentText(s);
/*     */     
/* 118 */     if (keyCode == 1)
/*     */     {
/* 120 */       actionPerformed(this.doneBtn);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 129 */     drawDefaultBackground();
/* 130 */     drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), width / 2, 40, 16777215);
/* 131 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 132 */     GlStateManager.pushMatrix();
/* 133 */     GlStateManager.translate(width / 2, 0.0F, 50.0F);
/* 134 */     float f = 93.75F;
/* 135 */     GlStateManager.scale(-f, -f, -f);
/* 136 */     GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/* 137 */     Block block = this.tileSign.getBlockType();
/*     */     
/* 139 */     if (block == Blocks.standing_sign)
/*     */     {
/* 141 */       float f1 = this.tileSign.getBlockMetadata() * 360 / 16.0F;
/* 142 */       GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
/* 143 */       GlStateManager.translate(0.0F, -1.0625F, 0.0F);
/*     */     }
/*     */     else
/*     */     {
/* 147 */       int i = this.tileSign.getBlockMetadata();
/* 148 */       float f2 = 0.0F;
/*     */       
/* 150 */       if (i == 2)
/*     */       {
/* 152 */         f2 = 180.0F;
/*     */       }
/*     */       
/* 155 */       if (i == 4)
/*     */       {
/* 157 */         f2 = 90.0F;
/*     */       }
/*     */       
/* 160 */       if (i == 5)
/*     */       {
/* 162 */         f2 = -90.0F;
/*     */       }
/*     */       
/* 165 */       GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
/* 166 */       GlStateManager.translate(0.0F, -1.0625F, 0.0F);
/*     */     }
/*     */     
/* 169 */     if (this.updateCounter / 6 % 2 == 0)
/*     */     {
/* 171 */       this.tileSign.lineBeingEdited = this.editLine;
/*     */     }
/*     */     
/* 174 */     TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
/* 175 */     this.tileSign.lineBeingEdited = -1;
/* 176 */     GlStateManager.popMatrix();
/* 177 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiEditSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */