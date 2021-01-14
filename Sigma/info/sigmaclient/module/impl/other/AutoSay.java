package info.sigmaclient.module.impl.other;

import java.util.ArrayList;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.command.impl.Sigmeme;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.event.RegisterEvent;

public class AutoSay extends Module {

    info.sigmaclient.util.Timer timer = new info.sigmaclient.util.Timer();
    public static String WORDS = "SAY";
    public static String MMODE = "MMODE";
    public String DELAY = "DELAY";
    public static String message = "I'm using Sigma Client !";
    String lastMeme = "";
    String lastMeme2 = "";
    String lastMeme3 = "";
    public AutoSay(ModuleData data) {
        super(data);
        settings.put(WORDS, new Setting(WORDS, "I'm using Sigma Client !", "Message to send."));
        settings.put(DELAY, new Setting(DELAY, 2500, "MS delay between messages.", 100, 100, 10000));
        settings.put(MMODE, new Setting<>(MMODE, new Options("Mode", "Custom", new String[]{"Custom", "Sigmeme", "Penshen"}), "Autosay messages."));
    }

    @Override
    public void onEnable(){
    	if(message.equalsIgnoreCase("I'm using Sigma Client !") && ((Options) settings.get(MMODE).getValue()).getSelected().equalsIgnoreCase("Custom")){
    		ChatUtil.printChat(Command.chatPrefix + "Setup your message by using .autosay <message>");
    	}
    }
    @Override
    @RegisterEvent(events = {EventTick.class})
    public void onEvent(Event event) {
    	String mmode = ((Options) settings.get(MMODE).getValue()).getSelected();
        settings.get(WORDS).setValue(message);
        if (timer.delay(((Number) settings.get(DELAY).getValue()).longValue())) {
        	String a = ((String) settings.get(WORDS).getValue());
        	switch(mmode){
        	case"Custom":{
        		ChatUtil.sendChat(a + " " + (int) (Math.random() * 100000));
        	}
        	break;
        	case"Sigmeme":{
        		String phrase = Sigmeme.phrases.get((int) (Math.random() * Sigmeme.phrases.size()));
        		while(phrase.equalsIgnoreCase(lastMeme) || phrase.equalsIgnoreCase(lastMeme2) || phrase.equalsIgnoreCase(lastMeme3)){
					phrase = Sigmeme.phrases.get((int) (Math.random() * Sigmeme.phrases.size()));
				}
				ChatUtil.sendChat(phrase);
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
        		ChatUtil.sendChat(phrase);
				lastMeme = lastMeme2;
				lastMeme2 = lastMeme3;
				lastMeme3 = phrase;
        		
        	}
        	break;
        	}  
            timer.reset();
        }
        
    }
}
