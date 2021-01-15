package nivia.modules.combat;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.ServerAddress;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.ModuleManager;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.modules.ModuleMode;
import nivia.modules.combat.Regen;
import nivia.modules.exploits.*;
import nivia.modules.miscellanous.AntiHunger;
import nivia.modules.miscellanous.ChestStealer;
import nivia.modules.miscellanous.Paralyze;
import nivia.modules.miscellanous.Scaffold;
import nivia.modules.movement.Fly;
import nivia.modules.movement.Glide;
import nivia.modules.movement.NoSlow;
import nivia.modules.movement.Speed;
import nivia.modules.player.Fastplace;
import nivia.modules.render.ESP;
import nivia.modules.render.Nametags;
import nivia.modules.render.Tracers;
import nivia.utils.Helper;
import nivia.utils.Logger;

import java.awt.*;

public class AutoBypass extends Module{
    public PropertyManager.Property<Boolean> Lock = new PropertyManager.Property<>(this, "Lock Mods", false);
    public DoubleProperty Timer = new DoubleProperty(this, "Check Time", 100, 50, 500, false);
    double timer = 0;

    public AutoBypass() {
        super("AutoBypass", 0, 13369344, Category.COMBAT, "Automatically sets all bypassing settings, toggles off banable modules and allows you to lock them", new String[]{"ab", "autob","presets","abypass"}, true);
    }

        @EventTarget
        public void onPre (EventPreMotionUpdates e){
	        if (Helper.mc().getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net") && Lock.value){
	            if(timer >= Timer.getValue()) {
	            	if(Pandora.getModManager().getModule(Regen.class).getState()) {
		            	Pandora.getModManager().getModule(Regen.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(AntiHunger.class).getState()) {
		            	Pandora.getModManager().getModule(AntiHunger.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(Paralyze.class).getState()) {
		            	Pandora.getModManager().getModule(Paralyze.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(FastUse.class).getState()) {
		            	Pandora.getModManager().getModule(FastUse.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(CivBreak.class).getState()) {
		            	Pandora.getModManager().getModule(CivBreak.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(Firion.class).getState()) {
		            	Pandora.getModManager().getModule(Firion.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(Teleport.class).getState()) {
		            	Pandora.getModManager().getModule(Teleport.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(Fly.class).getState()) {
		            	Pandora.getModManager().getModule(Fly.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(NoSlow.class).getState()) {
		            	Pandora.getModManager().getModule(NoSlow.class).setState(false);
		            }
	            	
	            	if(Pandora.getModManager().getModule(Fastplace.class).getState()) {
		            	Pandora.getModManager().getModule(Fastplace.class).setState(false);
		            }
		            
		            timer = 0;
	            } else {
	            	timer++;
	            }
	        }
        }

        public void onEnable(){
            super.onEnable();
            if (Helper.mc().getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net")) {
                Logger.logChat("|-----------***-----------|");
                Logger.logChat("Hypixel mode enabled");
                Logger.logChat("Modules disabled:");
                Logger.logChat("- Regen");
                Logger.logChat("- AntiHunger");
                Logger.logChat("- Paralyze");
                Logger.logChat("- FastUse");
                Logger.logChat("- CivBreak");
                Logger.logChat("- Firion");
                Logger.logChat("- Teleport");
                Logger.logChat("- Fly (not Glide)");
                Logger.logChat("- NoSlow");
                Logger.logChat("- FastPlace");
                Logger.logChat("|-----------***-----------|");
                Logger.logChat("Settings have been applied");
                Logger.logChat("|-----------***-----------|");
                //Modules Disabled
                Pandora.getModManager().getModule(Regen.class).setState(false);
                Pandora.getModManager().getModule(AntiHunger.class).setState(false);
                Pandora.getModManager().getModule(Paralyze.class).setState(false);
                Pandora.getModManager().getModule(FastUse.class).setState(false);
                Pandora.getModManager().getModule(CivBreak.class).setState(false);
                Pandora.getModManager().getModule(Firion.class).setState(false);
                Pandora.getModManager().getModule(Teleport.class).setState(false);
                Pandora.getModManager().getModule(Fly.class).setState(false);
                Pandora.getModManager().getModule(NoSlow.class).setState(false);
                Pandora.getModManager().getModule(Fastplace.class).setState(false);
                Pandora.getModManager().getModule(AntiBot.class).setState(true);

                ////////////////////////////Settings\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

                //Aura
                ((KillAura) Pandora.getModManager().getModule(KillAura.class)).mode.value = KillAura.AuraMode.ka.switchMode;
                ((KillAura) Pandora.getModManager().getModule(KillAura.class)).armorbreaker.value = false;
                KillAura.getAura().APS.setValue(10);
                KillAura.getAura().reach.setValue(4.4);
                KillAura.getAura().rnd.setValue(0);
                KillAura.getAura().sdelay.setValue(10);
                KillAura.getAura().autoblock.value = true;

                //Criticals
                ((Criticals) Pandora.getModManager().getModule(Criticals.class)).cMode.value = Criticals.CritMode.PACKET;

                //Speed
                ((Speed) Pandora.getModManager().getModule(Speed.class)).mode.value = Speed.speedMode.SLOWHOP;

                //Scaffold
                ((Scaffold) Pandora.getModManager().getModule(Scaffold.class)).Hypixel.value = true;

                //NoFall
                ((NoFall) Pandora.getModManager().getModule(NoFall.class)).mode.value = NoFall.Mode.NORMAL;

                //Phase
                ((Phase) Pandora.getModManager().getModule(Phase.class)).mode.value = Phase.PhaseMode.Weird;

                //Glide
                ((Glide) Pandora.getModManager().getModule(Glide.class)).lock.value = true;
                ((Glide) Pandora.getModManager().getModule(Glide.class)).damage.value = false;
                ((Glide) Pandora.getModManager().getModule(Glide.class)).lemon.value = true;
                ((Glide) Pandora.getModManager().getModule(Glide.class)).horizontalSpeed.setValue(0.3);
                ((Glide) Pandora.getModManager().getModule(Glide.class)).verticalSpeed.setValue(0.0);
                ((Glide) Pandora.getModManager().getModule(Glide.class)).glideSpeed.setValue(0.0);

                //Esp
                ((ESP) Pandora.getModManager().getModule(ESP.class)).invisibles.value = false;

                //Nametag
                ((Nametags) Pandora.getModManager().getModule(Nametags.class)).invisibles.value = false;

                //Tracer
                ((Tracers) Pandora.getModManager().getModule(Tracers.class)).invisibles.value = false;

                //Chest Stealer
                ((ChestStealer) Pandora.getModManager().getModule(ChestStealer.class)).delay.setValue(50);


            }
            if (Helper.mc().getCurrentServerData().serverIP.toLowerCase().contains("mineplex.com")) {
                Logger.logChat("|-----------***-----------|");
                Logger.logChat("Mineplex mode Enabled");
                Logger.logChat("Modules disabled:");
                Logger.logChat("- Paralyze");
                Logger.logChat("- AntiHunger");
                Logger.logChat("- CivBreak");
                Logger.logChat("|-----------***-----------|");
                Logger.logChat("Settings have been applied");
                Logger.logChat("|-----------***-----------|");

                Pandora.getModManager().getModule(AntiHunger.class).setState(false);
                Pandora.getModManager().getModule(CivBreak.class).setState(false);
                Pandora.getModManager().getModule(Paralyze.class).setState(false);
                Pandora.getModManager().getModule(AntiBot.class).setState(true);

                //Aura
                ((KillAura) Pandora.getModManager().getModule(KillAura.class)).mode.value = KillAura.AuraMode.ka.switchMode;
                ((KillAura) Pandora.getModManager().getModule(KillAura.class)).armorbreaker.value = false;
                KillAura.getAura().APS.setValue(10);
                KillAura.getAura().reach.setValue(5.0);
                KillAura.getAura().rnd.setValue(0);
                KillAura.getAura().sdelay.setValue(10);
                KillAura.getAura().autoblock.value = true;

                //Regen
                ((Regen) Pandora.getModManager().getModule(Regen.class)).health.value =19;
                ((Regen) Pandora.getModManager().getModule(Regen.class)).packets.value =50;

                //Criticals
                ((Criticals) Pandora.getModManager().getModule(Criticals.class)).cMode.value = Criticals.CritMode.PACKET;

                //Speed
                ((Speed) Pandora.getModManager().getModule(Speed.class)).mode.value = Speed.speedMode.SLOWHOP;

                //Scaffold
                ((Scaffold) Pandora.getModManager().getModule(Scaffold.class)).Hypixel.value = false;

                //NoFall
                ((NoFall) Pandora.getModManager().getModule(NoFall.class)).mode.value = NoFall.Mode.NORMAL;

                //Phase
                ((Phase) Pandora.getModManager().getModule(Phase.class)).mode.value = Phase.PhaseMode.Teleport;

                //Glide
                ((Glide) Pandora.getModManager().getModule(Glide.class)).lock.value = false;
                ((Glide) Pandora.getModManager().getModule(Glide.class)).damage.value = false;
                ((Glide) Pandora.getModManager().getModule(Glide.class)).lemon.value = false;
                ((Glide) Pandora.getModManager().getModule(Glide.class)).horizontalSpeed.setValue(0.5);
                ((Glide) Pandora.getModManager().getModule(Glide.class)).verticalSpeed.setValue(0.5);
                ((Glide) Pandora.getModManager().getModule(Glide.class)).glideSpeed.setValue(0.1);

                //Esp
                ((ESP) Pandora.getModManager().getModule(ESP.class)).invisibles.value = false;

                //Nametag
                ((Nametags) Pandora.getModManager().getModule(Nametags.class)).invisibles.value = false;

                //Tracer
                ((Tracers) Pandora.getModManager().getModule(Tracers.class)).invisibles.value = false;
            }
        }
        public void onDisable() {
            super.onDisable();
            if (Helper.mc().getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net")) {
                Logger.logChat("Hypixel mode disabled");
            }
            if (Helper.mc().getCurrentServerData().serverIP.toLowerCase().contains("mineplex.com")) {
                Logger.logChat("Mineplex mode disabled");
            }
        }
    protected void addCommand(){
        String[] Options = new String[]{"locksettings"};
        Pandora.getCommandManager().cmds.add(new Command
                ("AutoBypass", "Manages AutoBypass", Logger.LogExecutionFail("Option, Options:", Options), "presets", "autob","abypass") {
            @Override
            public void execute(String commandName, String[] arguments) {
                String message = arguments[1];
                switch (message.toLowerCase()) {
                    case "lock":
                    case "locksettings":
                        Lock.value = !Lock.value;
                        Logger.logToggleMessage("Lock", Lock.value);
                        break;

                }
            }
        });
    }
    }
