package photo65.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * class object for photos
 * @author mitchlew
 *
 */
public class PhotoData implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * String that holds caption of photo
	 */
	public String caption;
	/**
	 * URI that holds filepath
	 */
	public URI location;
	/**
	 * hashmap for tags
	 */
	public HashMap<String, String> tags;
    /**
     * photodata constructor
     * @param loc
     */
	public PhotoData(URI loc) {
		caption = "";
		location = loc;
		tags = new HashMap<String, String>();
	}
	/**
	 * additional photodata constructor
	 * @param orig
	 */
    public PhotoData(PhotoData orig)
	{
		this(orig.location);
		this.caption = orig.caption;
		for(String key : orig.tags.keySet())
		{
			this.tags.put(key, orig.tags.get(key));
		}
	}
   
    /**
     * method that gets time of photo 
     * @return
     */
    public Calendar getDate(){
		try{
			File newfile = new File(location);			
			Calendar calender = Calendar.getInstance();
			calender.setTimeInMillis(newfile.lastModified());
			calender.set(Calendar.MILLISECOND,0);
			return calender;
		}
		catch(Exception exception){
			Calendar calender = Calendar.getInstance();
			return calender;
		}
	}
    /**
     * method that gets last time of photo
     * @return
     */
    public String getLastModifiedDate(){
		try{
			File newfile = new File(location);		
			Calendar calender = Calendar.getInstance();
			calender.setTimeInMillis(newfile.lastModified());
			calender.set(Calendar.MILLISECOND,0);
			return calender.getTime().toString();
		}
		catch(Exception exception){
			Calendar calender = Calendar.getInstance();
			return calender.getTime().toString();
		}
	}
   
    /**
     * method that gets specific dimension of photo to fit in tilepane
     * @return
     */
    public ImageView getFittedImageView() {
    	Image fittedimage;
    	try {
    		fittedimage = new Image(new FileInputStream(new File(location)), 150, 0, true, true);
    	}catch(Exception e) {
    		System.out.println(e);
    		return null;
    	}
    	ImageView imageView = new ImageView(fittedimage);
    	imageView.setFitWidth(200);
    	return imageView;
    } 
    /**
     * turns photo into imagview to be displayed
     * @return
     */
    public ImageView getImageView(){
		Image fittedimage;
		try{
			fittedimage = new Image(new FileInputStream(new File(location)));
		}
		catch(Exception e){
			return null;
		}
		ImageView imageView = new ImageView(fittedimage);
		return imageView;
	}
    
    /**
     * gets image oh photo
     * @return
     */
    public Image getImage(){
		Image getimage = null;
		try{
			getimage = new Image(new FileInputStream(new File(location)));
		}
		catch(Exception e){
			return null;
		}
		return getimage;
	}


}
