package photo65.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringTokenizer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
/**
 * displays photo for user and allows that person to add descriptors to photo
 * @author mitchlew
 *
 */
public class Display {
	/**
	 * observable list
	 */
	private ObservableList<String> observelist;
	/**
	 * local photodata variable for class to use, is set by gallery
	 */
	PhotoData Photo;
	/**
	 * local album to use is set but gallery
	 */
	static Album Album;
	/**
	 * an index
	 */
	int index;
	/**
	 * button for logout
	 */
	@FXML Button logoutBtn;
	/**
	 * button for next photo
	 */
	@FXML Button nextBtn;
	/**
	 *  button for previous photo
	 */
	@FXML Button backBtn;
	/**
	 * button for adding caption
	 */
	@FXML Button captionBtn;
	/**
	 * button for adding tag
	 */
	@FXML Button tagBtn;
	/**
	 * button to return to album
	 */
	@FXML Button returnBtn;
	/**
	 * text field that is updated when caption is applied
	 */
	@FXML Text caption;
	/**
	 * label that is updated when photo is present
	 */
	@FXML Label dateLab;
	/**
	 * button to delete tags when selected
	 */
	@FXML Button deleteTag;
	/**
	 * shows photos present in an image view
	 */
	@FXML private ImageView showPhoto;
	/**
	 * anchorpane
	 */
	@FXML AnchorPane rootPane;
	/**
	 * listview that displays tags
	 */
	@FXML ListView<String> tagList;
	/**
	 * stage mainstage
	 */
	private Stage mainStage;
	/**
	 * initiailzing method
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) {
		this.mainStage = primaryStage;
		Photo = SceneController.currentPhoto;
		Album = SceneController.currentAlbum;
		index = Album.photos.indexOf(Photo);
		File file = new File(Photo.location);
        Image image = new Image(file.toURI().toString());
        showPhoto.setImage(image);
        dateLab.setText("Date: " + SceneController.currentPhoto.getLastModifiedDate().toString());
        caption.setText("Caption: " + SceneController.currentPhoto.caption);
		observelist = FXCollections.observableArrayList();
		observelist.add("(tag) , (value)");
		for(String a: Photo.tags.keySet()) {
			observelist.add(a + " , " + Photo.tags.get(a));
		}
		tagList.setItems(observelist);
	}
	/**
	 * logout method
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void onLogout(ActionEvent e) throws IOException
	{
		SceneController.viewLogin();
	}
	/**
	 * switches back to gallery scene
	 * @param e
	 * @throws IOException
	 */
	@FXML
	void onReturn(ActionEvent e) throws IOException
	{
		SceneController.viewGallery();
	}
	/**
	 * switches to next photo to be displayed
	 * @param e
	 */
	@FXML
	void forward(ActionEvent e) {
		if(index >= Album.photos.size() - 1) {
			Toast.makeText(mainStage, "Reached end of photos", 500, 500, 50);
			return;
		}
		Photo = Album.photos.get(++index);
		SceneController.currentPhoto = Photo;
		Image i = Photo.getImage();
		showPhoto.setImage(i);
		dateLab.setText("Date: " + Photo.getLastModifiedDate().toString());
		caption.setText("Caption: " + Photo.caption);
		updateList();
	}
	/**
	 * switches to previous photo to be displayed
	 * @param e
	 */
	@FXML
	void back(ActionEvent e) {
		if(index<=0){
			Toast.makeText(mainStage, "no more photos before this", 500, 500, 50);
			return;
		}
		Photo = Album.photos.get(--index);
		SceneController.currentPhoto = Photo;
		Image i = Photo.getImage();
		showPhoto.setImage(i);
		caption.setText("Caption: " + Photo.caption);
		dateLab.setText("Date: " + Photo.getLastModifiedDate().toString());
		updateList();
		
	}
	/**
	 * updates observelist 
	 */
	private void updateList() {
		observelist.clear();
		observelist.add("(tag) , (value)");
		for(String a: Photo.tags.keySet()) {
			observelist.add(a + " , " + Photo.tags.get(a));
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
	/**
	 * adds tags to photo
	 * @param e
	 */
	@FXML
	void tag(ActionEvent e) {
		 Dialog<Pair<String, String>> dialog = new Dialog<>();
		 dialog.setTitle("Tag Creator");
	    ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
		    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		    GridPane gridPane = new GridPane();
		    gridPane.setHgap(10);
		    gridPane.setVgap(10);
		    gridPane.setLayoutX(20);
		    gridPane.setLayoutY(20);
		    gridPane.setPadding(new Insets(20, 150, 10, 10));
		    TextField from = new TextField();
		    from.setPromptText("Descriptor");
		    TextField to = new TextField();
		    to.setPromptText("Value");
		    gridPane.add(from, 0, 0);
		    gridPane.add(new Label("To:"), 1, 0);
		    gridPane.add(to, 2, 0);
		    dialog.getDialogPane().setContent(gridPane);
		    Platform.runLater(() -> from.requestFocus());
		    dialog.setResultConverter(dialogButton -> {
		        if (dialogButton == loginButtonType) {
		            return new Pair<>(from.getText(), to.getText());
		        }
		        return null;
		    });
		    Optional<Pair<String, String>> result = dialog.showAndWait();
		    result.ifPresent(pair -> {
		        if(pair.getKey().isEmpty() || pair.getValue().isEmpty()) {
		        	Toast.makeText(mainStage, "No text inputed! try again!", 500, 500, 50);
		        	return;
		        }else {
		        Toast.makeText(mainStage, "Succesfully added tag!", 500, 500, 50);
	        	
		        Photo.tags.put(pair.getKey(), pair.getValue());
		        
		        updateList();
		        }
		        
		    });
	}
	/**
	 * adds caption to photo
	 * @param e
	 */
	@FXML
	void caption(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(mainStage);
		dialog.setTitle("Caption");
		dialog.setHeaderText("Enter Caption for Photo");
		dialog.setContentText("Enter the Caption: ");
		Optional<String> result = dialog.showAndWait();
		
		if (result.isPresent()){
			Photo.caption = new String(result.get());
			caption.setText("Caption: " + Photo.caption);
		}{
			Toast.makeText(mainStage, "No text inputed! No caption was entered!", 500, 500, 50);
        	
		}
	}
	/**
	 * removes tag from taglist for specific photos
	 * @param event
	 * @throws IOException
	 */
	@SuppressWarnings("unlikely-arg-type")
	@FXML
	public void removeTag(ActionEvent event) throws IOException{
		int selected = tagList.getSelectionModel().getSelectedIndex();
		String str = tagList.getItems().get(selected);
		StringTokenizer defaultTokenizer = new StringTokenizer(str);	 
		str =  defaultTokenizer.nextToken();
		tagList.getItems().remove(selected);
		Photo.tags.remove(str);
		
	}
}
