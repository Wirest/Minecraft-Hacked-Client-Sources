package saint.screens;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.utilities.Alt;
import saint.utilities.AltLoginThread;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class GuiAltManager extends GuiScreen {
   private GuiButton login;
   private GuiButton remove;
   private GuiButton rename;
   private AltLoginThread loginThread;
   private int offset;
   private float rectWidth;
   public Alt selectedAlt = null;
   private String status = "§7Waiting...";

   public GuiAltManager() {
      Saint.getAltManager().getContentList().clear();
      Saint.getFileManager().getFileUsingName("alts").loadFile();
   }

   public void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         if (this.loginThread == null) {
            this.mc.displayGuiScreen((GuiScreen)null);
         } else if (!this.loginThread.getStatus().equals("§eLogging in...") && !this.loginThread.getStatus().equals("§cDo not hit back! §eLogging in...")) {
            this.mc.displayGuiScreen((GuiScreen)null);
         } else {
            this.loginThread.setStatus("§cDo not hit back! §eLogging in...");
         }
         break;
      case 1:
         String user = this.selectedAlt.getUsername();
         String pass = this.selectedAlt.getPassword();
         this.loginThread = new AltLoginThread(user, pass);
         this.loginThread.start();
         break;
      case 2:
         if (this.loginThread != null) {
            this.loginThread = null;
         }

         Saint.getAltManager().getContentList().remove(this.selectedAlt);
         this.status = "§aRemoved.";
         this.selectedAlt = null;
         Saint.getFileManager().getFileUsingName("alts").saveFile();
         break;
      case 3:
         this.mc.displayGuiScreen(new GuiAddAlt(this));
         break;
      case 4:
         this.mc.displayGuiScreen(new GuiAltLogin(this));
         break;
      case 5:
         Alt randomAlt = (Alt)Saint.getAltManager().getContentList().get((new Random()).nextInt(Saint.getAltManager().getContentList().size()));
         String user1 = randomAlt.getUsername();
         String pass1 = randomAlt.getPassword();
         this.loginThread = new AltLoginThread(user1, pass1);
         this.loginThread.start();
         break;
      case 6:
         this.mc.displayGuiScreen(new GuiRenameAlt(this));
         break;
      case 7:
         Alt lastAlt = Saint.getAltManager().getLastAlt();
         if (lastAlt == null) {
            if (this.loginThread == null) {
               this.status = "§cThere is no last used alt!";
            } else {
               this.loginThread.setStatus("§cThere is no last used alt!");
            }
         } else {
            String user2 = lastAlt.getUsername();
            String pass2 = lastAlt.getPassword();
            this.loginThread = new AltLoginThread(user2, pass2);
            this.loginThread.start();
         }
      }

   }

   public void drawScreen(int par1, int par2, float par3) {
      if (Mouse.hasWheel()) {
         int wheel = Mouse.getDWheel();
         if (wheel < 0) {
            this.offset += 26;
            if (this.offset < 0) {
               this.offset = 0;
            }
         } else if (wheel > 0) {
            this.offset -= 26;
            if (this.offset < 0) {
               this.offset = 0;
            }
         }
      }

      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/saintbg.png"));
      Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), (float)scaledRes.getScaledWidth(), (float)scaledRes.getScaledHeight());
      RenderHelper.drawRect(8.0F, 7.0F, 12.0F + RenderHelper.getNahrFont().getStringWidth(this.mc.session.getUsername()), 20.0F, Integer.MIN_VALUE);
      RenderHelper.getNahrFont().drawString("Account Manager - " + Saint.getAltManager().getContentList().size() + " alts", (float)(this.width / 2) - RenderHelper.getNahrFont().getStringWidth("Account Manager - " + Saint.getAltManager().getContentList().size() + " alts") / 2.0F, 3.0F, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      RenderHelper.getNahrFont().drawString(this.loginThread == null ? this.status : this.loginThread.getStatus(), (float)(this.width / 2) - RenderHelper.getNahrFont().getStringWidth(this.loginThread == null ? this.status : this.loginThread.getStatus()) / 2.0F + 10.0F, 15.0F, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      if (!Saint.getAltManager().getContentList().isEmpty()) {
         RenderHelper.drawRect((float)(this.width / 2) - this.rectWidth / 2.0F - 6.0F, 33.0F, (float)(this.width / 2) + this.rectWidth / 2.0F + 6.0F, (float)(this.height - 50), Integer.MIN_VALUE);
      }

      this.rectWidth = 0.0F;
      RenderHelper.getNahrFont().drawString(this.mc.session.getUsername(), 10.0F, 5.0F, NahrFont.FontType.SHADOW_THIN, -7829368, -16777216);
      GL11.glPushMatrix();
      this.prepareScissorBox(0.0F, 33.0F, (float)this.width, (float)(this.height - 50));
      GL11.glEnable(3089);
      int y = 38;
      Iterator var7 = Saint.getAltManager().getContentList().iterator();

      while(true) {
         Alt alt;
         do {
            if (!var7.hasNext()) {
               GL11.glDisable(3089);
               GL11.glPopMatrix();
               super.drawScreen(par1, par2, par3);
               if (this.selectedAlt == null) {
                  this.login.enabled = false;
                  this.remove.enabled = false;
                  this.rename.enabled = false;
               } else {
                  this.login.enabled = true;
                  this.remove.enabled = true;
                  this.rename.enabled = true;
               }

               if (Keyboard.isKeyDown(200)) {
                  this.offset -= 26;
                  if (this.offset < 0) {
                     this.offset = 0;
                  }
               } else if (Keyboard.isKeyDown(208)) {
                  this.offset += 26;
                  if (this.offset < 0) {
                     this.offset = 0;
                  }
               }

               return;
            }

            alt = (Alt)var7.next();
         } while(!this.isAltInArea(y));

         String name;
         if (alt.getMask().equals("")) {
            name = alt.getUsername();
         } else {
            name = alt.getMask();
         }

         String pass;
         if (alt.getPassword().equals("")) {
            pass = "§cCracked";
         } else {
            pass = alt.getPassword().replaceAll(".", "*");
         }

         float textWidth = RenderHelper.getNahrFont().getStringWidth(name) + 4.0F;
         if (alt == this.selectedAlt && !Saint.getAltManager().getContentList().isEmpty()) {
            if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
               RenderHelper.drawBorderedRect((float)(this.width / 2) - textWidth / 2.0F, (float)(y - this.offset - 4), (float)(this.width / 2) + textWidth / 2.0F, (float)(y - this.offset + 20), 1.0F, -16777216, -2142943931);
            } else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
               RenderHelper.drawBorderedRect((float)(this.width / 2) - textWidth / 2.0F, (float)(y - this.offset - 4), (float)(this.width / 2) + textWidth / 2.0F, (float)(y - this.offset + 20), 1.0F, -16777216, -2142088622);
            } else {
               RenderHelper.drawBorderedRect((float)(this.width / 2) - textWidth / 2.0F, (float)(y - this.offset - 4), (float)(this.width / 2) + textWidth / 2.0F, (float)(y - this.offset + 20), 1.0F, -16777216, -2144259791);
            }
         } else if (this.isMouseOverAlt(par1, par2, y - this.offset) && !Saint.getAltManager().getContentList().isEmpty() && Mouse.isButtonDown(0)) {
            RenderHelper.drawBorderedRect((float)(this.width / 2) - textWidth / 2.0F, (float)(y - this.offset - 4), (float)(this.width / 2) + textWidth / 2.0F, (float)(y - this.offset + 20), 1.0F, -16777216, -2146101995);
         } else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
            RenderHelper.drawBorderedRect((float)(this.width / 2) - textWidth / 2.0F, (float)(y - this.offset - 4), (float)(this.width / 2) + textWidth / 2.0F, (float)(y - this.offset + 20), 1.0F, -16777216, -2145180893);
         }

         float rectWidth = RenderHelper.getNahrFont().getStringWidth(name);
         RenderHelper.getNahrFont().drawString(name, (float)(this.width / 2) - RenderHelper.getNahrFont().getStringWidth(name) / 2.0F, (float)(y - this.offset - 4), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
         RenderHelper.getNahrFont().drawString(pass, (float)(this.width / 2) - RenderHelper.getNahrFont().getStringWidth(pass) / 2.0F, (float)(y - this.offset - 2 + 10), NahrFont.FontType.SHADOW_THIN, -9868951, -16777216);
         if (this.rectWidth < rectWidth) {
            this.rectWidth = rectWidth;
         }

         y += 26;
      }
   }

   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 24, 75, 20, "Cancel"));
      this.buttonList.add(this.login = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login"));
      this.buttonList.add(this.remove = new GuiButton(2, this.width / 2 - 74, this.height - 24, 70, 20, "Remove"));
      this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add"));
      this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
      this.buttonList.add(new GuiButton(5, this.width / 2 - 154, this.height - 24, 70, 20, "Random"));
      this.buttonList.add(this.rename = new GuiButton(6, this.width / 2 + 4, this.height - 24, 70, 20, "Rename"));
      this.buttonList.add(new GuiButton(7, 1, this.height - 24, 50, 20, "Last Alt"));
      this.login.enabled = false;
      this.remove.enabled = false;
      this.rename.enabled = false;
   }

   private boolean isAltInArea(int y) {
      return y - this.offset <= this.height - 50;
   }

   private boolean isMouseOverAlt(int x, int y, int y1) {
      return (float)x >= (float)(this.width / 2) - this.rectWidth / 2.0F - 6.0F && y >= y1 - 4 && (float)x <= (float)(this.width / 2) + this.rectWidth / 2.0F + 6.0F && y <= y1 + 20 && x >= 0 && y >= 33 && x <= this.width && y <= this.height - 50;
   }

   protected void mouseClicked(int par1, int par2, int par3) {
      if (this.offset < 0) {
         this.offset = 0;
      }

      int y = 38 - this.offset;

      for(Iterator var6 = Saint.getAltManager().getContentList().iterator(); var6.hasNext(); y += 26) {
         Alt alt = (Alt)var6.next();
         if (this.isMouseOverAlt(par1, par2, y)) {
            if (alt == this.selectedAlt) {
               this.actionPerformed((GuiButton)this.buttonList.get(1));
               return;
            }

            this.selectedAlt = alt;
         }
      }

      try {
         super.mouseClicked(par1, par2, par3);
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public void prepareScissorBox(float x, float y, float x2, float y2) {
      ScaledResolution scale = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      int factor = scale.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }
}
