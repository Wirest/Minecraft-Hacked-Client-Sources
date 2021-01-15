// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import me.aristhena.event.events.KeyPressEvent;
import me.aristhena.event.events.TickEvent;
import me.aristhena.event.EventTarget;
import me.aristhena.event.events.Render2DEvent;
import me.aristhena.client.option.OptionManager;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.modules.render.hud.tabgui.NewTabGui;
import me.aristhena.client.module.modules.render.hud.tabgui.LucidTabGui;
import me.aristhena.client.module.modules.render.hud.IndigoTheme;
import me.aristhena.client.module.modules.render.hud.TrendyTheme;
import me.aristhena.client.module.modules.render.hud.CrestTheme;
import me.aristhena.client.module.modules.render.hud.NewVirtueTheme;
import me.aristhena.client.module.modules.render.hud.PlainTheme;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(enabled = true, shown = false)
public class Hud extends Module
{
    private PlainTheme plainTheme;
    private NewVirtueTheme newVirtueTheme;
    private CrestTheme crestTheme;
    private TrendyTheme trendyTheme;
    private IndigoTheme indigoTheme;
    public LucidTabGui lucidTab;
    public NewTabGui newTab;
    @Option.Op(name = "Show TabGui")
    private boolean tabgui;
    
    public Hud() {
        this.plainTheme = new PlainTheme("Plain Theme", true, this);
        this.newVirtueTheme = new NewVirtueTheme("New Virtue Theme", false, this);
        this.crestTheme = new CrestTheme("Crest Theme", false, this);
        this.trendyTheme = new TrendyTheme("Trendy Theme", false, this);
        this.indigoTheme = new IndigoTheme("Indigo Theme", false, this);
        this.lucidTab = new LucidTabGui("Lucid TabGui", true, this);
        this.newTab = new NewTabGui("New TabGui", false, this);
        this.tabgui = true;
    }
    
    @Override
    public void preInitialize() {
        if (this.tabgui) {
            this.newTab.setupSizes();
            this.lucidTab.setupSizes();
        }
        OptionManager.getOptionList().add(this.plainTheme);
        OptionManager.getOptionList().add(this.newVirtueTheme);
        OptionManager.getOptionList().add(this.crestTheme);
        OptionManager.getOptionList().add(this.trendyTheme);
        OptionManager.getOptionList().add(this.indigoTheme);
        OptionManager.getOptionList().add(this.lucidTab);
        OptionManager.getOptionList().add(this.newTab);
        super.preInitialize();
    }
    
    @EventTarget
    private void onRender2D(final Render2DEvent event) {
        this.plainTheme.onRender(event);
        this.newVirtueTheme.onRender(event);
        this.crestTheme.onRender(event);
        this.trendyTheme.onRender(event);
        this.indigoTheme.onRender(event);
        if (this.tabgui) {
            this.newTab.onRender(event);
            this.lucidTab.onRender(event);
        }
    }
    
    @EventTarget
    private void onTick(final TickEvent event) {
        IndigoTheme.updateFade();
    }
    
    @EventTarget
    private void onKeypress(final KeyPressEvent event) {
        if (this.tabgui) {
            this.newTab.onKeypress(event);
            this.lucidTab.onKeypress(event);
        }
    }
}
