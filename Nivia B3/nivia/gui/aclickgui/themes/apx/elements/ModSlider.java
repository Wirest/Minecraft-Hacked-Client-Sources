package nivia.gui.aclickgui.themes.apx.elements;

import net.minecraft.client.renderer.GlStateManager;
import nivia.gui.aclickgui.Element;
import nivia.gui.aclickgui.GuiAPX;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Helper;

/**
 * Created by Apex on 8/21/2016.
 */
public class ModSlider extends Element {

    private ModContainer parent;
    private Module mod;
    private DoubleProperty mv;

    public ModSlider(ModContainer panel, Module m, DoubleProperty modValue) {
        this.parent = panel;
        this.mod = m;
        this.mv = modValue;
        this.setHeight(24);
    }

    private boolean isSliding = false;

    @Override
    public void drawElement(float posX, float posY, int mouseX, int mouseY, float partialTicks) {
    	posX += 2;
    	this.setWidth(this.getWidth() + 2);
    	int uiColor = GuiAPX.color;
        this.setPosX(posX);
        this.setPosY(posY);

        
        float min = 0;
        float max = 0;
        if(mv.increase == 0){
        	min = (int)mv.min;
        	max = (int)mv.max;
        } else { 
        	min = (float) mv.min;
        	max = (float) mv.max;
        }
        float value = Float.parseFloat("" + mv.getValue());
      


        double percent = (100 *( (value - min) / (max - min) ));
        boolean isOverSlider = mouseX > posX && mouseY > posY + 8.5F && mouseX < posX + 100 && mouseY < posY + 19.5F;
        if (isOverSlider && isSliding) {
            double valueNew = (Math.round((min + (mouseX - posX - 1) * ((max - min) / 85))*100.0)/100.0);
            if (valueNew > max) valueNew = max;
            if (valueNew < min) valueNew = min;    
            if(mv.increase == 0)
            	mv.setValue((int)valueNew); 
            else 
            	mv.setValue(valueNew); 
        }
        if (!isOverSlider && isSliding)
            isSliding = false;
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75f, 0.75F, 0.75F);
        Helper.mc().fontRendererObj.drawStringWithShadow(mv.getName(), (posX + 2) * 1.33F, (posY + 1) * 1.33F, 0xFFCFCFCF);
        double v = (double)Math.round(mv.getValue() * 10d) / 10d;
        String valStr = "" + v;
        if(mv.increase == 0)
        	valStr = Integer.toString((int)mv.getValue());
 
        Helper.mc().fontRendererObj.drawStringWithShadow(valStr, (posX + 84 - (Helper.mc().fontRendererObj.getStringWidth(valStr) / 2f)) * 1.33F, (posY + 1) * 1.33F, uiColor);
        GlStateManager.popMatrix();
       

        double valAdd = ((percent / 100)*85);
        if (valAdd <= 2) valAdd = 2;

        Helper.get2DUtils().drawBorderedRect(posX + 2, posY + 12.5F, posX + 87, posY + 15.5F, 2, isOverSlider ? uiColor : 0x2FFFFFFF);
        Helper.get2DUtils().drawBorderedRect(posX + 2, posY + 12.5F, (float) (posX + 2 + valAdd), posY + 15.5F, uiColor, 0x30D11EFF);
    }

    @Override
    public float getHeight() {
        return 18;
    }

    @Override
    public float getRealHeight() {
        return 18;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean isOverSlider = mouseX > getPosX() + 2 && mouseY > getPosY() + 8.5F && mouseX < getPosX() + 87 && mouseY < getPosY() + 19.5F;
        if (button == 0 && isOverSlider) {
            isSliding = !isSliding;
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (isSliding)
            isSliding = false;
    }
}
