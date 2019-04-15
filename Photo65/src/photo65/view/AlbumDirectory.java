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

public class AlbumDirectory {
	@FXML AnchorPane rootPane;
	@FXML Button logoutBtn;
	@FXML Button openAlbumBtn;
	@FXML Button createAlbumBtn;
	@FXML Button deleteAlbumBtn;
	@FXML Button renameAlbumBtn;
	@FXML Button searchBtn;
	@FXML private TextField albumName;
	public static Users user;
	@FXML ListView<Album> albumList;
	public static ObservableList<Album> useralbums= FXCollections.observableArrayList(); 
	private Stage mainStage;
	Album renameAlb; 
	public void start(Stage primaryStage) {
		this.mainStage = primaryStage;
		albumList.setItems(useralbums);
	}
	public void onLogout() throws IOException {
		SceneController.viewLogin();
	}
	@FXML
	public void search(ActionEvent e) throws IOException {
		SceneController.viewSearch();
	}
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
						Toast.makeText(mainStage, "Album removed", 500, 500, 500);
					}
				}
			}
			Toast.makeText(mainStage, "Album removed", 500, 500, 50);
		}
	}
	public void rename(ActionEvent event) {
		String rename; 
		renameAlb = albumList.getSelectionModel().getSelectedItem();
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(mainStage); dialog.setTitle("Rename Album");
		dialog.setHeaderText("Enter desired new album Name");
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

