/*     */ package me.slowly.client.irc.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import me.slowly.client.Client;
/*     */ import me.slowly.client.util.ClientUtil;
/*     */ import me.slowly.client.util.Colors;
/*     */ import me.slowly.client.util.FlatColors;
/*     */ import me.slowly.client.util.RenderUtil;
/*     */ import me.slowly.client.util.TimeHelper;
/*     */ import me.slowly.client.util.fontmanager.FontManager;
/*     */ import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
/*     */ import me.slowly.client.util.handler.MouseInputHandler;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ 
/*     */ public class UICheckBox
/*     */ {
/*     */   private boolean enabled;
/*     */   private String name;
/*     */   private String desc;
/*  26 */   private String fileDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/" + "Slowly";
/*  27 */   private static ArrayList<UICheckBox> list = new ArrayList();
/*     */   private MouseInputHandler handler;
/*     */   private double animation;
/*     */   private int size;
/*  31 */   private TimeHelper timer = new TimeHelper();
/*     */   
/*     */   public UICheckBox(String name, String desc) {
/*  34 */     this.enabled = true;
/*  35 */     this.name = name;
/*  36 */     this.desc = desc;
/*  37 */     this.handler = new MouseInputHandler(0);
/*  38 */     list.add(this);
/*     */     try {
/*  40 */       load();
/*     */     } catch (IOException e) {
/*  42 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public UICheckBox(String name, String desc, boolean defaultState) {
/*  47 */     this.enabled = defaultState;
/*  48 */     this.name = name;
/*  49 */     this.desc = desc;
/*  50 */     this.handler = new MouseInputHandler(0);
/*  51 */     list.add(this);
/*     */     try {
/*  53 */       load();
/*     */     } catch (IOException e) {
/*  55 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void draw(int mouseX, int mouseY, int x, int y) {
/*  60 */     this.size = 10;
/*  61 */     this.animation = RenderUtil.getAnimationState(this.animation, this.enabled ? 1 : 0, this.enabled ? 5.0D - this.animation * 5.0D : 5.0D);
/*  62 */     Gui.drawRect(x, y, x + this.size, y + this.size, Colors.DARKGREY.c);
/*  63 */     boolean hover = (mouseX >= x) && (mouseX <= x + this.size) && (mouseY >= y) && (mouseY <= y + this.size);
/*  64 */     if (hover)
/*  65 */       Gui.drawRect(x, y, x + this.size, y + this.size, ClientUtil.reAlpha(FlatColors.WHITE.c, 0.05F));
/*  66 */     if ((this.handler.canExcecute()) && (hover)) {
/*  67 */       setEnabled(!isEnabled());
/*  68 */       save();
/*     */     }
/*     */     
/*  71 */     Gui.drawLine(getPosition(x + 1), getPosition(y + 1), getPositionReverse(x + this.size - 1), getPositionReverse(y + this.size - 1), new Color(FlatColors.BLUE.c));
/*     */     
/*  73 */     Gui.drawLine(getPositionReverse(x + this.size - 1), getPosition(y + 1), getPosition(x + 1), getPositionReverse(y + this.size - 1), new Color(FlatColors.BLUE.c));
/*     */     
/*  75 */     UnicodeFontRenderer font = Client.getInstance().getFontManager().consolas13;
/*  76 */     boolean hover2 = (mouseX >= x + this.size + 2) && (mouseX <= x + this.size + font.getStringWidth(this.name) + 2) && (mouseY >= y) && (mouseY <= y + this.size);
/*     */     
/*  78 */     font.drawString(this.name, x + this.size + 2, y + (this.size - font.FONT_HEIGHT) / 2.0F, -1);
/*     */     
/*  80 */     if (hover2) {
/*  81 */       if (this.timer.isDelayComplete(1000L)) {
/*  82 */         mouseY -= 6;
/*  83 */         RenderUtil.drawRoundedRect(mouseX - 2, mouseY - 1, mouseX + font.getStringWidth(this.desc) + 2, mouseY + font.FONT_HEIGHT + 1, 1.0F, Colors.BLACK.c);
/*  84 */         font.drawString(this.desc, mouseX, mouseY, -1);
/*     */       }
/*     */     } else {
/*  87 */       this.timer.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private float getPosition(int corner)
/*     */   {
/*  93 */     return (float)(corner + this.size / 2.0F - this.size / 2.0F * this.animation);
/*     */   }
/*     */   
/*     */   private float getPositionReverse(int corner) {
/*  97 */     return (float)(corner - this.size / 2.0F + this.size / 2.0F * this.animation);
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 101 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 105 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/* 109 */     return this.desc;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 113 */     return this.name;
/*     */   }
/*     */   
/*     */   public void save() {
/* 117 */     File f = new File(this.fileDir + "/uiCheckboxes.txt");
/*     */     try {
/* 119 */       if (!f.exists())
/* 120 */         f.createNewFile();
/* 121 */       PrintWriter pw = new PrintWriter(f);
/* 122 */       for (UICheckBox checkBox : list) {
/* 123 */         pw.print(checkBox.getName() + ":" + checkBox.getDesc() + ":" + checkBox.isEnabled() + "\n");
/*     */       }
/* 125 */       pw.close();
/*     */     } catch (Exception e) {
/* 127 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void load() throws IOException {
/* 132 */     File f = new File(this.fileDir + "/uiCheckboxes.txt");
/* 133 */     if (!f.exists()) {
/* 134 */       f.createNewFile();
/*     */     }
/*     */     else {
/* 137 */       @SuppressWarnings("resource")
BufferedReader br = new BufferedReader(new java.io.FileReader(f));
/*     */       String line;
/* 139 */       while ((line = br.readLine()) != null) { String line1 = null;
/* 140 */         @SuppressWarnings("null")
String[] split = line1.split(":");
/* 141 */         if ((getName().equals(split[0])) && (getDesc().equals(split[1]))) {
/* 142 */           setEnabled(Boolean.valueOf(split[2]).booleanValue());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


