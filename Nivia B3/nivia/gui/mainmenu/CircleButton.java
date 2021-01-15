package nivia.gui.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;

public class CircleButton
{
	private int id;
	private float x;
	private float y;
	private float radius;
	private int color;
	private float transition;
	private ResourceLocation resLoc;
	private GuiScreen nextScreen;
	private boolean play;
	private Action action;
	private boolean doTrans;
	private String name;

	public int getID()
	{
		return this.id;
	}

	public float getX()
	{
		return this.x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return this.y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public float getRadius()
	{
		return this.radius;
	}

	public int getColor()
	{
		return this.color;
	}

	public Action getActionValue()
	{
		return this.action;
	}

	public void setPlay(boolean play)
	{
		this.play = play;
	}

	public void setResLoc(ResourceLocation resLoc)
	{
		this.resLoc = resLoc;
	}

	public CircleButton(int buttonId, float x, float y, float radius, String name, Color color, ResourceLocation resLoc, boolean doTransition, GuiScreen nextScreen)
	{
		this.id = buttonId;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color.getRGB();
		this.resLoc = resLoc;
		this.nextScreen = nextScreen;
		this.doTrans = doTransition;
		this.action = Action.GuiScreen;
		this.name = name;
	}

	public CircleButton(int buttonId, float x, float y, float radius, String name, Color color, ResourceLocation resLoc, boolean doTransition, boolean play)
	{
		this.id = buttonId;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color.getRGB();
		this.resLoc = resLoc;
		this.play = play;
		this.doTrans = doTransition;
		this.action = Action.Boolean;
		this.name = name;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		float f = Helper.get2DUtils().factorDivider();
		
		isWithinButton(mouseX, mouseY);
		//Helper.get2DUtils().drawFilledCircle(this.xf, this.y - this.transition, this.radius, 360, 1.0F, 360, this.color);
		drawFullCircle(this.x, this.y - this.transition, this.radius + this.transition, 360, 1.0F, 360, this.color);
		if(this.name.equalsIgnoreCase("Multiplayer"))
			Helper.get2DUtils().drawCustomImage(this.x - this.radius / 1.26F, this.y - this.radius / 1.26F - this.transition, (int)(this.radius * 1.6F), (int)(this.radius * 1.6F), this.resLoc);
		else Helper.get2DUtils().drawCustomImage(this.x - this.radius , this.y - this.radius - this.transition, (int)(this.radius * 2F), (int)(this.radius * 2F), this.resLoc);
		
		
		Helper.get2DUtils().drawStringWithShadow(name, (this.x - Helper.get2DUtils().getStringWidth(name) / 2)   , (this.y * 1.23F) , 0xFFFFFF, RenderUtils.helvetica);
		//GlStateManager.enableRescaleNormal();
		
		
		//RenderHelper.drawIcon(this.x - this.radius / 1.34F, this.y - this.radius / 1.34F - this.transition, (int)(this.radius * 1.52F), (int)(this.radius * 1.52F), this.resLoc);
	}
	
    public static void drawFullCircle(float cx, float cy, double r, int segments, float lineWidth,
            int part, int c) {
    	GL11.glScalef(0.5f, 0.5f, 0.5f);
    	r *= 2.0;
    	cx *= 2;
    	cy *= 2;
    	float f = (c >> 24 & 0xFF) / 255.0f;
    	float f2 = (c >> 16 & 0xFF) / 255.0f;
    	float f3 = (c >> 8 & 0xFF) / 255.0f;
    	float f4 = (c & 0xFF) / 255.0f;
    	GL11.glEnable(3042);
    	GL11.glLineWidth(lineWidth);
    	GL11.glDisable(3553);
    	GL11.glEnable(2848);
    	GL11.glBlendFunc(770, 771);
    	GL11.glColor4f(f2, f3, f4, f);
    	GL11.glBegin(3);
    		for (int i = segments - part; i <= segments; ++i) {
    			double x = Math.sin(i * 3.141592653589793 / 180.0) * r;
    			double y = Math.cos(i * 3.141592653589793 / 180.0) * r;
    			GL11.glVertex2d(cx + x, cy + y);
    	}
    	GL11.glEnd();
    	GL11.glDisable(2848);
    	GL11.glEnable(3553);
    	GL11.glDisable(3042);
    	GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

	public Object getAction()
	{
		if (this.action == Action.GuiScreen)
		{
			if (this.nextScreen != null) {
				return this.nextScreen;
			}
			if (this.nextScreen == null) {
				Minecraft.getMinecraft().shutdown();
			}
		}
		else if (this.action == Action.Boolean)
		{
			return Boolean.valueOf(this.play);
		}
		return null;
	}

	public boolean isWithinButton(int mouseX, int mouseY)
	{
		if (Math.pow(mouseX - this.x, 2.0D) + Math.pow(mouseY - (this.y - this.transition), 2.0D) < Math.pow(this.radius + this.transition, 2.0D))
		{
			if ((this.transition <= 5.0F) && (this.doTrans)) {
				this.transition += 1.0F;
			}
			return true;
		}
		if ((this.transition > 0.0F) && (this.doTrans)) {
			this.transition -= 1.0F;
		}
		return false;
	}

	public static enum Action
	{
		GuiScreen,  Boolean;

		private Action() {}
	}
}
