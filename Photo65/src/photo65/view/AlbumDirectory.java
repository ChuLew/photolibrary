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
import jdk.nashorn.internal.ir.BreakableNode;





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
//		User = SceneController.currentUser;
//		obslist = FXCollections.observableArrayList(Users.albums);
//		albumList.setItems(obslist);
//		for(String a: User.albums.keySet()) {
//			obslist.add(a);
//		}
//		albumList.setItems(obslist);
//		albumList.getSelectionModel().selectedIndexProperty().addListener((obs,oldVal,newVal)-> selectItem(mainStage));
//		
		
	}
//	private Object selectItem(Stage mainStage2) {
//		albumList.getSelectionModel().getSelectedItem();
//		return null;
//	}
	public void onLogout() throws IOException {
		SceneController.viewLogin();
	}

	@FXML 
	public void addAlbum(ActionEvent e){
		String aname; 
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(mainStage); dialog.setTitle("Create Album");
		dialog.setHeaderText("Enter album name");
		dialog.setContentText("Enter name: ");
		Optional<String> result = dialog.showAndWait();
		if(result.get().isEmpty()) {
			Toast.makeText(mainStage, "Album name cannot be empty", 500, 500, 500);
			return;
		}
		aname = result.get();
//		if(albumName.getText()==null){
//			Alert alert = new Alert(AlertType.ERROR,"You didnt input a Album Name", ButtonType.CLOSE);
//			return;
//		}
//		else{
//			aname= albumName.getText(); 
//		}
		boolean unoriginal= false;
		Album thisAlbum= new Album(aname); 
		//useralbums= album.getItems(); 
		//Check to see if song has already been added before
		for (int i=0; i<useralbums.size(); i++){
			if(useralbums.get(i).albumName.equals(aname)){
				unoriginal=true; 
				Alert alert = new Alert(AlertType.ERROR, "This Album Name Already Exists", ButtonType.CLOSE);
				alert.showAndWait(); 
				break; 
			}
		}
		if(!unoriginal && !aname.equals("")){
			Alert alert = new Alert(AlertType.CONFIRMATION, "Add this album: " + aname + "?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {	
				System.out.println(thisAlbum);
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
					// TODO Auto-generated catch block
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
//			user.albums.remove((albumList.getSelectionModel().getSelectedItem()));
//			albumList.setItems(useralbums);
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
		boolean unoriginal=false; 
		int found = -1; 
		for (int i=0; i<useralbums.size(); i++){
			if(useralbums.get(i).albumName.equals(rename)){
				unoriginal=true; 
				Alert alert = new Alert(AlertType.ERROR, "This Album Name Already Exists", ButtonType.CLOSE);
				alert.showAndWait(); 
				break; 
			}
			if(useralbums.get(i).albumName.equals(renameAlb.albumName)){
				found= i; 
			}
		}
		if(!unoriginal){
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
	
	
}

