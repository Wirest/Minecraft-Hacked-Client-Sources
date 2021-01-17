/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;

public class EventRenderTileEntity
implements Event {
    private TileEntityRendererDispatcher tileEntityRendererDispatcher;
    private TileEntity tileentityIn;
    private float partialTicks;
    private int destroyStage;
    private double staticPlayerX;
    private double staticPlayerY;
    private double staticPlayerZ;
    public boolean cancel;

    public EventRenderTileEntity(TileEntityRendererDispatcher tileEntityRendererDispatcher, TileEntity tileentityIn, float partialTicks, int destroyStage, double staticPlayerX, double staticPlayerY, double staticPlayerZ) {
        this.tileEntityRendererDispatcher = tileEntityRendererDispatcher;
        this.tileentityIn = tileentityIn;
        this.partialTicks = partialTicks;
        this.destroyStage = destroyStage;
        this.staticPlayerX = staticPlayerX;
        this.staticPlayerY = staticPlayerY;
        this.staticPlayerZ = staticPlayerZ;
    }

    public TileEntityRendererDispatcher getTileEntityRendererDispatcher() {
        return this.tileEntityRendererDispatcher;
    }

    public TileEntity getTileentityIn() {
        return this.tileentityIn;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public int getDestroyStage() {
        return this.destroyStage;
    }

    public double getStaticPlayerX() {
        return this.staticPlayerX;
    }

    public double getStaticPlayerY() {
        return this.staticPlayerY;
    }

    public double getStaticPlayerZ() {
        return this.staticPlayerZ;
    }
}

