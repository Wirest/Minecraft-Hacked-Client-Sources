/*    */ package org.mcupdater.Yggdrasil;

/*    */
/*    */
/*    */
/*    */
/*    */ public class Agent
/*    */ {
	/*    */ private final String name;
	/*    */
	/*    */
	/*    */
	/*    */ private final int version;

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public Agent(String name, int version)
	/*    */ {
		/* 19 */ this.name = name;
		/* 20 */ this.version = version;
		/*    */ }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public String getName()
	/*    */ {
		/* 28 */ return this.name;
		/*    */ }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public int getVersion()
	/*    */ {
		/* 36 */ return this.version;
		/*    */ }
	/*    */ }