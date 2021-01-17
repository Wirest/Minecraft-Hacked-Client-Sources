/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.render.EventRender3D
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.tileentity.TileEntity
 */
package delta.module.modules;

import delta.Class192;
import delta.utils.BoundingBox;
import delta.utils.ColorUtils;
import delta.utils.RenderUtils;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.render.EventRender3D;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.tileentity.TileEntity;

public class ChestESP
extends Module {
    public ChestESP() {
        super("ChestESP", Category.Render);
        this.setDescription("Dessine une boite autour des coffres");
    }

    @EventTarget
    public void onRender3D(EventRender3D eventRender3D) {
        RenderUtils._hired();
        for (Object e : this.mc.theWorld.loadedTileEntityList) {
            if (!e.getClass().getName().toLowerCase().contains("chest") && !e.getClass().getName().toLowerCase().contains("carpenter") && !e.getClass().getName().toLowerCase().contains(Class192._pattern())) continue;
            TileEntity tileEntity = (TileEntity)e;
            RenderUtils._matrix(new BoundingBox(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord), ColorUtils.getColor(0L, 0.7f, 1), true);
        }
        RenderUtils._leading();
    }
}

