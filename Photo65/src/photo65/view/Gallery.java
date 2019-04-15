package photo65.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Gallery {
	@FXML AnchorPane rootPane;
	@FXML Button logBtn;
	@FXML Button retBtn;
	@FXML Button disBtn;
	@FXML Button addBtn;
	@FXML Button remBtn;
	@FXML Button copyBtn;
	@FXML Button moveBtn;
	@FXML ScrollPane scrollPane;
	@FXML public TilePane tilePane;
	@FXML ImageView invisIV;
	private Stage mainStage;
	private static Album album;
	private ImageView selected = null;
	public static ObservableList<PhotoData> all_photos = FXCollections.observableArrayList();
	private HashMap<ImageView, PhotoData> photoMap;
	
	
	public void start(Stage mainStage) {
		this.mainStage = mainStage;
		album = SceneController.currentAlbum;
		photoMap = new HashMap<>();
		int k = SceneController.currentAlbum.photos.size();
		for(int i = 0; i < k; i++) {
			PhotoData p = album.photos.get(i);
			//System.out.println(p.location);
			ImageView im = p.getFittedImageView();
			tilePane.getChildren().add(im);
			photoMap.put(im,p);
		}
	}

	public void onLogout() throws IOException {
		SceneController.viewLogin();
	}
	public void onReturn() throws IOException{
		SceneController.viewAlbumDirectory();
	}
	@FXML
	void display(ActionEvent e) throws IOException {
		if(selected == null) {
			Toast.makeText(mainStage, "No photo selected", 500, 500, 50);
			return;
		}
		SceneController.currentPhoto = photoMap.get(selected);
		SceneController.displayPhoto();
	}
	@FXML
	void add(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();	
		fileChooser.setTitle("Open Image file");	
		File newFile = fileChooser.showOpenDialog(mainStage);
		if(newFile != null && newFile.exists())
		{
			PhotoData photo = new PhotoData(newFile.toURI());
			album.photos.add(photo);
			ImageView im = photo.getFittedImageView();
			photoMap.put(im, photo);
			tilePane.getChildren().add(im);
			try {
				ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(Administrator.file));
				os.writeObject(new ArrayList<Users>(Administrator.observe_list)); 
				os.close();
			}catch (FileNotFoundException e1){
				e1.printStackTrace(); 
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
	}
	@FXML
	void imagePress(MouseEvent me) {
		if(!(me.getTarget() instanceof ImageView)) {
			return;
		}
		ImageView im = (ImageView)me.getTarget();
		ScaleTransition trans = new ScaleTransition();
		trans.setDuration(Duration.seconds(0.3));
		trans.setNode(im);
		trans.setToX(1.1);
		trans.setToY(1.1);
		trans.play();
		if(selected != null && !selected.equals(im)) {
			ScaleTransition trans1 = new ScaleTransition();
			trans1.setDuration(Duration.seconds(0.3));
			trans1.setNode(selected);
			trans1.setToX(1);
			trans1.setToY(1);
			trans1.play();
		}
		selected = im;
	}
	@FXML
	void remove(ActionEvent e) {
		if(selected == null) {
			Toast.makeText(mainStage, "No Photo Selected", 500, 500, 50);
			return;
		}else {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to remove this Picture?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			if(alert.getResult()== ButtonType.YES) {
				Toast.makeText(mainStage, "PhotoRemoved", 500, 500, 50);
				PhotoData p = photoMap.get(selected);
				album.photos.remove(p);
				tilePane.getChildren().remove(selected);
				selected = null;
				
				try {
					ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(Administrator.file));
					os.writeObject(new ArrayList<Users>(Administrator.observe_list)); 
					os.close();
				}catch (FileNotFoundException e1){
					e1.printStackTrace(); 
				}catch (IOException e1) {
					e1.printStackTrace();
				} 
			}
			else {
				Toast.makeText(mainStage, "photo not removed", 500, 500, 50);
			}
		}	
	}
	@FXML
	void copy(ActionEvent e) {
		List<String> alist = new ArrayList<String>();
		if(selected == null) {
			Toast.makeText(mainStage, "No Photo Selected", 500, 500, 50);
			return;
		}
		for(Album a : SceneController.currentUser.albums)
		{
			if(!a.albumName.equals(SceneController.currentAlbum.albumName))
				alist.add(a.albumName);
		}
		ChoiceDialog<String> dialog;
		if(SceneController.currentUser.albums.size() > 1)
			dialog = new ChoiceDialog<String>(alist.get(0), alist);
		else
		{
			Toast.makeText(mainStage, "No other album", 500, 500, 50);
			return;
		}
		dialog.setTitle("Pick Album");
		dialog.setHeaderText("Pick copy to album");
		dialog.setContentText("Choose the album to copy to");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			PhotoData p = photoMap.get(selected);
			int k = SceneController.currentUser.albums.size();
			for(int i =0; i<k; i++) {
				if(SceneController.currentUser.albums.get(i).albumName==result.get()) {
					SceneController.currentUser.albums.get(i).photos.add(p);
				}
			}
			try {
				ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(Administrator.file));
				os.writeObject(new ArrayList<Users>(Administrator.observe_list)); 
				os.close();
			}catch (FileNotFoundException e1){
				e1.printStackTrace(); 
			}catch (IOException e1) {
				e1.printStackTrace();
			} 
		}
	}
	@FXML
	void move(ActionEvent e) {
		List<String> alist = new ArrayList<String>();
		if(selected == null) {
			Toast.makeText(mainStage, "No Photo Selected", 500, 500, 50);
			return;
		}
		for(Album a : SceneController.currentUser.albums)
		{
			if(!a.albumName.equals(SceneController.currentAlbum.albumName))
				alist.add(a.albumName);
		}
		ChoiceDialog<String> dialog;
		if(SceneController.currentUser.albums.size() > 1)
			dialog = new ChoiceDialog<String>(alist.get(0), alist);
		else
		{
			Toast.makeText(mainStage, "No other album", 500, 500, 50);
			return;
		}
		dialog.setTitle("Moving Albums");
		dialog.setHeaderText("Move Album");
		dialog.setContentText("Which album are you moving this to?");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			PhotoData p = photoMap.get(selected);
			int k = SceneController.currentUser.albums.size();
			for(int i =0; i<k; i++) {
				if(SceneController.currentUser.albums.get(i).albumName==result.get()) {
					SceneController.currentUser.albums.get(i).photos.add(p);
				}
			}
			try {
				ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(Administrator.file));
				os.writeObject(new ArrayList<Users>(Administrator.observe_list)); 
				os.close();
			}catch (FileNotFoundException e1){
				e1.printStackTrace(); 
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
		remove(e);
	}
}
