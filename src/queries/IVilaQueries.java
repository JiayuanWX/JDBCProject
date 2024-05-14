package queries;

import java.sql.*;

public class IVilaQueries {
  
	Connection conn = null;
	Statement st;
	ResultSet rs;
	PreparedStatement pst;
	
	public IVilaQueries () {
	}
	
	public void openConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://dif-mysql:23306/DBI49", "DBI49", "DBI49");


		} catch (SQLException ex) {
			
			System.out.println("Connection Failed");
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}
	}
	
	public void closeConnection() {
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
			if (pst != null)
				pst.close();

		} catch (SQLException ex) {

			System.out.println("Closing Connection Went Wrong!");
		}
	}
	
	public void query1() {
		try {
			openConnection();

			st = conn.createStatement();
			
			String query = "SELECT e.Ssn, e.Fname, e.Lname, e.Salary " +
		               "FROM employee e " +
		               "JOIN ( " +
		               "    SELECT e1.Ssn " +
		               "    FROM employee e1 " +
		               "    JOIN dependent d ON e1.Ssn = d.Essn " +
		               "    WHERE d.Sex = 'F' " +
		               "    GROUP BY e1.Ssn " +
		               "    HAVING COUNT(*) = ( " +
		               "        SELECT MAX(dependent_count) " +
		               "        FROM ( " +
		               "            SELECT COUNT(*) AS dependent_count " +
		               "            FROM dependent " +
		               "            WHERE Essn = e1.Ssn " +
		               "            GROUP BY Essn " +
		               "        ) AS subquery " +
		               "    ) " +
		               "    ORDER BY COUNT(*) DESC " +
		               "    LIMIT 2 " +
		               ") AS top_employees ON e.Ssn = top_employees.Ssn " +
		               "ORDER BY e.Salary DESC " +
		               "LIMIT 1;";

			rs = st.executeQuery(query);
			
			rs.next();
			String fname = rs.getString("e.fname");
			String lname = rs.getString("e.Lname");
			String salary = rs.getString("e.Salary");
			System.out.println("The employee is " + fname + " "+ lname + "and his/her salary is " + salary + "\n");

		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}

	}
	
	public void query2() {
		try {
			openConnection();

			st = conn.createStatement();
			
			String query = "SELECT h1.hotelcity, h1.hotelname, COUNT(distinct htc1.CustomerId) AS custs " +
		               "FROM hotel_trip_customer AS htc1 " +
		               "NATURAL JOIN hotel AS h1 " +
		               "GROUP BY h1.hotelcity, h1.hotelname " +
		               "HAVING COUNT(distinct htc1.CustomerId) >= ALL ( " +
		               "    SELECT DISTINCT COUNT(distinct htc2.CustomerId) " +
		               "    FROM hotel_trip_customer AS htc2 " +
		               "    NATURAL JOIN hotel AS h2 " +
		               "    WHERE h1.hotelcity = h2.hotelcity " +
		               "    GROUP BY h2.hotelcity, h2.hotelname)";

			
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				String city = rs.getString("hotelcity");
				String hotel = rs.getString("hotelname");
				String custs = rs.getString("custs");
				System.out.println("For the city " + city + " the hotel is " + hotel + " and it has " + custs + " customers " + "\n");
			}

		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}
	}
	
	public void query3() {
		
		try {
			openConnection();

			st = conn.createStatement();
			
			String query = "SELECT CustomerId, c.custname, COUNT(distinct htc1.TripTo) AS distinctTrips " +
		               "FROM hotel_trip_customer htc1 " +
		               "NATURAL JOIN customer c " +
		               "GROUP BY htc1.CustomerId " +
		               "HAVING COUNT(distinct htc1.TripTo) = " +
		               "( " +
		               "    SELECT COUNT(DISTINCT TripTo) " +
		               "    FROM hotel_trip_customer htc2 " +
		               "    JOIN employee_customer ON htc2.CustomerId = employee_customer.Cust_Id " +
		               "    WHERE employee_customer.Emp_id IN ( " +
		               "        SELECT e.Ssn " +
		               "        FROM employee e " +
		               "        JOIN works_on w ON e.Ssn = w.Essn " +
		               "        GROUP BY e.Ssn " +
		               "        HAVING SUM(w.Hours) <= ALL( " +
		               "            SELECT SUM(w2.Hours) AS total_hours " +
		               "            FROM employee e2 " +
		               "            JOIN works_on w2 ON e2.Ssn = w2.Essn " +
		               "            GROUP BY e2.Ssn " +
		               "        ) " +
		               "    ) " +
		               ")";

			
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				String name = rs.getString("custname");
				String distinct = rs.getString("distinctTrips");
				int id = rs.getInt("CustomerId");
				System.out.println("Customer name: " + name + " with ID: " + id + " has gone to " + distinct + " trips" + "\n");
			}

		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}

	}
	

	public void extraQuery1() {
		
		try {
			openConnection();

			st = conn.createStatement();
			
			String query = "SELECT cl.Language, c.Name AS CountryName, c.Population " +
		            "FROM countrylanguage cl " +
		            "JOIN country c ON cl.CountryCode = c.Code " +
		            "WHERE c.Population = ( " +
		            "    SELECT MAX(c2.Population) " +
		            "    FROM countrylanguage cl2 " +
		            "    JOIN country c2 ON cl2.CountryCode = c2.Code " +
		            "    WHERE cl2.Language = cl.Language " +
		            ") " +
		            "ORDER BY c.Population DESC " +
		            "LIMIT 25;";

			
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				String lang = rs.getString("Language");
				String country = rs.getString("CountryName");
				int popu = rs.getInt("Population");
				System.out.println("For the language " + lang + " the country with most population is " + country + " and it has " + popu + " citizens." + "\n");
			}

		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}

	}
	


}