package photo65.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
/**
 * Album Object Class 
 * @author Mitch Lew
 *
 */
public class Album implements Serializable, Comparable<Album>{
	private static final long serialVersionUID = -1503934156878954986L;
	/**
	 * name of album
	 */
	public String albumName; 
	/**
	 * list of photos
	 */
	public ArrayList<PhotoData> photos; 
	/**
	 * earliest photo date
	 */
	Calendar min; 
	/**
	 * most recent photo date
	 */
	Calendar max; 
	/**
	 * constructor that initializes photo list
	 * @param name
	 */
	public Album(String name){
		albumName= name; 
		photos = new ArrayList<PhotoData>();
	}
	/**
	 * album constructor with a list of photos inputed
	 * @param n
	 * @param col
	 */
	public Album(String name, Collection<PhotoData> collection) {
		this(name);
		for(PhotoData photoloop : collection) {
			photos.add(new PhotoData(photoloop));
		}
	}
	/**
	 * toString method returns a string with date 
	 */
	public String toString(){
		int size; 
		Calendar min= null; 
		Calendar max= null; 
		String more;
		if(photos==null || photos.size()==0){
			size= 0;
			more=""; 
		}
		else{
			size= photos.size(); 
			for(int i=0; i<photos.size(); i++){
				if(min==null){
					min= photos.get(i).getDate(); 
				}
				if(max==null){
					max= photos.get(i).getDate(); 
				}
				if(photos.get(i).getDate().before(min)){
					min= photos.get(i).getDate(); 
				}
				if(photos.get(i).getDate().after(max)){
					max= photos.get(i).getDate();
				}
			}
			more= "\nFrom: " + min.getTime() + " to " + max.getTime(); 
		}
		String representation= albumName + "\n" + "Photos: " + size + more; 
		return representation; 
	}
/**
 * adds a new album to list
 * @param name
 */
	public void setName(String name){
		albumName= name; 
	}
	/**
	 * compare albums names
	 * @param album
	 * @return
	 */
	public int compareTo(Album album){
		return this.albumName.compareTo(album.albumName); 
	} 
	/**
	 * compare albums using comparator class
	 * @author mitchlew
	 *
	 */
	public static class Comparators{
		public static Comparator<Album> NAME= new Comparator<Album>(){
			@Override
			public int compare(Album a, Album b){
				return a.albumName.compareTo(b.albumName); 
			}
		}; 
	}
}
