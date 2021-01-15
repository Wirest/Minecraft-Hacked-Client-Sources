/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockTallGrass.EnumType;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.gen.FlatGeneratorInfo;
/*     */ import net.minecraft.world.gen.FlatLayerInfo;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiFlatPresets extends GuiScreen
/*     */ {
/*  26 */   private static final List<LayerItem> FLAT_WORLD_PRESETS = ;
/*     */   
/*     */   private final GuiCreateFlatWorld parentScreen;
/*     */   
/*     */   private String presetsTitle;
/*     */   private String presetsShare;
/*     */   private String field_146436_r;
/*     */   private ListSlot field_146435_s;
/*     */   private GuiButton field_146434_t;
/*     */   private GuiTextField field_146433_u;
/*     */   
/*     */   public GuiFlatPresets(GuiCreateFlatWorld p_i46318_1_)
/*     */   {
/*  39 */     this.parentScreen = p_i46318_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/*  48 */     this.buttonList.clear();
/*  49 */     Keyboard.enableRepeatEvents(true);
/*  50 */     this.presetsTitle = I18n.format("createWorld.customize.presets.title", new Object[0]);
/*  51 */     this.presetsShare = I18n.format("createWorld.customize.presets.share", new Object[0]);
/*  52 */     this.field_146436_r = I18n.format("createWorld.customize.presets.list", new Object[0]);
/*  53 */     this.field_146433_u = new GuiTextField(2, this.fontRendererObj, 50, 40, width - 100, 20);
/*  54 */     this.field_146435_s = new ListSlot();
/*  55 */     this.field_146433_u.setMaxStringLength(1230);
/*  56 */     this.field_146433_u.setText(this.parentScreen.func_146384_e());
/*  57 */     this.buttonList.add(this.field_146434_t = new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("createWorld.customize.presets.select", new Object[0])));
/*  58 */     this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  59 */     func_146426_g();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void handleMouseInput()
/*     */     throws IOException
/*     */   {
/*  67 */     super.handleMouseInput();
/*  68 */     this.field_146435_s.handleMouseInput();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onGuiClosed()
/*     */   {
/*  76 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/*  84 */     this.field_146433_u.mouseClicked(mouseX, mouseY, mouseButton);
/*  85 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/*  94 */     if (!this.field_146433_u.textboxKeyTyped(typedChar, keyCode))
/*     */     {
/*  96 */       super.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void actionPerformed(GuiButton button)
/*     */     throws IOException
/*     */   {
/* 105 */     if ((button.id == 0) && (func_146430_p()))
/*     */     {
/* 107 */       this.parentScreen.func_146383_a(this.field_146433_u.getText());
/* 108 */       this.mc.displayGuiScreen(this.parentScreen);
/*     */     }
/* 110 */     else if (button.id == 1)
/*     */     {
/* 112 */       this.mc.displayGuiScreen(this.parentScreen);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 121 */     drawDefaultBackground();
/* 122 */     this.field_146435_s.drawScreen(mouseX, mouseY, partialTicks);
/* 123 */     drawCenteredString(this.fontRendererObj, this.presetsTitle, width / 2, 8, 16777215);
/* 124 */     drawString(this.fontRendererObj, this.presetsShare, 50, 30, 10526880);
/* 125 */     drawString(this.fontRendererObj, this.field_146436_r, 50, 70, 10526880);
/* 126 */     this.field_146433_u.drawTextBox();
/* 127 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateScreen()
/*     */   {
/* 135 */     this.field_146433_u.updateCursorCounter();
/* 136 */     super.updateScreen();
/*     */   }
/*     */   
/*     */   public void func_146426_g()
/*     */   {
/* 141 */     boolean flag = func_146430_p();
/* 142 */     this.field_146434_t.enabled = flag;
/*     */   }
/*     */   
/*     */   private boolean func_146430_p()
/*     */   {
/* 147 */     return ((this.field_146435_s.field_148175_k > -1) && (this.field_146435_s.field_148175_k < FLAT_WORLD_PRESETS.size())) || (this.field_146433_u.getText().length() > 1);
/*     */   }
/*     */   
/*     */   private static void func_146425_a(String p_146425_0_, Item p_146425_1_, BiomeGenBase p_146425_2_, FlatLayerInfo... p_146425_3_)
/*     */   {
/* 152 */     func_175354_a(p_146425_0_, p_146425_1_, 0, p_146425_2_, null, p_146425_3_);
/*     */   }
/*     */   
/*     */   private static void func_146421_a(String p_146421_0_, Item p_146421_1_, BiomeGenBase p_146421_2_, List<String> p_146421_3_, FlatLayerInfo... p_146421_4_)
/*     */   {
/* 157 */     func_175354_a(p_146421_0_, p_146421_1_, 0, p_146421_2_, p_146421_3_, p_146421_4_);
/*     */   }
/*     */   
/*     */   private static void func_175354_a(String p_175354_0_, Item p_175354_1_, int p_175354_2_, BiomeGenBase p_175354_3_, List<String> p_175354_4_, FlatLayerInfo... p_175354_5_)
/*     */   {
/* 162 */     FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
/*     */     
/* 164 */     for (int i = p_175354_5_.length - 1; i >= 0; i--)
/*     */     {
/* 166 */       flatgeneratorinfo.getFlatLayers().add(p_175354_5_[i]);
/*     */     }
/*     */     
/* 169 */     flatgeneratorinfo.setBiome(p_175354_3_.biomeID);
/* 170 */     flatgeneratorinfo.func_82645_d();
/*     */     
/* 172 */     if (p_175354_4_ != null)
/*     */     {
/* 174 */       for (String s : p_175354_4_)
/*     */       {
/* 176 */         flatgeneratorinfo.getWorldFeatures().put(s, Maps.newHashMap());
/*     */       }
/*     */     }
/*     */     
/* 180 */     FLAT_WORLD_PRESETS.add(new LayerItem(p_175354_1_, p_175354_2_, p_175354_0_, flatgeneratorinfo.toString()));
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 185 */     func_146421_a("Classic Flat", Item.getItemFromBlock(Blocks.grass), BiomeGenBase.plains, Arrays.asList(new String[] { "village" }), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(2, Blocks.dirt), new FlatLayerInfo(1, Blocks.bedrock) });
/* 186 */     func_146421_a("Tunnelers' Dream", Item.getItemFromBlock(Blocks.stone), BiomeGenBase.extremeHills, Arrays.asList(new String[] { "biome_1", "dungeon", "decoration", "stronghold", "mineshaft" }), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(230, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 187 */     func_146421_a("Water World", Items.water_bucket, BiomeGenBase.deepOcean, Arrays.asList(new String[] { "biome_1", "oceanmonument" }), new FlatLayerInfo[] { new FlatLayerInfo(90, Blocks.water), new FlatLayerInfo(5, Blocks.sand), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(5, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 188 */     func_175354_a("Overworld", Item.getItemFromBlock(Blocks.tallgrass), BlockTallGrass.EnumType.GRASS.getMeta(), BiomeGenBase.plains, Arrays.asList(new String[] { "village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake" }), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 189 */     func_146421_a("Snowy Kingdom", Item.getItemFromBlock(Blocks.snow_layer), BiomeGenBase.icePlains, Arrays.asList(new String[] { "village", "biome_1" }), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.snow_layer), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 190 */     func_146421_a("Bottomless Pit", Items.feather, BiomeGenBase.plains, Arrays.asList(new String[] { "village", "biome_1" }), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(2, Blocks.cobblestone) });
/* 191 */     func_146421_a("Desert", Item.getItemFromBlock(Blocks.sand), BiomeGenBase.desert, Arrays.asList(new String[] { "village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon" }), new FlatLayerInfo[] { new FlatLayerInfo(8, Blocks.sand), new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/* 192 */     func_146425_a("Redstone Ready", Items.redstone, BiomeGenBase.desert, new FlatLayerInfo[] { new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock) });
/*     */   }
/*     */   
/*     */   static class LayerItem
/*     */   {
/*     */     public Item field_148234_a;
/*     */     public int field_179037_b;
/*     */     public String field_148232_b;
/*     */     public String field_148233_c;
/*     */     
/*     */     public LayerItem(Item p_i45518_1_, int p_i45518_2_, String p_i45518_3_, String p_i45518_4_)
/*     */     {
/* 204 */       this.field_148234_a = p_i45518_1_;
/* 205 */       this.field_179037_b = p_i45518_2_;
/* 206 */       this.field_148232_b = p_i45518_3_;
/* 207 */       this.field_148233_c = p_i45518_4_;
/*     */     }
/*     */   }
/*     */   
/*     */   class ListSlot extends GuiSlot
/*     */   {
/* 213 */     public int field_148175_k = -1;
/*     */     
/*     */     public ListSlot()
/*     */     {
/* 217 */       super(GuiFlatPresets.width, GuiFlatPresets.height, 80, GuiFlatPresets.height - 37, 24);
/*     */     }
/*     */     
/*     */     private void func_178054_a(int p_178054_1_, int p_178054_2_, Item p_178054_3_, int p_178054_4_)
/*     */     {
/* 222 */       func_148173_e(p_178054_1_ + 1, p_178054_2_ + 1);
/* 223 */       GlStateManager.enableRescaleNormal();
/* 224 */       RenderHelper.enableGUIStandardItemLighting();
/* 225 */       GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(p_178054_3_, 1, p_178054_4_), p_178054_1_ + 2, p_178054_2_ + 2);
/* 226 */       RenderHelper.disableStandardItemLighting();
/* 227 */       GlStateManager.disableRescaleNormal();
/*     */     }
/*     */     
/*     */     private void func_148173_e(int p_148173_1_, int p_148173_2_)
/*     */     {
/* 232 */       func_148171_c(p_148173_1_, p_148173_2_, 0, 0);
/*     */     }
/*     */     
/*     */     private void func_148171_c(int p_148171_1_, int p_148171_2_, int p_148171_3_, int p_148171_4_)
/*     */     {
/* 237 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 238 */       this.mc.getTextureManager().bindTexture(Gui.statIcons);
/* 239 */       float f = 0.0078125F;
/* 240 */       float f1 = 0.0078125F;
/* 241 */       int i = 18;
/* 242 */       int j = 18;
/* 243 */       Tessellator tessellator = Tessellator.getInstance();
/* 244 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 245 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 246 */       worldrenderer.pos(p_148171_1_ + 0, p_148171_2_ + 18, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 0) * 0.0078125F, (p_148171_4_ + 18) * 0.0078125F).endVertex();
/* 247 */       worldrenderer.pos(p_148171_1_ + 18, p_148171_2_ + 18, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 18) * 0.0078125F, (p_148171_4_ + 18) * 0.0078125F).endVertex();
/* 248 */       worldrenderer.pos(p_148171_1_ + 18, p_148171_2_ + 0, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 18) * 0.0078125F, (p_148171_4_ + 0) * 0.0078125F).endVertex();
/* 249 */       worldrenderer.pos(p_148171_1_ + 0, p_148171_2_ + 0, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 0) * 0.0078125F, (p_148171_4_ + 0) * 0.0078125F).endVertex();
/* 250 */       tessellator.draw();
/*     */     }
/*     */     
/*     */     protected int getSize()
/*     */     {
/* 255 */       return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
/*     */     }
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
/*     */     {
/* 260 */       this.field_148175_k = slotIndex;
/* 261 */       GuiFlatPresets.this.func_146426_g();
/* 262 */       GuiFlatPresets.this.field_146433_u.setText(((GuiFlatPresets.LayerItem)GuiFlatPresets.FLAT_WORLD_PRESETS.get(GuiFlatPresets.this.field_146435_s.field_148175_k)).field_148233_c);
/*     */     }
/*     */     
/*     */     protected boolean isSelected(int slotIndex)
/*     */     {
/* 267 */       return slotIndex == this.field_148175_k;
/*     */     }
/*     */     
/*     */ 
/*     */     protected void drawBackground() {}
/*     */     
/*     */ 
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */     {
/* 276 */       GuiFlatPresets.LayerItem guiflatpresets$layeritem = (GuiFlatPresets.LayerItem)GuiFlatPresets.FLAT_WORLD_PRESETS.get(entryID);
/* 277 */       func_178054_a(p_180791_2_, p_180791_3_, guiflatpresets$layeritem.field_148234_a, guiflatpresets$layeritem.field_179037_b);
/* 278 */       GuiFlatPresets.this.fontRendererObj.drawString(guiflatpresets$layeritem.field_148232_b, p_180791_2_ + 18 + 5, p_180791_3_ + 6, 16777215);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiFlatPresets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */