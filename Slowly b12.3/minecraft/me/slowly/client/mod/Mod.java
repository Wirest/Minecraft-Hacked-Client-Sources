/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod;

import com.darkmagician6.eventapi.EventManager;
import java.util.List;
import me.slowly.client.Client;
import me.slowly.client.util.FileUtil;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;

public class Mod {
    public Minecraft mc = Minecraft.getMinecraft();
    public EntityPlayerSP player;
    public Value showValue;
    private String name;
    private int color;
    private int key;
    private Category category;
    private boolean isEnabled;
    private String desc;
    public boolean openValues;
    public double arrowAnlge;
    public double animateX;
    public float posX;
    public double hoverOpacity;
    public float circleValue;
    public boolean canSeeCircle;
    public int[] circleCoords;
    public boolean clickedCircle;
    public int toggleTime = 0;

    public Mod(String name, Category category, int color) {
        this.player = this.mc.thePlayer;
        this.arrowAnlge = 0.0;
        this.animateX = 0.0;
        this.hoverOpacity = 0.0;
        this.name = name;
        this.color = color;
        this.key = -1;
        this.category = category;
        this.circleCoords = new int[2];
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void disableValues() {
    }

    public String getValue() {
        if (this.showValue == null) {
            return "";
        }
        return this.showValue.isValueMode ? this.showValue.getModeAt(this.showValue.getCurrentMode()) : String.valueOf(this.showValue.getValueState());
    }

    public String getNameWithSuffix() {
        return String.valueOf(this.getName()) + (this.getValue().equalsIgnoreCase("") ? "" : new StringBuilder(String.valueOf(this.getValue())).append(" ").toString());
    }

    public void set(boolean state) {
        this.set(state, false);
        Client.getInstance().getFileUtil().saveMods();
    }

    public void set(boolean state, boolean safe) {
        this.isEnabled = state;
        this.onToggle();
        if (state) {
            if (this.mc.theWorld != null) {
                this.onEnable();
            }
            EventManager.register(this);
        } else {
            if (this.mc.theWorld != null) {
                this.onDisable();
            }
            EventManager.unregister(this);
        }
        this.posX = this.mc.fontRendererObj.getStringWidth(this.getNameWithSuffix()) * (this.isEnabled ? 1 : 0);
        if (safe) {
            Client.getInstance().getFileUtil().saveMods();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public int getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean hasValues() {
        for (Value value : Value.list) {
            String name = value.getValueName().split("_")[0];
            if (!name.equalsIgnoreCase(this.getName())) continue;
            return true;
        }
        return false;
    }

    public static enum Category {
    	COMBAT("COMBAT", 0), 
        MOVEMENT("MOVEMENT", 1), 
        RENDER("RENDER", 2), 
        PLAYER("PLAYER", 3), 
        MISCELLANEOUS("MISCELLANEOUS", 4), 
        WORLD("WORLD", 5), 
        NONE("NONE", 6), 
        SETTINGS("SETTINGS", 7);
        

        private Category(String string2, int n2) {
        }
    }

	public void onUpdate() {
		toggleTime++;
	}

	public void onGui() {
		
	}

	public void onRender() {

		
	}



}

