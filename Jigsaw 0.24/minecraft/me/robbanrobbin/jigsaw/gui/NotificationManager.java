package me.robbanrobbin.jigsaw.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class NotificationManager {
	
	private ArrayList<NotificationWindow> notes = new ArrayList<NotificationWindow>();
	
	private boolean adding = false;
	
	private int addingCount = 0;
	
	public NotificationManager() {
		
	}
	
	public void addNotification(Notification note) {
		if(Jigsaw.ghostMode) {
			return;
		}
		if(Jigsaw.getUIRenderer() == null) {
			return;
		}
		try {
			NotificationWindow window = new NotificationWindow(note);
			window.setPosition(Jigsaw.getUIRenderer().getWidth() -  window.getWidth() - 3, Jigsaw.getUIRenderer().getHeight());
			notes.add(window);
			adding = true;
		}
		catch(Exception e) {
			try {
				if(Jigsaw.devVersion) {
					NotificationWindow window = new NotificationWindow(new Notification(Level.ERROR, "Error displaying note, please report to the creator of the client along with this message: " + e.getMessage()));
					window.setPosition(Jigsaw.getUIRenderer().getWidth() -  window.getWidth() - 3, Jigsaw.getUIRenderer().getHeight());
					notes.add(window);
					adding = true;
				}
			}
			catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}

	public void update() {
		if(Jigsaw.ghostMode) {
			return;
		}
		if(Minecraft.getMinecraft().thePlayer == null) {
			return;
		}
		for(NotificationWindow window : notes) {
			window.update();
		}
		Iterator<NotificationWindow> iter = notes.iterator();
		while(iter.hasNext()) {
			NotificationWindow window = iter.next();
			if(window.getLifeTime() > window.getNotification().getText().length() * 3) {
				iter.remove();
			}
		}
	}

	public void draw() {
		if(Jigsaw.ghostMode) {
			return;
		}
		if(Minecraft.getMinecraft().thePlayer == null) {
			return;
		}
		int space = -20;
		if(adding) {
			adding = false;
			addingCount = -space;
		}
		if(addingCount > 0) {
			addingCount--;
		}
		
		GlStateManager.pushMatrix();
		//GlStateManager.scale(2d / Minecraft.getMinecraft().gameSettings.guiScale, 2d / Minecraft.getMinecraft().gameSettings.guiScale, 1);
		GlStateManager.translate(0, -21, 0);
		GlStateManager.translate(0, addingCount, 0);
		for(int i = notes.size() - 1; i > -1; i--) {
			NotificationWindow window = notes.get(i);
			GlStateManager.translate(0, space, 0);
			window.setPosition(Jigsaw.getUIRenderer().getWidth() -  window.getWidth() - 3, Jigsaw.getUIRenderer().getHeight());
			window.draw();
			
		}
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
}
