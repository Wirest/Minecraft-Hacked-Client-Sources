// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.world.World;

public class StructureMineshaftStart extends StructureStart
{
    public StructureMineshaftStart() {
    }
    
    public StructureMineshaftStart(final World worldIn, final Random rand, final int chunkX, final int chunkZ) {
        super(chunkX, chunkZ);
        final StructureMineshaftPieces.Room structuremineshaftpieces$room = new StructureMineshaftPieces.Room(0, rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
        this.components.add(structuremineshaftpieces$room);
        structuremineshaftpieces$room.buildComponent(structuremineshaftpieces$room, this.components, rand);
        this.updateBoundingBox();
        this.markAvailableHeight(worldIn, rand, 10);
    }
}
