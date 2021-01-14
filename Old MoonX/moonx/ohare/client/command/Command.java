package moonx.ohare.client.command;


public class Command {
    private String label;
    private String[] handles;

    public Command(String label, String[] handles) {
        this.label = label;
        this.handles = handles;
    }

    public String[] getHandles() {
        return handles;
    }

    public void onRun(final String[] s) {
    }

    public String getLabel() {
        return this.label;
    }
}
