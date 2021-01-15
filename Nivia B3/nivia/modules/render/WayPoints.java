package nivia.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.Event3D;
import nivia.events.events.EventDeath;
import nivia.files.modulefiles.Waypoints;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.RenderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WayPoints extends Module {

	public WayPoints() {
		super("WayPoints", 0, 0, Category.RENDER, "Marks a spot on coords", new String[] { "wpoints", "wp" }, false);
	}

	/**
	 * Thx to Matthew
	 */
	public static List<Waypoint> waypoints = new ArrayList<>();
	public Property<Boolean> boxes = new Property<Boolean>(this, "Boxes", true);
	public Property<Boolean> nametags = new Property<Boolean>(this, "Nametags", true);
	public Property<Boolean> death = new Property<Boolean>(this, "On Death", false);
	boolean dead;

	@EventTarget(Priority.HIGHEST)
	public void onRender3D(Event3D e) {
		if (mc.isSingleplayer())
			return;
		RenderUtils.Stencil.getInstance().checkSetupFBO();
		Helper.get3DUtils().startDrawing();
		String server = getServer();
		for (Waypoint wp : waypoints) {
			if (!wp.getServer().equals(server))
				continue;
			if (wp.getDimension() != mc.thePlayer.dimension)
				continue;
			if (nametags.value)
				drawNameTag(wp);
		}
		if (boxes.value)
			drawWaypointOutline(e.getPartialTicks());
		Helper.get3DUtils().stopDrawing();
		mc.getFramebuffer().bindFramebuffer(true);
		mc.getFramebuffer().bindFramebufferTexture();
	}

	@EventTarget
	public void ondeath(EventDeath e) {
		if (!death.value)
			return;
		waypoints.add(new Waypoint("Death", getServer(), mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
				mc.thePlayer.dimension));
		try {
			Pandora.getFileManager().getFile(Waypoints.class).saveFile();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Logger.logChat(String.format("Death Waypoint added at %s, %s, %s", (float) mc.thePlayer.posX,
				(float) mc.thePlayer.posY, (float) mc.thePlayer.posZ));
	}

	private String getServer() {
		return (mc.getCurrentServerData() == null) ? "singleplayer" : mc.getCurrentServerData().serverIP;
	}

	private void drawNameTag(Waypoint wp) {
		double x = wp.getX() - mc.getRenderManager().renderPosX;
		double y = wp.getY() - mc.getRenderManager().renderPosY;
		double z = wp.getZ() - mc.getRenderManager().renderPosZ;
		double dist = mc.thePlayer.getDistance(wp.getX(), wp.getY(), wp.getZ());
		final String text = wp.getName() + " \247c" + Math.round(dist) + "m\247r";
		double far = this.mc.gameSettings.renderDistanceChunks * 12.8D;
		double dl = Math.sqrt(x * x + z * z + y * y);
		double d;
		if (dl > far) {
			d = far / dl;
			dist *= d;
			x *= d;
			y *= d;
			z *= d;
		}
		float var13 = ((float) dist / 5 <= 2 ? 2.0F : (float) dist / 10);
		float var14 = 0.016666668F * var13;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 2.5F, z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		if (mc.gameSettings.thirdPersonView == 2) {
			GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(mc.getRenderManager().playerViewX, -1.0F, 0.0F, 0.0F);
		} else {
			GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
		}
		GlStateManager.scale(-var14, -var14, var14);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		GlStateManager.func_179090_x();
		worldRenderer.startDrawingQuads();
		int var18 = mc.fontRendererObj.getStringWidth(text) / 2;
		worldRenderer.func_178960_a(0.0F, 0.0F, 0.0F, 0.3F);
		worldRenderer.addVertex(-var18 - 2, -2, 0.0D);
		worldRenderer.addVertex(-var18 - 2, 9, 0.0D);
		worldRenderer.addVertex(var18 + 2, 9, 0.0D);
		worldRenderer.addVertex(var18 + 2, -2, 0.0D);
		tessellator.draw();
		GlStateManager.func_179098_w();
		mc.fontRendererObj.drawStringWithShadow(text, -var18, 0, 0xFFFFFFFF);
		GlStateManager.popMatrix();
	}

	public static class Waypoint {
		private String name, server;
		private double x, y, z;
		private int dimension;

		public Waypoint(String name, String server, double x, double y, double z, int dimension) {
			this.name = name;
			this.server = server;
			this.x = x;
			this.y = y;
			this.z = z;
			this.dimension = dimension;
		}

		public String getName() {
			return name;
		}

		public String getServer() {
			return server;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getZ() {
			return z;
		}

		public int getDimension() {
			return dimension;
		}
	}

	public void drawWaypointOutline(float partialTicks) {
		final int entityDispList = GL11.glGenLists(1);
		RenderUtils.Stencil.getInstance().startLayer();
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL11.GL_CULL_FACE);
		final RenderUtils.Camera playerCam = new RenderUtils.Camera(Minecraft.getMinecraft().thePlayer);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		RenderUtils.Stencil.getInstance().setBuffer(true);
		GL11.glNewList(entityDispList, GL11.GL_COMPILE);
		GL11.glLineWidth(3);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		for (Waypoint wep : waypoints) {
			if (!wep.getServer().equals(getServer()))
				continue;
			if (wep.getDimension() != mc.thePlayer.dimension)
				continue;
			GlStateManager.disableLighting();
			double x = wep.getX() - mc.getRenderManager().renderPosX;
			double y = wep.getY() - mc.getRenderManager().renderPosY;
			double z = wep.getZ() - mc.getRenderManager().renderPosZ;
			double far = mc.gameSettings.renderDistanceChunks * 12.8D;
			double dl = Math.sqrt(x * x + z * z + y * y);
			double d;
			if (dl > (far - 40)) {
				d = far / dl;
				x *= d;
				y *= d;
				y -= 1.3;
				z *= d;
			}
			Helper.colorUtils().glColor(Helper.colorUtils().getRainbow(1, 1).getRGB(), 0.9F);
			Helper.get3DUtils().drawRombo(x, y, z);
			GlStateManager.enableLighting();
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		GL11.glColor3d(1, 1, 1);
		GL11.glEndList();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glCallList(entityDispList);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
		GL11.glCallList(entityDispList);
		RenderUtils.Stencil.getInstance().setBuffer(false);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glCallList(entityDispList);
		RenderUtils.Stencil.getInstance().cropInside();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glCallList(entityDispList);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
		GL11.glCallList(entityDispList);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		RenderHelper.disableStandardItemLighting();
		RenderUtils.Stencil.getInstance().stopLayer();
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDeleteLists(entityDispList, 1);
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("WayPoints", "Manages waypoints",
				Logger.LogExecutionFail("Option, Options:",
						new String[] { "Add", "Delete", "Death", "Here", "Boxes", "Nametags" }),
				"wp", "wpoints", "wpoint", "wayp", "waypoint") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				String name = "", x = "", y = "", z = "";
				try {
					name = arguments[2];
					x = arguments[3];
					y = arguments[4];
					z = arguments[5];
				} catch (Exception e) {
				}
				switch (message.toLowerCase()) {
					case "add":
					case "a":
						waypoints.add(new Waypoint(name, getServer(), Double.parseDouble(x), Double.parseDouble(y),
								Double.parseDouble(z), mc.thePlayer.dimension));
						Logger.logChat("Added waypoint \247b" + name + " \2477at: \247b" + x + " " + y + " " + z);
						try {
							Pandora.getFileManager().getFile(Waypoints.class).saveFile();
						} catch (IOException e) {
						}
						break;
					case "here":
					case "h":
						waypoints.add(new Waypoint("Pos" + waypoints.size(), getServer(), mc.thePlayer.posX, mc.thePlayer.posY,
								mc.thePlayer.posZ, mc.thePlayer.dimension));
						Logger.logChat("Added waypoint on the current Position");
						try {
							Pandora.getFileManager().getFile(Waypoints.class).saveFile();
						} catch (IOException e) {
						}
						break;
					case "delete":
					case "d":
					case "r":
					case "remove":
					case "del":
						for (Waypoint wp : waypoints) {
							if (wp.getName().equalsIgnoreCase(name)) {
								waypoints.remove(wp);
								Logger.logChat("Removed Waypoint: \247b" + name);
								try {
									Pandora.getFileManager().getFile(Waypoints.class).saveFile();
								} catch (IOException e) {
								}
								break;
							}
						}
						break;
					case "clear":
					case "c":
						Logger.logChat("Cleared \247b" + waypoints.size() + " \2477waypoints.");
						waypoints.clear();
						try {
							Pandora.getFileManager().getFile(Waypoints.class).saveFile();
						} catch (IOException e) {
						}
						break;
					case "boxes":
					case "box":
					case "esp":
					case "b":
						boxes.value = !boxes.value;
						Logger.logSetMessage("Waypoints", "Boxes", boxes);
						break;
					case "death":
					case "dead":
					case "ded":
						death.value = !death.value;
						Logger.logSetMessage("Waypoints", "Death Waypoints", death);
						break;
					case "nametags":
					case "tags":
					case "nt":
					case "t":
					case "namet":
					case "ntags":
						nametags.value = !nametags.value;
						Logger.logSetMessage("Waypoints", "Nametags", nametags);
						break;
					case "values":
						logValues();
						break;
					default:
						Logger.logChat(this.getError());
						break;
				}
			}
		});
	}
}
