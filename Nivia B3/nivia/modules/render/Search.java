package nivia.modules.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.Event3D;
import nivia.events.events.EventBlockRender;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.RenderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.minecraft.client.renderer.entity.RenderManager.*;

public class Search extends Module {
	public final ArrayList<Block> blocks = new ArrayList<>();
	public final List<Integer[]> blockCoordinates = new CopyOnWriteArrayList<>();
	private Property<Boolean> outline = new Property<Boolean>(this, "Outline", false);
	private Property<Boolean> skull = new Property<Boolean>(this, "Skulls", false);


	public DoubleProperty reach = new DoubleProperty(this, "Reach", 128, 32, 1024);
	public DoubleProperty limit = new DoubleProperty(this, "Limit", 100, 32, 1024);

	public Search() {
		super("Search", 0, 0, Category.RENDER, "Draws block's outlines.",
				new String[] { "search", "blockesp", "besp", "xray"}, false);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		mc.renderGlobal.loadRenderers();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		blockCoordinates.clear();
	}



	@EventTarget
	private void onBlockRender(EventBlockRender render) {
		if (blocks.isEmpty()) {
			return;
		}
		final int x = render.getX();
		final int y = render.getY();
		final int z = render.getZ();
		final Block block = Helper.blockUtils().getBlock(new BlockPos(x, y, z));
		if (!areCoordsLoaded(x, y, z) && blocks.contains(block)) {
			if (blockCoordinates.size() < limit.getValue())
				blockCoordinates.add(new Integer[] { x, y, z });
		}
	}

	@EventTarget
	private void onRender3D(Event3D e) {
		if (blocks.size() > 100)
			return;
		RenderUtils.Stencil.getInstance().checkSetupFBO();
		Helper.mc().entityRenderer.setupCameraTransform(Helper.mc().timer.renderPartialTicks, 0);
		this.drawBlockOutlines(e.getPartialTicks());
		
		mc.getFramebuffer().bindFramebuffer(true);
		mc.getFramebuffer().bindFramebufferTexture();

	}

	

	private void renderESP(Integer[] coords, Block block) {
		if (!blocks.contains(block))
			return;
		if(outline.value){			
			if(blocks.contains(Blocks.skull)) {
				skull.value = true;
				blocks.remove(Blocks.skull);
			}
		}
		final double x = (coords[0]) - renderPosX;
		final double y = (coords[1]) - renderPosY;
		final double z = (coords[2]) - renderPosZ;
		double[] maxBounds = new double[] { block.getBlockBoundsMaxX(), block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ() };
		if(outline.value)
		Helper.get3DUtils().drawBoundingBox(new AxisAlignedBB(x + block.getBlockBoundsMinX(), y, z + block.getBlockBoundsMinZ(), x + maxBounds[0], y + maxBounds[1], z + maxBounds[2]));
		else
		Helper.get3DUtils().drawOutlinedBox(new AxisAlignedBB(x + block.getBlockBoundsMinX(), y, z + block.getBlockBoundsMinZ(), x + maxBounds[0], y + maxBounds[1], z + maxBounds[2]));

	}

	private boolean areCoordsLoaded(double x, double y, double z) {
		for (final Integer[] block : blockCoordinates) {
			if (block[0] == x && block[1] == y && block[2] == z)
				return true;
		}
		return false;
	}

	public void drawBlockOutlines(float partialTicks) {
		if(!outline.value){
			Helper.get3DUtils().startDrawing();
			mc.entityRenderer.setupCameraTransform(partialTicks, 0);
			for (final Integer[] block1 : blockCoordinates) {
				Block block = Helper.blockUtils().getBlock(new BlockPos(block1[0], block1[1], block1[2]));
				GlStateManager.disableLighting();
				Helper.colorUtils().glColor(Helper.get3DUtils().getBlockColor(block));
				if (mc.thePlayer.getDistance(block1[0], block1[1], block1[2]) > reach.getValue() || !blocks.contains(block))
					blockCoordinates.remove(block1);
				else
					renderESP(block1, block);
				GlStateManager.enableLighting();
			}
			Helper.get3DUtils().stopDrawing();
			return;
		}
		final int entityDispList = GL11.glGenLists(1);
		RenderUtils.Stencil.getInstance().startLayer();
		GL11.glPushMatrix();
		mc.entityRenderer.setupCameraTransform(partialTicks, 0);
		GL11.glMatrixMode(5888);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(2884);
		final RenderUtils.Camera playerCam = new RenderUtils.Camera(Minecraft.getMinecraft().thePlayer);
		final Frustrum frustrum = new Frustrum();
		frustrum.setPosition(playerCam.getPosX(), playerCam.getPosY(), playerCam.getPosZ());
		GL11.glDisable(2929);
		GL11.glDepthMask(true);
		RenderUtils.Stencil.getInstance().setBuffer(true);
		GL11.glNewList(entityDispList, 4864);
		GL11.glLineWidth(3);
		GL11.glEnable(3042);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor3d(1, 1, 1);
		GL11.glDisable(3553);
		for (final Integer[] block1 : blockCoordinates) {
			Block block = Helper.blockUtils().getBlock(new BlockPos(block1[0], block1[1], block1[2]));
			GlStateManager.disableLighting();
			Helper.colorUtils().glColor(Helper.get3DUtils().getBlockColor(block));
			if (mc.thePlayer.getDistance(block1[0], block1[1], block1[2]) > reach.getValue() || !blocks.contains(block))
				blockCoordinates.remove(block1);
			else
				renderESP(block1, block);
			GlStateManager.enableLighting();
		}
		GL11.glEnable(3553);
		GL11.glPopMatrix();
		GL11.glColor3d(1, 1, 1);
		GL11.glEndList();
		GL11.glPolygonMode(1032, 6913);
		GL11.glCallList(entityDispList);
		GL11.glPolygonMode(1032, 6912);
		GL11.glCallList(entityDispList);
		RenderUtils.Stencil.getInstance().setBuffer(false);
		GL11.glPolygonMode(1032, 6914);
		GL11.glCallList(entityDispList);
		RenderUtils.Stencil.getInstance().cropInside();
		GL11.glPolygonMode(1032, 6913);
		GL11.glCallList(entityDispList);
		GL11.glPolygonMode(1032, 6912);
		GL11.glCallList(entityDispList);
		GL11.glPolygonMode(1032, 6914);
		RenderHelper.disableStandardItemLighting();
		RenderUtils.Stencil.getInstance().stopLayer();
		GL11.glDisable(2960);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDeleteLists(entityDispList, 1);
	}

	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command("Search", "Manages Search",
				Logger.LogExecutionFail("Option, Options:",
						new String[]{"Add", "Remove", "Clear", "Range", "Limit", "List"}),
				"Serch", "blockesp", "besp", "xray") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1], message2 = "";
				try {
					message2 = arguments[2];
				} catch (Exception e) {
				}
				switch (message.toLowerCase()) {
					
					case "outline":
					case "o":
					case "outl":
					case "oline":
						outline.value = !outline.value;
						Logger.logSetMessage("Search", "Outline", outline);
						break;
					case "skull":
					case "sk":
						skull.value = !skull.value;
						Logger.logSetMessage("Search", "Skull", skull);
						break;
					case "add":
						Block block = Helper.blockUtils().getBlockByIDorName(message2);
						if (Objects.isNull(block) || blocks.contains(block)) {
							Logger.LogExecutionFail("Block");
							return;
						}
					
						if (block instanceof BlockSkull && outline.value) {
							skull.value = true;
							return;
						}
						blocks.add(block);
						mc.renderGlobal.loadRenderers();
						Logger.logChat("Added " + EnumChatFormatting.AQUA + block.getLocalizedName() + EnumChatFormatting.GRAY + " to search's list.");
						break;
					case "del":
					case "delete":
					case "remove":
						block = Helper.blockUtils().getBlockByIDorName(message2);
						if (Objects.isNull(block) || !blocks.contains(block)) {
							Logger.LogExecutionFail("Block");
							return;
						}
						
						if (block instanceof BlockSkull && outline.value) {
							skull.value = false;
							return;
						}
						blocks.remove(block);
						Logger.logChat("Removed " + EnumChatFormatting.AQUA + block.getLocalizedName()
								+ EnumChatFormatting.GRAY + " from search's list.");
						break;
					case "clear":
					case "empty":
						blocks.clear();
						Logger.logChat("Cleared search's blocks.");
						break;
					case "range":
					case "reach":
						switch (message2) {
							case "actual":
								logValue(reach);
								break;
							case "reset":
								reach.reset();
								break;
							default:
								reach.setValue(Integer.parseInt(message2));
								Logger.logSetMessage("Search", "reach", reach);
								break;
						}
						break;
					case "limit":
					case "size":
						switch (message2) {
							case "actual":
								logValue(limit);
								break;
							case "reset":
								limit.reset();
								break;
							default:
								limit.setValue(Integer.parseInt(message2));
								Logger.logSetMessage("Search", "limit", limit);
								break;
						}
						break;
					case "list":case "l":
						if(blocks.size() == 0) {
							Logger.logChat("No blocks added");
							break;
						}
						Logger.logChat("Current items on search:");
						for(Block b : blocks) {
							Logger.logChat(EnumChatFormatting.GOLD + b.getLocalizedName());
						}
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
