package nivia.gui.aclickgui.themes.apx.elements;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import nivia.gui.aclickgui.Element;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;


/**
 * Created by Apex on 8/20/2016.
 */
public class ValueButton extends Element {

    private ModContainer parent;
    private Module mod;
    private Property mv;

    public ValueButton(ModContainer panel, Module m, Property modValue) {
        this.parent = panel;
        this.mod = m;
        this.mv = modValue;
        this.setHeight(12);
    }

    @Override
    public void drawElement(float posX, float posY, int mouseX, int mouseY, float partialTicks) {
    	posX += 2;
    	this.setWidth(this.getWidth() + 2);
        this.setPosX(posX);
        this.setPosY(posY);

        boolean toggled = (Boolean) mv.value;
        boolean overToggle = mouseX > posX + 2.5f && mouseX < posX + 90 && mouseY > posY + 1.95f && mouseY < posY + 9.4F;

        if (toggled) {
            GL11.glColor4f(1, 1, 1, 1);
            ResourceLocation checkMark = new ResourceLocation("textures/pandora/tick.png");
            Helper.get2DUtils().drawCustomImage(posX + 3, posY + 1.5F, 8,8, checkMark);
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75f, 0.75F, 0.75F);
        Helper.mc().fontRendererObj.drawStringWithShadow(mv.getName(), ((int) posX + 14) * 1.33F, ((int) posY + 3) * 1.33F, 0xFFCFCFCF);
        GlStateManager.popMatrix();

        Helper.get2DUtils().drawRect(posX + 2.5f, posY + 1.95f, posX + 10.7F, posY + 9.4F, overToggle ? 0x7FFFFFFF : 0x2FFFFFFF);


    }

    @Override
    public float getHeight() {
        return 12;
    }

    @Override
    public float getRealHeight() {
        return 12;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean overToggle = mouseX > getPosX() + 2.5f && mouseX < getPosX() + 90 && mouseY > getPosY() + 1.95f && mouseY < getPosY() + 9.4F;
        if (button == 0 && overToggle) {
            mv.value = (!(Boolean) mv.value);

        }
    }
}
