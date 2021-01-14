package info.sigmaclient.module.impl.minigames;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.command.CommandManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemSword;

public class Murder extends Module {

    public Murder(ModuleData data) {
        super(data);
        settings.put(MESSAGE, new Setting<>(MESSAGE, "{P} is trying to kill me!", "Sends a chat message. {P} = Murderer"));
        settings.put(MODE, new Setting<>(MODE, new Options("Chat Mode", "Client", new String[]{"Client", "Server"}), "Chat mesage mode."));
    }

    private String MESSAGE = "MESSAGE";
    private String MODE = "MODE";

    private info.sigmaclient.util.Timer timer = new info.sigmaclient.util.Timer();

    @Override
    @RegisterEvent(events = EventTick.class)
    public void onEvent(Event event) {
        for (Object o : mc.theWorld.loadedEntityList) {
            if (o instanceof EntityPlayer && timer.delay(15000)) {
                EntityPlayer ent = (EntityPlayer) o;
                if (ent != mc.thePlayer && ent.getCurrentEquippedItem() != null && isMurder(ent.getCurrentEquippedItem().getItem()) && !ent.isMurderer) {
                    ent.isMurderer = true;
                    switch (((Options) settings.get(MODE).getValue()).getSelected()) {
                        case "Server": {
                            String customChat = ((String) settings.get(MESSAGE).getValue());
                            customChat = customChat.replace("{P}", "%s");
                            ChatUtil.sendChat(String.format(customChat, ent.getName()));
                            break;
                        }
                        case "Client": {
                            ChatUtil.printChat(Command.chatPrefix + "\247d" + ent.getName() + " \2477is the murderer!");
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean isMurder(Item item){
    	if(item instanceof ItemMap || item.getUnlocalizedName().equalsIgnoreCase("item.ingotGold") ||
    			item instanceof ItemBow || item.getUnlocalizedName().equalsIgnoreCase("item.arrow") ||
    			item.getUnlocalizedName().equalsIgnoreCase("item.potion") ||
    			item.getUnlocalizedName().equalsIgnoreCase("item.paper") ||
    			item.getUnlocalizedName().equalsIgnoreCase("tile.tnt") || 
    			item.getUnlocalizedName().equalsIgnoreCase("item.web") ||
    			item.getUnlocalizedName().equalsIgnoreCase("item.bed") || 
    			item.getUnlocalizedName().equalsIgnoreCase("item.compass") || 
    			item.getUnlocalizedName().equalsIgnoreCase("item.comparator") ||
    			item.getUnlocalizedName().equalsIgnoreCase("item.shovelWood")){
    		return false;
    	}
    	return true;
    }
}
