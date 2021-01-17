package me.ihaq.iClient.modules.Render;


import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.values.BooleanValue;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect; 

public class Fullbright extends Module{
       
    private float gamaSetting;
    private static String mode = "none";
    
    public BooleanValue tabGui = new BooleanValue(this, "TabGUI", "tabgui", Boolean.valueOf(true));
	public BooleanValue arrayList = new BooleanValue(this, "ArrayList", "arraylist", Boolean.valueOf(true));
   
    public Fullbright() {
        super("Fullbright", Keyboard.KEY_NONE,Category.RENDER, mode);
    }
       
    @Override
    public void onEnable() {
        this.gamaSetting = mc.gameSettings.gammaSetting;
    }
   
    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = this.gamaSetting;
    }
   
    @EventTarget
    public void onUpdate(EventUpdate e) {
       
        if(!this.isToggled())
            return;
        
        if(mode.equals("gamma")){
        	setMode(" : \u00A7fGamma");
        	gamma();
        }else if(mode.equals("potion")){
        	setMode(": \u00A7fPotion");
        	potion();
        }
       
    }
    
    public void gamma(){
    	mc.gameSettings.gammaSetting = 100F;
    }
    
    public void potion(){
    	mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 0));
    }
   
}