package nivia.gui.altmanager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.files.Alts;
import nivia.gui.customscreens.CustomScreenUtils;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils.R2DUtils;

import java.io.IOException;

public class GuiAltManager extends GuiScreen
{
  private GuiButton login;
  private GuiButton remove;
  private GuiButton rename;
  private AltLoginThread loginThread;
  private int offset;
  public Alt selectedAlt = null;
  private String status = EnumChatFormatting.GRAY + "Idle...";

  public void actionPerformed(GuiButton button)
  {
    switch (button.id)
    {
      case 0:
        if (loginThread == null)
          mc.displayGuiScreen(null);
        else if ((!loginThread.getStatus().equals(EnumChatFormatting.AQUA + "Logging in...")) &&
                (!loginThread.getStatus().equals(EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.AQUA + " Logging in...")))
        {
          mc.displayGuiScreen(null);
        }
        else
        {
          loginThread.setStatus(EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.AQUA + " Logging in...");
        }
        break;
      case 1:
        String user = selectedAlt.getUsername();
        String pass = selectedAlt.getPassword();
        loginThread = new AltLoginThread(user, pass);
        loginThread.start();
        break;
      case 2:
        if (loginThread != null) {
          loginThread = null;
        }
        Pandora.getAltManager().registry.remove(selectedAlt);
        status = "\247aRemoved.";
        try { Pandora.getFileManager().getFile(Alts.class).saveFile(); } catch(Exception e){}
        selectedAlt = null;
        break;
      case 3:
        mc.displayGuiScreen(new GuiAddAlt(this));
        break;
      case 4:
        mc.displayGuiScreen(new GuiAltLogin(this));
        break;
      case 5:
        Alt randomAlt = (Alt) Pandora.getAltManager().registry.get(new java.util.Random()
                .nextInt(Pandora.getAltManager().registry.size()));
        String user1 = randomAlt.getUsername();
        String pass1 = randomAlt.getPassword();
        loginThread = new AltLoginThread(user1, pass1);
        loginThread.start();
        break;
      case 6:
        mc.displayGuiScreen(new GuiRenameAlt(this));
        break;
      case 7:
        Alt lastAlt = Pandora.getAltManager().lastAlt;
        if (lastAlt == null)
        {
          if (loginThread == null) {
            status = "?cThere is no last used alt!";
          } else {
            loginThread.setStatus("?cThere is no last used alt!");
          }
        }
        else
        {
          String user2 = lastAlt.getUsername();
          String pass2 = lastAlt.getPassword();
          loginThread = new AltLoginThread(user2, pass2);
          loginThread.start();
        }
        break;
      case 8:
        AltManager.registry.clear();
        try { Pandora.getFileManager().getFile(Alts.class).loadFile(); } catch(IOException e){e.printStackTrace();}
        status = "\247bReloaded!";
        break;
    }
  }

  public void drawScreen(int par1, int par2, float par3)
  {

    if (Mouse.hasWheel())
    {
      int wheel = Mouse.getDWheel();
      if (wheel < 0)
      {
        offset += 26;
        if (offset < 0) {
          offset = 0;
        }
      }
      else if (wheel > 0)
      {
        offset -= 26;
        if (offset < 0) {
          offset = 0;
        }
      }
    }
    CustomScreenUtils.drawBackground(CustomScreenUtils.alt);
    this.drawString(fontRendererObj, mc.session.getUsername(), 10, 10, 0xDDDDDD);
    this.drawCenteredString(fontRendererObj, "Account Manager - " + Pandora.getAltManager().registry.size() + " alts", width / 2, 10, -1);
    this.drawCenteredString(fontRendererObj, loginThread == null ? status : loginThread.getStatus(), width / 2, 20, -1);
    R2DUtils.drawBorderedRect(50.0F, 33.0F, width - 50, height - 50, 1.0F, Helper.colorUtils().RGBtoHEX(25, 25, 25, 255), Helper.colorUtils().RGBtoHEX(5, 5, 5, 255));
    GL11.glPushMatrix();
    this.prepareScissorBox(0.0F, 33.0F, width, height - 50);
    GL11.glEnable(3089);
    int y = 38;

    for (Alt alt : Pandora.getAltManager().registry) {
      if (isAltInArea(y)) {
        String name;
        if (alt.getMask().equals("")) {
          name = alt.getUsername();
        } else
          name = alt.getMask();
        String pass;
        if (alt.getPassword().equals("")) {
          pass = "\247cCracked";
        } else {
          pass = alt.getPassword().replaceAll(".", "*");
        }
        if (alt == selectedAlt)
        {
          if ((isMouseOverAlt(par1, par2, y - offset)) && (Mouse.isButtonDown(0))) {
            R2DUtils.drawBorderedRect(52.0F, y - offset - 4, width - 52, y - offset + 20, 1.0F, Helper.colorUtils().RGBtoHEX(45, 45, 45, 255), -2142943931);
          } else if (isMouseOverAlt(par1, par2, y - offset)) {
            R2DUtils.drawBorderedRect(52.0F, y - offset - 4, width - 52, y - offset + 20, 1.0F, Helper.colorUtils().RGBtoHEX(45, 45, 45, 255), -2142088622);
          } else {
            R2DUtils.drawBorderedRect(52.0F, y - offset - 4, width - 52, y - offset + 20, 1.0F, Helper.colorUtils().RGBtoHEX(45, 45, 45, 255), -2144259791);
          }
        }
        else if ((isMouseOverAlt(par1, par2, y - offset)) && (Mouse.isButtonDown(0))) {
          R2DUtils.drawBorderedRect(52.0F, y - offset - 4, width - 52, y - offset + 20, 1.0F, -Helper.colorUtils().RGBtoHEX(45, 45, 45, 255), -2146101995);
        } else if (isMouseOverAlt(par1, par2, y - offset)) {
          R2DUtils.drawBorderedRect(52.0F, y - offset - 4, width - 52, y - offset + 20, 1.0F, Helper.colorUtils().RGBtoHEX(45, 45, 45, 255), -2145180893);
        }
        drawCenteredString(fontRendererObj, name, width / 2, y - offset, -1);
        drawCenteredString(fontRendererObj, pass, width / 2, y - offset + 10, 5592405);
        if(!alt.getMask().isEmpty())
          Helper.get2DUtils().drawCustomImage(55F , y - offset - 1, 17, 17, alt.getHead());
        y += 26;
      }
    }

    GL11.glDisable(3089);
    GL11.glPopMatrix();
    super.drawScreen(par1, par2, par3);
    if (selectedAlt == null)
    {
      login.enabled = false;
      remove.enabled = false;
      rename.enabled = false;
    }
    else
    {
      login.enabled = true;
      remove.enabled = true;
      rename.enabled = true;
    }
    if (Keyboard.isKeyDown(200))
    {
      offset -= 26;
      if (offset < 0) {
        offset = 0;
      }
    }
    else if (Keyboard.isKeyDown(208))
    {
      offset += 26;
      if (offset < 0) {
        offset = 0;
      }
    }
  }

  public void initGui()
  {
    buttonList.add(new GuiButton(0, width / 2 + 116, height - 24, 75, 20, "Cancel"));
    buttonList.add(this.login = new GuiButton(1, width / 2 - 122, height - 48, 100, 20, "Login"));
    buttonList.add(this.remove = new GuiButton(2, width / 2 - 40, height - 24, 70, 20, "Remove"));
    buttonList.add(new GuiButton(3, width / 2 + 4 + 86, height - 48, 100, 20, "Add"));
    buttonList.add(new GuiButton(4, width / 2 - 16, height - 48, 100, 20, "Direct Login"));
    buttonList.add(new GuiButton(5, width / 2 - 122, height - 24, 78, 20, "Random"));
    buttonList.add(this.rename = new GuiButton(6, width / 2 + 38, height - 24, 70, 20, "Edit"));
    buttonList.add(new GuiButton(7, width / 2 - 190, height - 24, 60, 20, "Last Alt"));
    buttonList.add(new GuiButton(8, width / 2 - 190, height - 48, 60, 20, "Reload"));
    login.enabled = false;
    remove.enabled = false;
    rename.enabled = false;
  }

  private boolean isAltInArea(int y)
  {
    return y - offset <= height - 50;
  }

  private boolean isMouseOverAlt(int x, int y, int y1)
  {
    return (x >= 52) && (y >= y1 - 4) && (x <= width - 52) && (y <= y1 + 20) && (x >= 0) && (y >= 33) && (x <= width) && (y <= height - 50);
  }


  protected void mouseClicked(int par1, int par2, int par3)
  {
    if (offset < 0) {
      offset = 0;
    }
    int y = 38 - offset;
    for (Alt alt : Pandora.getAltManager().registry)
    {
      if (isMouseOverAlt(par1, par2, y))
      {
        if (alt == selectedAlt)
        {
          actionPerformed((GuiButton)buttonList.get(1));
          return;
        }
        selectedAlt = alt;
      }
      y += 26;
    }
    try
    {
      super.mouseClicked(par1, par2, par3);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void prepareScissorBox(float x, float y, float x2, float y2)
  {
    ScaledResolution scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
    int factor = scale.getScaleFactor();
    GL11.glScissor((int)(x * factor), (int)((scale.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
  }
}
