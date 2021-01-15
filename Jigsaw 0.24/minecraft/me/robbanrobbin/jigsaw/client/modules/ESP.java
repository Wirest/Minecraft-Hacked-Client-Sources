package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.modules.target.Team;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.hackerdetect.Hacker;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;

public class ESP extends Module {

	@Override
	public ModSetting[] getModSettings() {
//		final BasicCheckButton box1 = new BasicCheckButton("Players");
//		box1.setSelected(ClientSettings.playerESP);
//		box1.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.playerESP = box1.isSelected();
//			}
//		});
//
//		final BasicCheckButton box2 = new BasicCheckButton("Mobs");
//		box2.setSelected(ClientSettings.mobsESP);
//		box2.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.mobsESP = box2.isSelected();
//			}
//		});
//
//		final BasicCheckButton box3 = new BasicCheckButton("Animals");
//		box3.setSelected(ClientSettings.animalESP);
//		box3.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.animalESP = box3.isSelected();
//			}
//		});
//
//		final BasicCheckButton box4 = new BasicCheckButton("Blockhunt");
//		box4.setSelected(ClientSettings.blockHuntESP);
//		box4.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.blockHuntESP = box4.isSelected();
//			}
//		});
//
//		final BasicCheckButton box5 = new BasicCheckButton("Chests");
//		box5.setSelected(ClientSettings.chestESP);
//		box5.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.chestESP = box5.isSelected();
//			}
//		});
//		BasicLabel label1 = new BasicLabel("Extra:");
//		
//		final BasicCheckButton box6 = new BasicCheckButton("ESP Fade");
//		box6.setSelected(ClientSettings.espFade);
//		box6.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.espFade = box6.isSelected();
//			}
//		});
		CheckBtnSetting box1 = new CheckBtnSetting("Players", "playerESP");
		CheckBtnSetting box2 = new CheckBtnSetting("Mobs", "mobsESP");
		CheckBtnSetting box3 = new CheckBtnSetting("Animals", "animalESP");
		CheckBtnSetting box4 = new CheckBtnSetting("BlockHunt", "blockHuntESP");
		CheckBtnSetting box5 = new CheckBtnSetting("Chests", "chestESP");
		CheckBtnSetting box6 = new CheckBtnSetting("ESP Fade", "espFade");
		return new ModSetting[] { box1, box2, box3, box4, box5, box6 };
	}

	public ESP() {
		super("ESP", Keyboard.KEY_NONE, Category.RENDER, "Renders a box on entities.");
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onRender() {

		for (Object o : mc.theWorld.loadedEntityList) {
			if (ClientSettings.blockHuntESP) {
				Entity en = (Entity) o;
				if (en.isInvisible() && en instanceof EntityLiving && en.height < 0.5) {
					drawProphuntESP(0.4f, 0.4f, 1f, 1f, en);
				}
			}
			if (!this.currentMode.equals("Box")) {
				break;
			}
			if (ClientSettings.playerESP) {
				if (!(o instanceof EntityPlayerSP)) {
					if (o instanceof EntityPlayer) {
						EntityPlayer en = (EntityPlayer) o;
						if (Jigsaw.getFriendsMananger().isFriend(en)) {
							drawESP(0.0f, 1f, 1f, 1f, en);
							continue;
						}
						if(Utils.isBlacklisted(en)) {
							drawESP(0f, 0f, 0f, 1f, en);
						}
						else {
							if (Team.isOnTeam(en)) {
								drawESP(0.5f, 1f, 0.5f, 1f, en);
							} else {
								drawESP(1f, 1f, 1f, 1f, en);
							}
						}
					}
				}
			}
			if (ClientSettings.mobsESP) {
				if (o instanceof IMob) {
					Entity en = (Entity) o;
					drawMob(1, 0.1f, 0.5f, 1f, en);
				}

			}
			if (ClientSettings.animalESP) {
				if (o instanceof EntityAnimal) {
					Entity en = (Entity) o;
					drawMob(1, 1f, 0.5f, 1f, en);
				}
			}
		}
		if (ClientSettings.chestESP) {
			for (Object o : mc.theWorld.loadedTileEntityList) {
				if (!(o instanceof TileEntity)) {
					continue;
				}
				BlockPos blockPos = new BlockPos(((TileEntity) o).getPos());

				double x = blockPos.getX() - mc.getRenderManager().renderPosX;
				double y = blockPos.getY() - mc.getRenderManager().renderPosY;
				double z = blockPos.getZ() - mc.getRenderManager().renderPosZ;
				if (o instanceof TileEntityChest) {

					RenderTools.drawOutlinedBlockESP(x, y, z, 0.4f, 1f, 0.7f, 1f, 1f);
					RenderTools.drawSolidBlockESP(x, y, z, 0.4f, 1f, 0.7f, 0.2f);
				}
				if (o instanceof TileEntityEnderChest) {
					RenderTools.drawOutlinedBlockESP(x, y, z, 1f, 0.3f, 1f, 1f, 1f);
					RenderTools.drawSolidBlockESP(x, y, z, 1f, 0.3f, 1f, 0.2f);
				}
			}
		}

		super.onRender();
	}

	public void drawESP(float red, float green, float blue, float alpha, Entity e) {
		if (e instanceof EntityPlayer && !Jigsaw.getFriendsMananger().isFriend((EntityPlayer) e)) {
			EntityPlayer player = (EntityPlayer) e;
			Hacker hacker = HackerDetect.players.get(player.getName());
			if (hacker != null && hacker.getViolations() > 0) {
				red = 1f;
				green = 0.5f;
				blue = 0.5f;
			}
		}
		if (AuraUtils.hasEntity(e)) {
			red = 1;
			green = 0;
			blue = 0;
		}

		double xPos = (e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosX;
		double yPos = (e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosY;
		double zPos = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosZ;
		red -= (double)e.hurtResistantTime / 30d;
		green -= (double)e.hurtResistantTime / 30d;
		blue -= (double)e.hurtResistantTime / 30d;
		if(false) {
			if(ClientSettings.espFade) {
				RenderTools.drawSolidEntityESP(xPos, yPos, zPos, e.width / 2, e.height, 1f, 0.5f, 0.5f, Math.min(0.2f, mc.thePlayer.getDistanceToEntity(e) / 30f));
				RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, (mc.thePlayer.getDistanceToEntity(e) / 30f));
				
			}
			else {
				RenderTools.drawSolidEntityESP(xPos, yPos, zPos, e.width / 2, e.height, 1f, 0.5f, 0.5f, 0.2f);
				RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, alpha);
				
			}
		}
		else {
			if(ClientSettings.espFade) {
				RenderTools.drawSolidEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, Math.min(0.2f, mc.thePlayer.getDistanceToEntity(e) / 30f));
				RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, (mc.thePlayer.getDistanceToEntity(e) / 30f));
				
			}
			else {
				RenderTools.drawSolidEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, 0.2f);
				RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, e.width / 2, e.height, red, green, blue, alpha);
				
			}
		}
		
		
	}

	public void drawProphuntESP(float red, float green, float blue, float alpha, Entity e) {
		if (AuraUtils.hasEntity(e)) {
			red = 1;
			green = 0;
			blue = 0;
		}

		double xPos = (e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosX;
		double yPos = (e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosY;
		double zPos = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.timer.renderPartialTicks)
				- mc.getRenderManager().renderPosZ;

		RenderTools.drawOutlinedBlockESP(xPos - 0.5, yPos, zPos - 0.5f, red, green, blue, alpha, 2);
		RenderTools.drawSolidBlockESP(xPos - 0.5, yPos, zPos - 0.5f, red, green, blue, 0.3f);
	}

	public void drawMob(float red, float green, float blue, float alpha, Entity e) {
		drawESP(red, green, blue, alpha, e);
	}

	@Override
	public String[] getModes() {
		return new String[] { "Box" };
	}

	@Override
	public String getAddonText() {
		if(ClientSettings.espFade) {
			return this.currentMode + ", " + "Fade";
		}
		else {
			return this.currentMode;
		}
		
	}

}
