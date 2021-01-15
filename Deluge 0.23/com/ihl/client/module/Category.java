package com.ihl.client.module;

import net.minecraft.util.ResourceLocation;

public enum Category {

    COMBAT(0, "Combat", 0xFFFF0000),
    MISC(1, "Misc", 0xFFFFFF00),
    MOVEMENT(2, "Movement", 0xFF00FFFF),
    PLAYER(3, "Player", 0xFF00FF00),
    RENDER(4, "Render", 0xFFFF00FF);

    public int id;
    public String display;
    public int color;
    public ResourceLocation icon;

    Category(int id, String display, int color) {
        this.id = id;
        this.display = display;
        this.color = color;
        icon = new ResourceLocation("client/icons/category/" + display.toLowerCase() + ".png");
    }

}
