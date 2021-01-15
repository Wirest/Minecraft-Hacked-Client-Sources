package nivia.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;
import nivia.utils.utils.*;
import nivia.utils.utils.RenderUtils.ColorUtils;
import nivia.utils.utils.RenderUtils.R2DUtils;
import nivia.utils.utils.RenderUtils.R3DUtils;

public class Helper {
	private static EntityUtils entityUtils = new EntityUtils();
    private static CombatUtils combatUtils = new CombatUtils();
    private static BlockUtils blockUtils = new BlockUtils();
    private static PlayerUtils playerUtils = new PlayerUtils();
    private static R2DUtils r2DUtils = new R2DUtils();
    private static R3DUtils r3DUtils = new R3DUtils();
    private static ColorUtils colorUtils = new ColorUtils();
    private static WorldUtils worldUtils = new WorldUtils();
    private static MathUtils mathUtils = new MathUtils();
    private static InventoryUtils inventoryUtils = new InventoryUtils();

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    public static EntityPlayerSP player() {
        return mc().thePlayer;
    }
    public static WorldClient world() {
        return mc().theWorld;
    }
    public static EntityUtils entityUtils(){
    	return entityUtils;
    }
    public static WorldUtils worldUtils() {
    	return worldUtils;
    }
    public static CombatUtils combatUtils(){
    	return combatUtils;
    }
    public static InventoryUtils inventoryUtils(){
    	return inventoryUtils;
    }
    public static BlockUtils blockUtils(){
    	return blockUtils;
    }
    public static PlayerUtils playerUtils(){
    	return playerUtils;
    }
    public static R2DUtils get2DUtils(){
    	return r2DUtils;
    }
    public static R3DUtils get3DUtils(){
    	return r3DUtils;
    }
    public static ColorUtils colorUtils(){
    	return colorUtils;
    }
    public static MathUtils mathUtils(){
        return mathUtils;
    }
    public static void sendPacket(Packet p){
    	mc().getNetHandler().addToSendQueue(p);
    }
    
    
}
