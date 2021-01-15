package awfdd.wefsd.gsdbhfb;

import java.util.ArrayList;
import java.util.List;

public class Zabehjfb {
	private String data;
	private List<Zabehjfb> children = new ArrayList<Zabehjfb>();
	
	/**
	 * Creates a new node
	 * @param data
	 */
	public Zabehjfb(String data) {
		this.data = data;
	}
	
	/**
	 * Adds a child node to this node
	 * @param node
	 * @return
	 */
	public Zabehjfb addChild(Zabehjfb node) {
		this.children.add(node);
		return this;
	}
	
	/**
	 * Returns all child nodes of this node
	 * @return
	 */
	public List<Zabehjfb> getChildren() {
		return this.children;
	}
	
	/**
	 * Returns the data of this node
	 * @return
	 */
	public String getData() {
		return this.data;
	}
	
	/**
	 * Clears this node from child nodes
	 */
	public void clear() {
		this.children.clear();
	}
	
	private String printStr = "";
	@Override
	public String toString() {
		this.printStr = "";
		this.getStrRec(this, "", true);
		return this.printStr;
	}
	private void getStrRec(Zabehjfb prntNode, String prefix, boolean isTail) {
		prntNode.printStr = prntNode.printStr + (prefix + (isTail ? "'-- " : "|-- ") + this.data) + "\n";
        for (int i = 0; i < this.children.size() - 1; i++) {
        	this.children.get(i).getStrRec(prntNode, prefix + (isTail ? "    " : "|   "), false);
        }
        if (this.children.size() > 0) {
        	this.children.get(this.children.size() - 1).getStrRec(prntNode, prefix + (isTail ?"    " : "|   "), true);
        }
    }
}
