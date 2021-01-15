package nivia.managers;

import net.minecraft.util.StringUtils;

import java.util.ArrayList;

public class FriendManager {
	
public static ArrayList<Friend> friends = new ArrayList<Friend>();
	
	public static void addFriend(String name, String alias){
		Friend meme = new Friend(name, alias);
		if(!friends.contains(meme))
		friends.add(meme);
	}
	public static void addFriend(Friend f){	
		if(!friends.contains(f))
		friends.add(f);
	}
	
	public static void deleteFriend(String name){
		for(Friend friend: friends){
			if(friend.getName().equalsIgnoreCase(name) || friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name))){
				friends.remove(friend);		
		break;
			}
		}
	}
    public static Friend getFriendByName(String name){
        for(Friend frend: friends){
            if(frend.getName().equalsIgnoreCase(name))
                return frend;
            else return null;
        }
        return null;
    }
	public static Friend getFriendByAlias(String alias){
		for(Friend frend: friends){
			if(frend.getAlias().equalsIgnoreCase(alias)){
                return frend;
			} else return null;
		}
		return null;
	}
	public static boolean isFriend(String name){
		boolean isFriend = false;
		for(Friend friend: friends){
			if(friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name)) || friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name))){
				isFriend = true;
				break;
			}
		}
		return isFriend;
	}	
			
	/**
	 * Friend Class
	 */
	public static class Friend {
		private String name;
		private String alias;
		
		public Friend(String name, String alias) {
			this.name = name;
			this.alias = alias;
		}
		
		public String getName() {
			return name;
		}
		
		public String getAlias() {
			return alias;
		}
		
		public void setName(String s) {
			name = s;
		}
		
		public void setAlias(String s) {
			alias = s;
		}
	}
	
}
