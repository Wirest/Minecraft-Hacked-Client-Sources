/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.gen.FlatGeneratorInfo;
/*     */ import net.minecraft.world.gen.FlatLayerInfo;
/*     */ 
/*     */ public class GuiCreateFlatWorld extends GuiScreen
/*     */ {
/*     */   private final GuiCreateWorld createWorldGui;
/*  21 */   private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
/*     */   private String flatWorldTitle;
/*     */   private String field_146394_i;
/*     */   private String field_146391_r;
/*     */   private Details createFlatWorldListSlotGui;
/*     */   private GuiButton field_146389_t;
/*     */   private GuiButton field_146388_u;
/*     */   private GuiButton field_146386_v;
/*     */   
/*     */   public GuiCreateFlatWorld(GuiCreateWorld createWorldGuiIn, String p_i1029_2_) {
/*  31 */     this.createWorldGui = createWorldGuiIn;
/*  32 */     func_146383_a(p_i1029_2_);
/*     */   }
/*     */   
/*     */   public String func_146384_e() {
/*  36 */     return this.theFlatGeneratorInfo.toString();
/*     */   }
/*     */   
/*     */   public void func_146383_a(String p_146383_1_) {
/*  40 */     this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_146383_1_);
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  44 */     this.buttonList.clear();
/*  45 */     this.flatWorldTitle = I18n.format("createWorld.customize.flat.title", new Object[0]);
/*  46 */     this.field_146394_i = I18n.format("createWorld.customize.flat.tile", new Object[0]);
/*  47 */     this.field_146391_r = I18n.format("createWorld.customize.flat.height", new Object[0]);
/*  48 */     this.createFlatWorldListSlotGui = new Details();
/*  49 */     this.buttonList.add(this.field_146389_t = new GuiButton(2, width / 2 - 154, height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer", new Object[0]) + " (NYI)"));
/*  50 */     this.buttonList.add(this.field_146388_u = new GuiButton(3, width / 2 - 50, height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer", new Object[0]) + " (NYI)"));
/*  51 */     this.buttonList.add(this.field_146386_v = new GuiButton(4, width / 2 - 155, height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0])));
/*  52 */     this.buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
/*  53 */     this.buttonList.add(new GuiButton(5, width / 2 + 5, height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
/*  54 */     this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  55 */     this.field_146389_t.visible = (this.field_146388_u.visible = 0);
/*  56 */     this.theFlatGeneratorInfo.func_82645_d();
/*  57 */     func_146375_g();
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws java.io.IOException {
/*  61 */     super.handleMouseInput();
/*  62 */     this.createFlatWorldListSlotGui.handleMouseInput();
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/*  66 */     int i = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.field_148228_k - 1;
/*  67 */     if (button.id == 1) {
/*  68 */       this.mc.displayGuiScreen(this.createWorldGui);
/*  69 */     } else if (button.id == 0) {
/*  70 */       this.createWorldGui.chunkProviderSettingsJson = func_146384_e();
/*  71 */       this.mc.displayGuiScreen(this.createWorldGui);
/*  72 */     } else if (button.id == 5) {
/*  73 */       this.mc.displayGuiScreen(new GuiFlatPresets(this));
/*  74 */     } else if ((button.id == 4) && (func_146382_i())) {
/*  75 */       this.theFlatGeneratorInfo.getFlatLayers().remove(i);
/*  76 */       this.createFlatWorldListSlotGui.field_148228_k = Math.min(this.createFlatWorldListSlotGui.field_148228_k, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
/*     */     }
/*     */     
/*  79 */     this.theFlatGeneratorInfo.func_82645_d();
/*  80 */     func_146375_g();
/*     */   }
/*     */   
/*     */   public void func_146375_g() {
/*  84 */     boolean flag = func_146382_i();
/*  85 */     this.field_146386_v.enabled = flag;
/*  86 */     this.field_146388_u.enabled = flag;
/*  87 */     this.field_146388_u.enabled = false;
/*  88 */     this.field_146389_t.enabled = false;
/*     */   }
/*     */   
/*     */   private boolean func_146382_i() {
/*  92 */     return (this.createFlatWorldListSlotGui.field_148228_k > -1) && (this.createFlatWorldListSlotGui.field_148228_k < this.theFlatGeneratorInfo.getFlatLayers().size());
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  96 */     drawDefaultBackground();
/*  97 */     this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
/*  98 */     drawCenteredString(this.fontRendererObj, this.flatWorldTitle, width / 2, 8, 16777215);
/*  99 */     int i = width / 2 - 92 - 16;
/* 100 */     drawString(this.fontRendererObj, this.field_146394_i, i, 32, 16777215);
/* 101 */     drawString(this.fontRendererObj, this.field_146391_r, i + 2 + 213 - this.fontRendererObj.getStringWidth(this.field_146391_r), 32, 16777215);
/* 102 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   class Details extends GuiSlot {
/* 106 */     public int field_148228_k = -1;
/*     */     
/*     */     public Details() {
/* 109 */       super(GuiCreateFlatWorld.width, GuiCreateFlatWorld.height, 43, GuiCreateFlatWorld.height - 60, 24);
/*     */     }
/*     */     
/*     */     private void func_148225_a(int p_148225_1_, int p_148225_2_, ItemStack p_148225_3_) {
/* 113 */       func_148226_e(p_148225_1_ + 1, p_148225_2_ + 1);
/* 114 */       GlStateManager.enableRescaleNormal();
/* 115 */       if ((p_148225_3_ != null) && (p_148225_3_.getItem() != null)) {
/* 116 */         net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
/* 117 */         GuiCreateFlatWorld.this.itemRender.renderItemIntoGUI(p_148225_3_, p_148225_1_ + 2, p_148225_2_ + 2);
/* 118 */         net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
/*     */       }
/*     */       
/* 121 */       GlStateManager.disableRescaleNormal();
/*     */     }
/*     */     
/*     */     private void func_148226_e(int p_148226_1_, int p_148226_2_) {
/* 125 */       func_148224_c(p_148226_1_, p_148226_2_, 0, 0);
/*     */     }
/*     */     
/*     */     private void func_148224_c(int p_148224_1_, int p_148224_2_, int p_148224_3_, int p_148224_4_) {
/* 129 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 130 */       this.mc.getTextureManager().bindTexture(Gui.statIcons);
/* 131 */       float f = 0.0078125F;
/* 132 */       float f1 = 0.0078125F;
/* 133 */       int i = 18;
/* 134 */       int j = 18;
/* 135 */       Tessellator tessellator = Tessellator.getInstance();
/* 136 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 137 */       worldrenderer.begin(7, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX);
/* 138 */       worldrenderer.pos(p_148224_1_ + 0, p_148224_2_ + 18, GuiCreateFlatWorld.this.zLevel).tex((p_148224_3_ + 0) * 0.0078125F, (p_148224_4_ + 18) * 0.0078125F).endVertex();
/* 139 */       worldrenderer.pos(p_148224_1_ + 18, p_148224_2_ + 18, GuiCreateFlatWorld.this.zLevel).tex((p_148224_3_ + 18) * 0.0078125F, (p_148224_4_ + 18) * 0.0078125F).endVertex();
/* 140 */       worldrenderer.pos(p_148224_1_ + 18, p_148224_2_ + 0, GuiCreateFlatWorld.this.zLevel).tex((p_148224_3_ + 18) * 0.0078125F, (p_148224_4_ + 0) * 0.0078125F).endVertex();
/* 141 */       worldrenderer.pos(p_148224_1_ + 0, p_148224_2_ + 0, GuiCreateFlatWorld.this.zLevel).tex((p_148224_3_ + 0) * 0.0078125F, (p_148224_4_ + 0) * 0.0078125F).endVertex();
/* 142 */       tessellator.draw();
/*     */     }
/*     */     
/*     */     protected int getSize() {
/* 146 */       return GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size();
/*     */     }
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/* 150 */       this.field_148228_k = slotIndex;
/* 151 */       GuiCreateFlatWorld.this.func_146375_g();
/*     */     }
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 155 */       return slotIndex == this.field_148228_k;
/*     */     }
/*     */     
/*     */     protected void drawBackground() {}
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */     {
/* 162 */       FlatLayerInfo flatlayerinfo = (FlatLayerInfo)GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - entryID - 1);
/* 163 */       IBlockState iblockstate = flatlayerinfo.func_175900_c();
/* 164 */       Block block = iblockstate.getBlock();
/* 165 */       Item item = Item.getItemFromBlock(block);
/* 166 */       ItemStack itemstack = (block != Blocks.air) && (item != null) ? new ItemStack(item, 1, block.getMetaFromState(iblockstate)) : null;
/* 167 */       String s = itemstack == null ? "Air" : item.getItemStackDisplayName(itemstack);
/* 168 */       if (item == null) {
/* 169 */         if ((block != Blocks.water) && (block != Blocks.flowing_water)) {
/* 170 */           if ((block == Blocks.lava) || (block == Blocks.flowing_lava)) {
/* 171 */             item = Items.lava_bucket;
/*     */           }
/*     */         } else {
/* 174 */           item = Items.water_bucket;
/*     */         }
/*     */         
/* 177 */         if (item != null) {
/* 178 */           itemstack = new ItemStack(item, 1, block.getMetaFromState(iblockstate));
/* 179 */           s = block.getLocalizedName();
/*     */         }
/*     */       }
/*     */       
/* 183 */       func_148225_a(p_180791_2_, p_180791_3_, itemstack);
/* 184 */       GuiCreateFlatWorld.this.fontRendererObj.drawString(s, p_180791_2_ + 18 + 5, p_180791_3_ + 3, 16777215);
/*     */       String s1;
/* 186 */       String s1; if (entryID == 0) {
/* 187 */         s1 = I18n.format("createWorld.customize.flat.layer.top", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) }); } else { String s1;
/* 188 */         if (entryID == GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - 1) {
/* 189 */           s1 = I18n.format("createWorld.customize.flat.layer.bottom", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) });
/*     */         } else {
/* 191 */           s1 = I18n.format("createWorld.customize.flat.layer", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) });
/*     */         }
/*     */       }
/* 194 */       GuiCreateFlatWorld.this.fontRendererObj.drawString(s1, p_180791_2_ + 2 + 213 - GuiCreateFlatWorld.this.fontRendererObj.getStringWidth(s1), p_180791_3_ + 3, 16777215);
/*     */     }
/*     */     
/*     */     protected int getScrollBarX() {
/* 198 */       return this.width - 70;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiCreateFlatWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */