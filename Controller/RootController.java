package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RootController implements Initializable{
	public Stage primaryStage;
	
	@FXML private TextField textId;
	@FXML private PasswordField textPassword;
	@FXML private Button btnLogin;
	@FXML private Button btnClose;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnLogin.setOnAction(event-> {handlerBtnLoginAction();}); // �α��ι�ư �׼� �߰�
		btnClose.setOnAction(event-> {primaryStage.close();}); // �α���â �ݱ� ��ư �׼�
		textPassword.setOnKeyPressed(event -> {
	         if (event.getCode().equals(KeyCode.ENTER)) {
	            handlerBtnLoginAction();
	         }
	    });
	}
	public void handlerBtnLoginAction(){
		if(!(textId.getText().equals("a")&&textPassword.getText().equals("1"))) {
			callAlert("login error : Check your ID or Password.");
			textId.clear(); textPassword.clear();
			return;
		}
		try {
			Stage mainStage = new Stage();
			FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/main.fxml"));
			Parent root = loader.load();
			
			MainController mainController = loader.getController();
			mainController.mainStage=mainStage; // ȭ�� ü���� �߰�
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/addmain.css").toString());
			mainStage.setScene(scene);
			primaryStage.close(); //���� ����ȭ�� �ݱ�
			mainStage.show();
			
			callAlert("ȭ����ȯ���� : ����ȭ������ ��ȯ�Ǿ����ϴ�.");
		}catch (IOException e) {
			callAlert("ȭ����ȯ���� : ȭ�� ��ȯ�� ������ �ֽ��ϴ�.");
			e.printStackTrace();
		}
	}

	
	// ��Ÿ : �˸�â = "�������� : ���� ����� �Է����ּ���" (�� �ݷ��� �����ٰ�)
	private void callAlert(String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("�˸�â");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":")+1));
		alert.showAndWait();
	}
}
