package photo65.view;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
		if(albumName.getText()==null){
			aname= ""; 
		}
		else{
			aname= albumName.getText(); 
		}
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
}
