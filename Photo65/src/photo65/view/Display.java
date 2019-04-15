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

public class Display {
	private ObservableList<String> observelist;
	PhotoData Photo;
	static Album Album;
	int index;
	@FXML Button logoutBtn;
	@FXML Button nextBtn;
	@FXML Button backBtn;
	@FXML Button captionBtn;
	@FXML Button tagBtn;
	@FXML Button returnBtn;
	@FXML Text caption;
	@FXML Label dateLab;
	@FXML Button deleteTag;
	@FXML private ImageView showPhoto;
	@FXML AnchorPane rootPane;
	@FXML ListView<String> tagList;
	private Stage mainStage;
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
	@FXML
	void onLogout(ActionEvent e) throws IOException
	{
		SceneController.viewLogin();
	}
	@FXML
	void onReturn(ActionEvent e) throws IOException
	{
		SceneController.viewGallery();
	}
	@FXML
	void forward(ActionEvent e) {
		//System.out.println("next");
		if(index >= Album.photos.size() - 1) {
			Toast.makeText(mainStage, "Reached end of photos", 500, 500, 50);
			return;
		}
		Photo = Album.photos.get(++index);
		SceneController.currentPhoto = Photo;
		Image i = Photo.getImage();
		showPhoto.setImage(i);
		caption.setText("Caption: " + Photo.caption);
		dateLab.setText("Date: " + Photo.getLastModifiedDate().toString());
		updateList();
	}
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
		        }
		        Photo.tags.put(pair.getKey(), pair.getValue());
		        updateList();
		    });
	}
	@FXML
	void caption(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(mainStage);
		dialog.setTitle("Caption");
		dialog.setHeaderText("Enter Photo Caption");
		dialog.setContentText("Enter caption: ");
		Optional<String> result = dialog.showAndWait();
		
		if (result.isPresent())
		{
			Photo.caption = new String(result.get());
			caption.setText("Caption: " + Photo.caption);
		}
	}
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
