package skyline.specc.extras.buttons;

import java.awt.Color;
import java.awt.Font;

import net.minecraft.client.Mineman;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.RenderUtil;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.VitalFontRenderer;
import skyline.specc.utils.Wrapper;

public class Buttons
  extends GuiButton
{
      public VitalFontRenderer fontz;
	  private int x;
	  private int y;
	  private int x1;
	  private int y1;
	  private String text;
	  int alphaVal = 100;
	  int ytrans;
	  int alpha = 0;
	  int size = 0;
	  
	  public Buttons(int par1, int par2, int par3, int par4, int par5, String par6Str)
	  {
	    super(par1, par2, par3, par4, par5, par6Str);
		this.fontz = VitalFontRenderer.createFontRenderer(VitalFontRenderer.FontObjectType.CFONT, new Font("Comfortaa", 0, 25));
	    this.x = par2;
	    this.ytrans = 0;
	    this.y = par3;
	    this.x1 = par4;
	    this.y1 = par5;
	    this.text = par6Str;
	  }
	  
	  public Buttons(int i, int d, int k, String stringParams)
	  {
	    this(i, d, k, 200, 20, stringParams);
	  }
	  
	  int fade;
		public void drawButton(Mineman mc, int mouseX, int mouseY) {
		    FontRenderer font = Wrapper.getFontRenderer();
			if(this.visible) {
				this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
				if(!hovered) {
					this.fade = 90;
				}
				else {
					if(this.fade <= 30) {
						return;
					}
					if(this.fade != 230) {
						this.fade += 10;
					}
				}
			    float b = this.hovered ? new Color(150,0,0).getRGB() : new Color(255,255,255,255).getRGB();
				Color a = new Color(0,0,0, this.fade);
				FontRenderer var4 = mc.fontRendererObj;
				RenderUtil.drawbRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, a.getRGB(), -1);
				Gui.drawRect(this.xPosition, this.yPosition,this.xPosition + this.width, this.yPosition + this.height, a.getRGB());
				font.drawCenteredString(var4,this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, (int) b);
			}
			
		}
}