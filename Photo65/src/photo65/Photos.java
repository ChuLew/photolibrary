package photo65;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photo65.view.Administrator;
import photo65.view.Album;
import photo65.view.PhotoData;
import photo65.view.SceneController;
import photo65.view.Users;


public class Photos extends Application {
	private Stage primaryStage;
	private AnchorPane mainLayout;
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void start(Stage primaryStage) throws IOException, URISyntaxException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Photo Library");
		SceneController.primaryStage = primaryStage;
		String resources[] = {"STOCK/fan.jpg","STOCK/hj.jpg","STOCK/hos.jpg","STOCK/jj.jpg","STOCK/uk.jpg"};
		boolean stockTrue = false;
		for(int i = 0; i< Administrator.observe_list.size();i++) {
			if(Administrator.observe_list.get(i).username.equals("stock")) {
				stockTrue = true;
			}
		}
		if(!stockTrue) {
			Users stock = new Users("stock");
			Administrator.observe_list.add(stock);
			Album ql = new Album("stock album");
			stock.addAlbum(ql);
			int k = stock.albums.size();
			int holder = 0;
			for(int i =0;i<k;i++) {
				if(stock.albums.get(i).equals("stock album")) {
					holder = i;
				}
			}
			Album album = stock.albums.get(holder);
			ArrayList<PhotoData> photos = new ArrayList<>();
			for(String k1: resources) {
				File file = new File(k1);
				album.photos.add(new PhotoData(file.toURI()));
				}
		}
		SceneController.viewLogin();
		
		//showMainView();
	}
	
	private void showMainView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Photos.class.getResource("view/loginPage.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
