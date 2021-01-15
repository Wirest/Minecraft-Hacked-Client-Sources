package nivia.gui.aclickgui.themes.apx.elements;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import nivia.gui.aclickgui.Element;
import nivia.gui.aclickgui.GuiAPX;
import nivia.modules.Module;
import nivia.utils.Helper;


/**
 * Created by Apex on 8/23/2016.
 */
public class ModKeybind extends Element {
    private ModContainer parent;
    private Module mod;

    public ModKeybind(ModContainer panel, Module m) {
        this.parent = panel;
        this.mod = m;
        this.setHeight(12);
    }

    private boolean editing = false;

    @Override
    public void drawElement(float posX, float posY, int mouseX, int mouseY, float partialTicks) {
    	posX += 3;
        this.setWidth(this.getWidth() + 3);
        this.setPosX(posX);
        this.setPosY(posY);

        int uicolor = GuiAPX.color;

        boolean isOver = editing || mouseX > getPosX() + 10 && mouseX < getPosX() + 80 && mouseY > getPosY() + 2F && mouseY < getPosY() + 12.5F;

        Helper.get2DUtils().drawRect(posX + 10, posY + 2F, posX + 80, posY + 12.5F, 0x3F000000);
        Helper.get2DUtils().drawRect(posX + 10, posY + 2F, posX + 11, posY + 12.5F, isOver ? uicolor : 0x2FFFFFFF);
        Helper.get2DUtils().drawRect(posX + 79, posY + 2F, posX + 80, posY + 12.5F, isOver ? uicolor : 0x2FFFFFFF);


        String s = EnumChatFormatting.WHITE + "Keybind: " + EnumChatFormatting.RESET + (editing?"?":Keyboard.getKeyName(mod.getKeybind()));
        float w = Helper.mc().fontRendererObj.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75f, 0.75F, 0.75F);
        Helper.mc().fontRendererObj.drawStringWithShadow(s, (posX + 52 - (w/2)) * 1.33F, (posY + 5) * 1.33F, uicolor);
        GlStateManager.popMatrix();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)  {
        if (editing) {
            mod.setKeybind(keyCode);
            editing = false;
        }
    }

    @Override
    public float getHeight() {
        return 14;
    }

    @Override
    public float getRealHeight() {
        return 14;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean isOver = mouseX > getPosX() + 10 && mouseX < getPosX() + 80 && mouseY > getPosY() + 2F && mouseY < getPosY() + 12.5F;

        if (button == 0 && isOver) {
            if (editing) {
                editing = false;
            } else
                editing = true;
            //Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
        } else if (button == 1 && isOver && this.editing) {
            mod.setKeybind(0);
            editing = false;
            //Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
        }
    }
}
