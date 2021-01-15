/*     */ package rip.jutting.polaris.ui.altmanager;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.ui.login.GuiAltLogin;
/*     */ import rip.jutting.polaris.utils.FileUtils;
/*     */ import rip.jutting.polaris.utils.RenderUtils.R2DUtils;
/*     */ 
/*     */ public class GuiAltManager
/*     */   extends GuiScreen
/*     */ {
/*     */   private GuiButton login;
/*     */   private GuiButton remove;
/*     */   private GuiButton rename;
/*     */   private AltLoginThread loginThread;
/*     */   private int offset;
/*     */   public Alt selectedAlt;
/*     */   private String status;
/*     */   protected float oof;
/*     */   private static File ALTS_DIR;
/*     */   
/*     */   public GuiAltManager()
/*     */   {
/*  44 */     ALTS_DIR = FileUtils.getConfigFile("Alts");
/*  45 */     this.selectedAlt = null;
/*  46 */     this.status = (EnumChatFormatting.GRAY + "Waiting..");
/*     */   }
/*     */   
/*     */   public void actionPerformed(GuiButton button) throws IOException {
/*  50 */     switch (button.id) {
/*     */     case 0: 
/*  52 */       if (this.loginThread == null) {
/*  53 */         this.mc.displayGuiScreen(null);
/*     */       }
/*     */       else {
/*  56 */         if (!this.loginThread.getStatus().equals(EnumChatFormatting.YELLOW + "Logging in..."))
/*     */         {
/*  58 */           if (!this.loginThread.getStatus().equals(EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.YELLOW + " Logging in...")) {
/*  59 */             this.mc.displayGuiScreen(null);
/*  60 */             break;
/*     */           } }
/*  62 */         this.loginThread.setStatus(
/*  63 */           EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.YELLOW + " Logging in..."); }
/*  64 */       break;
/*     */     
/*     */     case 1: 
/*  67 */       String user = this.selectedAlt.getUsername();
/*  68 */       String pass = this.selectedAlt.getPassword();
/*  69 */       (this.loginThread = new AltLoginThread(user, pass)).start();
/*  70 */       break;
/*     */     
/*     */     case 2: 
/*  73 */       if (this.loginThread != null) {
/*  74 */         this.loginThread = null;
/*     */       }
/*  76 */       AltManager.registry.remove(this.selectedAlt);
/*  77 */       this.status = "§aRemoved.";
/*  78 */       save();
/*  79 */       this.selectedAlt = null;
/*  80 */       break;
/*     */     
/*     */     case 3: 
/*  83 */       this.mc.displayGuiScreen(new GuiAddAlt(this));
/*  84 */       break;
/*     */     
/*     */     case 4: 
/*  87 */       this.mc.displayGuiScreen(new GuiAltLogin(this));
/*  88 */       break;
/*     */     
/*     */     case 5: 
/*  91 */       ArrayList<Alt> registry = AltManager.registry;
/*  92 */       Random random = new Random();
/*  93 */       Alt randomAlt = (Alt)registry.get(random.nextInt(AltManager.registry.size()));
/*  94 */       String user2 = randomAlt.getUsername();
/*  95 */       String pass2 = randomAlt.getPassword();
/*  96 */       (this.loginThread = new AltLoginThread(user2, pass2)).start();
/*  97 */       break;
/*     */     
/*     */     case 6: 
/* 100 */       this.mc.displayGuiScreen(new GuiRenameAlt(this));
/* 101 */       break;
/*     */     
/*     */     case 7: 
/* 104 */       Alt lastAlt = AltManager.lastAlt;
/* 105 */       if (lastAlt != null) {
/* 106 */         String user3 = lastAlt.getUsername();
/* 107 */         String pass3 = lastAlt.getPassword();
/* 108 */         (this.loginThread = new AltLoginThread(user3, pass3)).start();
/*     */ 
/*     */       }
/* 111 */       else if (this.loginThread == null) {
/* 112 */         this.status = "?cThere is no last used alt!";
/*     */       }
/*     */       else {
/* 115 */         this.loginThread.setStatus("?cThere is no last used alt!");
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawScreen(int par1, int par2, float par3)
/*     */   {
/* 123 */     CFontRenderer font = FontLoaders.vardana12;
/* 124 */     if (Mouse.hasWheel()) {
/* 125 */       int wheel = Mouse.getDWheel();
/* 126 */       if (wheel < 0) {
/* 127 */         this.offset += 26;
/* 128 */         if (this.offset < 0) {
/* 129 */           this.offset = 0;
/*     */         }
/* 131 */       } else if (wheel > 0) {
/* 132 */         this.offset -= 26;
/* 133 */         if (this.offset < 0) {
/* 134 */           this.offset = 0;
/*     */         }
/*     */       }
/*     */     }
/* 138 */     drawCoolBackground();
/* 139 */     FontRenderer fontRendererObj = this.fontRendererObj;
/* 140 */     font.drawCenteredString("Alt Manger", width / 2, 10.0F, -1);
/* 141 */     font.drawCenteredString(this.loginThread == null ? this.status : this.loginThread.getStatus(), 
/* 142 */       width / 2, 20.0F, -1);
/* 143 */     RenderUtils.R2DUtils.drawBorderedRect(100.0F, 33.0F, width - 100, height - 50, 1.0F, 
/* 144 */       new Color(0, 0, 0, 30).getRGB(), new Color(0, 0, 0, 50).getRGB());
/* 145 */     GL11.glPushMatrix();
/* 146 */     prepareScissorBox(0.0F, 33.0F, width, height - 50);
/* 147 */     GL11.glEnable(3089);
/* 148 */     int y = 38;
/* 149 */     for (Alt alt : AltManager.registry) {
/* 150 */       if (isAltInArea(y)) { String name;
/*     */         String name;
/* 152 */         if (alt.getMask().equals("")) {
/* 153 */           name = alt.getUsername();
/*     */         } else
/* 155 */           name = alt.getMask();
/*     */         String pass;
/*     */         String pass;
/* 158 */         if (alt.getPassword().equals("")) {
/* 159 */           pass = "Offline";
/*     */         } else {
/* 161 */           pass = alt.getPassword().replaceAll(".", "*");
/*     */         }
/* 163 */         if (alt == this.selectedAlt) {
/* 164 */           if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
/* 165 */             RenderUtils.R2DUtils.drawBorderedRect(101.0F, y - this.offset - 4, width - 101.0F, 
/* 166 */               y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 167 */               new Color(0, 0, 0, 60).getRGB());
/* 168 */           } else if (isMouseOverAlt(par1, par2, y - this.offset)) {
/* 169 */             RenderUtils.R2DUtils.drawBorderedRect(101.0F, y - this.offset - 4, width - 101.0F, 
/* 170 */               y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 171 */               new Color(0, 0, 0, 60).getRGB());
/*     */           } else {
/* 173 */             RenderUtils.R2DUtils.drawBorderedRect(101.0F, y - this.offset - 4, width - 102, 
/* 174 */               y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 175 */               new Color(0, 0, 0, 60).getRGB());
/*     */           }
/* 177 */         } else if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
/* 178 */           RenderUtils.R2DUtils.drawBorderedRect(101.0F, y - this.offset - 4, width - 101.0F, 
/* 179 */             y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 180 */             new Color(0, 0, 0, 60).getRGB());
/* 181 */         } else if (isMouseOverAlt(par1, par2, y - this.offset)) {
/* 182 */           RenderUtils.R2DUtils.drawBorderedRect(101.0F, y - this.offset - 4, width - 101.0F, 
/* 183 */             y - this.offset + 20, 1.0F, new Color(0, 0, 0, 40).getRGB(), 
/* 184 */             new Color(0, 0, 0, 60).getRGB());
/*     */         }
/*     */         
/* 187 */         font.drawCenteredString(name, width / 2, y - this.offset, -1);
/* 188 */         font.drawCenteredString(pass, width / 2, y - this.offset + 10, 5592405);
/* 189 */         y += 26;
/*     */       }
/*     */     }
/* 192 */     GL11.glDisable(3089);
/* 193 */     GL11.glPopMatrix();
/* 194 */     super.drawScreen(par1, par2, par3);
/* 195 */     if (this.selectedAlt == null) {
/* 196 */       this.login.enabled = false;
/* 197 */       this.remove.enabled = false;
/* 198 */       this.rename.enabled = false;
/*     */     } else {
/* 200 */       this.login.enabled = true;
/* 201 */       this.remove.enabled = true;
/* 202 */       this.rename.enabled = true;
/*     */     }
/* 204 */     if (Keyboard.isKeyDown(200)) {
/* 205 */       this.offset -= 26;
/* 206 */       if (this.offset < 0) {
/* 207 */         this.offset = 0;
/*     */       }
/* 209 */     } else if (Keyboard.isKeyDown(208)) {
/* 210 */       this.offset += 26;
/* 211 */       if (this.offset < 0) {
/* 212 */         this.offset = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void initGui()
/*     */   {
/* 220 */     this.buttonList.add(new GuiButton(0, width / 2 + 4 + 50, height - 24, 100, 20, "Back"));
/* 221 */     this.buttonList.add(this.login = new GuiButton(1, width / 2 - 154, height - 48, 100, 
/* 222 */       20, "Login"));
/* 223 */     this.buttonList.add(this.remove = new GuiButton(2, width / 2 - 154, height - 24, 
/* 224 */       100, 20, "Remove"));
/* 225 */     this.buttonList
/* 226 */       .add(new GuiButton(3, width / 2 + 4 + 50, height - 48, 100, 20, "Add"));
/* 227 */     this.buttonList.add(
/* 228 */       new GuiButton(4, width / 2 - 50, height - 48, 100, 20, "Direct Login"));
/* 229 */     this.buttonList.add(this.rename = new GuiButton(6, width / 2 - 50, height - 24, 100, 
/* 230 */       20, "Edit"));
/* 231 */     this.login.enabled = false;
/* 232 */     this.remove.enabled = false;
/* 233 */     this.rename.enabled = false;
/*     */   }
/*     */   
/*     */   private boolean isAltInArea(int y) {
/* 237 */     return y - this.offset <= height - 50;
/*     */   }
/*     */   
/*     */   private boolean isMouseOverAlt(int x, int y, int y1) {
/* 241 */     return (x >= 102) && (y >= y1 - 4) && (x <= width - 52) && (y <= y1 + 20) && (x >= 0) && (y >= 33) && 
/* 242 */       (x <= width) && (y <= height - 50);
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int par1, int par2, int par3) throws IOException
/*     */   {
/* 247 */     if (this.offset < 0) {
/* 248 */       this.offset = 0;
/*     */     }
/* 250 */     int y = 38 - this.offset;
/* 251 */     for (Alt alt : AltManager.registry) {
/* 252 */       if (isMouseOverAlt(par1, par2, y)) {
/* 253 */         if (alt == this.selectedAlt) {
/* 254 */           actionPerformed((GuiButton)this.buttonList.get(1));
/* 255 */           return;
/*     */         }
/* 257 */         this.selectedAlt = alt;
/*     */       }
/* 259 */       y += 26;
/*     */     }
/*     */     try {
/* 262 */       super.mouseClicked(par1, par2, par3);
/*     */     } catch (IOException e) {
/* 264 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void prepareScissorBox(float x, float y, float x2, float y2) {
/* 269 */     ScaledResolution scale = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/* 270 */     int factor = scale.getScaleFactor();
/* 271 */     GL11.glScissor((int)(x * factor), (int)((ScaledResolution.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), 
/* 272 */       (int)((y2 - y) * factor));
/*     */   }
/*     */   
/*     */   public void load() throws IOException {
/* 276 */     BufferedReader xd = new BufferedReader(new FileReader(ALTS_DIR));
/*     */     String line;
/* 278 */     while ((line = xd.readLine()) != null) { String line;
/* 279 */       String[] fuq = line.split(":");
/* 280 */       for (int i = 0; i < 2; i++) {
/* 281 */         fuq[i].replace(" ", "");
/*     */       }
/* 283 */       if (fuq.length > 2) {
/* 284 */         AltManager.registry.add(new Alt(fuq[0], fuq[1], fuq[2]));
/*     */       } else {
/* 286 */         AltManager.registry.add(new Alt(fuq[0], fuq[1]));
/*     */       }
/*     */     }
/* 289 */     xd.close();
/* 290 */     System.out.println("Loaded alts!");
/*     */   }
/*     */   
/*     */   public void save() throws IOException {
/* 294 */     PrintWriter alts = new PrintWriter(new FileWriter(ALTS_DIR));
/* 295 */     for (Alt alt : AltManager.registry) {
/* 296 */       if (alt.getMask().equals("")) {
/* 297 */         alts.println(String.valueOf(alt.getUsername()) + ":" + alt.getPassword());
/*     */       } else {
/* 299 */         alts.println(String.valueOf(alt.getUsername()) + ":" + alt.getPassword() + ":" + alt.getMask());
/*     */       }
/*     */     }
/* 302 */     alts.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\altmanager\GuiAltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */