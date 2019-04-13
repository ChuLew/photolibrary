package photo65.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


public class Administrator {
	@FXML
	private Button add;
	@FXML
	private Button delete;
	@FXML
	private Button logout;
	@FXML
	private TextField newUsers;
	@FXML
	ListView<Users> usernames;
	public static ObservableList<Users> observe_list = FXCollections.observableArrayList();
	
	public static String file = "DirectoryOfUsers.bin";
	@FXML
	private void on_logout(ActionEvent event) throws IOException{
		FXMLLoader loader= new FXMLLoader(); 
		loader.setLocation(photo65.Photos.class.getResource("view/loginPage.fxml")); 
		Scene scene= new Scene(loader.load()); 
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); 
		primaryStage.setScene(scene);  
		primaryStage.show();
	}
	@FXML
	private void addUsers(ActionEvent event) {
		String username; 
		username = newUsers.getText();
		if(username == null) {
			username = "";
		}
		boolean alreadyUsed  = false;
		observe_list = usernames.getItems();
		Users user = new Users(username);
		for(int i = 0; i< observe_list.size();i++) {
			if(observe_list.get(i).username.equals(username)) {
				alreadyUsed = true;
				Alert alert = new Alert(AlertType.ERROR,"Username already in directory",ButtonType.OK);
				alert.showAndWait();
				break;
			}
		}
		if(!alreadyUsed && !username.contentEquals("")) {
			observe_list.add(user);
			Collections.sort(observe_list,Users.Comparators.NAME);
			usernames.setItems(observe_list);
			try {
				ObjectOutputStream os= new ObjectOutputStream(new FileOutputStream(file));
				os.writeObject(new ArrayList<Users>(observe_list)); 
				os.close();
			}catch (FileNotFoundException e){
				e.printStackTrace(); 
			}catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	@FXML
	private void deleteUsers(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete: " + usernames.getSelectionModel().getSelectedItem().username + "?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {			
			Users id = usernames.getSelectionModel().getSelectedItem();	
			for (int i=0; i<observe_list.size(); i++){
				if(observe_list.get(i).username.compareTo(id.username)==0){
					observe_list.remove(i); 
				}
			}
			usernames.setItems(observe_list);
			try {
				ObjectOutputStream stream= new ObjectOutputStream(new FileOutputStream(file));
				stream.writeObject(new ArrayList<Users>(observe_list)); 
				stream.close();
			}catch (FileNotFoundException e){
				e.printStackTrace(); 
			}catch (IOException e) {

				e.printStackTrace();
			} 
		}
	}
	public void start(Stage primaryStage) {
		usernames.setItems(observe_list);
	}
}
