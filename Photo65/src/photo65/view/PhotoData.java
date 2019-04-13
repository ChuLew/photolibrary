package photo65.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PhotoData implements Serializable{
	private static final long serialVersionUID = 1L;
	public URI location;
	public String caption;
	public HashMap<String, String> tags;
    public PhotoData(URI loc) {
		location = loc;
		caption = "";
		tags = new HashMap<String, String>();
	}
    public PhotoData(PhotoData orig)
	{
		this(orig.location);
		this.caption = orig.caption;
		for(String key : orig.tags.keySet())
		{
			this.tags.put(key, orig.tags.get(key));
		}
	}
    public String getLastModifiedDate(){
		try{
			File f = new File(location);		
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(f.lastModified());
			c.set(Calendar.MILLISECOND,0);
			return c.getTime().toString();
		}
		catch(Exception e){
			Calendar c = Calendar.getInstance();
			return c.getTime().toString();
		}
	}
    public Calendar getDate(){
		try{
			File f = new File(location);			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(f.lastModified());
			c.set(Calendar.MILLISECOND,0);
			return c;
		}
		catch(Exception e){
			Calendar c = Calendar.getInstance();
			return c;
		}
	}
    public Image getImage(){
		Image image = null;
		try{
			image = new Image(new FileInputStream(new File(location)));
		}
		catch(Exception e){
			return null;
		}
		return image;
	}
    
    public ImageView getImageView(){
		Image image;
		try{
			image = new Image(new FileInputStream(new File(location)));
		}
		catch(Exception e){
			return null;
		}
		ImageView iView = new ImageView(image);
		return iView;
	}
    
    public ImageView getFittedImageView() {
    	Image image;
    	try {
    		image = new Image(new FileInputStream(new File(location)), 150, 0, true, true);
    	}catch(Exception e) {
    		return null;
    	}
    	ImageView iView = new ImageView(image);
    	iView.setFitWidth(200);
    	return iView;
    }


}
