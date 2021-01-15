package nivia.gui.chod.component;

import org.lwjgl.input.Keyboard;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Wrapper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;

public class ChodsKeybind extends ChodsComponent {

    public static int selectedColor  = RenderUtils.getIntFromColor(new Color(57, 132, 164));
    public static int selectedColor2 = RenderUtils.getIntFromColor(new Color(75, 177, 219));
    public int inline     = RenderUtils.getIntFromColor(new Color(57, 57, 57));
    public int background = RenderUtils.getIntFromColor(new Color(116, 121, 122));

    private float hoverTime, enabledTime = 0F;

    public ChodsKeybind(Module mod) {
        this.title = "Keybind";
        this.mod = mod;
    }
    
    private boolean editing = false;

    @Override
    public void drawElement(int mouseX, int mouseY, float partialTicks) {
        String string = (mod.getKeybind() == 0 ? editing ? "Keybind: " + "?": "Keybind not set." : "Keybind: " + (editing ? "?" : Keyboard.getKeyName(mod.getKeybind())));
        RenderUtils.drawRectWH(this.x + 6, this.y + 2, mod.getKeybind() == 0 ? 87 : 5 + Helper.mc().fontRendererObj.getStringWidth(string), 20, inline);
        RenderUtils.drawRectWH(this.x + 7, this.y + 3, mod.getKeybind() == 0 ? 85 : 5 + Helper.mc().fontRendererObj.getStringWidth(string), 18, background);
        Wrapper.getFontRenderer().drawStringWithFagShadow(string, this.x + 10, this.y + 8, -1);
    }
    
    @Override
    public void keyTyped(char typedChar, int keyCode) {
    	if (editing) {
    		mod.setKeybind(keyCode);
    		editing = false;
    	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX / Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY / Helper.get2DUtils().scaledRes().getScaleFactor();
        if (isInside(scaledMouseX, scaledMouseY))
        	if (mouseButton == 0)
        		if (editing)
        			editing = false;
        		else
        			editing = true;
        	 else if (mouseButton == 1 && this.editing) {
        		mod.setKeybind(0);
        		editing = false;
        	}
    }

	@Override
	public void onUpdate(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		// TODO Auto-generated method stub
		
	}
}
