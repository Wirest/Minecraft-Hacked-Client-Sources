/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings.Factory;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiScreenCustomizePresets extends GuiScreen
/*     */ {
/*  16 */   private static final List<Info> field_175310_f = ;
/*     */   private ListPreset field_175311_g;
/*     */   private GuiButton field_175316_h;
/*     */   private GuiTextField field_175317_i;
/*     */   private GuiCustomizeWorldScreen field_175314_r;
/*  21 */   protected String field_175315_a = "Customize World Presets";
/*     */   private String field_175313_s;
/*     */   private String field_175312_t;
/*     */   
/*     */   public GuiScreenCustomizePresets(GuiCustomizeWorldScreen p_i45524_1_) {
/*  26 */     this.field_175314_r = p_i45524_1_;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  30 */     this.buttonList.clear();
/*  31 */     Keyboard.enableRepeatEvents(true);
/*  32 */     this.field_175315_a = I18n.format("createWorld.customize.custom.presets.title", new Object[0]);
/*  33 */     this.field_175313_s = I18n.format("createWorld.customize.presets.share", new Object[0]);
/*  34 */     this.field_175312_t = I18n.format("createWorld.customize.presets.list", new Object[0]);
/*  35 */     this.field_175317_i = new GuiTextField(2, this.fontRendererObj, 50, 40, width - 100, 20);
/*  36 */     this.field_175311_g = new ListPreset();
/*  37 */     this.field_175317_i.setMaxStringLength(2000);
/*  38 */     this.field_175317_i.setText(this.field_175314_r.func_175323_a());
/*  39 */     this.buttonList.add(this.field_175316_h = new GuiButton(0, width / 2 - 102, height - 27, 100, 20, I18n.format("createWorld.customize.presets.select", new Object[0])));
/*  40 */     this.buttonList.add(new GuiButton(1, width / 2 + 3, height - 27, 100, 20, I18n.format("gui.cancel", new Object[0])));
/*  41 */     func_175304_a();
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  45 */     super.handleMouseInput();
/*  46 */     this.field_175311_g.handleMouseInput();
/*     */   }
/*     */   
/*     */   public void onGuiClosed() {
/*  50 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  54 */     this.field_175317_i.mouseClicked(mouseX, mouseY, mouseButton);
/*  55 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  59 */     if (!this.field_175317_i.textboxKeyTyped(typedChar, keyCode)) {
/*  60 */       super.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  65 */     switch (button.id) {
/*     */     case 0: 
/*  67 */       this.field_175314_r.func_175324_a(this.field_175317_i.getText());
/*  68 */       this.mc.displayGuiScreen(this.field_175314_r);
/*  69 */       break;
/*     */     case 1: 
/*  71 */       this.mc.displayGuiScreen(this.field_175314_r);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  76 */     drawDefaultBackground();
/*  77 */     this.field_175311_g.drawScreen(mouseX, mouseY, partialTicks);
/*  78 */     drawCenteredString(this.fontRendererObj, this.field_175315_a, width / 2, 8, 16777215);
/*  79 */     drawString(this.fontRendererObj, this.field_175313_s, 50, 30, 10526880);
/*  80 */     drawString(this.fontRendererObj, this.field_175312_t, 50, 70, 10526880);
/*  81 */     this.field_175317_i.drawTextBox();
/*  82 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public void updateScreen() {
/*  86 */     this.field_175317_i.updateCursorCounter();
/*  87 */     super.updateScreen();
/*     */   }
/*     */   
/*     */   public void func_175304_a() {
/*  91 */     this.field_175316_h.enabled = func_175305_g();
/*     */   }
/*     */   
/*     */   private boolean func_175305_g() {
/*  95 */     return ((this.field_175311_g.field_178053_u > -1) && (this.field_175311_g.field_178053_u < field_175310_f.size())) || (this.field_175317_i.getText().length() > 1);
/*     */   }
/*     */   
/*     */   static {
/*  99 */     ChunkProviderSettings.Factory chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory("{ \"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":8.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":0.5, \"biomeScaleWeight\":2.0, \"biomeScaleOffset\":0.375, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":255 }");
/* 100 */     ResourceLocation resourcelocation = new ResourceLocation("textures/gui/presets/water.png");
/* 101 */     field_175310_f.add(new Info(I18n.format("createWorld.customize.custom.preset.waterWorld", new Object[0]), resourcelocation, chunkprovidersettings$factory));
/* 102 */     chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory("{\"coordinateScale\":3000.0, \"heightScale\":6000.0, \"upperLimitScale\":250.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
/* 103 */     resourcelocation = new ResourceLocation("textures/gui/presets/isles.png");
/* 104 */     field_175310_f.add(new Info(I18n.format("createWorld.customize.custom.preset.isleLand", new Object[0]), resourcelocation, chunkprovidersettings$factory));
/* 105 */     chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":5.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":1.0, \"biomeScaleWeight\":4.0, \"biomeScaleOffset\":1.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
/* 106 */     resourcelocation = new ResourceLocation("textures/gui/presets/delight.png");
/* 107 */     field_175310_f.add(new Info(I18n.format("createWorld.customize.custom.preset.caveDelight", new Object[0]), resourcelocation, chunkprovidersettings$factory));
/* 108 */     chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory("{\"coordinateScale\":738.41864, \"heightScale\":157.69133, \"upperLimitScale\":801.4267, \"lowerLimitScale\":1254.1643, \"depthNoiseScaleX\":374.93652, \"depthNoiseScaleZ\":288.65228, \"depthNoiseScaleExponent\":1.2092624, \"mainNoiseScaleX\":1355.9908, \"mainNoiseScaleY\":745.5343, \"mainNoiseScaleZ\":1183.464, \"baseSize\":1.8758626, \"stretchY\":1.7137525, \"biomeDepthWeight\":1.7553768, \"biomeDepthOffset\":3.4701107, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":2.535211, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
/* 109 */     resourcelocation = new ResourceLocation("textures/gui/presets/madness.png");
/* 110 */     field_175310_f.add(new Info(I18n.format("createWorld.customize.custom.preset.mountains", new Object[0]), resourcelocation, chunkprovidersettings$factory));
/* 111 */     chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":1000.0, \"mainNoiseScaleY\":3000.0, \"mainNoiseScaleZ\":1000.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":20 }");
/* 112 */     resourcelocation = new ResourceLocation("textures/gui/presets/drought.png");
/* 113 */     field_175310_f.add(new Info(I18n.format("createWorld.customize.custom.preset.drought", new Object[0]), resourcelocation, chunkprovidersettings$factory));
/* 114 */     chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":2.0, \"lowerLimitScale\":64.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":6 }");
/* 115 */     resourcelocation = new ResourceLocation("textures/gui/presets/chaos.png");
/* 116 */     field_175310_f.add(new Info(I18n.format("createWorld.customize.custom.preset.caveChaos", new Object[0]), resourcelocation, chunkprovidersettings$factory));
/* 117 */     chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":true, \"seaLevel\":40 }");
/* 118 */     resourcelocation = new ResourceLocation("textures/gui/presets/luck.png");
/* 119 */     field_175310_f.add(new Info(I18n.format("createWorld.customize.custom.preset.goodLuck", new Object[0]), resourcelocation, chunkprovidersettings$factory));
/*     */   }
/*     */   
/*     */   static class Info {
/*     */     public String field_178955_a;
/*     */     public ResourceLocation field_178953_b;
/*     */     public ChunkProviderSettings.Factory field_178954_c;
/*     */     
/*     */     public Info(String p_i45523_1_, ResourceLocation p_i45523_2_, ChunkProviderSettings.Factory p_i45523_3_) {
/* 128 */       this.field_178955_a = p_i45523_1_;
/* 129 */       this.field_178953_b = p_i45523_2_;
/* 130 */       this.field_178954_c = p_i45523_3_;
/*     */     }
/*     */   }
/*     */   
/*     */   class ListPreset extends GuiSlot {
/* 135 */     public int field_178053_u = -1;
/*     */     
/*     */     public ListPreset() {
/* 138 */       super(GuiScreenCustomizePresets.width, GuiScreenCustomizePresets.height, 80, GuiScreenCustomizePresets.height - 32, 38);
/*     */     }
/*     */     
/*     */     protected int getSize() {
/* 142 */       return GuiScreenCustomizePresets.field_175310_f.size();
/*     */     }
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/* 146 */       this.field_178053_u = slotIndex;
/* 147 */       GuiScreenCustomizePresets.this.func_175304_a();
/* 148 */       GuiScreenCustomizePresets.this.field_175317_i.setText(((GuiScreenCustomizePresets.Info)GuiScreenCustomizePresets.field_175310_f.get(GuiScreenCustomizePresets.this.field_175311_g.field_178053_u)).field_178954_c.toString());
/*     */     }
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 152 */       return slotIndex == this.field_178053_u;
/*     */     }
/*     */     
/*     */     protected void drawBackground() {}
/*     */     
/*     */     private void func_178051_a(int p_178051_1_, int p_178051_2_, ResourceLocation p_178051_3_)
/*     */     {
/* 159 */       int i = p_178051_1_ + 5;
/* 160 */       GuiScreenCustomizePresets.this.drawHorizontalLine(i - 1, i + 32, p_178051_2_ - 1, -2039584);
/* 161 */       GuiScreenCustomizePresets.this.drawHorizontalLine(i - 1, i + 32, p_178051_2_ + 32, -6250336);
/* 162 */       GuiScreenCustomizePresets.this.drawVerticalLine(i - 1, p_178051_2_ - 1, p_178051_2_ + 32, -2039584);
/* 163 */       GuiScreenCustomizePresets.this.drawVerticalLine(i + 32, p_178051_2_ - 1, p_178051_2_ + 32, -6250336);
/* 164 */       net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 165 */       this.mc.getTextureManager().bindTexture(p_178051_3_);
/* 166 */       int j = 32;
/* 167 */       int k = 32;
/* 168 */       Tessellator tessellator = Tessellator.getInstance();
/* 169 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 170 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 171 */       worldrenderer.pos(i + 0, p_178051_2_ + 32, 0.0D).tex(0.0D, 1.0D).endVertex();
/* 172 */       worldrenderer.pos(i + 32, p_178051_2_ + 32, 0.0D).tex(1.0D, 1.0D).endVertex();
/* 173 */       worldrenderer.pos(i + 32, p_178051_2_ + 0, 0.0D).tex(1.0D, 0.0D).endVertex();
/* 174 */       worldrenderer.pos(i + 0, p_178051_2_ + 0, 0.0D).tex(0.0D, 0.0D).endVertex();
/* 175 */       tessellator.draw();
/*     */     }
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 179 */       GuiScreenCustomizePresets.Info guiscreencustomizepresets$info = (GuiScreenCustomizePresets.Info)GuiScreenCustomizePresets.field_175310_f.get(entryID);
/* 180 */       func_178051_a(p_180791_2_, p_180791_3_, guiscreencustomizepresets$info.field_178953_b);
/* 181 */       GuiScreenCustomizePresets.this.fontRendererObj.drawString(guiscreencustomizepresets$info.field_178955_a, p_180791_2_ + 32 + 10, p_180791_3_ + 14, 16777215);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreenCustomizePresets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */