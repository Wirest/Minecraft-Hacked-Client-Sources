/*    */ package org.mcupdater.Yggdrasil;

/*    */
/*    */
/*    */ public class RefreshRequest
/*    */ {
	/*    */ private String accessToken;
	/*    */
	/*    */ private String clientToken;

	/*    */
	/*    */
	/*    */ public RefreshRequest(String access, String client)
	/*    */ {
		/* 13 */ this.accessToken = access;
		/* 14 */ this.clientToken = client;
		/*    */ }

	/*    */
	/*    */ public String getAccessToken() {
		/* 18 */ return this.accessToken;
		/*    */ }

	/*    */
	/*    */ public String getClientToken() {
		/* 22 */ return this.clientToken;
		/*    */ }
	/*    */ }