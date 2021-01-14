package me.Corbis.Execution.Security;

import me.Corbis.Execution.ui.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiSecurity extends GuiScreen {

    CleanTextField textField;
    CleanTextField textField2;
    LoginButton button;
    UnicodeFontRenderer ufr;


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(ufr == null){
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 1, 2);
        }

        ScaledResolution sr = new ScaledResolution(mc);
        mc.getTextureManager().bindTexture(new ResourceLocation("Execution/Splash.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
        GlStateManager.enableAlpha();
        mc.getTextureManager().bindTexture(new ResourceLocation("Execution/Security.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
        textField.draw(ufr);
        button.draw(mouseX, mouseY);
        textField2.draw(ufr);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        textField.keyTyped(typedChar, keyCode);
        textField2.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        System.out.println(textField.getText());
        System.out.println(textField2.getText());
        if(textField != null && textField2 != null) {
            textField.mouseClicked(mouseX, mouseY, ufr);
            textField2.mouseClicked(mouseX, mouseY, ufr);
            button.onClick(mouseX, mouseY, textField.text, textField2.text);


        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void initGui() {
        textField = new CleanTextField(width - width / 3, height / 2 - height / 4, "", "License ID");
        textField2 = new CleanTextField(width - width / 3, height / 2, "", "User ID");
        button = new LoginButton(width - width / 5, height / 2, 80, 80);
        super.initGui();
    }
}
