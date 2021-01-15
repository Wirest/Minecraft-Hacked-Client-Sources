/*    */ package org.mcupdater.Yggdrasil;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ public class ErrorResponse
/*    */ {
	/* 9 */ private String error = "";
	/* 10 */ private String errorMessage = "";
	/* 11 */ private String cause = "";

	/*    */
	/*    */ public String getError() {
		/* 14 */ return this.error;
		/*    */ }

	/*    */
	/*    */ public String getErrorMessage() {
		/* 18 */ return this.errorMessage;
		/*    */ }

	/*    */
	/*    */ public String getCause() {
		/* 22 */ return this.cause;
		/*    */ }
	/*    */ }