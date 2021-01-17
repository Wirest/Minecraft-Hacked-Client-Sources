package skyline.specc.mods.combat.killaura;

import net.minecraft.MoveEvents.UpdateEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.specc.SkyLine;
import skyline.specc.mods.combat.KillAuraMod;
import skyline.specc.mods.combat.antibot.Watchdodge;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.Speed;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.TimerUtils;

import static net.minecraft.client.Mineman.thePlayer;

/** Leaking this client will VOID your TOS and ability to use it!*/
public class Old extends ModMode<KillAuraMod>
{

    public Old(KillAuraMod parent, String name) {
        super(parent, name);
    }
    private int ticks;
	private static boolean Blocking;
    private static int bloctime;
	private int random;
    public Entity entity;
    public EntityLivingBase target;
    private TimerUtils timer;
	private static boolean isBlocking;
    private TimerUtils timer3 = new TimerUtils();
    public static World theWorld;
    @EventListener
    public void onUpdate(EventMotion event) {
        if (event.getType() == EventType.PRE) {
            ++this.ticks;
            //Initiating ticks and targeting closest entity
            entity = parent.closeEntity();
            //Rotating to targeted entity
            if (entity != null 
            	&& !SkyLine.getManagers().getFriendManager().hasFriend(entity.getName()) 
            	&& !Watchdodge.bots.contains(entity)
            	&& thePlayer.getDistanceToEntity(entity) <= parent.reach.getValue().doubleValue()) {
                parent.LookAtEntity(entity);
                event.getLocation().setPitch(parent.pitch);
                event.getLocation().setYaw(parent.yaw);
                //Blocking IF autoblock is ENABLED
                if (parent.autoblock.getValue() & ableToBlocc()) {
                    Blocking = true;
                    if (bloctime++ > 3) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
                        mc.playerController.sendUseItem(thePlayer, theWorld, thePlayer.getHeldItem());
                        bloctime = 0;
                    }
                } else if (target.isDead | !parent.autoblock.getValue() | !ableToBlocc()) {
                    Blocking = false;
                } 
             }
         } else {
            if (entity == null) return;
            target = (EntityLivingBase) entity;
            if (p.getDistanceToEntity(target) > parent.reach.getValue()) return;
            this.random = MathUtil.getRandomInRange(1, parent.APS.getValue());
            //Attaccing targeted entity rolf
            if (ticks >= 20.0 / this.random
            	&& !Watchdodge.bots.contains(entity)) {
            	parent.attack(target);
                target = null;
                ticks = 0;
            }
        }
    }
    //Making shur player is ABLE to BLOCC
    public static boolean ableToBlocc() {
        boolean var1;
        blocc:
        {
            if (thePlayer.getHeldItem() != null) {
                if (thePlayer.getHeldItem().getItem() instanceof ItemSword & KillAuraMod.autoblock.getValue()) {
                    if (thePlayer.isSwingInProgress) {
                        break blocc;
                    }
                }
            }

            if (!thePlayer.isBlocking()) {
                var1 = false;
                return var1;
            }
        }
        var1 = true;
        return var1;
    }
}