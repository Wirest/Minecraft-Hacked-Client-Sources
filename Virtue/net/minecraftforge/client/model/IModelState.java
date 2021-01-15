// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraftforge.client.model;

import com.google.common.base.Function;

public interface IModelState extends Function<IModelPart, TRSRTransformation>
{
    TRSRTransformation apply(final IModelPart p0);
}
