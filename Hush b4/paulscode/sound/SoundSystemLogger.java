// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

public class SoundSystemLogger
{
    public void message(final String message, final int indent) {
        String spacer = "";
        for (int x = 0; x < indent; ++x) {
            spacer += "    ";
        }
        final String messageText = spacer + message;
        System.out.println(messageText);
    }
    
    public void importantMessage(final String message, final int indent) {
        String spacer = "";
        for (int x = 0; x < indent; ++x) {
            spacer += "    ";
        }
        final String messageText = spacer + message;
        System.out.println(messageText);
    }
    
    public boolean errorCheck(final boolean error, final String classname, final String message, final int indent) {
        if (error) {
            this.errorMessage(classname, message, indent);
        }
        return error;
    }
    
    public void errorMessage(final String classname, final String message, final int indent) {
        String spacer = "";
        for (int x = 0; x < indent; ++x) {
            spacer += "    ";
        }
        final String headerLine = spacer + "Error in class '" + classname + "'";
        final String messageText = "    " + spacer + message;
        System.out.println(headerLine);
        System.out.println(messageText);
    }
    
    public void printStackTrace(final Exception e, final int indent) {
        this.printExceptionMessage(e, indent);
        this.importantMessage("STACK TRACE:", indent);
        if (e == null) {
            return;
        }
        final StackTraceElement[] stack = e.getStackTrace();
        if (stack == null) {
            return;
        }
        for (int x = 0; x < stack.length; ++x) {
            final StackTraceElement line = stack[x];
            if (line != null) {
                this.message(line.toString(), indent + 1);
            }
        }
    }
    
    public void printExceptionMessage(final Exception e, final int indent) {
        this.importantMessage("ERROR MESSAGE:", indent);
        if (e.getMessage() == null) {
            this.message("(none)", indent + 1);
        }
        else {
            this.message(e.getMessage(), indent + 1);
        }
    }
}
