/*     */ package net.minecraft.init;
/*     */ 
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemArmorStand;
/*     */ import net.minecraft.item.ItemBow;
/*     */ import net.minecraft.item.ItemEmptyMap;
/*     */ import net.minecraft.item.ItemEnchantedBook;
/*     */ import net.minecraft.item.ItemFishingRod;
/*     */ import net.minecraft.item.ItemMap;
/*     */ import net.minecraft.item.ItemPotion;
/*     */ import net.minecraft.item.ItemShears;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Items
/*     */ {
/*     */   private static Item getRegisteredItem(String name)
/*     */   {
/* 207 */     return (Item)Item.itemRegistry.getObject(new ResourceLocation(name));
/*     */   }
/*     */   
/*     */   static
/*     */   {
/* 212 */     if (!Bootstrap.isRegistered())
/*     */     {
/* 214 */       throw new RuntimeException("Accessed Items before Bootstrap!");
/*     */     }
/*     */   }
/*     */   
/* 218 */   public static final Item iron_shovel = getRegisteredItem("iron_shovel");
/* 219 */   public static final Item iron_pickaxe = getRegisteredItem("iron_pickaxe");
/* 220 */   public static final Item iron_axe = getRegisteredItem("iron_axe");
/* 221 */   public static final Item flint_and_steel = getRegisteredItem("flint_and_steel");
/* 222 */   public static final Item apple = getRegisteredItem("apple");
/* 223 */   public static final ItemBow bow = (ItemBow)getRegisteredItem("bow");
/* 224 */   public static final Item arrow = getRegisteredItem("arrow");
/* 225 */   public static final Item coal = getRegisteredItem("coal");
/* 226 */   public static final Item diamond = getRegisteredItem("diamond");
/* 227 */   public static final Item iron_ingot = getRegisteredItem("iron_ingot");
/* 228 */   public static final Item gold_ingot = getRegisteredItem("gold_ingot");
/* 229 */   public static final Item iron_sword = getRegisteredItem("iron_sword");
/* 230 */   public static final Item wooden_sword = getRegisteredItem("wooden_sword");
/* 231 */   public static final Item wooden_shovel = getRegisteredItem("wooden_shovel");
/* 232 */   public static final Item wooden_pickaxe = getRegisteredItem("wooden_pickaxe");
/* 233 */   public static final Item wooden_axe = getRegisteredItem("wooden_axe");
/* 234 */   public static final Item stone_sword = getRegisteredItem("stone_sword");
/* 235 */   public static final Item stone_shovel = getRegisteredItem("stone_shovel");
/* 236 */   public static final Item stone_pickaxe = getRegisteredItem("stone_pickaxe");
/* 237 */   public static final Item stone_axe = getRegisteredItem("stone_axe");
/* 238 */   public static final Item diamond_sword = getRegisteredItem("diamond_sword");
/* 239 */   public static final Item diamond_shovel = getRegisteredItem("diamond_shovel");
/* 240 */   public static final Item diamond_pickaxe = getRegisteredItem("diamond_pickaxe");
/* 241 */   public static final Item diamond_axe = getRegisteredItem("diamond_axe");
/* 242 */   public static final Item stick = getRegisteredItem("stick");
/* 243 */   public static final Item bowl = getRegisteredItem("bowl");
/* 244 */   public static final Item mushroom_stew = getRegisteredItem("mushroom_stew");
/* 245 */   public static final Item golden_sword = getRegisteredItem("golden_sword");
/* 246 */   public static final Item golden_shovel = getRegisteredItem("golden_shovel");
/* 247 */   public static final Item golden_pickaxe = getRegisteredItem("golden_pickaxe");
/* 248 */   public static final Item golden_axe = getRegisteredItem("golden_axe");
/* 249 */   public static final Item string = getRegisteredItem("string");
/* 250 */   public static final Item feather = getRegisteredItem("feather");
/* 251 */   public static final Item gunpowder = getRegisteredItem("gunpowder");
/* 252 */   public static final Item wooden_hoe = getRegisteredItem("wooden_hoe");
/* 253 */   public static final Item stone_hoe = getRegisteredItem("stone_hoe");
/* 254 */   public static final Item iron_hoe = getRegisteredItem("iron_hoe");
/* 255 */   public static final Item diamond_hoe = getRegisteredItem("diamond_hoe");
/* 256 */   public static final Item golden_hoe = getRegisteredItem("golden_hoe");
/* 257 */   public static final Item wheat_seeds = getRegisteredItem("wheat_seeds");
/* 258 */   public static final Item wheat = getRegisteredItem("wheat");
/* 259 */   public static final Item bread = getRegisteredItem("bread");
/* 260 */   public static final ItemArmor leather_helmet = (ItemArmor)getRegisteredItem("leather_helmet");
/* 261 */   public static final ItemArmor leather_chestplate = (ItemArmor)getRegisteredItem("leather_chestplate");
/* 262 */   public static final ItemArmor leather_leggings = (ItemArmor)getRegisteredItem("leather_leggings");
/* 263 */   public static final ItemArmor leather_boots = (ItemArmor)getRegisteredItem("leather_boots");
/* 264 */   public static final ItemArmor chainmail_helmet = (ItemArmor)getRegisteredItem("chainmail_helmet");
/* 265 */   public static final ItemArmor chainmail_chestplate = (ItemArmor)getRegisteredItem("chainmail_chestplate");
/* 266 */   public static final ItemArmor chainmail_leggings = (ItemArmor)getRegisteredItem("chainmail_leggings");
/* 267 */   public static final ItemArmor chainmail_boots = (ItemArmor)getRegisteredItem("chainmail_boots");
/* 268 */   public static final ItemArmor iron_helmet = (ItemArmor)getRegisteredItem("iron_helmet");
/* 269 */   public static final ItemArmor iron_chestplate = (ItemArmor)getRegisteredItem("iron_chestplate");
/* 270 */   public static final ItemArmor iron_leggings = (ItemArmor)getRegisteredItem("iron_leggings");
/* 271 */   public static final ItemArmor iron_boots = (ItemArmor)getRegisteredItem("iron_boots");
/* 272 */   public static final ItemArmor diamond_helmet = (ItemArmor)getRegisteredItem("diamond_helmet");
/* 273 */   public static final ItemArmor diamond_chestplate = (ItemArmor)getRegisteredItem("diamond_chestplate");
/* 274 */   public static final ItemArmor diamond_leggings = (ItemArmor)getRegisteredItem("diamond_leggings");
/* 275 */   public static final ItemArmor diamond_boots = (ItemArmor)getRegisteredItem("diamond_boots");
/* 276 */   public static final ItemArmor golden_helmet = (ItemArmor)getRegisteredItem("golden_helmet");
/* 277 */   public static final ItemArmor golden_chestplate = (ItemArmor)getRegisteredItem("golden_chestplate");
/* 278 */   public static final ItemArmor golden_leggings = (ItemArmor)getRegisteredItem("golden_leggings");
/* 279 */   public static final ItemArmor golden_boots = (ItemArmor)getRegisteredItem("golden_boots");
/* 280 */   public static final Item flint = getRegisteredItem("flint");
/* 281 */   public static final Item porkchop = getRegisteredItem("porkchop");
/* 282 */   public static final Item cooked_porkchop = getRegisteredItem("cooked_porkchop");
/* 283 */   public static final Item painting = getRegisteredItem("painting");
/* 284 */   public static final Item golden_apple = getRegisteredItem("golden_apple");
/* 285 */   public static final Item sign = getRegisteredItem("sign");
/* 286 */   public static final Item oak_door = getRegisteredItem("wooden_door");
/* 287 */   public static final Item spruce_door = getRegisteredItem("spruce_door");
/* 288 */   public static final Item birch_door = getRegisteredItem("birch_door");
/* 289 */   public static final Item jungle_door = getRegisteredItem("jungle_door");
/* 290 */   public static final Item acacia_door = getRegisteredItem("acacia_door");
/* 291 */   public static final Item dark_oak_door = getRegisteredItem("dark_oak_door");
/* 292 */   public static final Item bucket = getRegisteredItem("bucket");
/* 293 */   public static final Item water_bucket = getRegisteredItem("water_bucket");
/* 294 */   public static final Item lava_bucket = getRegisteredItem("lava_bucket");
/* 295 */   public static final Item minecart = getRegisteredItem("minecart");
/* 296 */   public static final Item saddle = getRegisteredItem("saddle");
/* 297 */   public static final Item iron_door = getRegisteredItem("iron_door");
/* 298 */   public static final Item redstone = getRegisteredItem("redstone");
/* 299 */   public static final Item snowball = getRegisteredItem("snowball");
/* 300 */   public static final Item boat = getRegisteredItem("boat");
/* 301 */   public static final Item leather = getRegisteredItem("leather");
/* 302 */   public static final Item milk_bucket = getRegisteredItem("milk_bucket");
/* 303 */   public static final Item brick = getRegisteredItem("brick");
/* 304 */   public static final Item clay_ball = getRegisteredItem("clay_ball");
/* 305 */   public static final Item reeds = getRegisteredItem("reeds");
/* 306 */   public static final Item paper = getRegisteredItem("paper");
/* 307 */   public static final Item book = getRegisteredItem("book");
/* 308 */   public static final Item slime_ball = getRegisteredItem("slime_ball");
/* 309 */   public static final Item chest_minecart = getRegisteredItem("chest_minecart");
/* 310 */   public static final Item furnace_minecart = getRegisteredItem("furnace_minecart");
/* 311 */   public static final Item egg = getRegisteredItem("egg");
/* 312 */   public static final Item compass = getRegisteredItem("compass");
/* 313 */   public static final ItemFishingRod fishing_rod = (ItemFishingRod)getRegisteredItem("fishing_rod");
/* 314 */   public static final Item clock = getRegisteredItem("clock");
/* 315 */   public static final Item glowstone_dust = getRegisteredItem("glowstone_dust");
/* 316 */   public static final Item fish = getRegisteredItem("fish");
/* 317 */   public static final Item cooked_fish = getRegisteredItem("cooked_fish");
/* 318 */   public static final Item dye = getRegisteredItem("dye");
/* 319 */   public static final Item bone = getRegisteredItem("bone");
/* 320 */   public static final Item sugar = getRegisteredItem("sugar");
/* 321 */   public static final Item cake = getRegisteredItem("cake");
/* 322 */   public static final Item bed = getRegisteredItem("bed");
/* 323 */   public static final Item repeater = getRegisteredItem("repeater");
/* 324 */   public static final Item cookie = getRegisteredItem("cookie");
/* 325 */   public static final ItemMap filled_map = (ItemMap)getRegisteredItem("filled_map");
/* 326 */   public static final ItemShears shears = (ItemShears)getRegisteredItem("shears");
/* 327 */   public static final Item melon = getRegisteredItem("melon");
/* 328 */   public static final Item pumpkin_seeds = getRegisteredItem("pumpkin_seeds");
/* 329 */   public static final Item melon_seeds = getRegisteredItem("melon_seeds");
/* 330 */   public static final Item beef = getRegisteredItem("beef");
/* 331 */   public static final Item cooked_beef = getRegisteredItem("cooked_beef");
/* 332 */   public static final Item chicken = getRegisteredItem("chicken");
/* 333 */   public static final Item cooked_chicken = getRegisteredItem("cooked_chicken");
/* 334 */   public static final Item mutton = getRegisteredItem("mutton");
/* 335 */   public static final Item cooked_mutton = getRegisteredItem("cooked_mutton");
/* 336 */   public static final Item rabbit = getRegisteredItem("rabbit");
/* 337 */   public static final Item cooked_rabbit = getRegisteredItem("cooked_rabbit");
/* 338 */   public static final Item rabbit_stew = getRegisteredItem("rabbit_stew");
/* 339 */   public static final Item rabbit_foot = getRegisteredItem("rabbit_foot");
/* 340 */   public static final Item rabbit_hide = getRegisteredItem("rabbit_hide");
/* 341 */   public static final Item rotten_flesh = getRegisteredItem("rotten_flesh");
/* 342 */   public static final Item ender_pearl = getRegisteredItem("ender_pearl");
/* 343 */   public static final Item blaze_rod = getRegisteredItem("blaze_rod");
/* 344 */   public static final Item ghast_tear = getRegisteredItem("ghast_tear");
/* 345 */   public static final Item gold_nugget = getRegisteredItem("gold_nugget");
/* 346 */   public static final Item nether_wart = getRegisteredItem("nether_wart");
/* 347 */   public static final ItemPotion potionitem = (ItemPotion)getRegisteredItem("potion");
/* 348 */   public static final Item glass_bottle = getRegisteredItem("glass_bottle");
/* 349 */   public static final Item spider_eye = getRegisteredItem("spider_eye");
/* 350 */   public static final Item fermented_spider_eye = getRegisteredItem("fermented_spider_eye");
/* 351 */   public static final Item blaze_powder = getRegisteredItem("blaze_powder");
/* 352 */   public static final Item magma_cream = getRegisteredItem("magma_cream");
/* 353 */   public static final Item brewing_stand = getRegisteredItem("brewing_stand");
/* 354 */   public static final Item cauldron = getRegisteredItem("cauldron");
/* 355 */   public static final Item ender_eye = getRegisteredItem("ender_eye");
/* 356 */   public static final Item speckled_melon = getRegisteredItem("speckled_melon");
/* 357 */   public static final Item spawn_egg = getRegisteredItem("spawn_egg");
/* 358 */   public static final Item experience_bottle = getRegisteredItem("experience_bottle");
/* 359 */   public static final Item fire_charge = getRegisteredItem("fire_charge");
/* 360 */   public static final Item writable_book = getRegisteredItem("writable_book");
/* 361 */   public static final Item written_book = getRegisteredItem("written_book");
/* 362 */   public static final Item emerald = getRegisteredItem("emerald");
/* 363 */   public static final Item item_frame = getRegisteredItem("item_frame");
/* 364 */   public static final Item flower_pot = getRegisteredItem("flower_pot");
/* 365 */   public static final Item carrot = getRegisteredItem("carrot");
/* 366 */   public static final Item potato = getRegisteredItem("potato");
/* 367 */   public static final Item baked_potato = getRegisteredItem("baked_potato");
/* 368 */   public static final Item poisonous_potato = getRegisteredItem("poisonous_potato");
/* 369 */   public static final ItemEmptyMap map = (ItemEmptyMap)getRegisteredItem("map");
/* 370 */   public static final Item golden_carrot = getRegisteredItem("golden_carrot");
/* 371 */   public static final Item skull = getRegisteredItem("skull");
/* 372 */   public static final Item carrot_on_a_stick = getRegisteredItem("carrot_on_a_stick");
/* 373 */   public static final Item nether_star = getRegisteredItem("nether_star");
/* 374 */   public static final Item pumpkin_pie = getRegisteredItem("pumpkin_pie");
/* 375 */   public static final Item fireworks = getRegisteredItem("fireworks");
/* 376 */   public static final Item firework_charge = getRegisteredItem("firework_charge");
/* 377 */   public static final ItemEnchantedBook enchanted_book = (ItemEnchantedBook)getRegisteredItem("enchanted_book");
/* 378 */   public static final Item comparator = getRegisteredItem("comparator");
/* 379 */   public static final Item netherbrick = getRegisteredItem("netherbrick");
/* 380 */   public static final Item quartz = getRegisteredItem("quartz");
/* 381 */   public static final Item tnt_minecart = getRegisteredItem("tnt_minecart");
/* 382 */   public static final Item hopper_minecart = getRegisteredItem("hopper_minecart");
/* 383 */   public static final ItemArmorStand armor_stand = (ItemArmorStand)getRegisteredItem("armor_stand");
/* 384 */   public static final Item iron_horse_armor = getRegisteredItem("iron_horse_armor");
/* 385 */   public static final Item golden_horse_armor = getRegisteredItem("golden_horse_armor");
/* 386 */   public static final Item diamond_horse_armor = getRegisteredItem("diamond_horse_armor");
/* 387 */   public static final Item lead = getRegisteredItem("lead");
/* 388 */   public static final Item name_tag = getRegisteredItem("name_tag");
/* 389 */   public static final Item command_block_minecart = getRegisteredItem("command_block_minecart");
/* 390 */   public static final Item record_13 = getRegisteredItem("record_13");
/* 391 */   public static final Item record_cat = getRegisteredItem("record_cat");
/* 392 */   public static final Item record_blocks = getRegisteredItem("record_blocks");
/* 393 */   public static final Item record_chirp = getRegisteredItem("record_chirp");
/* 394 */   public static final Item record_far = getRegisteredItem("record_far");
/* 395 */   public static final Item record_mall = getRegisteredItem("record_mall");
/* 396 */   public static final Item record_mellohi = getRegisteredItem("record_mellohi");
/* 397 */   public static final Item record_stal = getRegisteredItem("record_stal");
/* 398 */   public static final Item record_strad = getRegisteredItem("record_strad");
/* 399 */   public static final Item record_ward = getRegisteredItem("record_ward");
/* 400 */   public static final Item record_11 = getRegisteredItem("record_11");
/* 401 */   public static final Item record_wait = getRegisteredItem("record_wait");
/* 402 */   public static final Item prismarine_shard = getRegisteredItem("prismarine_shard");
/* 403 */   public static final Item prismarine_crystals = getRegisteredItem("prismarine_crystals");
/* 404 */   public static final Item banner = getRegisteredItem("banner");
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\init\Items.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */