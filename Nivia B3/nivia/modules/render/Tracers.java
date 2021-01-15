package nivia.modules.render;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.Event3D;
import nivia.managers.FriendManager;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tracers extends Module {
	public ArrayList<Entity> ents = new ArrayList<Entity>();
	public PropertyManager.DoubleProperty entities = new PropertyManager.DoubleProperty(this, "Entities", 50, 10, 150);
	public Property<Boolean> animals = new Property<Boolean>(this, "Animals", false);
	public Property<Boolean> monsters = new Property<Boolean>(this, "Monsters", false);
	public Property<Boolean> players = new Property<Boolean>(this, "Players", true);
	public Property<Boolean> invisibles = new Property<Boolean>(this, "Invisibles", true);
	public PropertyManager.DoubleProperty firstColor = new PropertyManager.DoubleProperty(this, "First Color", 0xFFFF0000, -999999999, 999999999, false, true);
	public PropertyManager.DoubleProperty lastColor = new PropertyManager.DoubleProperty(this, "Second Color", 0xFFFFFFFF, -999999999, 999999999, false,true);

	public Tracers() {
		super("Tracers", 0, 0, Category.RENDER, "Draws a line to nearby ents", new String[] { "tracer", "trcrs" },
				false);
	}

	@EventTarget
	public void onRender3D(Event3D e) {
		if(ents.size() > entities.getValue()) ents.clear();

		for (Object o : mc.theWorld.loadedEntityList) {
			Entity en = (Entity) o;
			if(ents.size() > entities.getValue()) break;
			if ((this.animals.value) && ((en instanceof EntityAnimal)) && (!this.ents.contains(en))) {
				this.ents.add(en);
			}
			if ((this.players.value) && ((en instanceof EntityPlayer))
					&& (!this.ents.contains(en) && !(en instanceof EntityPlayerSP))) {
				this.ents.add(en);
			}
			if ((this.monsters.value) && ((en instanceof EntityMob)) && (!this.ents.contains(en))) {
				this.ents.add(en);
			}
			if ((!this.animals.value) && (this.ents.contains(en)) && ((en instanceof EntityAnimal))) {
				this.ents.remove(en);
			}
			if ((!this.players.value) && (this.ents.contains(en)) && ((en instanceof EntityPlayer))) {
				this.ents.remove(en);
			}
			if ((!this.monsters.value) && (this.ents.contains(en)) && ((en instanceof EntityMob))) {
				this.ents.remove(en);
			}
			if(this.ents.contains(en) && en.isInvisible() && !invisibles.value) ents.remove(en);

		}
		for (final Entity entity : (List<Entity>) mc.theWorld.loadedEntityList) {
			if ((entity.isEntityAlive() && ents.contains(entity))) {
				
				final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks()
						- RenderManager.renderPosX;
				final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks()
						- RenderManager.renderPosY;
				final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks()
						- RenderManager.renderPosZ;
				boolean old = Helper.mc().gameSettings.viewBobbing;
				Helper.get3DUtils().startDrawing();
				Helper.mc().gameSettings.viewBobbing = false;
				Helper.mc().entityRenderer.setupCameraTransform(Helper.mc().timer.renderPartialTicks, 2);
				Helper.mc().gameSettings.viewBobbing = old;

				renderTracer(entity, posX, posY, posZ);
				Helper.get3DUtils().stopDrawing();
			}
		}
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

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Tracers", "Manages tracers' values",
				Logger.LogExecutionFail("Option, Options:", new String[] { "Animals", "Players", "Monsters", "Entities", "First Color", "Second color" }), "tr") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				String message2 = "";
				try {
					message2 = arguments[2];
				} catch (Exception e) {
				}
				switch (message) {
					case "animals":
					case "a":
					case "Animals":
						animals.value = !animals.value;
						Logger.logSetMessage("Tracers", "Animals", animals);
						break;
					case "Monsters":
					case "mobs":
					case "monsters":
					case "m":
						monsters.value = !monsters.value;
						Logger.logSetMessage("Tracers", "Monsters", monsters);
						break;
					case "ent":case"e":
					case "entities":
						switch(message2){
							case "actual":
								logValue(entities);
								break;
							case "reset":
								entities.reset();
								break;
							default:
								int newScale = Integer.parseInt(message2);
								entities.setValue(newScale);
								Logger.logSetMessage("Nametags", "Entities", entities);
								break;
						}
						break;
					case "Players":
					case "p":
					case "players":
						players.value = !players.value;
						Logger.logSetMessage("Tracers", "Players", players);
						break;
					case "fcolor":
					case "fc":
					case "firstcolor":
					case "fcolour":
						try {
							int RHex = Integer.valueOf(arguments[2]);
							int G = Integer.valueOf(arguments[3]);
							int B = Integer.valueOf(arguments[4]);
							if (G > 255) G = 255;
							if (B > 255) B = 255;
							if (RHex > 255) RHex = 255;
							java.awt.Color c = new java.awt.Color(RHex, G, B);
							Logger.logChat("Set \2476first \2477color to \2476" + String.format("%s %s %s or %s", c.getRed(), c.getGreen(), c.getBlue(), c.getRGB()));
							firstColor.setValue(c.getRGB());
							break;
						} catch (Exception e) {
							try {
								int RHex = Integer.parseInt(arguments[2], 16);
								java.awt.Color c = new java.awt.Color(RHex);
								Logger.logChat("Set \2476first \2477color to \2476" + String.format("%s %s %s or %s", c.getRed(), c.getGreen(), c.getBlue(), c.getRGB()));
								firstColor.setValue(c.getRGB());
							} catch (Exception ex) {
								Logger.logChat("Could not apply the specified color!");
								break;
							}
						}
						break;
					case "scolor":
					case "sc":
					case "secondcolor":
					case "scolour":
						try {
							int RHex = Integer.valueOf(arguments[2]);
							int G = Integer.valueOf(arguments[3]);
							int B = Integer.valueOf(arguments[4]);
							if (G > 255) G = 255;
							if (B > 255) B = 255;
							if (RHex > 255) RHex = 255;
							java.awt.Color c = new java.awt.Color(RHex, G, B);
							lastColor.setValue(c.getRGB());
							Logger.logChat("Set \2476second \2477color to \2476" + String.format("%s %s %s or %s", c.getRed(), c.getGreen(), c.getBlue(), c.getRGB()));
							break;
						} catch (Exception e) {
							try {
								int RHex = Integer.parseInt(arguments[2], 16);
								java.awt.Color c = new java.awt.Color(RHex);
								lastColor.setValue(c.getRGB());
								Logger.logChat("Set \2476second \2477color to \2476" + String.format("%s %s %s or %s", c.getRed(), c.getGreen(), c.getBlue(), c.getRGB()));
							} catch (Exception ex) {
								Logger.logChat("Could not apply the specified color!");
								break;
							}
						}
						break;
					default:
						Logger.logChat(this.getError());
						break;
				}
			}
		});
	}

}
