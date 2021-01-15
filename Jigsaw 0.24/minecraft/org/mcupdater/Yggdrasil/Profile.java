/*    */ package org.mcupdater.Yggdrasil;

/*    */
/*    */
/*    */ public class Profile
/*    */ {
	/*    */ private String id;
	/*    */
	/*    */ private String name;

	/*    */
	/*    */
	/*    */ public Profile(String id, String name)
	/*    */ {
		/* 13 */ this.id = id;
		/* 14 */ this.name = name;
		/*    */ }

	/*    */
	/*    */ public String getId() {
		/* 18 */ return this.id;
		/*    */ }

	/*    */
	/*    */ public String getName() {
		/* 22 */ return this.name;
		/*    */ }
	/*    */ }