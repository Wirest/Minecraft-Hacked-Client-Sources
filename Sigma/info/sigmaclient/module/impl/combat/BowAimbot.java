/**
 * Time: 10:13:34 PM
 * Date: Jan 5, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.combat;

import java.util.ArrayList;
import java.util.List;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;

/**
 * @author cool1
 */
public class BowAimbot extends Module {

    boolean send, isFiring;
    public static final String SILENT = "SILENT";
    public static EntityLivingBase target;
    public BowAimbot(ModuleData data) {
        super(data);

		settings.put(SILENT, new Setting<>(SILENT, true, "Server-sided rotations."));
   
    }

    @Override
    public void onDisable(){
    	target = null;
    }
    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
               target = getTarg();
                if(shouldAim()){
                    if (target != null) {
                        float[] rotations = RotationUtils.getBowAngles(target);
                        boolean silent = (Boolean) settings.get(SILENT).getValue();
                        if(silent){
                        	em.setYaw(rotations[0]);
                        	em.setPitch(rotations[1]);
                        }else{
                        	mc.thePlayer.rotationYaw = rotations[0];
                        	mc.thePlayer.rotationPitch = rotations[1];
                        }
                    }
                }
            }
        }
    }
    public static boolean shouldAim(){
    	if(mc.thePlayer.inventory.getCurrentItem() == null || !(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow))
    		return false;
    	if(Client.getModuleManager().get(FastBow.class).isEnabled() && mc.gameSettings.keyBindUseItem.pressed)
    		return true;
    	if(mc.thePlayer.isUsingItem())
            return true;
    	return false;
    }
    private EntityLivingBase getTarg() {
        List<EntityLivingBase> loaded = new ArrayList();
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) o;
                if (ent instanceof EntityPlayer && ent != mc.thePlayer && mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) < 65 && !FriendManager.isFriend(ent.getName())) {
                    if (ent == Killaura.vip) {
                        return ent;
                    }
                    loaded.add(ent);
                }
            }
        }
        if (loaded.isEmpty()) {
            return null;
        }
        loaded.sort((o1, o2) -> {
            float[] rot1 = RotationUtils.getRotations(o1);
            float[] rot2 = RotationUtils.getRotations(o2);
            return (int) ((RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rot1[0])
                    + RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rot1[1]))
                    - (RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rot2[0])
                    + RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rot2[1])));
        });
        EntityLivingBase target = loaded.get(0);
        return target;
    }

}
