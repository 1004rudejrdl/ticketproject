package Controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtility {
	public static Connection getConnection() {
		Connection con=null;
		try {
			// 1. MySql database Ŭ������ �ε��Ѵ�.
			Class.forName("com.mysql.jdbc.Driver");
			
			// 2. �ּ�, ID, password�� ���ؼ� ���ӿ�û�Ѵ�.
			con = DriverManager.getConnection("jdbc:mysql://localhost/performance", "root", "123456");
//			MainController.callAlert("���Ἲ�� : �����ͺ��̽� ���� ����");
		}catch(Exception e) {
			MainController.callAlert("������� : �����ͺ��̽� ���� ����, ���� ���");
			e.printStackTrace();
			return null;
		}
		return con;
	}
	
}
