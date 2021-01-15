package nivia.modules;


import com.stringer.annotations.HideAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.StringUtils;
import nivia.Pandora;
import nivia.commands.commands.Legit;
import nivia.events.EventTarget;
import nivia.events.events.EventKeyPress;
import nivia.events.events.EventPacketSend;
import nivia.events.events.EventTick;
import nivia.files.modulefiles.Modules;
import nivia.managers.EventManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.combat.KillAura;
import nivia.modules.render.GUI;
import nivia.security.ConnectionUtils;
import nivia.security.HWIDTools;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.AESUtils;
import nivia.utils.utils.AESUtils.HWID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;

@HideAccess
public class Hooked {
	public static Property<Boolean> safe = new Property<Boolean>(null, "Safe Friends", true);
    public static Property<midClickMode> midcMode = new Property<midClickMode>(null, "MidClick Mode", midClickMode.Friend);
    public static Property<Boolean> smart = new Property<Boolean>(null, "Smart", false);
    public static boolean sallosMM = true;
	private Minecraft mc = Minecraft.getMinecraft();
	private boolean b = true;
	
	public Hooked(){
		EventManager.register(this);
        addHookedCommands();
	}
	


	
	@EventTarget
	public void call(EventKeyPress e){
		Pandora.getModManager().mods.stream().filter(m -> e.getEventKey() == m.getKeybind()).forEach(Module::Toggle);
	}
	
	
    /**
     * @Credits Anodise
     */
    @EventTarget
    public void call(EventPacketSend packet){
        if(packet.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage packetChatMessage = (C01PacketChatMessage)packet.getPacket();
            this.mc.ingameGUI.getTabList().getPlayerList().forEach(o -> {
                NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)o;
                String username = StringUtils.stripControlCodes(this.mc.ingameGUI.getTabList().func_175243_a(playerInfo));
                if(Pandora.getFriendManager().isFriend(username)) {
                    String alias = Pandora.getFriendManager().getFriendByName(username).getAlias();
                    packetChatMessage.setMessage(packetChatMessage.getMessage().replaceAll("(?i)" + Matcher.quoteReplacement("-" + alias), username));
                }
            });
        }
        if ((packet.getPacket() instanceof C14PacketTabComplete))
        {
            C14PacketTabComplete packetTabComplete = (C14PacketTabComplete)packet.getPacket();
            if (packetTabComplete.getMessage().startsWith(Pandora.prefix))
            {
                Random random = new Random();
                String[] arguments = packetTabComplete.getMessage().split(" ");
                String[] replacements = { "hi ", "hello ", "hey ", "what's up ", "hey M8 ", "mr ", "miss ", "ya "};
                packetTabComplete.setMessage(replacements[random.nextInt(replacements.length)] + arguments[(arguments.length - 1)]);

            }
        }
        if(packet.getPacket() instanceof C02PacketUseEntity){
        	if(KillAura.getAura().friends.value && KillAura.getAura().getState()) return;
            C02PacketUseEntity useEntity = (C02PacketUseEntity) packet.getPacket();
            if(useEntity.getAction().equals(C02PacketUseEntity.Action.ATTACK)){
                EntityPlayer ent = (EntityPlayer) useEntity.getEntityFromWorld(mc.theWorld);
                if(Pandora.getFriendManager().isFriend(ent.getName()) && safe.value)
                    packet.setCancelled(true);
            }
        }
    }
	@EventTarget
	public void call(EventTick e){
		if(b && Objects.nonNull(Helper.mc().thePlayer) && Objects.nonNull(Helper.world())){
			try {
				Pandora.getFileManager().getFile(Modules.class).loadFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
			gui.gui.updateTabs(true);
			try {
				ConnectionUtils.authorize(HWIDTools.a());
			} catch (Exception eg) {}

			b = false;
			gui.setState(true);
			Logger.logChat("For support or any other inquiry, contact us at https://nivia.co");
			Logger.logChat("Do -nivia cmds & -nivia mods to list them. Use the help command for their info.");
		        
			try {
				ConnectionUtils.authorize(HWIDTools.a());
			} catch (Exception er) {
				
			}
			
		}
		if(smart.value && (Helper.world() != null && Helper.player() != null)){
			Helper.mc().theWorld.loadedEntityList.stream().filter(EntityPlayer.class::isInstance).forEach(ent -> {
    			boolean Freecam = Pandora.getModManager().getModState("Freecam");
    			if(Helper.mc().thePlayer.getDistanceToEntity((Entity) ent) < (Freecam ? 6 : 64) && Helper.mc().thePlayer.canEntityBeSeen((Entity) ent)) {
    				    Pandora.getCommandManager().getCommand(Legit.class).execute("legit", new String[0]);
    				    return;
    				}
			});
        }
		if(mc.gameSettings.keyBindPickBlock.getIsKeyPressed() && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer){
			String name = mc.objectMouseOver.entityHit.getName();
            if(midcMode.value == midClickMode.Friend) {
                if (Pandora.getFriendManager().isFriend(name)) {
                    Pandora.getFriendManager().deleteFriend(name);
                    mc.gameSettings.keyBindPickBlock.pressed = false;
                    return;
                }
                mc.gameSettings.keyBindPickBlock.pressed = false;
                Pandora.getFriendManager().addFriend(name, name);
                Logger.logChat("You have added \247b" + name.replaceFirst(name.substring(1), "\247b" + name.substring(1)) + " \2477to your friend list");
            }
		}
	}
	
    private enum midClickMode{
         Friend;
    }
    private void addHookedCommands(){

    }
    public static boolean AuthMD5() throws Exception {
    	  try {
    		  String declink = AESUtils.AESDecrypt("6nOhr6ND9/YSXw54x2wb", 20);
    	      URL url = new URL(declink);
    	      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    	      String line;
    	      if ((line = in.readLine().trim()).startsWith(HWID.getMD5())) {
    	    	  in.close();
    	    	  return true;
    	      } else {
    	    	  System.exit(0);
    	    	  in.close();
    	    	  return false;
    	      }
    	    }
    	    catch (IOException e) {
    	      e.printStackTrace();
    	      System.exit(0);
  	      return false;
    	    }
    }
  	
	
	
	public static String sha(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.reset();
			byte[] buffer = input.getBytes("UTF-8");
			md.update(buffer);
			byte[] digest = md.digest();

			String hexStr = "";
			for (int i = 0; i < digest.length; i++) {
				hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
			}
			return hexStr;
		} catch (Exception e){ System.exit(0); }
		return "medness";
	}
}
