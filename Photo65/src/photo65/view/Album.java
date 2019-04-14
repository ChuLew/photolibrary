package photo65.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
	public String albumName; 
	public ArrayList<PhotoData> photos; 
	Calendar min; 
	Calendar max; 
	public Album(String name){
		albumName= name; 
		photos = new ArrayList<PhotoData>();
	}
	public Album(String n, Collection<PhotoData> col) {
		this(n);
		for(PhotoData p : col) {
			photos.add(new PhotoData(p));
		}
	}
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
			//SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
			more= "\nFrom: " + min.getTime() + " to " + max.getTime(); 
		}
		String representation= albumName + "\n" + "Photos: " + size + more; 
		return representation; 
		//return albumName; 
	}

	public void setName(String name){
		albumName= name; 
	}

	public int compareTo(Album album){
		return this.albumName.compareTo(album.albumName); 
	} 
	public static class Comparators{
		public static Comparator<Album> NAME= new Comparator<Album>(){
			@Override
			public int compare(Album a, Album b){
				return a.albumName.compareTo(b.albumName); 
			}
		}; 
	}
}
