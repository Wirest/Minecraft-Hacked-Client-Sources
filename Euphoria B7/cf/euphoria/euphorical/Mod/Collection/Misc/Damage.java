// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Misc;

import cf.euphoria.euphorical.Utils.EntityUtils;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.NumValue;
import cf.euphoria.euphorical.Mod.Mod;

public class Damage extends Mod
{
    private NumValue dmg;
    
    public Damage() {
        super("Damage", Category.MISC);
        this.dmg = new NumValue("Damage Val.", 1.0, 1.0, 40.0, BoundedRangeComponent.ValueDisplay.INTEGER);
    }
    
    @Override
    public void onEnable() {
        EntityUtils.damagePlayer((int)this.dmg.getValue());
        this.setEnabled(false);
    }
}
