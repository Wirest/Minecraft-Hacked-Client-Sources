// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.loading;

import org.newdawn.slick.util.Log;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.InternalTextureLoader;
import java.util.ArrayList;

public class LoadingList
{
    private static LoadingList single;
    private ArrayList deferred;
    private int total;
    
    static {
        LoadingList.single = new LoadingList();
    }
    
    public static LoadingList get() {
        return LoadingList.single;
    }
    
    public static void setDeferredLoading(final boolean loading) {
        LoadingList.single = new LoadingList();
        InternalTextureLoader.get().setDeferredLoading(loading);
        SoundStore.get().setDeferredLoading(loading);
    }
    
    public static boolean isDeferredLoading() {
        return InternalTextureLoader.get().isDeferredLoading();
    }
    
    private LoadingList() {
        this.deferred = new ArrayList();
    }
    
    public void add(final DeferredResource resource) {
        ++this.total;
        this.deferred.add(resource);
    }
    
    public void remove(final DeferredResource resource) {
        Log.info("Early loading of deferred resource due to req: " + resource.getDescription());
        --this.total;
        this.deferred.remove(resource);
    }
    
    public int getTotalResources() {
        return this.total;
    }
    
    public int getRemainingResources() {
        return this.deferred.size();
    }
    
    public DeferredResource getNext() {
        if (this.deferred.size() == 0) {
            return null;
        }
        return this.deferred.remove(0);
    }
}
