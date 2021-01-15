/*     */ package net.minecraft.client.gui.inventory;
/*     */ 
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ContainerBeacon;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ 
/*     */ public class GuiBeacon extends GuiContainer
/*     */ {
/*  26 */   private static final org.apache.logging.log4j.Logger logger = ;
/*  27 */   private static final ResourceLocation beaconGuiTextures = new ResourceLocation("textures/gui/container/beacon.png");
/*     */   private IInventory tileBeacon;
/*     */   private ConfirmButton beaconConfirmButton;
/*     */   private boolean buttonsNotDrawn;
/*     */   
/*     */   public GuiBeacon(InventoryPlayer playerInventory, IInventory tileBeaconIn)
/*     */   {
/*  34 */     super(new ContainerBeacon(playerInventory, tileBeaconIn));
/*  35 */     this.tileBeacon = tileBeaconIn;
/*  36 */     this.xSize = 230;
/*  37 */     this.ySize = 219;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  46 */     super.initGui();
/*  47 */     this.buttonList.add(this.beaconConfirmButton = new ConfirmButton(-1, this.guiLeft + 164, this.guiTop + 107));
/*  48 */     this.buttonList.add(new CancelButton(-2, this.guiLeft + 190, this.guiTop + 107));
/*  49 */     this.buttonsNotDrawn = true;
/*  50 */     this.beaconConfirmButton.enabled = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/*  58 */     super.updateScreen();
/*  59 */     int i = this.tileBeacon.getField(0);
/*  60 */     int j = this.tileBeacon.getField(1);
/*  61 */     int k = this.tileBeacon.getField(2);
/*     */     
/*  63 */     if ((this.buttonsNotDrawn) && (i >= 0))
/*     */     {
/*  65 */       this.buttonsNotDrawn = false;
/*     */       
/*  67 */       for (int l = 0; l <= 2; l++)
/*     */       {
/*  69 */         int i1 = net.minecraft.tileentity.TileEntityBeacon.effectsList[l].length;
/*  70 */         int j1 = i1 * 22 + (i1 - 1) * 2;
/*     */         
/*  72 */         for (int k1 = 0; k1 < i1; k1++)
/*     */         {
/*  74 */           int l1 = net.minecraft.tileentity.TileEntityBeacon.effectsList[l][k1].id;
/*  75 */           PowerButton guibeacon$powerbutton = new PowerButton(l << 8 | l1, this.guiLeft + 76 + k1 * 24 - j1 / 2, this.guiTop + 22 + l * 25, l1, l);
/*  76 */           this.buttonList.add(guibeacon$powerbutton);
/*     */           
/*  78 */           if (l >= i)
/*     */           {
/*  80 */             guibeacon$powerbutton.enabled = false;
/*     */           }
/*  82 */           else if (l1 == j)
/*     */           {
/*  84 */             guibeacon$powerbutton.func_146140_b(true);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  89 */       int i2 = 3;
/*  90 */       int j2 = net.minecraft.tileentity.TileEntityBeacon.effectsList[i2].length + 1;
/*  91 */       int k2 = j2 * 22 + (j2 - 1) * 2;
/*     */       
/*  93 */       for (int l2 = 0; l2 < j2 - 1; l2++)
/*     */       {
/*  95 */         int i3 = net.minecraft.tileentity.TileEntityBeacon.effectsList[i2][l2].id;
/*  96 */         PowerButton guibeacon$powerbutton2 = new PowerButton(i2 << 8 | i3, this.guiLeft + 167 + l2 * 24 - k2 / 2, this.guiTop + 47, i3, i2);
/*  97 */         this.buttonList.add(guibeacon$powerbutton2);
/*     */         
/*  99 */         if (i2 >= i)
/*     */         {
/* 101 */           guibeacon$powerbutton2.enabled = false;
/*     */         }
/* 103 */         else if (i3 == k)
/*     */         {
/* 105 */           guibeacon$powerbutton2.func_146140_b(true);
/*     */         }
/*     */       }
/*     */       
/* 109 */       if (j > 0)
/*     */       {
/* 111 */         PowerButton guibeacon$powerbutton1 = new PowerButton(i2 << 8 | j, this.guiLeft + 167 + (j2 - 1) * 24 - k2 / 2, this.guiTop + 47, j, i2);
/* 112 */         this.buttonList.add(guibeacon$powerbutton1);
/*     */         
/* 114 */         if (i2 >= i)
/*     */         {
/* 116 */           guibeacon$powerbutton1.enabled = false;
/*     */         }
/* 118 */         else if (j == k)
/*     */         {
/* 120 */           guibeacon$powerbutton1.func_146140_b(true);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 125 */     this.beaconConfirmButton.enabled = ((this.tileBeacon.getStackInSlot(0) != null) && (j > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws java.io.IOException
/*     */   {
/* 133 */     if (button.id == -2)
/*     */     {
/* 135 */       this.mc.displayGuiScreen(null);
/*     */     }
/* 137 */     else if (button.id == -1)
/*     */     {
/* 139 */       String s = "MC|Beacon";
/* 140 */       PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
/* 141 */       packetbuffer.writeInt(this.tileBeacon.getField(1));
/* 142 */       packetbuffer.writeInt(this.tileBeacon.getField(2));
/* 143 */       this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(s, packetbuffer));
/* 144 */       this.mc.displayGuiScreen(null);
/*     */     }
/* 146 */     else if ((button instanceof PowerButton))
/*     */     {
/* 148 */       if (((PowerButton)button).func_146141_c())
/*     */       {
/* 150 */         return;
/*     */       }
/*     */       
/* 153 */       int j = button.id;
/* 154 */       int k = j & 0xFF;
/* 155 */       int i = j >> 8;
/*     */       
/* 157 */       if (i < 3)
/*     */       {
/* 159 */         this.tileBeacon.setField(1, k);
/*     */       }
/*     */       else
/*     */       {
/* 163 */         this.tileBeacon.setField(2, k);
/*     */       }
/*     */       
/* 166 */       this.buttonList.clear();
/* 167 */       initGui();
/* 168 */       updateScreen();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
/*     */   {
/* 177 */     RenderHelper.disableStandardItemLighting();
/* 178 */     drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 14737632);
/* 179 */     drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 14737632);
/*     */     
/* 181 */     for (GuiButton guibutton : this.buttonList)
/*     */     {
/* 183 */       if (guibutton.isMouseOver())
/*     */       {
/* 185 */         guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
/* 186 */         break;
/*     */       }
/*     */     }
/*     */     
/* 190 */     RenderHelper.enableGUIStandardItemLighting();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
/*     */   {
/* 198 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 199 */     this.mc.getTextureManager().bindTexture(beaconGuiTextures);
/* 200 */     int i = (width - this.xSize) / 2;
/* 201 */     int j = (height - this.ySize) / 2;
/* 202 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/* 203 */     this.itemRender.zLevel = 100.0F;
/* 204 */     this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.emerald), i + 42, j + 109);
/* 205 */     this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.diamond), i + 42 + 22, j + 109);
/* 206 */     this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.gold_ingot), i + 42 + 44, j + 109);
/* 207 */     this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.iron_ingot), i + 42 + 66, j + 109);
/* 208 */     this.itemRender.zLevel = 0.0F;
/*     */   }
/*     */   
/*     */   static class Button extends GuiButton
/*     */   {
/*     */     private final ResourceLocation field_146145_o;
/*     */     private final int field_146144_p;
/*     */     private final int field_146143_q;
/*     */     private boolean field_146142_r;
/*     */     
/*     */     protected Button(int p_i1077_1_, int p_i1077_2_, int p_i1077_3_, ResourceLocation p_i1077_4_, int p_i1077_5_, int p_i1077_6_)
/*     */     {
/* 220 */       super(p_i1077_2_, p_i1077_3_, 22, 22, "");
/* 221 */       this.field_146145_o = p_i1077_4_;
/* 222 */       this.field_146144_p = p_i1077_5_;
/* 223 */       this.field_146143_q = p_i1077_6_;
/*     */     }
/*     */     
/*     */     public void drawButton(Minecraft mc, int mouseX, int mouseY)
/*     */     {
/* 228 */       if (this.visible)
/*     */       {
/* 230 */         mc.getTextureManager().bindTexture(GuiBeacon.beaconGuiTextures);
/* 231 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 232 */         this.hovered = ((mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height));
/* 233 */         int i = 219;
/* 234 */         int j = 0;
/*     */         
/* 236 */         if (!this.enabled)
/*     */         {
/* 238 */           j += this.width * 2;
/*     */         }
/* 240 */         else if (this.field_146142_r)
/*     */         {
/* 242 */           j += this.width * 1;
/*     */         }
/* 244 */         else if (this.hovered)
/*     */         {
/* 246 */           j += this.width * 3;
/*     */         }
/*     */         
/* 249 */         drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
/*     */         
/* 251 */         if (!GuiBeacon.beaconGuiTextures.equals(this.field_146145_o))
/*     */         {
/* 253 */           mc.getTextureManager().bindTexture(this.field_146145_o);
/*     */         }
/*     */         
/* 256 */         drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_146144_p, this.field_146143_q, 18, 18);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean func_146141_c()
/*     */     {
/* 262 */       return this.field_146142_r;
/*     */     }
/*     */     
/*     */     public void func_146140_b(boolean p_146140_1_)
/*     */     {
/* 267 */       this.field_146142_r = p_146140_1_;
/*     */     }
/*     */   }
/*     */   
/*     */   class CancelButton extends GuiBeacon.Button
/*     */   {
/*     */     public CancelButton(int p_i1074_2_, int p_i1074_3_, int p_i1074_4_)
/*     */     {
/* 275 */       super(p_i1074_3_, p_i1074_4_, GuiBeacon.beaconGuiTextures, 112, 220);
/*     */     }
/*     */     
/*     */     public void drawButtonForegroundLayer(int mouseX, int mouseY)
/*     */     {
/* 280 */       GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), mouseX, mouseY);
/*     */     }
/*     */   }
/*     */   
/*     */   class ConfirmButton extends GuiBeacon.Button
/*     */   {
/*     */     public ConfirmButton(int p_i1075_2_, int p_i1075_3_, int p_i1075_4_)
/*     */     {
/* 288 */       super(p_i1075_3_, p_i1075_4_, GuiBeacon.beaconGuiTextures, 90, 220);
/*     */     }
/*     */     
/*     */     public void drawButtonForegroundLayer(int mouseX, int mouseY)
/*     */     {
/* 293 */       GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.done", new Object[0]), mouseX, mouseY);
/*     */     }
/*     */   }
/*     */   
/*     */   class PowerButton extends GuiBeacon.Button
/*     */   {
/*     */     private final int field_146149_p;
/*     */     private final int field_146148_q;
/*     */     
/*     */     public PowerButton(int p_i1076_2_, int p_i1076_3_, int p_i1076_4_, int p_i1076_5_, int p_i1076_6_)
/*     */     {
/* 304 */       super(p_i1076_3_, p_i1076_4_, GuiContainer.inventoryBackground, 0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18, 198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
/* 305 */       this.field_146149_p = p_i1076_5_;
/* 306 */       this.field_146148_q = p_i1076_6_;
/*     */     }
/*     */     
/*     */     public void drawButtonForegroundLayer(int mouseX, int mouseY)
/*     */     {
/* 311 */       String s = I18n.format(Potion.potionTypes[this.field_146149_p].getName(), new Object[0]);
/*     */       
/* 313 */       if ((this.field_146148_q >= 3) && (this.field_146149_p != Potion.regeneration.id))
/*     */       {
/* 315 */         s = s + " II";
/*     */       }
/*     */       
/* 318 */       GuiBeacon.this.drawCreativeTabHoveringText(s, mouseX, mouseY);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\inventory\GuiBeacon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */