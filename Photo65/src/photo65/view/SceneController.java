package photo65.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photo65.Photos;
import photo65.view.Login;

/**
 * specific class for switching scenes
 * @author mitchlew
 *
 */
public class SceneController {
	/**
	 * stage
	 */
	public static Stage primaryStage;
	/**
	 * holds currentuser for other scenes to track 
	 */
	public static Users currentUser;
	/**
	 * holds current album for other scenes to track
	 */
	public static Album currentAlbum;
	/**
	 * holds current photo for other scenes to track
	 */
	public static PhotoData currentPhoto;
	/**
	 * switches scene to login page
	 * @throws IOException
	 */
	public static void viewLogin() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(Photos.class.getResource("view/loginPage.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Login loginController = loader.getController();
		loginController.start(primaryStage);
		Scene scene = new Scene(root);		
		primaryStage.setScene(scene);
		primaryStage.show(); 
	}
	/**
	 * switches scene to admin page
	 * @throws IOException
	 */
	public static void viewAdmin() throws IOException {
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(Photos.class.getResource("view/adminPage.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Administrator adminController = loader.getController();
		adminController.start(primaryStage);
		Scene scene = new Scene(root);		
		primaryStage.setScene(scene);
		primaryStage.show(); 
		
	}
	/**
	 * switches scene to album directory page
	 * @throws IOException
	 */
	public static void viewAlbumDirectory() throws IOException {
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(Photos.class.getResource("view/albumDirectory.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		AlbumDirectory AlbDirController = loader.getController();
		AlbDirController.start(primaryStage);// cause of problem
		Scene scene = new Scene(root);		
		primaryStage.setScene(scene);
		primaryStage.show(); 
		
	}
	/**
	 * switches scene to gallery page
	 * @throws IOException
	 */
	public static void viewGallery() throws IOException{
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(Photos.class.getResource("view/photoGallery.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Gallery metGal = loader.getController();
		metGal.start(primaryStage);// cause of problem
		Scene scene = new Scene(root);		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * switches scene to display page for photos
	 * @throws IOException
	 */
	public static void displayPhoto() throws IOException {
		
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(Photos.class.getResource("view/Display.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Display photgal = loader.getController();
		photgal.start(primaryStage);// cause of problem
		Scene scene = new Scene(root);		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * switches scene to search page
	 * @throws IOException
	 */
	public static void viewSearch() throws IOException {
		
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(Photos.class.getResource("view/search1.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Search1 psearch = loader.getController();
		psearch.start(primaryStage);// cause of problem
		Scene scene = new Scene(root);		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
