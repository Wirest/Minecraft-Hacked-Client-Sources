package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone extends CommandBase {
   public String getCommandName() {
      return "clone";
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender sender) {
      return "commands.clone.usage";
   }

   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length < 9) {
         throw new WrongUsageException("commands.clone.usage", new Object[0]);
      } else {
         sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
         BlockPos blockpos = parseBlockPos(sender, args, 0, false);
         BlockPos blockpos1 = parseBlockPos(sender, args, 3, false);
         BlockPos blockpos2 = parseBlockPos(sender, args, 6, false);
         StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
         StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.func_175896_b()));
         int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
         if (i > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", new Object[]{i, 32768});
         } else {
            boolean flag = false;
            Block block = null;
            int j = -1;
            if ((args.length < 11 || !args[10].equals("force") && !args[10].equals("move")) && structureboundingbox.intersectsWith(structureboundingbox1)) {
               throw new CommandException("commands.clone.noOverlap", new Object[0]);
            } else {
               if (args.length >= 11 && args[10].equals("move")) {
                  flag = true;
               }

               if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256 && structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256) {
                  World world = sender.getEntityWorld();
                  if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox1)) {
                     boolean flag1 = false;
                     if (args.length >= 10) {
                        if (args[9].equals("masked")) {
                           flag1 = true;
                        } else if (args[9].equals("filtered")) {
                           if (args.length < 12) {
                              throw new WrongUsageException("commands.clone.usage", new Object[0]);
                           }

                           block = getBlockByText(sender, args[11]);
                           if (args.length >= 13) {
                              j = parseInt(args[12], 0, 15);
                           }
                        }
                     }

                     List list = Lists.newArrayList();
                     List list1 = Lists.newArrayList();
                     List list2 = Lists.newArrayList();
                     LinkedList linkedlist = Lists.newLinkedList();
                     BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);

                     for(int k = structureboundingbox.minZ; k <= structureboundingbox.maxZ; ++k) {
                        for(int l = structureboundingbox.minY; l <= structureboundingbox.maxY; ++l) {
                           for(int i1 = structureboundingbox.minX; i1 <= structureboundingbox.maxX; ++i1) {
                              BlockPos blockpos4 = new BlockPos(i1, l, k);
                              BlockPos blockpos5 = blockpos4.add(blockpos3);
                              IBlockState iblockstate = world.getBlockState(blockpos4);
                              if ((!flag1 || iblockstate.getBlock() != Blocks.air) && (block == null || iblockstate.getBlock() == block && (j < 0 || iblockstate.getBlock().getMetaFromState(iblockstate) == j))) {
                                 TileEntity tileentity = world.getTileEntity(blockpos4);
                                 if (tileentity != null) {
                                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                                    tileentity.writeToNBT(nbttagcompound);
                                    list1.add(new CommandClone.StaticCloneData(blockpos5, iblockstate, nbttagcompound));
                                    linkedlist.addLast(blockpos4);
                                 } else if (!iblockstate.getBlock().isFullBlock() && !iblockstate.getBlock().isFullCube()) {
                                    list2.add(new CommandClone.StaticCloneData(blockpos5, iblockstate, (NBTTagCompound)null));
                                    linkedlist.addFirst(blockpos4);
                                 } else {
                                    list.add(new CommandClone.StaticCloneData(blockpos5, iblockstate, (NBTTagCompound)null));
                                    linkedlist.addLast(blockpos4);
                                 }
                              }
                           }
                        }
                     }

                     if (flag) {
                        Iterator var27;
                        BlockPos blockpos7;
                        for(var27 = linkedlist.iterator(); var27.hasNext(); world.setBlockState(blockpos7, Blocks.barrier.getDefaultState(), 2)) {
                           blockpos7 = (BlockPos)var27.next();
                           TileEntity tileentity1 = world.getTileEntity(blockpos7);
                           if (tileentity1 instanceof IInventory) {
                              ((IInventory)tileentity1).clear();
                           }
                        }

                        var27 = linkedlist.iterator();

                        while(var27.hasNext()) {
                           blockpos7 = (BlockPos)var27.next();
                           world.setBlockState(blockpos7, Blocks.air.getDefaultState(), 3);
                        }
                     }

                     List list3 = Lists.newArrayList();
                     list3.addAll(list);
                     list3.addAll(list1);
                     list3.addAll(list2);
                     List list4 = Lists.reverse(list3);

                     Iterator var32;
                     CommandClone.StaticCloneData commandclone$staticclonedata3;
                     TileEntity tileentity3;
                     for(var32 = list4.iterator(); var32.hasNext(); world.setBlockState(commandclone$staticclonedata3.field_179537_a, Blocks.barrier.getDefaultState(), 2)) {
                        commandclone$staticclonedata3 = (CommandClone.StaticCloneData)var32.next();
                        tileentity3 = world.getTileEntity(commandclone$staticclonedata3.field_179537_a);
                        if (tileentity3 instanceof IInventory) {
                           ((IInventory)tileentity3).clear();
                        }
                     }

                     i = 0;
                     var32 = list3.iterator();

                     while(var32.hasNext()) {
                        commandclone$staticclonedata3 = (CommandClone.StaticCloneData)var32.next();
                        if (world.setBlockState(commandclone$staticclonedata3.field_179537_a, commandclone$staticclonedata3.blockState, 2)) {
                           ++i;
                        }
                     }

                     for(var32 = list1.iterator(); var32.hasNext(); world.setBlockState(commandclone$staticclonedata3.field_179537_a, commandclone$staticclonedata3.blockState, 2)) {
                        commandclone$staticclonedata3 = (CommandClone.StaticCloneData)var32.next();
                        tileentity3 = world.getTileEntity(commandclone$staticclonedata3.field_179537_a);
                        if (commandclone$staticclonedata3.field_179536_c != null && tileentity3 != null) {
                           commandclone$staticclonedata3.field_179536_c.setInteger("x", commandclone$staticclonedata3.field_179537_a.getX());
                           commandclone$staticclonedata3.field_179536_c.setInteger("y", commandclone$staticclonedata3.field_179537_a.getY());
                           commandclone$staticclonedata3.field_179536_c.setInteger("z", commandclone$staticclonedata3.field_179537_a.getZ());
                           tileentity3.readFromNBT(commandclone$staticclonedata3.field_179536_c);
                           tileentity3.markDirty();
                        }
                     }

                     var32 = list4.iterator();

                     while(var32.hasNext()) {
                        commandclone$staticclonedata3 = (CommandClone.StaticCloneData)var32.next();
                        world.notifyNeighborsRespectDebug(commandclone$staticclonedata3.field_179537_a, commandclone$staticclonedata3.blockState.getBlock());
                     }

                     List list5 = world.func_175712_a(structureboundingbox, false);
                     if (list5 != null) {
                        Iterator var36 = list5.iterator();

                        while(var36.hasNext()) {
                           NextTickListEntry nextticklistentry = (NextTickListEntry)var36.next();
                           if (structureboundingbox.isVecInside(nextticklistentry.position)) {
                              BlockPos blockpos8 = nextticklistentry.position.add(blockpos3);
                              world.scheduleBlockUpdate(blockpos8, nextticklistentry.getBlock(), (int)(nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
                           }
                        }
                     }

                     if (i <= 0) {
                        throw new CommandException("commands.clone.failed", new Object[0]);
                     } else {
                        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                        notifyOperators(sender, this, "commands.clone.success", new Object[]{i});
                     }
                  } else {
                     throw new CommandException("commands.clone.outOfWorld", new Object[0]);
                  }
               } else {
                  throw new CommandException("commands.clone.outOfWorld", new Object[0]);
               }
            }
         }
      }
   }

   public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : (args.length > 3 && args.length <= 6 ? func_175771_a(args, 3, pos) : (args.length > 6 && args.length <= 9 ? func_175771_a(args, 6, pos) : (args.length == 10 ? getListOfStringsMatchingLastWord(args, new String[]{"replace", "masked", "filtered"}) : (args.length == 11 ? getListOfStringsMatchingLastWord(args, new String[]{"normal", "force", "move"}) : (args.length == 12 && "filtered".equals(args[9]) ? getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys()) : null)))));
   }

   static class StaticCloneData {
      public final BlockPos field_179537_a;
      public final IBlockState blockState;
      public final NBTTagCompound field_179536_c;

      public StaticCloneData(BlockPos p_i46037_1_, IBlockState p_i46037_2_, NBTTagCompound p_i46037_3_) {
         this.field_179537_a = p_i46037_1_;
         this.blockState = p_i46037_2_;
         this.field_179536_c = p_i46037_3_;
      }
   }
}
