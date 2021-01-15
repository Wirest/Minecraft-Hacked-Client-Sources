package nivia.gui.aclickgui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import nivia.gui.aclickgui.themes.apx.APXTheme;
import nivia.modules.render.GUI;
import nivia.utils.Helper;

import java.io.IOException;

/**
 * Created by Apex on 07/08/2016
 */
public class GuiAPX extends GuiScreen {

    public static Theme getTheme() {
        return theme;
    }
    public static int color = 0xFFD11EFF;
    public static void setTheme(Theme theme) {
        GuiAPX.theme = theme;
    }

    private static Theme theme = new APXTheme();

    public static void insert() {
        theme.insert();
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theme.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        theme.mouseReleased(mouseX, mouseY, mouseButton);
    }

    private ResourceLocation safeResource = new ResourceLocation("Ace2/safe.png");
    private ResourceLocation fullResource = new ResourceLocation("Ace2/full.png");
    private ResourceLocation recoResource = new ResourceLocation("Ace2/record.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) { 
    	this.color = GUI.getClickGuiColor();
        theme.drawScreen(mouseX, mouseY, partialTicks);
        mc.fontRendererObj.drawStringWithShadow("Huge Thanks to Apex", Helper.get2DUtils().scaledRes().getScaledWidth() - mc.fontRendererObj.getStringWidth("Huge Thanks to Apex "), Helper.get2DUtils().scaledRes().getScaledHeight() - 20, 0xFFCCCCCC);
        mc.fontRendererObj.drawStringWithShadow("ClickGUI still work in progress", Helper.get2DUtils().scaledRes().getScaledWidth() - mc.fontRendererObj.getStringWidth("ClickGUI still work in progress "), Helper.get2DUtils().scaledRes().getScaledHeight() - 10, 0xFFCCCCCC);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {       
      theme.mouseClicked(mouseX, mouseY, button);       
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        try {
            theme.handleMouseInput();
        } catch (Exception e){
            e.printStackTrace();
        }
        super.handleMouseInput();
    }

    @Override
    public void onGuiClosed() {
        
    }
}
