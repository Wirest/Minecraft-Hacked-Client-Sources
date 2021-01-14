package info.sigmaclient.management.command.impl;

import net.minecraft.util.MathHelper;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.misc.ChatUtil;

public class HClip extends Command {

    public HClip(String[] names, String description) {
        super(names, description);

    }



	@Override
    public void fire(String[] args) {
        if (args == null) {
            printUsage();
            return;
        }
        if (args.length == 1) {
        	try{
            	float var1 = mc.thePlayer.rotationYaw * 0.017453292F;
                mc.thePlayer.setPosition(mc.thePlayer.posX -  MathHelper.sin(var1)*Double.parseDouble(args[0]),
                		mc.thePlayer.posY ,
                		mc.thePlayer.posZ + MathHelper.cos(var1)*Double.parseDouble(args[0]));
        	}catch(NumberFormatException e){
        		printUsage();
        		
        	}
        	return;
        }
        printUsage();
    }

    @Override
    public String getUsage() {
        return "hclip <distance>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
