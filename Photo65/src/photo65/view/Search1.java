package photo65.view;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;


public class Search1 {
	/**
	 * datepicker for begining date for search
	 */
	@FXML 
	DatePicker begDate;
	/**
	 * date picker for end date for search
	 */
	@FXML 
	DatePicker endingDate;
	/**
	 * logout button
	 */
	@FXML 
	Button logoutBtn;
	/**
	 * back button
	 */
	@FXML 
	Button backBtn;
	/**
	 *button for searching with tags 
	 */
	@FXML
	Button searchTagBtn;
	/**
	 * button for searching with date
	 */
	@FXML 
	Button searchDateBtn;
	/**
	 * button for switching between and or or
	 */
	@FXML 
	Button logicBtn;
	/**
	 * button for picking choice of first tag
	 */
	@FXML 
	ChoiceBox<Object> firstTag;
	/**
	 * button for picking choice for second tag
	 */
	@FXML
	ChoiceBox<Object> secondTag;
	/**
	 * rootPane
	 */
	@FXML 
	AnchorPane rootPane;
	@FXML 
	ScrollPane scrollPane;
	@FXML 
	TilePane tilePane;
	/**
	 * textfield for entering tag value
	 */
	@FXML 
	TextField firstTagField;
	/**
	 * 
	 *tagfield for entering second tag value
	 */
	@FXML 
	TextField secondTagField;
	/**
	 * observableList that holds albums
	 */
	public static ObservableList<Album> useralbums= FXCollections.observableArrayList(); 
	/**
	 * variable that tracks user which is set from other class
	 */
	private Users User;
	/**
	 * string that holds selected item
	 */
	public String itemSelect;
	private Stage mainStage;
	/**
	 * arraylist of search results
	 */
	private ArrayList<PhotoData> result;
    private boolean logic;
    /**
     * set that holds tags
     */
	private Set<String> tags;
	/**
	 * initializing set
	 * @param mainStage
	 */
	public void start(Stage mainStage){
		this.mainStage = mainStage;
		User = SceneController.currentUser;
		tags = new HashSet<>();
		for(Album albumLoop: User.albums) {
			for(PhotoData phto: albumLoop.photos) {
				for(String tent: phto.tags.keySet()) {
					tags.add(tent);
				}
			}	
		}
		firstTag.setItems(FXCollections.observableArrayList(tags.toArray()));
		secondTag.setItems(FXCollections.observableArrayList(tags.toArray()));
		logic = true; 
	}
	/**
	 * switches logic to and or or and text for button
	 * @param e
	 */
	@FXML
    void swapLogic(ActionEvent e) {
    	logic = !logic;
    	if(logic) {
    		logicBtn.setText("&");
    	}else {
    		logicBtn.setText("+");
    	}
    }
	/**
	 * logout method
	 * @param e
	 * @throws IOException
	 */
    @FXML
    void logout(ActionEvent e) throws IOException
	{
    	SceneController.viewLogin();
	}
    /**
     * go back to album directory method
     * @param e
     * @throws IOException
     */
    @FXML
    void goback(ActionEvent e) throws IOException
    {
    	SceneController.viewAlbumDirectory();
    }
    /**
     * method that searches for photos dates
     * @param e
     * @throws IOException
     */
    @FXML
    void searchDate(ActionEvent e) throws IOException {
   	if(begDate.getValue() == null || endingDate.getValue() == null) {
    		Toast.makeText(mainStage, "Must fill out both dates", 500, 500, 50);
    		return;
    	}
    	Calendar sd = GregorianCalendar.from(begDate.getValue().atStartOfDay(ZoneId.systemDefault()));
    	Calendar ed = GregorianCalendar.from(endingDate.getValue().atStartOfDay(ZoneId.systemDefault()));	
    	if(ed.compareTo(sd) < 0 ) {
    		Toast.makeText(mainStage, "Start that can not be after end date!", 500, 500, 50);
    		return;
    	}
    	result = new ArrayList<>();
    	tilePane.getChildren().clear();
    	for(Album albumloop: User.albums) {
    		for(PhotoData photoloop: albumloop.photos) {
    			Calendar cal = photoloop.getDate();
    			if(cal.compareTo(sd) >= 0 && cal.compareTo(ed) <= 0) {
    				result.add(photoloop);
    				tilePane.getChildren().add(photoloop.getFittedImageView());
    			}
    		}
    	}
    	Toast.makeText(mainStage, "Search complete", 500, 500, 50);
    }
    /**
     * method that searches for photos by tag
     * @param e
     */
    @FXML
    void searchTag(ActionEvent e) {
    	if(firstTagField.getText().isEmpty() || firstTag.getValue() == null) {
    		Toast.makeText(mainStage, "Must fill out first tag to search", 500, 500, 50);
    		return;
    	}   
    	String valueFirst = firstTagField.getText();
     	String tagFirst = (String)firstTag.getValue();
    	Predicate<PhotoData> logic1 = l -> {
    		if(!l.tags.containsKey(tagFirst)) 
    			return false;
    		return l.tags.get(tagFirst).equals(valueFirst);
    	};
    	Predicate<PhotoData> logic2;
    	String tagSecond,valueSecond;
    	if(!(secondTagField.getText().isEmpty() || secondTag.getValue() == null)) {
    		valueSecond = secondTagField.getText();
    		tagSecond = (String)secondTag.getValue();
    		logic2 = l -> {
        		if(!l.tags.containsKey(tagSecond)) 
        			return false;
        		return l.tags.get(tagSecond).equals(valueSecond);
        	};
        	if(logic) {
        		logic1 = logic1.and(logic2);
        	}else {
        		logic1 = logic1.or(logic2);
        	}
    	}
    	tilePane.getChildren().clear();
    	result = new ArrayList<>();
    	for(Album albumloop: User.albums) {
			for(PhotoData photoloop: albumloop.photos) {
				if(logic1.test(photoloop)) {
					result.add(photoloop);
					tilePane.getChildren().add(photoloop.getFittedImageView());
				}
			}	
		}
    	Toast.makeText(mainStage, "Tag Search Complete", 500, 500, 50);
    }   
    @SuppressWarnings("unlikely-arg-type")
	@FXML 
    void createAlbum(ActionEvent e) {
    	if(result == null || result.isEmpty()) {
			Toast.makeText(mainStage, "No results", 500, 500, 500);
			return;
		}
    	TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(mainStage); dialog.setTitle("Create Album");
		dialog.setHeaderText("Enter album name");
		dialog.setContentText("Enter name: ");
		Optional<String> res = dialog.showAndWait();
		
		if(res.get().isEmpty()) {
			Toast.makeText(mainStage, "Album name cannot be empty", 500, 500, 50);
			return;
		}
		if (res.isPresent())
		{
			if(User.albums.contains(res.get()))
			{
				Toast.makeText(mainStage, "Album already exists", 500, 500, 50);
				return;
			}
			else
			{
				Album a = new Album(res.get());
				for(PhotoData p: result) {
					a.photos.add(p);
				}
				SceneController.currentUser.albums.add(a);
				Collections.sort(AlbumDirectory.useralbums,Album.Comparators.NAME);
				AlbumDirectory.useralbums.add(a);
				Collections.sort(AlbumDirectory.useralbums,Album.Comparators.NAME);
				try {
					ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(Administrator.file));
					os.writeObject(new ArrayList<Users>(Administrator.observe_list)); 
					os.close();
				}catch (FileNotFoundException event){
					event.printStackTrace(); 
				}catch (IOException event) {
					event.printStackTrace();
				} 
				Toast.makeText(mainStage, "Album " + res.get() + " created", 500, 500, 50);
			}
			
		}
    }
    

}
