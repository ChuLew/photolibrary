package photo65.view;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photo65.Photos;
import photo65.view.Login;


public class SceneController {
	public static Stage primaryStage;
	private AnchorPane mainLayout;
	public static Users currentUser;
	public static Album currentAlbum;
	public static PhotoData currentPhoto;
	
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

	public static void viewSearch() throws IOException {
		
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(Photos.class.getResource("view/search.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Search psearch = loader.getController();
		psearch.start(primaryStage);// cause of problem
		Scene scene = new Scene(root);		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
