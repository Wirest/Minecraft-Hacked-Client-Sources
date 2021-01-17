// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.RealmsScreen;

public abstract class RealmsScreenWithCallback<T> extends RealmsScreen
{
    abstract void callback(final T p0);
}
