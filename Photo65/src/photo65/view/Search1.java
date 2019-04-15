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
	@FXML ChoiceBox<Object> tagChoice1;
	@FXML ChoiceBox<Object> tagChoice2;
	@FXML AnchorPane rootPane;
	@FXML ScrollPane scrollPane;
	@FXML TilePane tilePane;
	@FXML TextField inputTag1;
	@FXML TextField inputTag2;
	@FXML DatePicker startDate;
	@FXML DatePicker endDate;
	@FXML Button logoutBtn;
	@FXML Button backBtn;
	@FXML Button tagsearchBtn;
	@FXML Button datesearchBtn;
	@FXML Button logicBtn;
	
	public String selectedItem;
	private Set<String> tags;
	private Stage mainStage;
	private boolean logic;
	public static ObservableList<Album> useralbums= FXCollections.observableArrayList(); 
	private Users User;
	private ArrayList<PhotoData> result;
    public void start(Stage mainStage)
	{
		this.mainStage = mainStage;
		User = SceneController.currentUser;
		tags = new HashSet<>();
		for(Album a: User.albums) {
			for(PhotoData p: a.photos) {
				for(String t: p.tags.keySet()) {
					tags.add(t);
				}
			}	
		}
		tagChoice1.setItems(FXCollections.observableArrayList(tags.toArray()));
		tagChoice2.setItems(FXCollections.observableArrayList(tags.toArray()));
		logic = true; 
	}
    @FXML
    void logout(ActionEvent e) throws IOException
	{
    	SceneController.viewLogin();
	}
    @FXML
    void goback(ActionEvent e) throws IOException
    {
    	SceneController.viewAlbumDirectory();
    }
    @FXML
    void searchDate(ActionEvent e) throws IOException
    {
   	if(startDate.getValue() == null || endDate.getValue() == null) {
    		Toast.makeText(mainStage, "Cannot leave dates empty", 500, 500, 500);
    		return;
    	}
    	Calendar sd = GregorianCalendar.from(startDate.getValue().atStartOfDay(ZoneId.systemDefault()));
    	Calendar ed = GregorianCalendar.from(endDate.getValue().atStartOfDay(ZoneId.systemDefault()));	
    	if(ed.compareTo(sd) < 0 ) {
    		Toast.makeText(mainStage, "Invalid time inputs", 500, 500, 500);
    		return;
    	}
    	result = new ArrayList<>();
    	tilePane.getChildren().clear();
    	System.out.println(sd.getTime().toString() + ed.getTime().toString());
    	for(Album a: User.albums) {
    		for(PhotoData p: a.photos) {
    			Calendar c = p.getDate();
    			System.out.println(c.getTime());
    			if(c.compareTo(sd) >= 0 && c.compareTo(ed) <= 0) {
    				result.add(p);
    				tilePane.getChildren().add(p.getFittedImageView());
    			}
    		}
    	}
    }
    @FXML
    void searchTag(ActionEvent e) {
    	if(inputTag1.getText().isEmpty() || tagChoice1.getValue() == null) {
    		Toast.makeText(mainStage, "Tag 1 must be set", 500, 500, 500);
    		return;
    	}   
    	String tag1 = (String)tagChoice1.getValue();
    	String val1 = inputTag1.getText();
     	Predicate<PhotoData> logic1 = l -> {
    		if(!l.tags.containsKey(tag1)) 
    			return false;
    		return l.tags.get(tag1).equals(val1);
    	};
    	Predicate<PhotoData> logic2;
    	String tag2;
    	String val2;
    	if(!(inputTag2.getText().isEmpty() || tagChoice2.getValue() == null)) {
    		tag2 = (String)tagChoice2.getValue();
    		val2 = inputTag2.getText();
    		logic2 = l -> {
        		if(!l.tags.containsKey(tag2)) 
        			return false;
        		return l.tags.get(tag2).equals(val2);
        	};
        	if(logic) {
        		logic1 = logic1.and(logic2);
        	}else {
        		logic1 = logic1.or(logic2);
        	}
    	}
    	tilePane.getChildren().clear();
    	result = new ArrayList<>();
    	for(Album a: User.albums) {
			for(PhotoData p: a.photos) {
				System.out.println(logic1.test(p));
				if(logic1.test(p)) {
					result.add(p);
					tilePane.getChildren().add(p.getFittedImageView());
				}
			}	
		}
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
    @FXML
    void swapLogic(ActionEvent e) {
    	logic = !logic;
    	if(logic) {
    		logicBtn.setText("&");
    	}else {
    		logicBtn.setText("+");
    	}
    }

}
