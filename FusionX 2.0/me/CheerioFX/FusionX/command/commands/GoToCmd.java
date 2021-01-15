// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import net.minecraft.util.BlockPos;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.utils.EntityUtils2;
import me.CheerioFX.FusionX.utils.PathFinder.GotoAI;
import me.CheerioFX.FusionX.command.Command;

public class GoToCmd extends Command
{
    private GotoAI ai;
    private boolean enabled;
    private EntityUtils2.TargetSettings targetSettings;
    
    public GoToCmd() {
        this.targetSettings = new EntityUtils2.TargetSettings() {
            @Override
            public boolean targetFriends() {
                return true;
            }
            
            @Override
            public boolean targetBehindWalls() {
                return true;
            }
        };
    }
    
    @EventTarget
    public void onPreMotionUpdates(final EventPreMotionUpdates event) {
        for (int i = 0; i < 5; ++i) {
            this.ai.update();
            if (this.ai.isDone() || this.ai.isFailed()) {
                if (this.ai.isFailed()) {
                    FusionX.addChatMessage("Could not find a path.");
                }
                this.disable();
            }
        }
    }
    
    private void disable() {
        this.ai.stop();
        this.enabled = false;
        EventManager.unregister(this);
    }
    
    public boolean isActive() {
        return this.enabled;
    }
    
    @Override
    public String getAlias() {
        return "goto";
    }
    
    @Override
    public String getDescription() {
        return "Goes to a place using the Pathfinder AI.";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "goto";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (this.enabled) {
            this.disable();
            if (args.length == 0) {
                return;
            }
        }
        else {
            final int[] goal = this.argsToPos(this.targetSettings, args);
            this.ai = new GotoAI(new BlockPos(goal[0], goal[1], goal[2]));
        }
        this.enabled = true;
        EventManager.register(this);
    }
}
