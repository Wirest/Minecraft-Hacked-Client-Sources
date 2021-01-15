package nivia.modules.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.Event3D;
import nivia.managers.FriendManager;
import nivia.managers.PropertyManager;
import nivia.managers.StaffManager;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;

import java.awt.*;
import java.util.ArrayList;

public class 	MurderMysterys extends Module {
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public PropertyManager.DoubleProperty firstColor = new PropertyManager.DoubleProperty(this, "First Color", 0xFFFF0000, -999999999, 999999999, false, true);
	public PropertyManager.DoubleProperty lastColor = new PropertyManager.DoubleProperty(this, "Second Color", 0xFFFFFFFF, -999999999, 999999999, false, true);

	public MurderMysterys() {
		super("MurderMysterys", 0, 0, Category.RENDER, "Draws a line to murderer.", new String[] { "murder", "mrdr", "mrdrmystery", "mystery", "murdermystery", "murdermyst" }, false);
	}

	@EventTarget(Priority.HIGH)
	public void onRender3D(Event3D e) {
		mc.theWorld.loadedEntityList.forEach(o -> {
			Entity en = (Entity)o;
			if (!en.isEntityAlive() && entities.contains(en)) {
				entities.remove(en);
			}
			
			if (en instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) en;

				if (player != mc.thePlayer) {
					if (player.getCurrentEquippedItem() != null) {
						if (player.getCurrentEquippedItem().getItem() instanceof ItemSword) {
							if (!entities.contains(en)) {
								Logger.logChat(en.getName() + " is the murderer!");
								entities.add(en);
							}
						} else if (entities.contains(en)) {
								entities.remove(en);
						}
					}
				}
			}
		});
		
		mc.theWorld.loadedEntityList.forEach(o -> {
			Entity entity = (Entity)o;
			if ((entity.isEntityAlive() && entities.contains(entity))) {

				final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks() - RenderManager.renderPosX;
				final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks() - RenderManager.renderPosY;
				final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks() - RenderManager.renderPosZ;
				
				boolean old = Helper.mc().gameSettings.viewBobbing;

				Helper.get3DUtils().startDrawing();
				Helper.mc().gameSettings.viewBobbing = false;
				Helper.mc().entityRenderer.setupCameraTransform(Helper.mc().timer.renderPartialTicks, 2);
				Helper.mc().gameSettings.viewBobbing = old;
				renderTracer(entity, posX, posY, posZ);
				Helper.get3DUtils().stopDrawing();

			}
		});
	}

	private boolean hasSword(EntityPlayer en) {
	    for (int i = 0; i < 8; i++) {
		if (en.getInventory()[i].getItem() instanceof ItemSword) {
		    return true;
		}
	    }
	    return false;
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		entities.clear();
	}

	private void renderTracer(Entity entity, double x, double y, double z) {
		final float distance = Helper.player().getDistanceToEntity(entity);

		boolean isSpecial = FriendManager.isFriend(entity.getName());
		int color;
		if (FriendManager.isFriend(entity.getName()))
			color = 0xFF00CCFF;
		else {
			float xD = distance / 48;
			if(xD >= 1) xD = 1;
			color = Helper.colorUtils().blend(new Color((int) lastColor.getValue()), new Color( (int) firstColor.getValue()), xD).getRGB();
		}
		boolean entityesp = Pandora.getModManager().getModState("ESP");
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		if (!isSpecial)
			Helper.colorUtils().glColor(Helper.colorUtils().transparency(color, 1));
		else
			Helper.colorUtils().glColor(color);

		GL11.glLineWidth(isSpecial ? 3 : 1.5f);
		GL11.glBegin(1);
		GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
		GL11.glVertex3d(x, y, z);
		GL11.glEnd();
		GL11.glBegin(1);
		GL11.glVertex3d(x, y, z);
		GL11.glVertex3d(x, y + (entityesp ? 0 : 1.2), z);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();

	}
}
