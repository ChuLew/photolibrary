package photo65.view;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * 
 * @author mitchlew
 *
 */
public class AlbumDirectory {
	/**
	 * Root pane
	 */
	@FXML AnchorPane rootPane;
	/**
	 * button to logout
	 */
	@FXML Button logoutBtn;
	/**
	 * button to open album
	 */
	@FXML Button openAlbumBtn;
	/**
	 * button to create album
	 */
	@FXML Button createAlbumBtn;
	/**
	 * button to delete album
	 */
	@FXML Button deleteAlbumBtn;
	/**
	 * button to rename album
	 */
	@FXML Button renameAlbumBtn;
	/**
	 * button to search for photos
	 */
	@FXML Button searchBtn;
	/**
	 * textfield to name album
	 */
	@FXML private TextField albumName;
	/**
	 * user variable set from login page to access that users information
	 */
	public static Users user;
	/**
	 * listview that displays albums
	 */
	@FXML ListView<Album> albumList;
	/**
	 * list that stores albums and written to file
	 */
	public static ObservableList<Album> useralbums= FXCollections.observableArrayList(); 
	/**
	 * mainstage
	 */
	private Stage mainStage;
	/**
	 * variable to hold albums new name
	 */
	Album renameAlb; 
	/**
	 * iniatilizing method
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) {
		this.mainStage = primaryStage;
		albumList.setItems(useralbums);
	}
	/**
	 *method that switches scene to login
	 * @throws IOException
	 */
	public void onLogout() throws IOException {
		SceneController.viewLogin();
	}
	/**
	 * switches scene to search
	 * @param e
	 * @throws IOException
	 */
	@FXML
	public void search(ActionEvent e) throws IOException {
		SceneController.viewSearch();
	}
	/**
	 * adds album 
	 * @param e
	 */
	@FXML 
	public void addAlbum(ActionEvent e){
		String album_name; 
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(mainStage); dialog.setTitle("Album Creation");
		dialog.setHeaderText("Input new album name");
		dialog.setHeight(20);
		dialog.setWidth(20);
		dialog.setContentText("Input name: ");
		Optional<String> result = dialog.showAndWait();
		if(result.get().isEmpty()) {
			Toast.makeText(mainStage, "No album name Inputed! try again.", 500, 500, 50);
			return;
		}
		album_name = result.get();
		boolean alreadyUsed= false;
		Album thisAlbum= new Album(album_name); 
		int jn = thisAlbum.photos.size();
		for (int i=0; i<useralbums.size(); i++){
			if(useralbums.get(i).albumName.equals(album_name)){
				alreadyUsed=true; 
				Alert alert = new Alert(AlertType.ERROR, "Album Name Already Exists, No Dupicates!", ButtonType.CLOSE);
				alert.showAndWait(); 
				break; 
			}
		}
		if(!alreadyUsed && !album_name.equals("")){
			Alert alert = new Alert(AlertType.CONFIRMATION, "Add this album: " + album_name + "?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {	
				useralbums.add(thisAlbum); 
				Collections.sort(useralbums,Album.Comparators.NAME);
				albumList.setItems(useralbums);
				List<Album> list = useralbums.stream().collect(Collectors.toList());
				user.albums= (ArrayList<Album>) list;
				try {
					ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(Administrator.file));
					os.writeObject(new ArrayList<Users>(Administrator.observe_list)); 
					os.close();
				}catch (FileNotFoundException event){
					event.printStackTrace(); 
				}catch (IOException event) {
					event.printStackTrace();
				} 
			}
		}
		albumName.clear();
	}
	/**
	 * deletes selected album
	 * @param E
	 */
	@FXML 
	void deleteAlbum(ActionEvent E) {
		Album deleted= albumList.getSelectionModel().getSelectedItem(); 
		
		if(albumList.getSelectionModel().getSelectedItem()==null) {
			Toast.makeText(mainStage, "Nothing Selected", 500, 500, 50);
			return;
		}
		else {
			for(int i=0; i<useralbums.size(); i++){
				if(useralbums.get(i).albumName.equals(deleted.albumName)){
					Alert alert = new Alert(AlertType.CONFIRMATION, "Delete Album: " + deleted.albumName + "?", ButtonType.YES, ButtonType.NO);
					alert.showAndWait();
					if (alert.getResult() == ButtonType.YES) {	
						useralbums.remove(i); 
						List<Album> list = useralbums.stream().collect(Collectors.toList());
						user.albums= (ArrayList<Album>) list;
						try {
							ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(Administrator.file));
							os.writeObject(new ArrayList<Users>(Administrator.observe_list)); 
							os.close();
						}catch (FileNotFoundException e){
							e.printStackTrace(); 
						}catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						Toast.makeText(mainStage, "Album removed", 500, 500, 50);
					}else {
						Toast.makeText(mainStage, "Album was not removed", 500, 500, 50);
					}
				}
			}
		}
	}
	/**
	 * used to rename albums
	 * @param event
	 */
	public void rename(ActionEvent event) {
		String rename; 
		renameAlb = albumList.getSelectionModel().getSelectedItem();
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(mainStage); dialog.setTitle("Rename Album");
		dialog.setHeaderText("Enter desired new album Name");
		dialog.setHeight(20);
		dialog.setWidth(20);
		dialog.setContentText("Enter new name: ");
		Optional<String> result = dialog.showAndWait();
		if(result.get().isEmpty()) {
			Toast.makeText(mainStage, "Album name cannot be empty", 500, 500, 500);
			return;
		}
		rename = result.get();
		boolean alreadyUsed=false; 
		int found = -1; 
		for (int i=0; i<useralbums.size(); i++){
			if(useralbums.get(i).albumName.equals(rename)){
				alreadyUsed=true; 
				Alert alert = new Alert(AlertType.ERROR, "This Album Name Already Exists", ButtonType.CLOSE);
				alert.showAndWait(); 
				break; 
			}
			if(useralbums.get(i).albumName.equals(renameAlb.albumName)){
				found= i; 
			}
		}
		if(!alreadyUsed){
			useralbums.get(found).setName(rename);
			Collections.sort(useralbums,Album.Comparators.NAME);
			albumList.setItems(useralbums);
			List<Album> list = useralbums.stream().collect(Collectors.toList());
			user.albums= (ArrayList<Album>) list;
			try {
				ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(Administrator.file));
				os.writeObject(new ArrayList<Users>(Administrator.observe_list)); 
				os.close();
			}catch (FileNotFoundException e){
				e.printStackTrace(); 
			}catch (IOException e) {
		
				e.printStackTrace();
			} 
		}
	}
	/**
	 * switchess scene to gallery, displays photos from albums in that gallery
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void openAlbum(ActionEvent e) throws IOException {
		if(albumList.getSelectionModel().getSelectedItem()==null) {
			Toast.makeText(mainStage, "Nothing Selected", 500, 500, 50);
			return;
		}
			SceneController.currentAlbum = albumList.getSelectionModel().getSelectedItem();
			SceneController.viewGallery();
	}
	
}

