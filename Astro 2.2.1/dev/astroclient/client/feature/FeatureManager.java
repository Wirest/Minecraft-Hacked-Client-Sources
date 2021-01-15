package dev.astroclient.client.feature;

import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.input.EventKey;
import dev.astroclient.client.feature.impl.combat.*;
import dev.astroclient.client.feature.impl.exploits.ChatBypass;
import dev.astroclient.client.feature.impl.exploits.NoFall;
import dev.astroclient.client.feature.impl.exploits.PingSpoof;
import dev.astroclient.client.feature.impl.hud.HUD;
import dev.astroclient.client.feature.impl.hud.hud.HudEditor;
import dev.astroclient.client.feature.impl.combat.AimAssist;
import dev.astroclient.client.feature.impl.combat.AutoClicker;
import dev.astroclient.client.feature.impl.combat.HitBox;
import dev.astroclient.client.feature.impl.combat.Reach;
import dev.astroclient.client.feature.impl.miscellaneous.*;
import dev.astroclient.client.feature.impl.movement.*;
import dev.astroclient.client.feature.impl.visuals.*;
import net.minecraft.client.Minecraft;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

public class FeatureManager implements JAHfb {

    //This makes it easier to get the instance of a feature.
    public KillAura killAura;
    public Flight flight;
    public Scaffold scaffold;
    public Chams chams;
    public OutlineESP outline;
    public StreamerMode streamerMode;
    public NoSlowdown noSlowdown;
    public Speed speed;
    public AutoPlay autoPlay;
    public ClickGUI clickGUI;
    public Animation animation;
    public AntiBot antiBot;
    public Reach reach;
    public HitBox hitBox;
    public TimeChanger timeChanger;
    public HUD hud;
    public Step step;
    public Skeletal skeletal;
    public NoHurtCam noHurtCam;

    private List<Feature> features;

    public FeatureManager() {
        features = new ArrayList<>();
        System.out.println("Collecting..");
        collectFeatures();
    }

    private void collectFeatures() {
        addFeature(killAura = new KillAura());
        addFeature(flight = new Flight());
        addFeature(new Sprint());
        addFeature(chams = new Chams());
        addFeature(hud = new HUD());
        addFeature(new ESP2D());
        addFeature(clickGUI = new ClickGUI());
        addFeature(new Velocity());
        addFeature(new NoFall());
        addFeature(noSlowdown = new NoSlowdown());
        addFeature(new InventoryManager());
        addFeature(timeChanger = new TimeChanger());
        addFeature(new Stealer());
        addFeature(new Criticals());
        addFeature(scaffold = new Scaffold());
        addFeature(step = new Step());
        addFeature(autoPlay = new AutoPlay());
        addFeature(animation = new Animation());
        addFeature(new ChestESP());
        addFeature(speed = new Speed());
        addFeature(new PingSpoof());
        addFeature(new AutoClicker());
        addFeature(new AutoTool());
        addFeature(new NoVoid());
        addFeature(new InvMove());
        addFeature(new Crosshair());
        addFeature(antiBot = new AntiBot());
        addFeature(new AimAssist());
        addFeature(reach = new Reach());
        addFeature(hitBox = new HitBox());
        addFeature(streamerMode = new StreamerMode());
        addFeature(new AutoPotion());
        addFeature(new NoRotate());
        addFeature(skeletal = new Skeletal());
        addFeature(noHurtCam = new NoHurtCam());
        addFeature(new ChatBypass());
        addFeature(new SpeedMine());
        addFeature(new AutoSay());
        addFeature(outline = new OutlineESP());
        addFeature(new AutoSword());
    }

    private void addFeature(Feature feature) {
        features.add(feature);
        Client.INSTANCE.propertyManager.registerFeature(feature);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public List<Feature> getFeaturesForCategory(Category category) {
        List<Feature> features = new ArrayList<>();
        for (Feature feature : getFeatures()) {
            if (feature.getCategory() == category)
                features.add(feature);
        }
        return features;
    }


    public Feature get(Class clazz) {
        try {
            for (Feature feature : getFeatures()) {
                if (feature.getClass() == clazz)
                    return feature;
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    public Feature get(String label) {
        try {
            for (Feature feature : getFeatures()) {
                if (feature.getLabel().equalsIgnoreCase(label))
                    return feature;
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    @Subscribe
    public void onEvent(EventKey eventKey) {
        for (Feature feature : Client.INSTANCE.featureManager.getFeatures()) {
            if (feature instanceof ToggleableFeature) {
                ToggleableFeature toggleableFeature = (ToggleableFeature) feature;
                if (eventKey.getKey() == toggleableFeature.getBind())
                    toggleableFeature.toggle();
            }
        }
        if(eventKey.getKey() == Keyboard.KEY_INSERT)
            Minecraft.getMinecraft().displayGuiScreen(new HudEditor());
    }

    @Override
    public boolean isEnabled() {
        return Minecraft.getMinecraft().thePlayer != null;
    }
}
