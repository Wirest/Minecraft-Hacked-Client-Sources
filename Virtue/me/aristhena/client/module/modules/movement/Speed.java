// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement;

import net.minecraft.potion.Potion;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.TickEvent;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.event.EventTarget;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.client.option.OptionManager;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.modules.movement.speed.Arenabrawl;
import me.aristhena.client.module.modules.movement.speed.GayHop;
import me.aristhena.client.module.modules.movement.speed.Jump;
import me.aristhena.client.module.modules.movement.speed.Minez;
import me.aristhena.client.module.modules.movement.speed.Vanilla;
import me.aristhena.client.module.modules.movement.speed.Bhop;
import me.aristhena.client.module.modules.movement.speed.Latest;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod
public class Speed extends Module
{
    public Latest latest;
    public Bhop bhop;
    private Vanilla vanilla;
    private Minez minez;
    private Jump fuckingcool;
    public GayHop gayHop;
    private Arenabrawl arenabrawl;
    @Option.Op(min = 0.2, max = 10.0, increment = 0.05)
    public double speed;
    
    public Speed() {
        this.latest = new Latest("Latest", true, this);
        this.bhop = new Bhop("Bhop", false, this);
        this.vanilla = new Vanilla("Vanilla", false, this);
        this.minez = new Minez("Minez", false, this);
        this.fuckingcool = new Jump("Fucking Cool", false, this);
        this.gayHop = new GayHop("GayHop", false, this);
        this.arenabrawl = new Arenabrawl("Arenabrawl", false, this);
        this.speed = 0.5;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.latest);
        OptionManager.getOptionList().add(this.bhop);
        OptionManager.getOptionList().add(this.gayHop);
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.minez);
        OptionManager.getOptionList().add(this.fuckingcool);
        OptionManager.getOptionList().add(this.arenabrawl);
        this.updateSuffix();
        super.preInitialize();
    }
    
    @Override
    public void enable() {
        this.latest.enable();
        this.bhop.enable();
        this.vanilla.enable();
        this.minez.enable();
        this.fuckingcool.enable();
        this.arenabrawl.enable();
        super.enable();
    }
    
    @EventTarget
    private void onMove(final MoveEvent event) {
        this.latest.onMove(event);
        this.bhop.onMove(event);
        this.gayHop.onMove(event);
        this.vanilla.onMove(event);
        this.minez.onMove(event);
        this.fuckingcool.onMove(event);
        this.arenabrawl.onMove(event);
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        this.latest.onUpdate(event);
        this.bhop.onUpdate(event);
        this.gayHop.onUpdate(event);
        this.vanilla.onUpdate(event);
        this.minez.onUpdate(event);
        this.fuckingcool.onUpdate(event);
    }
    
    @EventTarget
    private void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    private void updateSuffix() {
        if (this.latest.getValue()) {
            this.setSuffix("Latest");
        }
        else if (this.bhop.getValue()) {
            this.setSuffix("Bhop");
        }
        else if (this.gayHop.getValue()) {
            this.setSuffix("Gay Hop");
        }
        else if (this.vanilla.getValue()) {
            this.setSuffix("Vanilla");
        }
        else if (this.arenabrawl.getValue()) {
            this.setSuffix("Arenabrawl");
        }
        else if (this.fuckingcool.getValue()) {
            this.setSuffix("Jump");
        }
        else {
            this.setSuffix("Minez");
        }
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (ClientUtils.player().isPotionActive(Potion.moveSpeed)) {
            final int amplifier = ClientUtils.player().getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
}
