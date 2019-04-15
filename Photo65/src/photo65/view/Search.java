package photo65.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;




public class Search {
	@FXML private ScrollPane scrollpane;
	@FXML private Button searchBtn;
	@FXML private Button returnBtn;
	@FXML private Button createAlbumBtn;
	@FXML private Button logoutBtn;
	@FXML private TextField tagName;
	@FXML private TextField tagValue;
	@FXML private RadioButton byDate;
	@FXML private RadioButton byTag;
	@FXML private DatePicker start;
	@FXML private DatePicker end;
	@FXML private RadioButton orClick;
	@FXML private RadioButton andClick;
	@FXML private Button addTag;
	@FXML private Button clearTag;
	@FXML private ListView<String> tagNames;
	@FXML private ListView<String> tagValues;
	@FXML private TilePane tilepane;
	@FXML private AnchorPane rootPane;
	ObservableList<String> tagNamesList= FXCollections.observableArrayList();
	ObservableList<String> tagValuesList= FXCollections.observableArrayList();
	boolean disableLogic = false;
	boolean add= false; 
	public static ArrayList<PhotoData> searchphotos = new ArrayList();
	public int count; 
	public boolean first;
	boolean original = true;
	private ArrayList<PhotoData> result;
	private Stage mainStage;
	private Users User;
	
	public void start(Stage primaryStage) {
		
		this.mainStage = primaryStage;
		User = SceneController.currentUser;
		final ToggleGroup group = new ToggleGroup();
		byDate.setToggleGroup(group);
		byTag.setToggleGroup(group);
		final ToggleGroup groupLogic = new ToggleGroup();
		orClick.setToggleGroup(groupLogic);
		andClick.setToggleGroup(groupLogic);
		searchBtn.setDisable(true);
		
		
	}
	public void onLogout() throws IOException{
		SceneController.viewLogin();
	}
	public void onReturn() throws IOException{
		SceneController.viewAlbumDirectory();
	}
	@FXML
	public void addTag(ActionEvent event) throws IOException{
	
		if (checkTags(tagName,tagValue)) {
			disableLogic=true;
			orClick.setDisable(disableLogic);
			andClick.setDisable(disableLogic);
			searchBtn.setDisable(false);
			tagNamesList.add(tagName.getText());
			tagValuesList.add(tagValue.getText());
		}
		
		tagNames.setItems(tagNamesList);
		tagValues.setItems(tagValuesList);
		tagName.setText("");
		tagValue.setText("");
	}
	@FXML
	public void changeLogic(ActionEvent event) throws IOException{
		if(byDate.selectedProperty().getValue()) {
			start.setVisible(true);
			end.setVisible(true);
			tagName.setVisible(false);
			tagValue.setVisible(false);
			tagNames.setVisible(false);
			tagValues.setVisible(false);
		}
		else {
			start.setVisible(false);
			end.setVisible(false);
			tagName.setVisible(true);
			tagValue.setVisible(true);
			tagNames.setVisible(true);
			tagValues.setVisible(true);
		}
	}
	public boolean checkTags(TextField tagname,TextField tagvalue) {
		if(tagname.getText().compareTo("")!=0 && tagvalue.getText().compareTo("")!=0) {
			return true;
		}else if(tagname.getText().compareTo("")!=0 && tagvalue.getText().compareTo("")==0) {
			return false;
		}else if(tagname.getText().compareTo("")==0 && tagvalue.getText().compareTo("")!=0) {
			return false;
		}else {
			return false;
		}
	}

	public void postResults(ObservableList<String> searchResult) {
		if (searchResult.size()==0) {
			Alert alert = new Alert(AlertType.WARNING, "Please try a different search. No results found!", ButtonType.OK);
			alert.showAndWait();
			return;
			
		}else {
			//nothing
		}
	}
	@FXML
	public void clearTag(ActionEvent event) throws IOException{
		tagNamesList.clear();
		tagValuesList.clear();
		disableLogic=false;
		orClick.setDisable(disableLogic);
		andClick.setDisable(disableLogic);
		tagNames.setItems(tagNamesList);
		tagValues.setItems(tagValuesList);
		searchBtn.setDisable(true);
	}
	@FXML
	void searchDate(ActionEvent e) throws IOException
    {
    	//System.out.println(startDate.getValue());
    	
    	if(start.getValue() == null || end.getValue() == null) {
    		Toast.makeText(mainStage, "Cannot leave dates empty", 500, 500, 500);
    		return;
    	}
    	
    	Calendar sd = GregorianCalendar.from(start.getValue().atStartOfDay(ZoneId.systemDefault()));
    	Calendar ed = GregorianCalendar.from(end.getValue().atStartOfDay(ZoneId.systemDefault()));	
    	if(ed.compareTo(sd) < 0 ) {
    		Toast.makeText(mainStage, "Invalid time inputs", 500, 500, 50);
    		return;
    	}
    	result = new ArrayList<>();
		tilepane.getChildren().clear();
    	System.out.println(sd.getTime().toString() + ed.getTime().toString());
    	for(Album a: User.albums){
    		for(PhotoData p: a.photos) {
    			Calendar c = p.getDate();
    			System.out.println(c.getTime());
    			if(c.compareTo(sd) >= 0 && c.compareTo(ed) <= 0) {
    				result.add(p);
    				tilepane.getChildren().add(p.getFittedImageView());
    			}
    		}
    	}
    }
	@FXML
    void searchTag(ActionEvent e) {
			searchphotos.clear();
			original = true;
			//ObservableList<String> searchResult= FXCollections.observableArrayList();
			Album searchAlbum; 
			for(int i=0; i<AlbumDirectory.useralbums.size(); i++){
				searchAlbum= AlbumDirectory.useralbums.get(i); 
				if(searchAlbum.photos!=null){
					for(int counter=0;counter<searchAlbum.photos.size();counter++) {
						System.out.println("checked photo");
						PhotoData photo = searchAlbum.photos.get(counter);
						if(orClick.selectedProperty().getValue()) { //or logic
							String checktagname = "";
							String checktagvalue = "";
							for(int t = 0; t<tagNamesList.size();t++) {
								checktagname = tagNamesList.get(t);
								checktagvalue = tagValuesList.get(t);
								
								if(photo.tags.containsKey(checktagname)) {
									if(photo.tags.get(checktagname).contains(checktagvalue)) {
										//System.out.println(searchAlbum.photos.get(counter).file.toPath() + " , caption: " + AlbumPhotos.aphotos.get(counter).caption);
										//searchResult.add(searchAlbum.photos.get(counter).file.toPath().toString());
										
										//check if picture already exists in list
										for(int checker=0;checker<searchphotos.size();checker++) {
											try {
												if(searchphotos.get(checker).location.toURL().toString().compareTo(photo.location.toURL().toString())==0) {
													//picture is already inserted
													original = false;
													break;
												}else {
													//picture not added
												}
											} catch (MalformedURLException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
										if(original) {
											searchphotos.add(photo);
											break;
										}
									}
								}
							}
						}else { //and logic
							boolean andLogicMatch = true;
							String checktagname = "";
							String checktagvalue = "";
							if(searchAlbum.photos.get(counter).tags.size()<tagNamesList.size()) {
								andLogicMatch = false;
							}else {
								for(int t = 0; t<tagNamesList.size();t++) {
									checktagname = tagNamesList.get(t);
									checktagvalue = tagValuesList.get(t);
									
									if(photo.tags.containsKey(checktagname)) {
										if(photo.tags.get(checktagname).contains(checktagvalue)) {
											/*System.out.println(AlbumPhotos.aphotos.get(counter).file.toPath() + " , caption: " + AlbumPhotos.aphotos.get(counter).caption);
											searchResult.add(AlbumPhotos.aphotos.get(counter).file.toPath().toString());
											break;*/
										}else {
											andLogicMatch=false;
											break;
										}
									}
								}
							}
							if(andLogicMatch) {
								//System.out.println(searchAlbum.photos.get(counter).file.toPath() + " , caption: " + AlbumPhotos.aphotos.get(counter).caption);
								//searchResult.add(searchAlbum.photos.get(counter).file.toPath().toString());
								for(int checker=0;checker<searchphotos.size();checker++) {
									try {
										if(searchphotos.get(checker).location.toURL().toString().compareTo(photo.location.toURL().toString())==0) {
											//picture is already inserted
											original = false;
											break;
										}else {
											//picture not added
										}
									} catch (MalformedURLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								if(original) {
									searchphotos.add(photo);
								}
							}else {
								//didn't meet all the tag requirements
							}
						}
					}
				}
			}
			
			//results.setItems(searchphotos);
			/* Set the imageView to the Pictures
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * */
			tilepane.getChildren().clear();
			result = new ArrayList<>();
			for(PhotoData p: searchphotos) {
    				result.add(p);
    				tilepane.getChildren().add(p.getFittedImageView());
			}
			//add photos to listview 
			 count= 0; 
		     first= true; 
//		     results.setCellFactory(param -> new ListCell<Photo>() {
//				private ImageView imageView = new ImageView();
//
//				@Override
//				public void updateItem(Photo name, boolean empty) {
//					super.updateItem(name, empty);
//					if (empty) {
//						setText(null);
//						setGraphic(null);
//					} else {
//						if (first) {
//							first = false;
//						} else if (count < searchphotos.size()) {
//							try {
//								Image image = new Image(searchphotos.get(count).file.toURI().toURL().toString());
//								imageView.setImage(image);
//								imageView.setFitHeight(50);
//								imageView.setFitWidth(50);
//								count++;
//							} catch (MalformedURLException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//
//						}
//						setText(name.toString());
//						setGraphic(imageView);
//					}
//				}
//			});
		
    }
	@FXML
	public void changeBy(ActionEvent event) throws IOException{
		if(byDate.selectedProperty().getValue()) { //selected date
			start.setVisible(true);
			end.setVisible(true);
			tagName.setVisible(false);
			tagValue.setVisible(false);
			tagNames.setVisible(false);
			tagValues.setVisible(false);
			orClick.setVisible(false);
			andClick.setVisible(false);
			clearTag.setVisible(false);
			addTag.setVisible(false);
			searchBtn.setDisable(false);
		}else { //selected tag
			start.setVisible(false);
			end.setVisible(false);
			tagName.setVisible(true);
			tagValue.setVisible(true);
			tagNames.setVisible(true);
			tagValues.setVisible(true);
			orClick.setVisible(true);
			andClick.setVisible(true);
			clearTag.setVisible(true);
			addTag.setVisible(true);
			searchBtn.setDisable(true);
		}
	}
}
