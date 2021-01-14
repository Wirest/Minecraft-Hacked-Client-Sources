package me.Corbis.Execution.module.implementations;

import com.google.common.collect.Ordering;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class StaffDetection extends Module {
    public StaffDetection(){
        super("StaffDetection", Keyboard.KEY_NONE, Category.MISC);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        for(Entity e : mc.theWorld.loadedEntityList){
            if(e instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer) e;
                switch (player.getName()){
                    case "alex_markey":
                    case "vislo":
                    case "Teddzy":
                    case "Timppali":
                    case "Unadvised":
                    case "fouffy":
                    case "teddy3684":
                    case "KierenBoal":
                    case "AcceptedAppeal":
                    case "UglyKidSteve":
                    case "Swaggle":
                    case "Bouncehouses":
                    case "AyeItsBeck":
                    case "socialisinq":
                    case "Kane":
                    case "Chilo_":
                    case "Mu3l":
                    case "TrippedUp":
                    case "xBenz":
                    case "Eunbin":
                    case "kvng_steph":
                    case "seekingattention":
                    case "AZXG":
                    case "Cxrtr":
                    case "MicroSquid":
                    case "PorkChopH3X":
                    case "W5vil":
                    case "Hypnoticxd":
                    case "yourbbg":
                    case "Vervain":
                    case "Abar":
                    case "fajoszz":
                    case "Incarnati0n_":
                    case "JakeOnPc":
                    case "iSloth":
                    case "Bequty_":
                    case "Mauricioh":
                    case "Minninq":
                    case "sinderr":
                    case "PistolPet":
                    case "mxbel":
                    case "Pinkapie":
                    case "CoolElla":
                    case "DrBrando":
                    case "Nikki_":
                    case "Elecctricc":
                    case "kuieren":
                    case "6hb":
                    case "Swinger":
                    case "Rev3rse":
                    case "89p":
                    case "nonRacist":
                    case "Tennente_":
                    case "Dionnysus":
                    case "Simplistiq":
                    case "YoureKindaSus":
                    case "Darenn":
                    case "creeper7777777":
                    case "ltself":
                    case "Alex_Tila":
                    case "TeddyOreo":
                    case "kryfex":
                    case "Melodieee":
                    case "TheKingOfTurtlez":
                    case "reallylazy":
                    case "tjester300":
                    case "InsaneIsMyName":
                        if(mc.thePlayer.ticksExisted % 5 == 0) {
                            Execution.instance.addChatMessage("Staff Detected! Leaving game automatically.");
                            mc.thePlayer.sendChatMessage("/leave");
                        }
                        break;
                }
            }
        }
    }
    /*
case "alex_markey":
case "vislo":
case "Teddzy":
case "Timppali":
case "Unadvised":
case "fouffy":
case "teddy3684":
case "KierenBoal":
case "AcceptedAppeal":
case "UglyKidSteve":
case "Swaggle":
case "Bouncehouses":
case "AyeItsBeck":
case "socialisinq":
case "Kane":
case "Chilo_":
case "Mu3l":
case "TrippedUp":
case "xBenz":
case "Eunbin":
case "kvng_steph":
case "seekingattention":
case "AZXG":
case "Cxrtr":
case "MicroSquid":
case "PorkChopH3X":
case "W5vil":
case "Hypnoticxd":
case "yourbbg":
case "Vervain":
case "Abar":
case "fajoszz":
case "Incarnati0n_":
case "JakeOnPc":
case "iSloth":
case "Bequty_":
case "Mauricioh":
case "Minninq":
case "sinderr":
case "PistolPet":
case "mxbel":
case "Pinkapie":
case "CoolElla":
case "DrBrando":
case "Nikki_":
case "Elecctricc":
case "kuieren":
case "6hb":
case "Swinger":
case "Rev3rse":
case "89p":
case "nonRacist":
case "Tennente_":
case "Dionnysus":
case "Simplistiq":
case "YoureKindaSus":
case "Darenn":
case "creeper7777777":
case "ltself":
case "Alex_Tila":
case "TeddyOreo":
case "kryfex":
case "Melodieee":
case "TheKingOfTurtlez":
case "reallylazy":
case "tjester300":
case "InsaneIsMyName":

     */

    public Ordering<NetworkPlayerInfo> field_175252_a(){
        try
        {
            final Class<GuiPlayerTabOverlay> c = GuiPlayerTabOverlay.class;
            final Field f = c.getField("field_175252_a");
            f.setAccessible(true);
            return (Ordering<NetworkPlayerInfo>)f.get(GuiPlayerTabOverlay.class);
        }catch(Exception e)
        {
            return null;

        }
    }

    private List<EntityPlayer> getPlayerTabList() {
        final List<EntityPlayer> list;
        (list = new ArrayList<EntityPlayer>()).clear();
        Ordering<NetworkPlayerInfo> field_175252_a = field_175252_a();
        if(field_175252_a == null){
            return list;
        }

        final List players = field_175252_a.sortedCopy(mc.thePlayer.sendQueue.getPlayerInfoMap());
        for(final Object o : players){
            final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if(info == null){
                continue;
            }
            list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }


}
