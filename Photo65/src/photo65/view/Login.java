package photo65.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import photo65.Photos;

/***
 * Controller class for loginPage.fxml
 * @author Mitch Lew
 */

public class Login implements Initializable{
	@FXML
	private Button loger;
	
	@FXML
	private TextField enteredName;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			ObjectInputStream in= new ObjectInputStream(new FileInputStream(Administrator.file));
			List<Users> userlist= (List<Users>) in.readObject();  
			Administrator.observe_list= FXCollections.observableList(userlist); 
			in.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace(); 
		}catch (IOException event) {
			event.printStackTrace();
		} 
	}
	public void confirmLogin(ActionEvent event) throws IOException{
		String name = enteredName.getText(); 
		if(name.equals("admin")){
			System.out.println("GOT HERE");
			FXMLLoader loader= new FXMLLoader(); 
			loader.setLocation(Photos.class.getResource("view/adminPage.fxml")); 
			Scene scene= new Scene(loader.load()); 
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); 
			primaryStage.setScene(scene);  
			primaryStage.show();
		}
		
	}


}
