package me.slowly.client.ui.altmanager;

import com.google.common.base.Predicate;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import me.slowly.client.Client;
import me.slowly.client.ui.buttons.UIFlatButton;
import me.slowly.client.ui.textfield.UITextField;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAltManager extends GuiScreen {
   private final GuiScreen parentScreen;
   private UIFlatButton loginButton;
   private UIFlatButton addAltButton;
   private UIFlatButton editAltButton;
   private UIFlatButton deleteAltButton;
   private UIFlatButton importAltsButton;
   private UIFlatButton cancelButton;
   private int scroll;
   float opacity = 0.0F;
   private float sliderY = 0.0F;
   private int sliderY2;
   private float sliderOpacity;
   private boolean clickedSlider;
   private boolean dragSlider;
   private final JFileChooser fc = new JFileChooser();
   private ArrayList particles;
   private final int MAX_PARTICLES = 5000;
   private Random random = new Random();
   public static TimeHelper timer = new TimeHelper();
   private boolean clicked;
   private UITextField usernameField;
   private UITextField passwordField;
   private UITextField usernamePasswordField;
   private Predicate field_181032_r = new Predicate<String>() {
      public boolean apply(String p_apply_1_) {
         if (p_apply_1_.length() == 0) {
            return true;
         } else {
            String[] astring = p_apply_1_.split(":");
            if (astring.length == 0) {
               return true;
            } else {
               try {
                  String s = IDN.toASCII(astring[0]);
                  return true;
               } catch (IllegalArgumentException var4) {
                  return false;
               }
            }
         }
      }
   };
   public static ArrayList toDelete = new ArrayList();

   public GuiAltManager(GuiScreen p_i1033_1_) {
      this.parentScreen = p_i1033_1_;
   }

   public void updateScreen() {
      this.usernameField.updateCursorCounter();
      this.passwordField.updateCursorCounter();
      this.usernamePasswordField.updateCursorCounter();
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      AltManager.loadAltsFromFile();
      this.scroll = 0;
      int c = -15698006;
      this.particles = new ArrayList();
      this.particles.clear();
      this.buttonList.clear();
      this.buttonList.add(this.addAltButton = new UIFlatButton(1, this.width / 2 + 220, 52, 65, 15, "Add Alt", FlatColors.ASPHALT.c));
      this.buttonList.add(this.cancelButton = new UIFlatButton(4, 10, this.height - 20, 65, 15, "Back", FlatColors.ASPHALT.c));
      this.buttonList.add(this.importAltsButton = new UIFlatButton(5, 80, this.height - 20, 105, 15, "Import", FlatColors.ASPHALT.c));
      this.usernameField = new UITextField(0, this.fontRendererObj, this.width / 2 - 190, 48, 200, 20);
      this.usernameField.setFocused(true);
      this.passwordField = new UITextField(1, this.fontRendererObj, this.width / 2 + 15, 48, 200, 20);
      this.passwordField.setMaxStringLength(128);
      this.passwordField.func_175205_a(this.field_181032_r);
      this.usernamePasswordField = new UITextField(2, this.fontRendererObj, this.width / 2 - 190, 18, 405, 20);
      this.usernamePasswordField.setMaxStringLength(128);
      this.usernamePasswordField.func_175205_a(this.field_181032_r);
      ((GuiButton)this.buttonList.get(0)).enabled = this.usernameField.getText().length() > 0 && this.passwordField.getText().length() > 0 || this.usernamePasswordField.getText().split(":").length >= 2;
      this.sliderOpacity = 0.5F;
      this.opacity = 0.0F;
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.id == 1) {
         if (!this.usernameField.getText().isEmpty() && !this.passwordField.getText().isEmpty()) {
            AltManager.guiSlotList.add(new GuiAltSlot(this.usernameField.getText(), this.passwordField.getText()));
         } else if (!this.usernamePasswordField.getText().isEmpty()) {
            String[] account = this.usernamePasswordField.getText().split(":");
            AltManager.guiSlotList.add(new GuiAltSlot(account[0], account[1]));
         }

         this.usernameField.setText("");
         this.usernamePasswordField.setText("");
         this.passwordField.setText("");
         AltManager.saveAltsToFile();
      }

      if (button.id == 4) {
         AltManager.saveAltsToFile();
         this.mc.displayGuiScreen(this.parentScreen);
      }

      if (button.id == 5) {
         Runnable run = () -> {
            this.importAlts();
         };
         (new Thread(run)).start();
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.usernameField.textboxKeyTyped(typedChar, keyCode);
      this.passwordField.textboxKeyTyped(typedChar, keyCode);
      this.usernamePasswordField.textboxKeyTyped(typedChar, keyCode);
      if (keyCode == 15) {
         this.usernameField.setFocused(!this.usernameField.isFocused());
         this.passwordField.setFocused(!this.passwordField.isFocused());
         this.usernamePasswordField.setFocused(!this.passwordField.isFocused());
      }

      if (keyCode == 28 || keyCode == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      ((GuiButton)this.buttonList.get(0)).enabled = this.usernameField.getText().length() > 0 && this.passwordField.getText().length() > 0 || this.usernamePasswordField.getText().split(":").length >= 2;
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
      this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
      this.usernamePasswordField.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      toDelete.clear();
      UnicodeFontRenderer fr = Client.getInstance().getFontManager().VERDANA40;
      ScaledResolution res = new ScaledResolution(this.mc);
      if (Keyboard.isKeyDown(1)) {
         this.mc.displayGuiScreen(this.parentScreen);
      }

      boolean topHeight = true;
      int darkGray = -15658735;
      int lightGray = -15066598;
      int red = -1023904;
      if (this.opacity < 1.0F) {
         this.opacity += 0.1F;
      }

      if (this.opacity > 1.0F) {
         this.opacity = 1.0F;
      }

      Gui.drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), FlatColors.WHITE.c);
      Gui.drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), ClientUtil.reAlpha(Colors.BLACK.c, 0.2F));
      int y = 0;
      Client.getInstance().getFontManager().simpleton40.drawString("AltManager", 10.0F, 21.0F, ClientUtil.reAlpha(Colors.DARKGREY.c, this.opacity));
      UnicodeFontRenderer infoFont = Client.getInstance().getFontManager().robotobold15;
      boolean premium = this.mc.session.getProfile().isComplete();
      String strPremium = premium ? "Premium" : "Cracked";
      infoFont.drawString("Username:", 20.0F, 40.0F, ClientUtil.reAlpha(Colors.DARKGREY.c, this.opacity));
      infoFont.drawString(this.mc.session.getUsername() + " (" + strPremium + ")", 60.0F, 40.5F, ClientUtil.reAlpha(Colors.GREY.c, this.opacity));
      infoFont.drawString("Alts:", 20.0F, 50.0F, ClientUtil.reAlpha(Colors.DARKGREY.c, this.opacity));
      infoFont.drawString(String.valueOf(AltManager.guiSlotList.size()), 40.0F, 50.5F, ClientUtil.reAlpha(Colors.GREY.c, this.opacity));
      int MIN_HEIGHT = 75;
      int MAX_HEIGHT = res.getScaledHeight() - 35;
      float percent = (this.sliderY - (float)MIN_HEIGHT) / (float)(MAX_HEIGHT - MIN_HEIGHT);
      float scrollAmount = (float)(-Mouse.getDWheel()) * 0.07F;
      if (scrollAmount > 0.0F) {
         if (this.sliderY + scrollAmount < (float)MAX_HEIGHT) {
            this.sliderY += scrollAmount;
         } else {
            this.sliderY = (float)MAX_HEIGHT;
         }
      } else if (scrollAmount < 0.0F) {
         if (this.sliderY - scrollAmount > (float)MIN_HEIGHT) {
            this.sliderY += scrollAmount;
         } else {
            this.sliderY = (float)MIN_HEIGHT;
         }
      }

      int all = 0;

      for(Iterator var20 = AltManager.guiSlotList.iterator(); var20.hasNext(); all += 25) {
         GuiAltSlot slot = (GuiAltSlot)var20.next();
      }

      int slotY = -((int)((float)all * percent - (float)(75 + y)));
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor(0, 75, res.getScaledWidth(), res.getScaledHeight() - 110);

      Iterator var21;
      GuiAltSlot slot;
      for(var21 = AltManager.guiSlotList.iterator(); var21.hasNext(); slotY += 25) {
         slot = (GuiAltSlot)var21.next();
         slot.y = slotY;
         slot.opacity = this.opacity;
         slot.WIDTH = res.getScaledWidth() - 95;
         slot.MIN_HEIGHT = 50;
         slot.MAX_HEIGHT = res.getScaledHeight() - 10;
         slot.drawScreen(mouseX, mouseY);
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
      var21 = toDelete.iterator();

      while(var21.hasNext()) {
         slot = (GuiAltSlot)var21.next();
         delte(slot.getUsername(), slot.getPassword());
      }

      UnicodeFontRenderer textBoxFont = Client.getInstance().getFontManager().simpleton10;
      textBoxFont.drawString("Email:Password", (float)(this.width / 2 - 190), 12.0F, Colors.DARKGREY.c);
      textBoxFont.drawString("Email", (float)(this.width / 2 - 190), 42.0F, Colors.DARKGREY.c);
      textBoxFont.drawString("Password", (float)(this.width / 2 + 15), 42.0F, Colors.DARKGREY.c);
      this.usernameField.drawTextBox();
      this.passwordField.drawTextBox();
      this.usernamePasswordField.drawTextBox();
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.drawSlider(mouseX, mouseY);
   }

   public static void delte(String username, String password) {
      for(int i = 0; i < AltManager.guiSlotList.size(); ++i) {
         GuiAltSlot slot = (GuiAltSlot)AltManager.guiSlotList.get(i);
         if (slot.getUsername().equalsIgnoreCase(username) && slot.getPassword().equalsIgnoreCase(password)) {
            AltManager.guiSlotList.remove(i);
         }
      }

      AltManager.saveAltsToFile();
   }

   private void drawSlider(int mouseX, int mouseY) {
      ScaledResolution res = new ScaledResolution(this.mc);
      boolean MIN_HEIGHT = true;
      int MAX_HEIGHT = res.getScaledHeight() - 35;
      int WIDTH = res.getScaledWidth() - 150;
      int radius = 5;
      if (!AltManager.guiSlotList.isEmpty()) {
         AltManager.guiSlotList.get(0);
      }

      int allAltsHeight = AltManager.guiSlotList.size() * (AltManager.guiSlotList.size() == 0 ? 0 : 25);
      float height;
      if (allAltsHeight <= MAX_HEIGHT - 75) {
         height = (float)(MAX_HEIGHT - 75);
      } else {
         height = (float)(MAX_HEIGHT - 75) / (float)(allAltsHeight + 12) * (float)(MAX_HEIGHT - 75);
      }

      if (height > (float)(MAX_HEIGHT - 75)) {
         height = (float)(MAX_HEIGHT - 75);
      }

      int x = res.getScaledWidth() - radius - 2;
      int y = (int)this.sliderY;
      int x2 = x + radius;
      int y2 = (int)(this.sliderY + height - (float)radius);
      Gui.drawRect(x, 75, x2 + 2, MAX_HEIGHT, ClientUtil.reAlpha(Colors.GREY.c, 0.15F));
      boolean yAdd = height < 2.0F;
      boolean hover = mouseX >= x && mouseX <= x2 && mouseY >= y - (yAdd ? 2 : 0) && mouseY <= y2 + (yAdd ? 2 : 0);
      int color = !hover && !this.clickedSlider ? FlatColors.GREY.c : FlatColors.DARK_GREY.c;
      if (Mouse.isButtonDown(0)) {
         if (!this.clickedSlider && hover) {
            this.clickedSlider = true;
            this.sliderY2 = (int)((float)mouseY - this.sliderY);
         }
      } else {
         this.clickedSlider = false;
      }

      if (this.clickedSlider) {
         this.sliderY = (float)(mouseY - this.sliderY2);
      }

      if (this.sliderY + height > (float)MAX_HEIGHT) {
         this.sliderY = (float)MAX_HEIGHT - height;
      }

      if (this.sliderY < 75.0F) {
         this.sliderY = 75.0F;
      }

      Gui.drawRect(x + 1, 1 + (int)this.sliderY - (yAdd ? 2 : 0), x2 + 1, (int)(this.sliderY + height - (float)radius) + (yAdd ? 2 : 0), ClientUtil.reAlpha(color, this.opacity));
   }

   private void scroll(boolean canScroll) {
      int scroll_ = Mouse.getDWheel() / 12;
      if (canScroll && scroll_ > 0) {
         this.scroll -= scroll_;
      }

      if (this.scroll < 0) {
         this.scroll = 0;
      }

   }

   private void importAlts() {
      File fromFile = null;
      this.fc.setFileFilter(new FileNameExtensionFilter("Text Files", new String[]{"txt"}));
      int returnVal = this.fc.showOpenDialog((Component)null);
      this.fc.requestFocus();
      if (returnVal == 0) {
         fromFile = this.fc.getSelectedFile();
         ArrayList altsToImport = new ArrayList();

         try {
            @SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fromFile));

            String line;
            while((line = bufferedReader.readLine()) != null) {
               String[] alt = line.split(":");
               if (alt.length > 0) {
                  altsToImport.add(line);
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         try {
            FileWriter fWriter = new FileWriter(AltManager.altFile, true);
            PrintWriter writer = new PrintWriter(fWriter);
            Iterator var7 = altsToImport.iterator();

            while(var7.hasNext()) {
               String s = (String)var7.next();
               writer.write(s + "\n");
            }

            writer.close();
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         this.mc.displayGuiScreen(this);
      }

   }

   private String getStatus() {
      return this.mc.session == null ? "Cracked as " : "Logged in as " + this.mc.session.getUsername();
   }
}
