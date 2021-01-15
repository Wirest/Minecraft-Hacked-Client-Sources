package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.awt.Point;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;

public class Slider extends Component {
	
	private String title;
	private double increment = 0.0;
	private Number value;
	private double minValue;
	private double maxValue;
	private ValueFormat format;
	private SliderTask task;
	private double preValue;
	private float preTicks = 100;
	
	public Slider(String title, Number value, double minValue, double maxValue, double increment, ValueFormat format, SliderTask task) {
		this.title = title;
		this.value = value;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.increment = increment;
		this.format = format;
		this.task = task;
	}

	@Override
	public void update() {
		if(preValue != getValue().doubleValue()) {
			task.task(this);
		}
	}

	@Override
	public void draw() {
		//System.out.println(GuiUtils.partialTicks);
		//System.out.println(preValue + " : " + getValue().doubleValue() + " STATIC");
		if(preTicks > GuiUtils.partialTicks) {
			//System.out.println(preValue + " : " + getValue().doubleValue() + " CHANGED");
			preValue = getValue().doubleValue();
		}
		preTicks = GuiUtils.partialTicks;
		GuiUtils.translate(this, false);
		Point mouse = GuiUtils.calculateMouseLocation();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0, 0);
			glVertex2d(0, getHeight());
			glVertex2d(getWidth(), getHeight());
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(0, 0);
			glVertex2d(getWidth(), 0);
		}
		glEnd();
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.8f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_LINES);
		{
			glVertex2d(0, getHeight() - 0.5);
			glVertex2d(getWidth(), getHeight() - 0.5);
		}
		glEnd();
		double sliderWidth = ((value.doubleValue() - minValue) / (maxValue - minValue)) * getWidth();
		GL11.glColor4f(1f, 1f, 1f, 1f - Jigsaw.getClickGuiManager().getAlpha());
		glBegin(GL11.GL_QUADS);
		{
			glVertex2d(0.5, fontRenderer.FONT_HEIGHT - 1);
			glVertex2d(0.5, getHeight() - 0.5);
			glVertex2d(sliderWidth, getHeight() - 0.5);
			glVertex2d(sliderWidth, fontRenderer.FONT_HEIGHT - 1);
		}
		glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(Jigsaw.getClickGuiManager().getAlpha() <= 0.5f) {
			fontRenderer.drawStringWithShadow(title, 1, -1, 
					0xffffffff);
			String sValue = getValueString();
			fontRenderer.drawStringWithShadow(sValue, (int) (getWidth() - fontRenderer.getStringWidth(sValue) - 2), -1, 
					0xffffffff);
		}
		GL11.glDisable(GL11.GL_BLEND);
		glEnable(GL_CULL_FACE);
		GuiUtils.translate(this, true);
	}
	
	@Override
	public void onDragged(int x, int y, double mx, double my, int button) {
		super.onDragged(x, y, mx, my, button);
		if(button == 0) {
			this.setValue((((mx) / getWidth()) * (maxValue - minValue)) + minValue);
		}
	}
	
	public void setValue(Number value) {
		this.value = value;
	}
	
	private String getValueString() {
		String sValue = "";
		if(format == ValueFormat.DECIMAL) {
			sValue = String.valueOf(Math.round(value.doubleValue() * 10.0) / 10.0);
		}
		if(format == ValueFormat.PERCENT) {
			sValue = String.valueOf((Math.round((value.doubleValue() / maxValue) * 1000d)) / 10d) + "%";
		}
		if(format == ValueFormat.INT) {
			sValue = String.valueOf(Math.round(value.doubleValue()));
		}
		return sValue;
	}

	@Override
	public double getPreferedWidth() {
		return fontRenderer.getStringWidth(title + getValueString() + "___");
	}

	@Override
	public double getPreferedHeight() {
		return fontRenderer.FONT_HEIGHT + 5;
	}
	
	public String getTitle() {
		return title;
	}
	
	public double getIncrement() {
		return increment;
	}
	
	public double getMinValue() {
		return minValue;
	}
	
	public double getMaxValue() {
		return maxValue;
	}
	
	public Number getValue() {
		return value;
	}
	
}
