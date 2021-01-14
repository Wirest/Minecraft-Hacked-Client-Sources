/**
 * Time: 6:29:26 PM
 * Date: Jan 3, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.render;

import java.awt.Color;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;

/**
 * @author cool1
 */
public class Lines extends Module {

    public static String CHEST = "CHEST";
    public static String SIGN = "SIGN";
    public static String SPAWNER = "SPAWNER";
    public static String PLAYER = "PLAYER";
    private final String COLORMODE = "COLORMODE";
    float h;
    /**
     * @param data
     */
    public Lines(ModuleData data) {
        super(data);
        settings.put(SIGN, new Setting<>(SIGN, false, "Draw lines at signs."));
        settings.put(CHEST, new Setting<>(CHEST, false, "Draw lines at chests."));
        settings.put(PLAYER, new Setting<>(PLAYER, true, "Draw lines at players."));
        settings.put(SPAWNER, new Setting<>(SPAWNER, false, "Draw lines at spawners."));
        settings.put(COLORMODE, new Setting<>(COLORMODE, new Options("Color", "Rainbow", new String[]{"Rainbow", "Team", "Health", "Custom"}), "ESP color."));
    }

    /* (non-Javadoc)
     * @see EventListener#onEvent(Event)
     */
    @Override
    @RegisterEvent(events = {EventRender3D.class})
    public void onEvent(Event event) {
    	String colormode = ((Options)settings.get(COLORMODE).getValue()).getSelected();
        EventRender3D er = (EventRender3D) event;
        if (h > 255) {
			h = 0;
		}
        h+= 0.1;
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityPlayer && ((Boolean) settings.get(PLAYER).getValue())) {
                EntityPlayer ent = (EntityPlayer) o;
                if (ent != mc.thePlayer && !ent.isInvisible()) {
                	
                	int renderColor = Colors.getColor(Client.cm.getESPColor().getRed(), Client.cm.getESPColor().getGreen(), Client.cm.getESPColor().getBlue(), 255);
                	switch(colormode){
                	case"Rainbow":
                		final Color color = Color.getHSBColor(h / 255.0f, 0.6f, 1.0f);
            			renderColor = color.getRGB();
                		break;
                	case"Team":
                		String text = ent.getDisplayName().getFormattedText();
	                	if(Character.toLowerCase(text.charAt(0)) == '§'){
	                    	char oneMore = Character.toLowerCase(text.charAt(1));
	                    	int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
	                    	if (colorCode < 16) {
	                            try {
	                                int newColor = mc.fontRendererObj.colorCode[colorCode];   
	                                 renderColor = Colors.getColor((newColor >> 16), (newColor >> 8 & 0xFF), (newColor & 0xFF), 255);
	                            } catch (ArrayIndexOutOfBoundsException ignored){}
	                        }
	                	}else{
	                		renderColor = Colors.getColor(255, 255, 255, 255);
	                	}
                		break;
                	case"Health":{
                    	float health = ent.getHealth();
                        if (health > 20) {
                            health = 20;
                        }
                        float[] fractions = new float[]{0f, 0.5f, 1f};
                        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                        float progress = (health * 5) * 0.01f;
                        Color customColor = ESP.blendColors(fractions, colors, progress).brighter();
                        renderColor = customColor.getRGB();
                    }
                    break;
                	}
                    float posX = (float) ((float) (ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * er.renderPartialTicks) - RenderManager.renderPosX);
                    float posY = (float) ((float) (ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * er.renderPartialTicks) - RenderManager.renderPosY);
                    float posZ = (float) ((float) (ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * er.renderPartialTicks) - RenderManager.renderPosZ);
                    final boolean bobbing = mc.gameSettings.viewBobbing;
                    mc.gameSettings.viewBobbing = false;
                    RenderingUtil.draw3DLine(posX, posY, posZ, renderColor);
                    mc.gameSettings.viewBobbing = bobbing;
                }
            }
        }
        for (Object o : mc.theWorld.loadedTileEntityList) {
            int color = -1;
            if (o instanceof TileEntityChest && ((Boolean) settings.get(CHEST).getValue())) {
                TileEntityChest ent = (TileEntityChest) o;
                color = Colors.getColor(114, 0, 187);
                float posX = (float) ((float) ent.getPos().getX() - RenderManager.renderPosX);
                float posY = (float) ((float) ent.getPos().getY() - RenderManager.renderPosY);
                float posZ = (float) ((float) ent.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                mc.gameSettings.viewBobbing = bobbing;
            }

            if (o instanceof TileEntityMobSpawner && ((Boolean) settings.get(SPAWNER).getValue())) {
                TileEntityMobSpawner ent = (TileEntityMobSpawner) o;
                color = Colors.getColor(255, 156, 0);
                float posX = (float) ((float) ent.getPos().getX() - RenderManager.renderPosX);
                float posY = (float) ((float) ent.getPos().getY() - RenderManager.renderPosY);
                float posZ = (float) ((float) ent.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                mc.gameSettings.viewBobbing = bobbing;
            }

            if (o instanceof TileEntitySign && ((Boolean) settings.get(SIGN).getValue())) {
                TileEntitySign ent = (TileEntitySign) o;
                color = Colors.getColor(130, 162, 0);
                float posX = (float) ((float) ent.getPos().getX() - RenderManager.renderPosX);
                float posY = (float) ((float) ent.getPos().getY() - RenderManager.renderPosY);
                float posZ = (float) ((float) ent.getPos().getZ() - RenderManager.renderPosZ);
                final boolean bobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                RenderingUtil.draw3DLine(posX, posY, posZ, color);
                mc.gameSettings.viewBobbing = bobbing;
            }

        }
    }

}
