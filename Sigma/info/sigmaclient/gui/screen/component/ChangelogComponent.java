package info.sigmaclient.gui.screen.component;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;
import info.sigmaclient.management.animate.Opacity;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

public class ChangelogComponent {
	List<String>list;
	String title;
	TTFFontRenderer font;
	int x,y,wheelc = 0;
	Translate wheel;
	double width, height;
	boolean isHovered;
	Opacity opacity,opacity2;
	public ChangelogComponent(List<String>list, String title, int x, int y, TTFFontRenderer font){
		this.wheel = new Translate(0, 0);
		this.list = list;
		this.title = title;
		this.x = x;
		this.y = y;
		this.font = font;
		double height = 0;
		for(String str : list)
			height += font.getHeight(str) + 1.55f;
		this.height = height;
		double width = 0;
		for(String str : list)
			if(font.getWidth(str) > width)
				width = font.getWidth(str);
		this.width = width;
		this.opacity = new Opacity(255);
		this.opacity2 = new Opacity(255);
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public boolean isMouseInside(int mouseX, int mouseY, int screenHeight){
		return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY()-screenHeight- 20 && mouseY <= getY()-screenHeight + getHeight();
	}
	public List<String> getList(){
		return this.list;
	}
	public void resetOpacity(){
		this.opacity = new Opacity(0);
		//this.opacity2 = new Opacity(0);
	}
	public String getTitle(){
		return this.title;
	}
	public double getHeight(){
		return this.height;
	}
	public double getWidth(){
		return this.width;
	}
	public int getWheel(){
		return this.wheelc;
	}
	public void setWheel(int wheel){
		this.wheelc = wheel;
		if(this.wheelc > 0)
			this.wheelc = 0;
	}
	public void updateWheel(int mouseX, int mouseY, int screenHeight){
		this.isHovered = isMouseInside(mouseX, mouseY, screenHeight);
		if (Mouse.hasWheel()) {
  	   		final int wheelR = Mouse.getDWheel();
  	   		if(this.isHovered){
	  	   		if (wheelR < 0) {
	  	   			setWheel(getWheel() - 18);
	  	   			if(Math.abs(getWheel()) > getHeight())
	  	   				setWheel(-(int)getHeight()); 	   			
	  	   		} else if (wheelR > 0) 
	  	   			setWheel(getWheel() + 18); 
  	   		}
  	   		 
  	   	}
	}
	public void draw(int s, int screenHeight){
		wheel.interpolate(0, wheelc, 6);
		opacity.interp(255, 6);
		opacity2.interp(this.isHovered ? 100 : 255, 10);
		int color = Colors.getColor(255, 255, 255, (int)opacity.getOpacity());
		int bcolor = Colors.getColor(255, (int)opacity2.getOpacity(), (int)opacity2.getOpacity(), (int)opacity.getOpacity());
    	glPushMatrix();
    	int bot = screenHeight - (int)(getY() + getHeight() - screenHeight) - 9;
    	glScissor((getX()-3)*s,(bot + 6)*s,(int)(getWidth()+4)*s,(int)(getHeight()+6.5)*s);
    	glEnable(GL_SCISSOR_TEST);
    	
	    int y = getY();
	    for(String str : list){
	    	font.drawString(str, x, y + wheel.getY(), color);
	    	y += font.getHeight(str)+2;
	    }
	    
	    glDisable(GL_SCISSOR_TEST);
	    float maxY = y+ wheel.getY();
	    RenderingUtil.drawRect(getX()-2, getY()-4.5f, getX() + (float)getWidth() + 1, getY()-3.5f, bcolor);
	    RenderingUtil.drawRect(getX()-2, getY()-24, getX()-1,maxY + 0.5f, bcolor);
	    RenderingUtil.drawRect(getX()-2, maxY, getX() + (float)getWidth() + 1,maxY+1, bcolor);
	    RenderingUtil.drawRect(getX()+ (float)getWidth(), getY()-24, getX() + (float)getWidth() + 1,maxY + 1f, bcolor);
	    RenderingUtil.drawRect(getX()-2, getY()-24.5f, getX() + (float)getWidth() + 1, getY()-23.5f, bcolor);
	    font.drawString(getTitle(), x + (float)getWidth()/2 - font.getWidth(title)/2, getY()-16, color);
		glPopMatrix();    
	}
}
