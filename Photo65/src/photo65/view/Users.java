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
	/**
	 * constructor
	 * @param name
	 */
	public Users(String name){
		this.username= name; 
		this.albums = new ArrayList<Album>();
	}
	/**
	 * method for adding to arraaylist of albums
	 * @param album
	 */
	public void addAlbum(Album album){
		albums.add(album);
	}
	/**
	 * method that returns username
	 */
	public String toString(){
		return username; 
	}
	/**
	 * compare method
	 */
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
