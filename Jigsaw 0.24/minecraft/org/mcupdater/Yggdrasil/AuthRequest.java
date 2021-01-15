/*    */ package org.mcupdater.Yggdrasil;

/*    */
/*    */
/*    */
/*    */
/*    */ public class AuthRequest
/*    */ {
	/*    */ private Agent agent;
	/*    */
	/*    */
	/*    */
	/*    */ private String username;
	/*    */
	/*    */
	/*    */
	/*    */ private String password;
	/*    */
	/*    */
	/*    */ private String clientToken;

	/*    */
	/*    */
	/*    */
	/*    */ public AuthRequest(Agent agent, String username, String password, String clientToken)
	/*    */ {
		/* 25 */ this.agent = agent;
		/* 26 */ this.username = username;
		/* 27 */ this.password = password;
		/* 28 */ this.clientToken = clientToken;
		/*    */ }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public String getUsername()
	/*    */ {
		/* 36 */ return this.username;
		/*    */ }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public String getClientToken()
	/*    */ {
		/* 44 */ return this.clientToken;
		/*    */ }
	/*    */ }