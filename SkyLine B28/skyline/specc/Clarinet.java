package skyline.specc;

import java.util.ArrayList;
import java.util.List;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.Value;
import skyline.specc.extras.chat.ChatColor;

public class Clarinet {

	private List<Value> vals = new ArrayList<Value>();

	private String name;
	private int build;
	private String[] authors;
	private ClientData data;

	public Clarinet(String name, int build, String[] authors, ClientData data) {
		this.name = name;
		this.build = build;
		this.authors = authors;
		this.data = data;
	}


	public void addValue(Value value){
		this.vals.add(value);
	}

	public List<Value> getVals() {
		return vals;
	}


	public void setVals(List<Value> vals) {
		this.vals = vals;
	}

	public String getName() {
		return name;
	}

	public int getBuild() {
		return build;
	}

	public String[] getAuthors() {
		return authors;
	}

	public void start(){

	}

	public ClientData getData() {
		return data;
	}

	public void setData(ClientData data) {
		this.data = data;
	}

	public static class ClientData {

		private ChatColor displayColor;

		public ClientData(ChatColor displayColor){
			this.displayColor = displayColor;
		}

		public ChatColor getDisplayColor(){
			return this.displayColor;
		}

	}

}
