/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.client.resources.ResourcePackListEntry;
/*     */ import net.minecraft.client.resources.ResourcePackListEntryFound;
/*     */ import net.minecraft.client.resources.ResourcePackRepository;
/*     */ import net.minecraft.client.resources.ResourcePackRepository.Entry;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.Util.EnumOS;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GuiScreenResourcePacks extends GuiScreen
/*     */ {
/*  20 */   private static final Logger logger = ;
/*     */   private final GuiScreen parentScreen;
/*     */   private List<ResourcePackListEntry> availableResourcePacks;
/*     */   private List<ResourcePackListEntry> selectedResourcePacks;
/*     */   private GuiResourcePackAvailable availableResourcePacksList;
/*     */   private GuiResourcePackSelected selectedResourcePacksList;
/*  26 */   private boolean changed = false;
/*     */   
/*     */   public GuiScreenResourcePacks(GuiScreen parentScreenIn) {
/*  29 */     this.parentScreen = parentScreenIn;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  33 */     this.buttonList.add(new GuiOptionButton(2, width / 2 - 154, height - 48, I18n.format("resourcePack.openFolder", new Object[0])));
/*  34 */     this.buttonList.add(new GuiOptionButton(1, width / 2 + 4, height - 48, I18n.format("gui.done", new Object[0])));
/*  35 */     if (!this.changed) {
/*  36 */       this.availableResourcePacks = Lists.newArrayList();
/*  37 */       this.selectedResourcePacks = Lists.newArrayList();
/*  38 */       ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
/*  39 */       resourcepackrepository.updateRepositoryEntriesAll();
/*  40 */       List<ResourcePackRepository.Entry> list = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
/*  41 */       list.removeAll(resourcepackrepository.getRepositoryEntries());
/*     */       
/*  43 */       for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
/*  44 */         this.availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
/*     */       }
/*     */       
/*  47 */       for (ResourcePackRepository.Entry resourcepackrepository$entry1 : Lists.reverse(resourcepackrepository.getRepositoryEntries())) {
/*  48 */         this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
/*     */       }
/*     */       
/*  51 */       this.selectedResourcePacks.add(new net.minecraft.client.resources.ResourcePackListEntryDefault(this));
/*     */     }
/*     */     
/*  54 */     this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, height, this.availableResourcePacks);
/*  55 */     this.availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
/*  56 */     this.availableResourcePacksList.registerScrollButtons(7, 8);
/*  57 */     this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, height, this.selectedResourcePacks);
/*  58 */     this.selectedResourcePacksList.setSlotXBoundsFromLeft(width / 2 + 4);
/*  59 */     this.selectedResourcePacksList.registerScrollButtons(7, 8);
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  63 */     super.handleMouseInput();
/*  64 */     this.selectedResourcePacksList.handleMouseInput();
/*  65 */     this.availableResourcePacksList.handleMouseInput();
/*     */   }
/*     */   
/*     */   public boolean hasResourcePackEntry(ResourcePackListEntry p_146961_1_) {
/*  69 */     return this.selectedResourcePacks.contains(p_146961_1_);
/*     */   }
/*     */   
/*     */   public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry p_146962_1_) {
/*  73 */     return hasResourcePackEntry(p_146962_1_) ? this.selectedResourcePacks : this.availableResourcePacks;
/*     */   }
/*     */   
/*     */   public List<ResourcePackListEntry> getAvailableResourcePacks() {
/*  77 */     return this.availableResourcePacks;
/*     */   }
/*     */   
/*     */   public List<ResourcePackListEntry> getSelectedResourcePacks() {
/*  81 */     return this.selectedResourcePacks;
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  85 */     if (button.enabled) { boolean flag;
/*  86 */       if (button.id == 2) {
/*  87 */         File file1 = this.mc.getResourcePackRepository().getDirResourcepacks();
/*  88 */         String s = file1.getAbsolutePath();
/*  89 */         if (Util.getOSType() == Util.EnumOS.OSX) {
/*     */           try {
/*  91 */             logger.info(s);
/*  92 */             Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
/*  93 */             return;
/*     */           } catch (IOException ioexception1) {
/*  95 */             logger.error("Couldn't open file", ioexception1);
/*     */           }
/*  97 */         } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
/*  98 */           String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { s });
/*     */           try
/*     */           {
/* 101 */             Runtime.getRuntime().exec(s1);
/* 102 */             return;
/*     */           } catch (IOException ioexception) {
/* 104 */             logger.error("Couldn't open file", ioexception);
/*     */           }
/*     */         }
/*     */         
/* 108 */         flag = false;
/*     */         try
/*     */         {
/* 111 */           Class<?> oclass = Class.forName("java.awt.Desktop");
/* 112 */           Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 113 */           oclass.getMethod("browse", new Class[] { java.net.URI.class }).invoke(object, new Object[] { file1.toURI() });
/*     */         } catch (Throwable throwable) {
/* 115 */           logger.error("Couldn't open link", throwable);
/* 116 */           flag = true;
/*     */         }
/*     */         
/* 119 */         if (flag) {
/* 120 */           logger.info("Opening via system class!");
/* 121 */           org.lwjgl.Sys.openURL("file://" + s);
/*     */         }
/* 123 */       } else if (button.id == 1) {
/* 124 */         if (this.changed) {
/* 125 */           List<ResourcePackRepository.Entry> list = Lists.newArrayList();
/*     */           
/* 127 */           for (ResourcePackListEntry resourcepacklistentry : this.selectedResourcePacks) {
/* 128 */             if ((resourcepacklistentry instanceof ResourcePackListEntryFound)) {
/* 129 */               list.add(((ResourcePackListEntryFound)resourcepacklistentry).func_148318_i());
/*     */             }
/*     */           }
/*     */           
/* 133 */           java.util.Collections.reverse(list);
/* 134 */           this.mc.getResourcePackRepository().setRepositories(list);
/* 135 */           this.mc.gameSettings.resourcePacks.clear();
/* 136 */           this.mc.gameSettings.field_183018_l.clear();
/*     */           
/* 138 */           for (ResourcePackRepository.Entry resourcepackrepository$entry : list) {
/* 139 */             this.mc.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());
/* 140 */             if (resourcepackrepository$entry.func_183027_f() != 1) {
/* 141 */               this.mc.gameSettings.field_183018_l.add(resourcepackrepository$entry.getResourcePackName());
/*     */             }
/*     */           }
/*     */           
/* 145 */           this.mc.gameSettings.saveOptions();
/* 146 */           this.mc.refreshResources();
/*     */         }
/*     */         
/* 149 */         this.mc.displayGuiScreen(this.parentScreen);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 155 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 156 */     this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
/* 157 */     this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 161 */     super.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 165 */     drawBackground(0);
/* 166 */     this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
/* 167 */     this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
/* 168 */     drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title", new Object[0]), width / 2, 16, 16777215);
/* 169 */     drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo", new Object[0]), width / 2 - 77, height - 26, 8421504);
/* 170 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public void markChanged() {
/* 174 */     this.changed = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreenResourcePacks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */