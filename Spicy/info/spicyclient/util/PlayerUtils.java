package info.spicyclient.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class PlayerUtils {
	
	public static List<EntityPlayer> getTabPlayerList() {
        final NetHandlerPlayClient var4 = Minecraft.getMinecraft().thePlayer.sendQueue;
        final List<EntityPlayer> list = new ArrayList<>();
        final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.getPlayerInfoMap());
        for (final Object o : players) {
            final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if (info == null) {
                continue;
            }
            list.add(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
	
    /**
     * Gets the closest player to the entity within the specified distance (if distance is less than 0 then ignored).
     * Args: entity, dist
     */
    public static EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance)
    {
        return getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance);
    }

    /**
     * Gets the closest player to the point within the specified distance (distance can be set to less than 0 to not
     * limit the distance). Args: x, y, z, dist
     */
    public static EntityPlayer getClosestPlayer(double x, double y, double z, double distance)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;
        
        CopyOnWriteArrayList<EntityPlayer> playerEntities = Minecraft.getMinecraft().theWorld.playerEntities;
        
        if (playerEntities.contains(Minecraft.getMinecraft().thePlayer)) {
        	playerEntities.remove(Minecraft.getMinecraft().thePlayer);
        }
        
        for (int i = 0; i < playerEntities.size(); ++i)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)Minecraft.getMinecraft().theWorld.playerEntities.get(i);
            
            if (entityplayer1 == Minecraft.getMinecraft().thePlayer) {
            	
            }else {
            	if (EntitySelectors.NOT_SPECTATING.apply(entityplayer1))
                {
                    double d1 = entityplayer1.getDistanceSq(x, y, z);

                    if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0))
                    {
                        d0 = d1;
                        entityplayer = entityplayer1;
                    }
                }
            }
            
        }

        return entityplayer;
    }
	
}
