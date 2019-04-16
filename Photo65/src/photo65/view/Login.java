package photo65.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/***
 * Controller class for loginPage.fxml
 * @author Mitch Lew
 */

public class Login{
	/**
	 * button to login
	 */
	@FXML private Button loger;
	/**
	 * textfield to enter name
	 */
	@FXML private TextField enteredName;
	Stage mainStage;
	/**
	 * initializig method reads in file
	 * @param mainStage
	 */
	public void start(Stage mainStage) {                
			try {
				ObjectInputStream in= new ObjectInputStream(new FileInputStream(Administrator.file));
				@SuppressWarnings("unchecked")
				List<Users> userlist= (List<Users>) in.readObject();  
				Administrator.observe_list= FXCollections.observableList(userlist); 
				in.close();
			}catch (ClassNotFoundException c){
				c.printStackTrace(); 
			}catch (IOException event) {
				event.printStackTrace();
			} 
	}
	/**
	 * confirms login for entername textfield
	 * @param event
	 * @throws IOException
	 */
	public void confirmLogin(ActionEvent event) throws IOException{
		String name = enteredName.getText(); 
		if(name.equals("admin")){
			SceneController.viewAdmin();
		}else {
			boolean alogin = false;
			for(int i=0; i<Administrator.observe_list.size(); i++){
				if(Administrator.observe_list.get(i).username.equals(name)){
					alogin = true;
					AlbumDirectory.user= Administrator.observe_list.get(i); 
					SceneController.currentUser = Administrator.observe_list.get(i);
					if(!(Administrator.observe_list.get(i).albums==null)){
						AlbumDirectory.useralbums= FXCollections.observableArrayList(Administrator.observe_list.get(i).albums);  
					}
					else {
						AlbumDirectory.useralbums = FXCollections.observableArrayList();
					}
					SceneController.viewAlbumDirectory();
				}
				
			}
			if(!alogin) {
					Toast.makeText(mainStage, "No user with that login!", 500, 500, 50);
					
				}
		}
	}


}
