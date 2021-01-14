package info.sigmaclient.module.impl.minigames;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.command.impl.Sigmeme;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.combat.AntiBot;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;

import org.lwjgl.input.Keyboard;

import com.ibm.icu.util.StringTrieBuilder.Option;



public class AutoGG extends Module {

    public static int multiSwap;
    public static boolean isSwapping;
    public static boolean settingKey;
    public static boolean keysSet;
    private static String MODE = "MODE";
    private static String AUTOLMODE = "AUTOLMODE";
    private static String L = "L";
    String lastMeme = "";
    String lastMeme2 = "";
    String lastMeme3 = "";
    ArrayList<String>memeQueue = new ArrayList<>();
    public int multiKey;
    public int single;
    Timer timer = new Timer();
    boolean mineplexL = false;
    public AutoGG(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Hypixel", new String[]{"Hypixel", "Mineplex", "Cubecraft"}), "AutoGG server."));
        settings.put(AUTOLMODE, new Setting<>(AUTOLMODE, new Options("AutoL", "Basic", new String[]{"Basic", "Sigmeme", "Penshen"}), "AutoL method."));
        settings.put(L, new Setting<>(L, false, "AutoL."));
    }

    @Override
    public void onEnable() {
        isSwapping = false;
    }

    @Override
    @RegisterEvent(events = {EventPacket.class, EventTick.class})
    public void onEvent(Event event) {
    	String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();
    	String lmode = ((Options) settings.get(AUTOLMODE).getValue()).getSelected();
    	if(event instanceof EventTick){
    		
    		if(!memeQueue.isEmpty()){
    			try{
    				if(mc.thePlayer.ticksExisted <= 3){
        				memeQueue.clear();
        			}
    				long timerV = 3200;
    				if(!currentmode.equalsIgnoreCase("Hypixel"))
    					timerV = 0;				
        			if(timer.delay(timerV) && !memeQueue.isEmpty()){
        				timer.reset();
            			String send = memeQueue.get(0);
            			ChatUtil.sendChat(send);           	
            			memeQueue.remove(0);
        			}
    			}catch(ConcurrentModificationException e){
    			}
    			
    			
    		}
    		if(!(mc.currentScreen instanceof GuiIngameMenu) && !(mc.currentScreen instanceof GuiInventory) && mc.currentScreen !=null){
    			if(mineplexL){
    				mineplexL = false;
    			}
    		}
    		if(mc.theWorld == null){
    			mineplexL = false;
    		}
    		
    		if(timer.delay(5000) && mineplexL){
    			mineplexL = false;
    			ChatUtil.sendChat("gg");
    		}
    	}
    	if(event instanceof EventPacket){
        EventPacket ep = (EventPacket) event;
       
        if(ep.isIncoming() && ep.getPacket() instanceof S02PacketChat){
        	
        	S02PacketChat packet = (S02PacketChat)ep.getPacket();
        	String message = packet.func_148915_c().getUnformattedText();
        	
        	if(currentmode.equalsIgnoreCase("Hypixel")){
        	if((Boolean)settings.get(L).getValue()){
        		String[] strs = message.split(" ");
        		if(message.toLowerCase().contains("was killed by " + mc.thePlayer.getName().toLowerCase() + ".") ||
        				message.toLowerCase().contains("was thrown into the void by " + mc.thePlayer.getName().toLowerCase() + ".")||
        				message.toLowerCase().contains("was thrown off a cliff by " + mc.thePlayer.getName().toLowerCase() + ".")){
        			int players = 0;
        			try{
        				for(Object o : mc.theWorld.loadedEntityList){
            				Entity e = (Entity)o;
            				if(e instanceof EntityPlayer){
            					EntityPlayer p = (EntityPlayer)e;
            					if(!AntiBot.getInvalid().contains(p) && !p.isInvisible()){
            						players ++;
            					}
            				}
            			}
        			}catch(ConcurrentModificationException e){
        			}
        			
        			if(players != 1 && players != 2){
        				sendL(lmode, message);	
        			}
        		}
        		}
        	
      		
        	if((message.equals("You won! Want to play again? Click here! ")||
        			message.contains("coins! (Win)"))){
        		memeQueue.add("gg");
        	}
        	}else if(currentmode.equalsIgnoreCase("Mineplex")){
        		String[] teams = {"Green", "Red", "Blue", "Yellow"};
        		for(int i =0; i < teams.length; i++){
        			if(message.equals(teams[i] + " won the game!")){
        				timer.reset();
        				mineplexL = true;
        			}
        		}
        		if((Boolean)settings.get(L).getValue()){
        			if(message.toLowerCase().contains("killed by " + mc.thePlayer.getName().toLowerCase() + " ")){
        				sendL(lmode, message);
        					
        			}
        		}
        	}else if(currentmode.equalsIgnoreCase("Cubecraft")){
        		if((Boolean)settings.get(L).getValue()){
        			if(message.toLowerCase().contains("was slain by " + mc.thePlayer.getName().toLowerCase()) ||
        			   message.toLowerCase().contains("burned to death while fighting " + mc.thePlayer.getName().toLowerCase()) ||
        			   message.toLowerCase().contains("was shot by " + mc.thePlayer.getName().toLowerCase()) ||
        			   message.toLowerCase().contains("burnt to a crisp while fighting " + mc.thePlayer.getName().toLowerCase()) ||
        			   message.toLowerCase().contains("couldn't fly while escaping " + mc.thePlayer.getName().toLowerCase()) ||
        			   message.toLowerCase().contains("thought they could survive in the void while escaping " + mc.thePlayer.getName().toLowerCase()) ||
        			   message.toLowerCase().contains("fell to their death while escaping " + mc.thePlayer.getName().toLowerCase())){  
        				sendL(lmode, message);
        			}
        		}
        		if(packet.func_148915_c().getFormattedText().contains("§r§r§e" + mc.thePlayer.getName() + "§r§a won the game!§r")){
        			memeQueue.add("gg");
        		}
        	}
        	}
        }
    }
    
    void sendL(String mode, String message){
    	String[] strs = message.split(" ");
		switch (mode){
		case"Basic":{
			memeQueue.add("L" + " " + strs[0]);
		}
		break;
		case"Sigmeme":{
			String phrase = Sigmeme.phrases.get((int) (Math.random() * Sigmeme.phrases.size()));
			
			while(phrase.equalsIgnoreCase(lastMeme) || phrase.equalsIgnoreCase(lastMeme2) || phrase.equalsIgnoreCase(lastMeme3)){
				phrase = Sigmeme.phrases.get((int) (Math.random() * Sigmeme.phrases.size()));
			}
			String mm = ((Options) settings.get(MODE).getValue()).getSelected();
			if(mm.equalsIgnoreCase("Cubecraft")){
				phrase = "L" + " " + strs[0];
			}
			memeQueue.add(phrase);
			lastMeme = lastMeme2;
			lastMeme2 = lastMeme3;
			lastMeme3 = phrase;
		}
		break;
		case"Penshen":{
    		String phrase = Sigmeme.penshen.get((int) (Math.random() * Sigmeme.penshen.size()));
    		while(phrase.equalsIgnoreCase(lastMeme) || phrase.equalsIgnoreCase(lastMeme2) || phrase.equalsIgnoreCase(lastMeme3)){
				phrase = Sigmeme.penshen.get((int) (Math.random() * Sigmeme.penshen.size()));
			}
			memeQueue.add(phrase);
			lastMeme = lastMeme2;
			lastMeme2 = lastMeme3;
			lastMeme3 = phrase;
    		
    	}
		break;
		}
    }
}
