/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ 
/*     */ public enum EnumFacing implements IStringSerializable
/*     */ {
/*  12 */   DOWN("DOWN", 0, 0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)), 
/*  13 */   UP("UP", 1, 1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)), 
/*  14 */   NORTH("NORTH", 2, 2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)), 
/*  15 */   SOUTH("SOUTH", 3, 3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)), 
/*  16 */   WEST("WEST", 4, 4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)), 
/*  17 */   EAST("EAST", 5, 5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
/*     */   
/*     */ 
/*     */   private final int index;
/*     */   
/*     */   private final int opposite;
/*     */   
/*     */   private final int horizontalIndex;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final Axis axis;
/*     */   
/*     */   private final AxisDirection axisDirection;
/*     */   
/*     */   private final Vec3i directionVec;
/*     */   
/*     */   public static final EnumFacing[] VALUES;
/*     */   
/*     */   private static final EnumFacing[] HORIZONTALS;
/*     */   
/*     */   private static final Map NAME_LOOKUP;
/*     */   
/*     */   private static final EnumFacing[] $VALUES;
/*     */   private static final String __OBFID = "CL_00001201";
/*     */   
/*     */   private EnumFacing(String p_i13_3_, int p_i13_4_, int p_i13_5_, int p_i13_6_, int p_i13_7_, String p_i13_8_, AxisDirection p_i13_9_, Axis p_i13_10_, Vec3i p_i13_11_)
/*     */   {
/*  45 */     this.index = p_i13_5_;
/*  46 */     this.horizontalIndex = p_i13_7_;
/*  47 */     this.opposite = p_i13_6_;
/*  48 */     this.name = p_i13_8_;
/*  49 */     this.axis = p_i13_10_;
/*  50 */     this.axisDirection = p_i13_9_;
/*  51 */     this.directionVec = p_i13_11_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/*  59 */     return this.index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHorizontalIndex()
/*     */   {
/*  67 */     return this.horizontalIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AxisDirection getAxisDirection()
/*     */   {
/*  75 */     return this.axisDirection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumFacing getOpposite()
/*     */   {
/*  83 */     return getFront(this.opposite);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumFacing rotateAround(Axis axis)
/*     */   {
/*  92 */     switch (EnumFacing.1.field_179515_a[axis.ordinal()])
/*     */     {
/*     */     case 1: 
/*  95 */       if ((this != WEST) && (this != EAST))
/*     */       {
/*  97 */         return rotateX();
/*     */       }
/*     */       
/* 100 */       return this;
/*     */     
/*     */     case 2: 
/* 103 */       if ((this != UP) && (this != DOWN))
/*     */       {
/* 105 */         return rotateY();
/*     */       }
/*     */       
/* 108 */       return this;
/*     */     
/*     */     case 3: 
/* 111 */       if ((this != NORTH) && (this != SOUTH))
/*     */       {
/* 113 */         return rotateZ();
/*     */       }
/*     */       
/* 116 */       return this;
/*     */     }
/*     */     
/* 119 */     throw new IllegalStateException("Unable to get CW facing for axis " + axis);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumFacing rotateY()
/*     */   {
/* 128 */     switch (EnumFacing.1.field_179513_b[ordinal()])
/*     */     {
/*     */     case 1: 
/* 131 */       return EAST;
/*     */     
/*     */     case 2: 
/* 134 */       return SOUTH;
/*     */     
/*     */     case 3: 
/* 137 */       return WEST;
/*     */     
/*     */     case 4: 
/* 140 */       return NORTH;
/*     */     }
/*     */     
/* 143 */     throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private EnumFacing rotateX()
/*     */   {
/* 152 */     switch (EnumFacing.1.field_179513_b[ordinal()])
/*     */     {
/*     */     case 1: 
/* 155 */       return DOWN;
/*     */     
/*     */     case 2: 
/*     */     case 4: 
/*     */     default: 
/* 160 */       throw new IllegalStateException("Unable to get X-rotated facing of " + this);
/*     */     
/*     */     case 3: 
/* 163 */       return UP;
/*     */     
/*     */     case 5: 
/* 166 */       return NORTH;
/*     */     }
/*     */     
/* 169 */     return SOUTH;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private EnumFacing rotateZ()
/*     */   {
/* 178 */     switch (EnumFacing.1.field_179513_b[ordinal()])
/*     */     {
/*     */     case 2: 
/* 181 */       return DOWN;
/*     */     
/*     */     case 3: 
/*     */     default: 
/* 185 */       throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
/*     */     
/*     */     case 4: 
/* 188 */       return UP;
/*     */     
/*     */     case 5: 
/* 191 */       return EAST;
/*     */     }
/*     */     
/* 194 */     return WEST;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumFacing rotateYCCW()
/*     */   {
/* 203 */     switch (EnumFacing.1.field_179513_b[ordinal()])
/*     */     {
/*     */     case 1: 
/* 206 */       return WEST;
/*     */     
/*     */     case 2: 
/* 209 */       return NORTH;
/*     */     
/*     */     case 3: 
/* 212 */       return EAST;
/*     */     
/*     */     case 4: 
/* 215 */       return SOUTH;
/*     */     }
/*     */     
/* 218 */     throw new IllegalStateException("Unable to get CCW facing of " + this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFrontOffsetX()
/*     */   {
/* 227 */     return this.axis == Axis.X ? this.axisDirection.getOffset() : 0;
/*     */   }
/*     */   
/*     */   public int getFrontOffsetY()
/*     */   {
/* 232 */     return this.axis == Axis.Y ? this.axisDirection.getOffset() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFrontOffsetZ()
/*     */   {
/* 240 */     return this.axis == Axis.Z ? this.axisDirection.getOffset() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName2()
/*     */   {
/* 248 */     return this.name;
/*     */   }
/*     */   
/*     */   public Axis getAxis()
/*     */   {
/* 253 */     return this.axis;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumFacing byName(String name)
/*     */   {
/* 261 */     return name == null ? null : (EnumFacing)NAME_LOOKUP.get(name.toLowerCase());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumFacing getFront(int index)
/*     */   {
/* 269 */     return VALUES[MathHelper.abs_int(index % VALUES.length)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumFacing getHorizontal(int p_176731_0_)
/*     */   {
/* 277 */     return HORIZONTALS[MathHelper.abs_int(p_176731_0_ % HORIZONTALS.length)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumFacing fromAngle(double angle)
/*     */   {
/* 285 */     return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 0x3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumFacing random(Random rand)
/*     */   {
/* 293 */     return values()[rand.nextInt(values().length)];
/*     */   }
/*     */   
/*     */   public static EnumFacing getFacingFromVector(float p_176737_0_, float p_176737_1_, float p_176737_2_)
/*     */   {
/* 298 */     EnumFacing enumfacing = NORTH;
/* 299 */     float f = Float.MIN_VALUE;
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 301 */     int j = (arrayOfEnumFacing = values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing1 = arrayOfEnumFacing[i];
/*     */       
/* 303 */       float f1 = p_176737_0_ * enumfacing1.directionVec.getX() + p_176737_1_ * enumfacing1.directionVec.getY() + p_176737_2_ * enumfacing1.directionVec.getZ();
/*     */       
/* 305 */       if (f1 > f)
/*     */       {
/* 307 */         f = f1;
/* 308 */         enumfacing = enumfacing1;
/*     */       }
/*     */     }
/*     */     
/* 312 */     return enumfacing;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 317 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 322 */     return this.name;
/*     */   }
/*     */   
/*     */   public static EnumFacing func_181076_a(AxisDirection p_181076_0_, Axis p_181076_1_) {
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 327 */     int j = (arrayOfEnumFacing = values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/* 329 */       if ((enumfacing.getAxisDirection() == p_181076_0_) && (enumfacing.getAxis() == p_181076_1_))
/*     */       {
/* 331 */         return enumfacing;
/*     */       }
/*     */     }
/*     */     
/* 335 */     throw new IllegalArgumentException("No such direction: " + p_181076_0_ + " " + p_181076_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vec3i getDirectionVec()
/*     */   {
/* 343 */     return this.directionVec;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  35 */     VALUES = new EnumFacing[6];
/*     */     
/*     */ 
/*  38 */     HORIZONTALS = new EnumFacing[4];
/*  39 */     NAME_LOOKUP = Maps.newHashMap();
/*  40 */     $VALUES = new EnumFacing[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     EnumFacing[] arrayOfEnumFacing;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 347 */     int j = (arrayOfEnumFacing = values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/* 349 */       VALUES[enumfacing.index] = enumfacing;
/*     */       
/* 351 */       if (enumfacing.getAxis().isHorizontal())
/*     */       {
/* 353 */         HORIZONTALS[enumfacing.horizontalIndex] = enumfacing;
/*     */       }
/*     */       
/* 356 */       NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(), enumfacing);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum Axis
/*     */     implements Predicate, IStringSerializable
/*     */   {
/* 462 */     X("X", 0, "x", EnumFacing.Plane.HORIZONTAL), 
/* 463 */     Y("Y", 1, "y", EnumFacing.Plane.VERTICAL), 
/* 464 */     Z("Z", 2, "z", EnumFacing.Plane.HORIZONTAL);
/*     */     
/*     */     private static final Map NAME_LOOKUP;
/*     */     private final String name;
/*     */     private final EnumFacing.Plane plane;
/*     */     private static final Axis[] $VALUES;
/*     */     private static final String __OBFID = "CL_00002321";
/*     */     
/*     */     private Axis(String p_i10_3_, int p_i10_4_, String p_i10_5_, EnumFacing.Plane p_i10_6_)
/*     */     {
/* 474 */       this.name = p_i10_5_;
/* 475 */       this.plane = p_i10_6_;
/*     */     }
/*     */     
/*     */     public static Axis byName(String name)
/*     */     {
/* 480 */       return name == null ? null : (Axis)NAME_LOOKUP.get(name.toLowerCase());
/*     */     }
/*     */     
/*     */     public String getName2()
/*     */     {
/* 485 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isVertical()
/*     */     {
/* 490 */       return this.plane == EnumFacing.Plane.VERTICAL;
/*     */     }
/*     */     
/*     */     public boolean isHorizontal()
/*     */     {
/* 495 */       return this.plane == EnumFacing.Plane.HORIZONTAL;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 500 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean apply(EnumFacing p_apply_1_)
/*     */     {
/* 505 */       return (p_apply_1_ != null) && (p_apply_1_.getAxis() == this);
/*     */     }
/*     */     
/*     */     public EnumFacing.Plane getPlane()
/*     */     {
/* 510 */       return this.plane;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 515 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean apply(Object p_apply_1_)
/*     */     {
/* 520 */       return apply((EnumFacing)p_apply_1_);
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 466 */       NAME_LOOKUP = Maps.newHashMap();
/*     */       
/*     */ 
/* 469 */       $VALUES = new Axis[] { X, Y, Z };
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       Axis[] arrayOfAxis;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 524 */       int j = (arrayOfAxis = values()).length; for (int i = 0; i < j; i++) { Axis enumfacing$axis = arrayOfAxis[i];
/*     */         
/* 526 */         NAME_LOOKUP.put(enumfacing$axis.getName2().toLowerCase(), enumfacing$axis);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum AxisDirection {
/* 532 */     POSITIVE("POSITIVE", 0, 1, "Towards positive"), 
/* 533 */     NEGATIVE("NEGATIVE", 1, -1, "Towards negative");
/*     */     
/*     */     private final int offset;
/*     */     private final String description;
/* 537 */     private static final AxisDirection[] $VALUES = { POSITIVE, NEGATIVE };
/*     */     private static final String __OBFID = "CL_00002320";
/*     */     
/*     */     private AxisDirection(String p_i11_3_, int p_i11_4_, int p_i11_5_, String p_i11_6_)
/*     */     {
/* 542 */       this.offset = p_i11_5_;
/* 543 */       this.description = p_i11_6_;
/*     */     }
/*     */     
/*     */     public int getOffset()
/*     */     {
/* 548 */       return this.offset;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 553 */       return this.description;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum Plane implements Predicate, Iterable {
/* 558 */     HORIZONTAL("HORIZONTAL", 0), 
/* 559 */     VERTICAL("VERTICAL", 1);
/*     */     
/* 561 */     private static final Plane[] $VALUES = { HORIZONTAL, VERTICAL };
/*     */     
/*     */     private static final String __OBFID = "CL_00002319";
/*     */     
/*     */ 
/*     */     private Plane(String p_i12_3_, int p_i12_4_) {}
/*     */     
/*     */     public EnumFacing[] facings()
/*     */     {
/* 570 */       switch (EnumFacing.EnumFacing.1.field_179514_c[ordinal()])
/*     */       {
/*     */       case 1: 
/* 573 */         return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
/*     */       case 2: 
/* 575 */         return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
/*     */       }
/* 577 */       throw new Error("Someone's been tampering with the universe!");
/*     */     }
/*     */     
/*     */ 
/*     */     public EnumFacing random(Random rand)
/*     */     {
/* 583 */       EnumFacing[] aenumfacing = facings();
/* 584 */       return aenumfacing[rand.nextInt(aenumfacing.length)];
/*     */     }
/*     */     
/*     */     public boolean apply(EnumFacing p_apply_1_)
/*     */     {
/* 589 */       return (p_apply_1_ != null) && (p_apply_1_.getAxis().getPlane() == this);
/*     */     }
/*     */     
/*     */     public Iterator iterator()
/*     */     {
/* 594 */       return Iterators.forArray(facings());
/*     */     }
/*     */     
/*     */     public boolean apply(Object p_apply_1_)
/*     */     {
/* 599 */       return apply((EnumFacing)p_apply_1_);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\EnumFacing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */