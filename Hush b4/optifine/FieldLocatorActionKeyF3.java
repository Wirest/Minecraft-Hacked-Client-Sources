// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class FieldLocatorActionKeyF3 implements IFieldLocator
{
    @Override
    public Field getField() {
        final Class oclass = Minecraft.class;
        final Field field = this.getFieldRenderChunksMany();
        if (field == null) {
            Config.log("(Reflector) Field not present: " + oclass.getName() + ".actionKeyF3 (field renderChunksMany not found)");
            return null;
        }
        final Field field2 = ReflectorRaw.getFieldAfter(Minecraft.class, field, Boolean.TYPE, 0);
        if (field2 == null) {
            Config.log("(Reflector) Field not present: " + oclass.getName() + ".actionKeyF3");
            return null;
        }
        return field2;
    }
    
    private Field getFieldRenderChunksMany() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final boolean flag = minecraft.renderChunksMany;
        final Field[] afield = Minecraft.class.getDeclaredFields();
        minecraft.renderChunksMany = true;
        final Field[] afield2 = ReflectorRaw.getFields(minecraft, afield, Boolean.TYPE, Boolean.TRUE);
        minecraft.renderChunksMany = false;
        final Field[] afield3 = ReflectorRaw.getFields(minecraft, afield, Boolean.TYPE, Boolean.FALSE);
        minecraft.renderChunksMany = flag;
        final Set<Field> set = new HashSet<Field>(Arrays.asList(afield2));
        final Set<Field> set2 = new HashSet<Field>(Arrays.asList(afield3));
        final Set<Field> set3 = new HashSet<Field>(set);
        set3.retainAll(set2);
        final Field[] afield4 = set3.toArray(new Field[set3.size()]);
        return (afield4.length != 1) ? null : afield4[0];
    }
}
