package nivia.modules.render;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.optifine.Config;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.Event3D;
import nivia.managers.FriendManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.RenderUtils;
import nivia.utils.utils.RenderUtils.Camera;
import nivia.utils.utils.RenderUtils.Stencil;
import shadersmod.client.Shaders;

public class ESP extends Module {

	//Huge thanks to Zeb / valkyrie.

	public ESP() {
		super("ESP", 0, 0, Category.RENDER, "Draws a box around entities.", new String[]{"entityesp", "mobesp", "playeresp", "animalesp" , "eesp"}, false);
		try{
			normalS = RenderUtils.ShaderUtil.createShader("void main( void ) {\n" +
					"\tgl_FragColor = vec4(0.0,1,0.3, 0.7);\n" +
					"}", RenderUtils.ShaderUtil.TYPE_FRAG);
			friendS = RenderUtils.ShaderUtil.createShader("void main( void ) {\n" +
					"\tgl_FragColor = vec4(0, 1, 1, 0.7);\n" +
					"}", RenderUtils.ShaderUtil.TYPE_FRAG);
			enemyS = RenderUtils.ShaderUtil.createShader("void main( void ) {\n" +
					"\tgl_FragColor = vec4(1,0,0,0.7);\n" +
					"}", RenderUtils.ShaderUtil.TYPE_FRAG);
			miscS = RenderUtils.ShaderUtil.createShader("void main( void ) {\n" +
					"\tgl_FragColor = vec4(1,1,1,0.7);\n" +
					"}", RenderUtils.ShaderUtil.TYPE_FRAG);
		} catch(Exception e){e.printStackTrace();}
		setState(false);
	}
	public Property<Boolean> animals = new Property<Boolean>(this, "Animals" , false);
	public Property<Boolean> monsters = new Property<Boolean>(this, "Monsters" , false);
	public Property<Boolean> players = new Property<Boolean>(this, "Players" , true);
	public Property<Boolean> items = new Property<Boolean>(this, "Items" , true);
	public Property<Boolean> invisibles = new Property<Boolean>(this, "Invisibles", true);
	public Property<ESPMode> mode = new Property<ESPMode>(this, "Mode" , ESPMode.BOX);

	public static enum ESPMode {
		OBOX, OUTLINE, BOX, FRAME;
	}
	private int normalS = -1;
	private int friendS = -1;
	private int enemyS = -1;
	private int miscS = -1;
	
	
	@Override
	public void onEnable(){
		super.onEnable();
		if(mode.value.equals(ESPMode.OUTLINE) && Shaders.isShaderPackInitialized) {
			mode.value = ESPMode.BOX;
			Logger.logChat("Outline esp does not support shaders.");
		}
		if((mode.value.equals(ESPMode.OUTLINE) || mode.value.equals(ESPMode.OBOX)) && Config.isFastRender()) 
			mc.gameSettings.ofFastRender = false;
		
	}

	@EventTarget(Priority.HIGH)
	public void onRender3D(Event3D e){
		if(mode.value.equals(ESPMode.OUTLINE) && Shaders.isShaderPackInitialized) {
			mode.value = ESPMode.BOX;
			Logger.logChat("Outline esp does not support shaders.");
		}
		this.setSuffix(mode.value.toString());
		for(Object entity : mc.theWorld.loadedEntityList){
			Entity ent = (Entity) entity;
			final double posX = ent.lastTickPosX
					+ (ent.posX - ent.lastTickPosX)
					* e.getPartialTicks() - RenderManager.renderPosX;
			final double posY = ent.lastTickPosY
					+ (ent.posY - ent.lastTickPosY)
					* e.getPartialTicks() - RenderManager.renderPosY;
			final double posZ = ent.lastTickPosZ
					+ (ent.posZ - ent.lastTickPosZ)
					* e.getPartialTicks() - RenderManager.renderPosZ;
			if(isValidTarget(ent) && mode.value.equals(ESPMode.BOX)) {
				GL11.glPushMatrix();
				GL11.glTranslated(posX, posY, posZ);
				GL11.glRotatef(-ent.rotationYaw, 0.0F, ent.height, 0.0F);
				GL11.glTranslated(-posX, -posY, -posZ);
				mc.entityRenderer.setupCameraTransform(e.getPartialTicks(), 0  );
				RenderUtils.R3DUtils.RenderLivingEntityBox((Entity) entity, e.getPartialTicks(), false);
				GL11.glPopMatrix();
			}
			if(isValidTarget(ent) && mode.value.equals(ESPMode.FRAME)){
				GL11.glPushMatrix();
				GL11.glTranslated(posX, posY, posZ);
				GL11.glRotatef(-ent.rotationYaw, 0.0F, ent.height, 0.0F);
				GL11.glTranslated(-posX, -posY, -posZ);
				mc.entityRenderer.setupCameraTransform(e.getPartialTicks(), 0  );
				RenderUtils.R3DUtils.RenderLivingEntityBox((Entity) entity, e.getPartialTicks(), true);
				GL11.glPopMatrix();

			}
		}
		if(!mode.value.equals(ESPMode.OBOX)) return;
		RenderUtils.Stencil.getInstance().checkSetupFBO();
		if(mode.value == ESPMode.OBOX) drawCombinedBoxes(e.getPartialTicks(), false);
		mc.getFramebuffer().bindFramebuffer(true);
		mc.getFramebuffer().bindFramebufferTexture();
	}
	public int getColor(Entity entity){
		if (FriendManager.isFriend(entity.getName()))
			return 0xFF11AACC;
		else {
			if (entity instanceof EntityPlayer || entity instanceof EntityItem)
				return 0xFF00FF00;
			else if (entity instanceof EntityMob)
				return 0xFFFF0000;
			else if (entity instanceof EntityAnimal)
				return 0xFFCCAA00;
		}
		return 0xFFFFFFFF;
	}
	
	public static boolean isLemonESP() {		
		ESP esp = (ESP) Pandora.getModManager().getModule(ESP.class);
		return esp.mode.value.equals(ESPMode.OUTLINE) && !Shaders.isShaderPackInitialized && esp.getState();
	}
	
	public boolean isValidTarget(Entity e){
		if(!(e instanceof EntityAnimal) && !(e instanceof EntityMob) && !(e instanceof EntityPlayer) && !(e instanceof EntityItem)) return false;
		if ((!animals.value) && ((e instanceof EntityAnimal))) return false;
		if ((!players.value || e instanceof EntityPlayerSP) && ((e instanceof EntityPlayer))) return false;
		if ((!monsters.value)&& ((e instanceof EntityMob))) return false;
		if ((!items.value)&& ((e instanceof EntityItem))) return false;
		if(e.isInvisible() && !invisibles.value) return false;
		return true;
	}
	
	
	public void drawCombinedBoxes(final float partialTicks, boolean shaders) {
		final int entityDispList = GL11.glGenLists(1);
		Stencil.getInstance().startLayer();
		GL11.glPushMatrix();
		mc.entityRenderer.setupCameraTransform(partialTicks, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL11.GL_CULL_FACE);
		final Camera playerCam = new Camera(Minecraft.getMinecraft().thePlayer);
		final Frustrum frustrum = new Frustrum();
		frustrum.setPosition(playerCam.getPosX(), playerCam.getPosY(), playerCam.getPosZ());
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		Stencil.getInstance().setBuffer(true);
		GL11.glNewList(entityDispList, 4864);
		for (final Object obj : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			final Entity entity = (Entity) obj;
			if (entity == Minecraft.getMinecraft().thePlayer || !isValidTarget(entity))
				continue;
			GL11.glLineWidth(3);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			final Camera entityCam = new Camera(entity);
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glTranslated(entityCam.getPosX() - playerCam.getPosX(), entityCam.getPosY() - playerCam.getPosY(), entityCam.getPosZ() - playerCam.getPosZ());
			final Render entityRender = mc.getRenderManager().getEntityRenderObject(entity);
			if (entityRender != null) {
				if (entity instanceof EntityLivingBase || entity instanceof EntityItem) {
					if (FriendManager.isFriend(entity.getName())) GL11.glColor4f(0, 1, 1, 1);
					else {
						if (entity instanceof EntityPlayer || entity instanceof EntityItem) Helper.colorUtils().glColor(Helper.colorUtils().getRainbow(1, 1));
						else if (entity instanceof EntityMob) Helper.colorUtils().glColor(0x99992EE8);
						else if (entity instanceof EntityAnimal) Helper.colorUtils().glColor(0x990EFFA2);
					}
					final double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.renderPosX;
					final double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.renderPosY;
					final double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.renderPosZ;
					if (!shaders) GlStateManager.disableLighting();
					Helper.get3DUtils().drawBoundingBox(new AxisAlignedBB(
									entity.boundingBox.minX - entity.posX + (posX),
									entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + (posY),
									entity.boundingBox.minZ - entity.posZ + (posZ),
									entity.boundingBox.maxX - entity.posX + (posX),
									entity.boundingBox.maxY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY - 0.1) + (posY),
									entity.boundingBox.maxZ - entity.posZ + (posZ)));
					if (!shaders)
						GlStateManager.enableLighting();
				}
			}
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
		GL11.glColor3d(1, 1, 1);
		GL11.glEndList();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glCallList(entityDispList);
		Stencil.getInstance().setBuffer(false);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glCallList(entityDispList);
		Stencil.getInstance().cropInside();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glCallList(entityDispList);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		Minecraft.getMinecraft().entityRenderer.func_175072_h();
		RenderHelper.disableStandardItemLighting();
		Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		Stencil.getInstance().stopLayer();
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glPopMatrix();
		Minecraft.getMinecraft().entityRenderer.func_175072_h();
		RenderHelper.disableStandardItemLighting();
		Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		GL11.glDeleteLists(entityDispList, 1);
	}
	protected void addCommand(){
		Pandora.getCommandManager().cmds.add(new Command
				("ESP", "Manages ESP' values", Logger.LogExecutionFail("Option, Options:", new String[]{"Animals", "Players", "Monsters", "Outline", "Box", "OBox", "Frame"}) , "tr") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message.toLowerCase()) {
					case "animals":
					case "a":
						animals.value = !animals.value;
						Logger.logSetMessage("ESP", "Animals", animals);
						break;
					case "mobs":
					case "monsters":
					case "m":
						monsters.value = !monsters.value;
						Logger.logSetMessage("ESP", "Monsters", monsters);
						break;
					case "invis":
					case "i":
					case "invisibles":
						invisibles.value = !invisibles.value;
						Logger.logSetMessage("ESP", "Invisibles", invisibles);
						break;
					case "p":
					case "players":
						players.value = !players.value;
						Logger.logSetMessage("ESP", "Players", players);
						break;
					case "obox":
					case "ob":
						mode.value = ESPMode.OBOX;
						Logger.logSetMessage("ESP", "Mode", mode);
						break;
					case "outline":
					case "o":
						mode.value = ESPMode.OUTLINE;
						Logger.logSetMessage("ESP", "Mode", mode);
						break;
					case "box":
					case "b":
						mode.value = ESPMode.BOX;
						Logger.logSetMessage("ESP", "Mode", mode);
						break;
					case "frame":
					case "fr":
					case "f":
						mode.value = ESPMode.FRAME;
						Logger.logSetMessage("ESP", "Mode", mode);
						break;
					case "values":
						logValues();
						break;
					default:
						Logger.logChat(this.getError());
						break;
				}
			}});
	}
}
