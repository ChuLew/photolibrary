package photo65.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * User Object Class 
 * @author Mitch Lew
 *
 */
public class Users implements Serializable, Comparable<Users>{ 

	private static final long serialVersionUID =  3897026392760534999L;
	public String username; 
	public ArrayList<Album> albums;  
	public Users(String name){
		this.username= name; 
		this.albums = new ArrayList<Album>();
	}
	public void addAlbum(Album album){
		albums.add(album);
	}
	public String toString(){
		return username; 
	}
	public int compareTo(Users user){
		return this.username.compareTo(user.username); 
	} 
	public static class Comparators{
		public static Comparator<Users> NAME= new Comparator<Users>(){
			@Override
			public int compare(Users a, Users b){
				return a.username.compareTo(b.username); 
			}
		}; 
	}
}
