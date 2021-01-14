package me.Corbis.Execution.module;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.module.implementations.HUD;
import me.Corbis.Execution.ui.Notifications.Notification;
import me.Corbis.Execution.ui.Notifications.NotificationType;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.PlayMusic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

public class Module {
    protected Minecraft mc = Minecraft.getMinecraft();
    int key;
    String name, suffix;
    Category category;
    public boolean isEnabled;
    String displayName;
    public float mSize;
    public float lastSize;
    public static UnicodeFontRenderer ufr;
    public Module(String name, int key, Category category){
        if(ufr == null)
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 2 , 2);
       // mSize = 0;
        //  lastSize = 0;

        if(this.displayName == null){
            this.displayName = name;
        }
        this.name = name;
        this.key = key;
        this.category = category;




    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        if(enabled){

            Execution.instance.eventManager.register(this);
            mSize = 0;
            lastSize = ufr.getStringWidth(this.getDisplayName());

        }else {
            Execution.instance.eventManager.unregister(this);
            mSize = ufr.getStringWidth(this.getDisplayName());
            lastSize = 0;

        }
        isEnabled = enabled;
        if(Execution.instance.saveLoad != null){
            Execution.instance.saveLoad.save();
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void toggle(){

        this.isEnabled = !isEnabled;
        PlayMusic.playSound(isEnabled ? "on.wav " : "off.wav");
        this.onToggle();
        if(isEnabled){
            this.onEnable();
        }else {
            this.onDisable();
        }
        if(Execution.instance.saveLoad != null){
            Execution.instance.saveLoad.save();
        }

        Execution.instance.notificationManager.show(new Notification(getName() + (isEnabled ? EnumChatFormatting.GREEN + " Enabled" : EnumChatFormatting.RED + " Disabled"), 2, NotificationType.INFO));



    }





    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void onEnable(){
        Execution.instance.eventManager.register(this);
        mSize = 0;
        lastSize = ufr.getStringWidth(this.getDisplayName());


    }
    public void onDisable(){
        Execution.instance.eventManager.unregister(this);
        mSize = ufr.getStringWidth(this.getDisplayName());
        lastSize =0;

    }
    public void onToggle(){}

    public Minecraft getMc(){
        return mc;
    }
}
