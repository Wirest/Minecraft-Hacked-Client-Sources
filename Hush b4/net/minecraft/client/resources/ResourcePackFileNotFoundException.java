// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.io.File;
import java.io.FileNotFoundException;

public class ResourcePackFileNotFoundException extends FileNotFoundException
{
    public ResourcePackFileNotFoundException(final File p_i1294_1_, final String p_i1294_2_) {
        super(String.format("'%s' in ResourcePack '%s'", p_i1294_2_, p_i1294_1_));
    }
}
