package nivia.modules.combat;

import com.stringer.annotations.HideAccess;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.security.InvokeDynamics;
import nivia.security.StringEncryption;
import nivia.utils.Helper;
import nivia.utils.Logger;
 
@StringEncryption
@InvokeDynamics
@HideAccess
public class AntiBot extends Module {
    private Property<DetectionMode> detectionMode = new Property<DetectionMode>(this, "Mode", DetectionMode.NULL);
    private Property<Boolean> autoDetection = new Property<>(this, "Auto Detection", true);
 
    public AntiBot() {
        super("AntiBot", Keyboard.KEY_NONE, 0xFFFFFFFF, Category.COMBAT, "Remove all entities that are considered bots.", new String[] { "antibots", "nobot", "nobots", "nonpc", "nonpcs" }, true);
    }
 
    @EventTarget
    public void preMotionUpdates(EventPreMotionUpdates event) {
        this.setSuffix(this.detectionMode.value.toString());
        if (this.autoDetection.value) {
            if (Helper.mc().getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net")) {
                this.detectionMode.value = DetectionMode.HYPIXEL;
            }
            if (Helper.mc().getCurrentServerData().serverIP.toLowerCase().contains("mineplex.com")) {
                this.detectionMode.value = DetectionMode.MINEPLEX;
            }
        }
        if (this.detectionMode.value == DetectionMode.NULL) {
            for (Object object : Helper.world().playerEntities) {
                EntityPlayer entityPlayer = (EntityPlayer) object;
                if (entityPlayer != null && entityPlayer != Helper.player()) {
                    try {
                        NetworkPlayerInfo networkPlayerInfo = Helper.mc().getNetHandler().func_175102_a(entityPlayer.getUniqueID());
                        if (networkPlayerInfo.getGameType().isSurvivalOrAdventure() || networkPlayerInfo.getGameType().isCreative()) {
                            // empty block
                        }
                    } catch (Exception e) {
                        Helper.world().removeEntity(entityPlayer);
                    }
                }
            }
        }
        if (this.detectionMode.value == DetectionMode.HYPIXEL) {
            for (Object object : Helper.world().playerEntities) {
                EntityPlayer entityPlayer = (EntityPlayer) object;
                if (entityPlayer != null && entityPlayer != Helper.player()) {
                    if (entityPlayer.getDisplayName().getFormattedText().equalsIgnoreCase(entityPlayer.getName() + "\247r") && !Helper.player().getDisplayName().getFormattedText().equalsIgnoreCase(Helper.player().getName() + "\247r")) {
                        Helper.world().removeEntity(entityPlayer);
                    }
                }
            }
        }
        if (this.detectionMode.value == DetectionMode.MINEPLEX) {
            for (Object object : Helper.world().playerEntities) {
                EntityPlayer entityPlayer = (EntityPlayer) object;
                if (entityPlayer != null && entityPlayer != Helper.player()) {
                    if (entityPlayer.getName().startsWith("Body #")) {
                        Helper.world().removeEntity(entityPlayer);
                    }
                    if (entityPlayer.getMaxHealth() == 20.0f) {
                        Helper.world().removeEntity(entityPlayer);
                    }
                }
            }
        }
    }
   
    protected void addCommand() {
        Pandora.getCommandManager().cmds.add(new Command("AntiBot", "Manages antibot values", Logger.LogExecutionFail("Option, Options:", new String[]{ "Null", "Hypixel", "Mineplex", "AutoDetection", "Values" }) , "ab") {
            @Override
            public void execute(String commandName, String[] arguments) {
                switch (arguments[1].toLowerCase()) {
                    case "null":
                        detectionMode.value = DetectionMode.NULL;
                        Logger.logSetMessage("AntiBot", "Mode", detectionMode);
                        break;
                    case "hypixel": case "hyp": case "hp":
                        detectionMode.value = DetectionMode.HYPIXEL;
                        Logger.logSetMessage("AntiBot", "Mode", detectionMode);
                        break;
                    case "mineplex": case "mp":
                        detectionMode.value = DetectionMode.MINEPLEX;
                        Logger.logSetMessage("AntiBot", "Mode", detectionMode);
                        break;
                    case "autodetection": case "autodetect": case "autod": case "adetect": case "ad":
                        autoDetection.value = !autoDetection.value;
                        Logger.logToggleMessage("Auto Detection", autoDetection.value);
                        break;
                    case "values": case "actual":
                        logValues();
                        break;
                    default:
                        Logger.logChat(this.getError());
                        break;
                }
            }
        });
    }
   
    private enum DetectionMode {
        NULL, HYPIXEL, MINEPLEX;
    }
}