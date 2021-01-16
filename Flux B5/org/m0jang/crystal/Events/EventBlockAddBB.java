package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventBlockAddBB implements Event {
   private Block block;
   private AxisAlignedBB axisAlignedBB;
   private BlockPos pos;
   private Entity entity;

   public EventBlockAddBB(Block block, BlockPos pos, AxisAlignedBB axisAlignedBB, Entity entity) {
      this.block = block;
      this.axisAlignedBB = axisAlignedBB;
      this.pos = pos;
      this.entity = entity;
   }

   public Block getBlock() {
      return this.block;
   }

   public AxisAlignedBB getAxisAlignedBB() {
      return this.axisAlignedBB;
   }

   public void setAxisAlignedBB(AxisAlignedBB axisAlignedBB) {
      this.axisAlignedBB = axisAlignedBB;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
