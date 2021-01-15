package nivia.gui.aclickgui.themes.apx.elements;

import net.minecraft.client.renderer.GlStateManager;
import nivia.gui.aclickgui.GuiAPX;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;

/**
 * Created by Apex on 8/21/2016.
 */
public class ModMode extends nivia.gui.aclickgui.Element {

    private ModContainer parent;
    private Module mod;
    private Property mv;
    private Enum[] modes;
    
    public ModMode(ModContainer panel, Module m, Property modValue) {
        this.parent = panel;
        this.mod = m;
        this.mv = modValue;
        this.setHeight(20);
    }

    @Override
    public void drawElement(float posX, float posY, int mouseX, int mouseY, float partialTicks) {
        posX += 3;
        this.setWidth(this.getWidth() + 3);
        this.setPosX(posX);
        this.setPosY(posY);
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75f, 0.75F, 0.75F);

        Helper.mc().fontRendererObj.drawStringWithShadow(mv.getName(), (posX + 1) * 1.33f, (posY + 2) * 1.33F, 0xFFCFCFCF);
        GlStateManager.popMatrix();

        boolean overRight = mouseX > posX + 59 && mouseX < posX + 88 && mouseY >= posY + 1.5F && mouseY <= posY + 10.5F;
        boolean overLeft = mouseX > posX + 30 && mouseX < posX + 59 && mouseY >= posY + 1.5F && mouseY <= posY + 10.5F;

        int uicolor = GuiAPX.color;
        Helper.get2DUtils().drawRect(posX + 30, posY + 1.5F, posX + 88, posY + 10.5F, 0x4F000000);
        Helper.get2DUtils().drawRect(posX + 87, posY + 1.5F, posX + 88, posY + 10.5F, overRight ? uicolor : 0x2FFFFFFF);
        Helper.get2DUtils().drawRect(posX + 30, posY + 1.5F, posX + 31, posY + 10.5F, overLeft ? uicolor : 0x2FFFFFFF);


        Helper.get2DUtils().drawSmallString("<", posX + 32, posY + 1.2F, overLeft ? uicolor : 0x2FFFFFFF);
        Helper.get2DUtils().drawSmallString(">", posX + 86 - (Helper.get2DUtils().getStringWidth(">") / 2), posY + 1.2F, overRight ? uicolor : 0x2FFFFFFF);

        String valName = mv.value.toString();
        float strWidth = Helper.mc().fontRendererObj.getStringWidth(valName) / 2;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75f, 0.75F, 0.75F);
        Helper.mc().fontRendererObj.drawStringWithShadow(valName, (posX + 57 - (strWidth/2)) * 1.33F, (posY + 3.8F) * 1.33F, 0xFFCFCFCF);
        GlStateManager.popMatrix();
    }

    @Override
    public float getHeight() {
        return 12;
    }

    @Override
    public float getRealHeight() {
        return 12;
    }
    private static int index;
    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean overRight = mouseX > getPosX() + 59 && mouseX < getPosX() + 88 && mouseY >= getPosY() + 1.5F && mouseY <= getPosY() + 10.5F;
        boolean overLeft = mouseX > getPosX() + 30 && mouseX < getPosX() + 59 && mouseY >= getPosY() + 1.5F && mouseY <= getPosY() + 10.5F;

        if (button == 0) {          
            if (overLeft) {
            	modes = (Enum[]) mv.value.getClass().getEnumConstants();
                if (index != 0)
                    index--;
                else index = modes.length - 1;
                mv.value = modes[index];               
            } else if (overRight) {
            	 modes = (Enum[]) mv.value.getClass().getEnumConstants();
                 if (index < modes.length - 1)
                     index++;
                 else index = 0;
                 mv.value = modes[index];
 
            }
        }
    }
}
