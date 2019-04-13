package photo65.view;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	private Stage mainStage;
	public void start(Stage primaryStage) {
		this.mainStage = primaryStage;
	}
	public void onLogout() throws IOException {
		SceneController.viewLogin();
	}

}
