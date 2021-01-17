package net.minecraftforge.client.model;

import com.google.common.base.Function;

public interface IModelState extends Function<IModelPart, TRSRTransformation> {
    TRSRTransformation apply(IModelPart var1);
}
