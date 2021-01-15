/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.AnvilConverterException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.world.storage.ISaveFormat;
/*     */ import net.minecraft.world.storage.ISaveHandler;
/*     */ import net.minecraft.world.storage.SaveFormatComparator;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback
/*     */ {
/*  22 */   private static final Logger logger = ;
/*  23 */   private final DateFormat field_146633_h = new SimpleDateFormat();
/*     */   protected GuiScreen parentScreen;
/*  25 */   protected String field_146628_f = "Select world";
/*     */   private boolean field_146634_i;
/*     */   private int field_146640_r;
/*     */   private List<SaveFormatComparator> field_146639_s;
/*     */   private List field_146638_t;
/*     */   private String field_146637_u;
/*     */   private String field_146636_v;
/*  32 */   private String[] field_146635_w = new String[4];
/*     */   private boolean field_146643_x;
/*     */   private GuiButton deleteButton;
/*     */   private GuiButton selectButton;
/*     */   private GuiButton renameButton;
/*     */   private GuiButton recreateButton;
/*     */   
/*     */   public GuiSelectWorld(GuiScreen parentScreenIn) {
/*  40 */     this.parentScreen = parentScreenIn;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  44 */     this.field_146628_f = I18n.format("selectWorld.title", new Object[0]);
/*     */     try
/*     */     {
/*  47 */       func_146627_h();
/*     */     } catch (AnvilConverterException anvilconverterexception) {
/*  49 */       logger.error("Couldn't load level list", anvilconverterexception);
/*  50 */       this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", anvilconverterexception.getMessage()));
/*  51 */       return;
/*     */     }
/*     */     
/*  54 */     this.field_146637_u = I18n.format("selectWorld.world", new Object[0]);
/*  55 */     this.field_146636_v = I18n.format("selectWorld.conversion", new Object[0]);
/*  56 */     this.field_146635_w[net.minecraft.world.WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival", new Object[0]);
/*  57 */     this.field_146635_w[net.minecraft.world.WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative", new Object[0]);
/*  58 */     this.field_146635_w[net.minecraft.world.WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure", new Object[0]);
/*  59 */     this.field_146635_w[net.minecraft.world.WorldSettings.GameType.SPECTATOR.getID()] = I18n.format("gameMode.spectator", new Object[0]);
/*  60 */     this.field_146638_t = new List(this.mc);
/*  61 */     this.field_146638_t.registerScrollButtons(4, 5);
/*  62 */     func_146618_g();
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  66 */     super.handleMouseInput();
/*  67 */     this.field_146638_t.handleMouseInput();
/*     */   }
/*     */   
/*     */   private void func_146627_h() throws AnvilConverterException {
/*  71 */     ISaveFormat isaveformat = this.mc.getSaveLoader();
/*  72 */     this.field_146639_s = isaveformat.getSaveList();
/*  73 */     java.util.Collections.sort(this.field_146639_s);
/*  74 */     this.field_146640_r = -1;
/*     */   }
/*     */   
/*     */   protected String func_146621_a(int p_146621_1_) {
/*  78 */     return ((SaveFormatComparator)this.field_146639_s.get(p_146621_1_)).getFileName();
/*     */   }
/*     */   
/*     */   protected String func_146614_d(int p_146614_1_) {
/*  82 */     String s = ((SaveFormatComparator)this.field_146639_s.get(p_146614_1_)).getDisplayName();
/*  83 */     if (StringUtils.isEmpty(s)) {
/*  84 */       s = I18n.format("selectWorld.world", new Object[0]) + " " + (p_146614_1_ + 1);
/*     */     }
/*     */     
/*  87 */     return s;
/*     */   }
/*     */   
/*     */   public void func_146618_g() {
/*  91 */     this.buttonList.add(this.selectButton = new GuiButton(1, width / 2 - 154, height - 52, 150, 20, I18n.format("selectWorld.select", new Object[0])));
/*  92 */     this.buttonList.add(new GuiButton(3, width / 2 + 4, height - 52, 150, 20, I18n.format("selectWorld.create", new Object[0])));
/*  93 */     this.buttonList.add(this.renameButton = new GuiButton(6, width / 2 - 154, height - 28, 72, 20, I18n.format("selectWorld.rename", new Object[0])));
/*  94 */     this.buttonList.add(this.deleteButton = new GuiButton(2, width / 2 - 76, height - 28, 72, 20, I18n.format("selectWorld.delete", new Object[0])));
/*  95 */     this.buttonList.add(this.recreateButton = new GuiButton(7, width / 2 + 4, height - 28, 72, 20, I18n.format("selectWorld.recreate", new Object[0])));
/*  96 */     this.buttonList.add(new GuiButton(0, width / 2 + 82, height - 28, 72, 20, I18n.format("gui.cancel", new Object[0])));
/*  97 */     this.selectButton.enabled = false;
/*  98 */     this.deleteButton.enabled = false;
/*  99 */     this.renameButton.enabled = false;
/* 100 */     this.recreateButton.enabled = false;
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/* 104 */     if (button.enabled) {
/* 105 */       if (button.id == 2) {
/* 106 */         String s = func_146614_d(this.field_146640_r);
/* 107 */         if (s != null) {
/* 108 */           this.field_146643_x = true;
/* 109 */           GuiYesNo guiyesno = func_152129_a(this, s, this.field_146640_r);
/* 110 */           this.mc.displayGuiScreen(guiyesno);
/*     */         }
/* 112 */       } else if (button.id == 1) {
/* 113 */         func_146615_e(this.field_146640_r);
/* 114 */       } else if (button.id == 3) {
/* 115 */         this.mc.displayGuiScreen(new GuiCreateWorld(this));
/* 116 */       } else if (button.id == 6) {
/* 117 */         this.mc.displayGuiScreen(new GuiRenameWorld(this, func_146621_a(this.field_146640_r)));
/* 118 */       } else if (button.id == 0) {
/* 119 */         this.mc.displayGuiScreen(this.parentScreen);
/* 120 */       } else if (button.id == 7) {
/* 121 */         GuiCreateWorld guicreateworld = new GuiCreateWorld(this);
/* 122 */         ISaveHandler isavehandler = this.mc.getSaveLoader().getSaveLoader(func_146621_a(this.field_146640_r), false);
/* 123 */         WorldInfo worldinfo = isavehandler.loadWorldInfo();
/* 124 */         isavehandler.flush();
/* 125 */         guicreateworld.func_146318_a(worldinfo);
/* 126 */         this.mc.displayGuiScreen(guicreateworld);
/*     */       } else {
/* 128 */         this.field_146638_t.actionPerformed(button);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_146615_e(int p_146615_1_) {
/* 134 */     this.mc.displayGuiScreen(null);
/* 135 */     if (!this.field_146634_i) {
/* 136 */       this.field_146634_i = true;
/* 137 */       String s = func_146621_a(p_146615_1_);
/* 138 */       if (s == null) {
/* 139 */         s = "World" + p_146615_1_;
/*     */       }
/*     */       
/* 142 */       String s1 = func_146614_d(p_146615_1_);
/* 143 */       if (s1 == null) {
/* 144 */         s1 = "World" + p_146615_1_;
/*     */       }
/*     */       
/* 147 */       if (this.mc.getSaveLoader().canLoadWorld(s)) {
/* 148 */         this.mc.launchIntegratedServer(s, s1, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void confirmClicked(boolean result, int id) {
/* 154 */     if (this.field_146643_x) {
/* 155 */       this.field_146643_x = false;
/* 156 */       if (result) {
/* 157 */         ISaveFormat isaveformat = this.mc.getSaveLoader();
/* 158 */         isaveformat.flushCache();
/* 159 */         isaveformat.deleteWorldDirectory(func_146621_a(id));
/*     */         try
/*     */         {
/* 162 */           func_146627_h();
/*     */         } catch (AnvilConverterException anvilconverterexception) {
/* 164 */           logger.error("Couldn't load level list", anvilconverterexception);
/*     */         }
/*     */       }
/*     */       
/* 168 */       this.mc.displayGuiScreen(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 173 */     this.field_146638_t.drawScreen(mouseX, mouseY, partialTicks);
/* 174 */     drawCenteredString(this.fontRendererObj, this.field_146628_f, width / 2, 20, 16777215);
/* 175 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public static GuiYesNo func_152129_a(GuiYesNoCallback p_152129_0_, String p_152129_1_, int p_152129_2_) {
/* 179 */     String s = I18n.format("selectWorld.deleteQuestion", new Object[0]);
/* 180 */     String s1 = "'" + p_152129_1_ + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]);
/* 181 */     String s2 = I18n.format("selectWorld.deleteButton", new Object[0]);
/* 182 */     String s3 = I18n.format("gui.cancel", new Object[0]);
/* 183 */     GuiYesNo guiyesno = new GuiYesNo(p_152129_0_, s, s1, s2, s3, p_152129_2_);
/* 184 */     return guiyesno;
/*     */   }
/*     */   
/*     */   class List extends GuiSlot {
/*     */     public List(Minecraft mcIn) {
/* 189 */       super(GuiSelectWorld.width, GuiSelectWorld.height, 32, GuiSelectWorld.height - 64, 36);
/*     */     }
/*     */     
/*     */     protected int getSize() {
/* 193 */       return GuiSelectWorld.this.field_146639_s.size();
/*     */     }
/*     */     
/*     */     protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/* 197 */       GuiSelectWorld.this.field_146640_r = slotIndex;
/* 198 */       boolean flag = (GuiSelectWorld.this.field_146640_r >= 0) && (GuiSelectWorld.this.field_146640_r < getSize());
/* 199 */       GuiSelectWorld.this.selectButton.enabled = flag;
/* 200 */       GuiSelectWorld.this.deleteButton.enabled = flag;
/* 201 */       GuiSelectWorld.this.renameButton.enabled = flag;
/* 202 */       GuiSelectWorld.this.recreateButton.enabled = flag;
/* 203 */       if ((isDoubleClick) && (flag)) {
/* 204 */         GuiSelectWorld.this.func_146615_e(slotIndex);
/*     */       }
/*     */     }
/*     */     
/*     */     protected boolean isSelected(int slotIndex) {
/* 209 */       return slotIndex == GuiSelectWorld.this.field_146640_r;
/*     */     }
/*     */     
/*     */     protected int getContentHeight() {
/* 213 */       return GuiSelectWorld.this.field_146639_s.size() * 36;
/*     */     }
/*     */     
/*     */     protected void drawBackground() {
/* 217 */       GuiSelectWorld.this.drawDefaultBackground();
/*     */     }
/*     */     
/*     */     protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 221 */       SaveFormatComparator saveformatcomparator = (SaveFormatComparator)GuiSelectWorld.this.field_146639_s.get(entryID);
/* 222 */       String s = saveformatcomparator.getDisplayName();
/* 223 */       if (StringUtils.isEmpty(s)) {
/* 224 */         s = GuiSelectWorld.this.field_146637_u + " " + (entryID + 1);
/*     */       }
/*     */       
/* 227 */       String s1 = saveformatcomparator.getFileName();
/* 228 */       s1 = s1 + " (" + GuiSelectWorld.this.field_146633_h.format(new Date(saveformatcomparator.getLastTimePlayed()));
/* 229 */       s1 = s1 + ")";
/* 230 */       String s2 = "";
/* 231 */       if (saveformatcomparator.requiresConversion()) {
/* 232 */         s2 = GuiSelectWorld.this.field_146636_v + " " + s2;
/*     */       } else {
/* 234 */         s2 = GuiSelectWorld.this.field_146635_w[saveformatcomparator.getEnumGameType().getID()];
/* 235 */         if (saveformatcomparator.isHardcoreModeEnabled()) {
/* 236 */           s2 = EnumChatFormatting.DARK_RED + I18n.format("gameMode.hardcore", new Object[0]) + EnumChatFormatting.RESET;
/*     */         }
/*     */         
/* 239 */         if (saveformatcomparator.getCheatsEnabled()) {
/* 240 */           s2 = s2 + ", " + I18n.format("selectWorld.cheats", new Object[0]);
/*     */         }
/*     */       }
/*     */       
/* 244 */       GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s, p_180791_2_ + 2, p_180791_3_ + 1, 16777215);
/* 245 */       GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s1, p_180791_2_ + 2, p_180791_3_ + 12, 8421504);
/* 246 */       GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, s2, p_180791_2_ + 2, p_180791_3_ + 12 + 10, 8421504);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiSelectWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */