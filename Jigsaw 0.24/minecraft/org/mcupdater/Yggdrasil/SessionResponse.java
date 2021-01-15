/*    */ package org.mcupdater.Yggdrasil;

/*    */
/*    */
/*    */
/*    */ public class SessionResponse/*    */ extends ErrorResponse
/*    */ {
	/*    */ private String accessToken;
	/*    */
	/*    */
	/*    */ private String clientToken;
	/*    */
	/*    */ private Profile[] availableProfiles;
	/*    */
	/*    */ private Profile selectedProfile;

	/*    */
	/*    */
	/*    */ public String getAccessToken()
	/*    */ {
		/* 20 */ return this.accessToken;
		/*    */ }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public String getClientToken()
	/*    */ {
		/* 29 */ return this.clientToken;
		/*    */ }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public Profile[] getAvailableProfiles()
	/*    */ {
		/* 38 */ return this.availableProfiles;
		/*    */ }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public Profile getSelectedProfile()
	/*    */ {
		/* 47 */ return this.selectedProfile;
		/*    */ }

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public String getSessionId()
	/*    */ {
		/* 56 */ return "token:" + this.accessToken + ":" + this.selectedProfile.getId();
		/*    */ }
	/*    */ }