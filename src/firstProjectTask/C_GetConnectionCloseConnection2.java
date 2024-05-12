package firstProjectTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class C_GetConnectionCloseConnection2 {

	public static void main(String[] args) {

		Connection conn = null;
		try {

			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName("com.mysql.cj.jdbc.Driver");// .newInstance();

//			conn = DriverManager.getConnection("jdbc:mysql://dif-mysql.ehu.es:23306/DBI70"+"user=DBI70&password=DBI70");
// three parameters
			conn = DriverManager.getConnection("jdbc:mysql://dif-mysql.ehu.es:23306/DBI49", "DBI49", "DBI49"); // three parameters
			System.out.println(" --> Connection Established"); 
			// Do something with the Connection

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.out.println("ooops  --> Connection Failed");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// HERE WE NEED TO CLOSE THE CONNECTION
				if (conn != null)
					conn.close();
				System.out.println(" --> Connection Closed"); // parameters
			} catch (SQLException e) {
				System.out.println("ooops  --> Closing the Connection Failed");
			}

		}
	}
}
