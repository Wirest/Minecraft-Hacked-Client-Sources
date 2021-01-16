package net.minecraftforge.common.model;

import com.google.common.base.Optional;

public interface IModelState
{
    Optional<TRSRTransformation> apply(Optional <? extends IModelPart > var1);
}
