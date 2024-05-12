package firstProjectTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class D_jdbc1createStmt {

	public static void main(String[] args) throws SQLException {

		Connection conn = null;
		Statement myfirststmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://dif-mysql.ehu.es:23306/DBI49", "DBI49", "DBI49"); // three parameters
			System.out.println("connection Establishedd!!!");

			myfirststmt = conn.createStatement();
			rs = myfirststmt.executeQuery("select * from employee;");

			while (rs.next()) {
				String fname = rs.getString("Fname");
				String lname = rs.getString("Lname");
				String ssn = rs.getString("Ssn");

				System.out.println("Name: " + fname + "\t  Surname: " + lname + "\t\t  Ssn: " + ssn);
			}

		} catch (SQLException ex) {
			System.out.println("SQLexception   " + ex.getMessage());
			System.out.println("SQLstate   " + ex.getSQLState());
			System.out.println("Error   " + ex.getErrorCode());
			System.out.println(" oooo  Connection failed!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {

				if (rs != null)
					rs.close();
				if (myfirststmt != null)
					myfirststmt.close();
				if (conn != null)
					conn.close();

				System.out.println(" --> Connection Closed!");
			} catch (SQLException ex) {

				System.out.println(" Closing Connection Went Wrong!");
			}

		}

	}

}
