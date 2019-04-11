package photo65.view;

import java.io.Serializable;

import java.util.Comparator;


/**
 * User Object Class 
 * @author Mitch Lew
 *
 */
public class Users implements Serializable, Comparable<Users>{ 
	
	String username; 

	public Users(String username){
		this.username= username; 
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
			public int compare(Users first, Users second){
				return first.username.compareTo(second.username); 
			}
		}; 
	}

}
