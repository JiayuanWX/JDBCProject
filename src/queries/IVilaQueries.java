package queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public class IVilaQueries {

	Connection conn = null;
	Statement st;
	ResultSet rs;
	PreparedStatement pst;
	Savepoint savepoint1 = null;
	
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
	
	public void extraQuery2() {
		
		try {
			openConnection();

			st = conn.createStatement();
			
			String query = "SELECT p.nameId, COUNT(DISTINCT r.restaurname) AS NewYorkRestaurantsVisited " +
	                  "FROM person p " +
	                  "JOIN frequents f ON p.nameId = f.nameId " +
	                  "JOIN restaurant r ON f.restaurname = r.restaurname " +
	                  "WHERE r.city = 'New York' " +
	                  "GROUP BY p.nameId " +
	                  "HAVING NewYorkRestaurantsVisited = ( " +
	                  "    SELECT COUNT(*) " +
	                  "    FROM restaurant " +
	                  "    WHERE city = 'New York' " +
	                  ")";
     
				rs = st.executeQuery(query);

     // Process the result set
			
			while(rs.next()) {
				String name = rs.getString("nameId");
				int num = rs.getInt("NewYorkRestaurantsVisited");
                System.out.println(name + " has visited " + num + " restaurants of New York.");			
                }

		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}

	}
	
	
	public void insertTransaction() {
		try {
			openConnection();

			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				System.out.println("Something went wrong when disabling autocommit");
			}

			st = conn.createStatement();

			// Show EMPLOYEE relation before inserting
			System.out.println("================= EMPLOYEE RELATION BEFORE INSERTING =================\n");
			String beforeEmployees = "SELECT * FROM employee";
			rs = st.executeQuery(beforeEmployees);

			while (rs.next()) {
				String fname = rs.getString("Fname");
				String lname = rs.getString("Lname");
				String ssn = rs.getString("Ssn");
				String sex = rs.getString("Sex");
				Double salary = rs.getDouble("Salary");
				String supSsn = rs.getString("Super_ssn");
				int dno = rs.getInt("Dno");

				System.out.println("Employee name: " + fname + "\t last name: " + lname + "\t ssn: " + ssn + "\t sex: " + sex + "\t salary: " + salary + "\t super ssn: " + supSsn + "\t dno: " + dno + "\n");
			}

			// Show DEPENDENT relation before inserting
			System.out.println("================= DEPENDENT RELATION BEFORE INSERTING =================\n");
			String beforeDependents = "SELECT * FROM dependent";
			rs = st.executeQuery(beforeDependents);

			while (rs.next()) {
				String essn = rs.getString("Essn");
				String dependentName = rs.getString("Dependent_name");
				String sex = rs.getString("Sex");
				String bdate = rs.getString("Bdate");
				String relationship = rs.getString("Relationship");

				System.out.println("Essn: " + essn + "\t dependent name: " + dependentName + "\t sex: " + sex + "\t birth date: " + bdate + "\t relationship: " + relationship + "\n");
			}

			try {
				// Adding a dependent to an employee that does not exist
				savepoint1 = conn.setSavepoint("savepoint1");
				String update1 = "INSERT INTO dependent (Essn, Dependent_name, Sex, Bdate, Relationship) VALUES ('999999999', 'John', 'M', '2010-05-15', 'Son')";
				st.executeUpdate(update1);

			} catch (SQLException ex) {
				if (ex.getMessage().contains("Essn")) {
					System.out.println("The employee doesn't exist in EMPLOYEE relation, so foreign key constraint is violated!!! Let's rollback.");

					try {
						conn.rollback(savepoint1);
						System.out.println("Rolled back!\n");
					} catch (SQLException e) {
						System.out.println("Couldn't rollback to savepoint1!!");
						e.printStackTrace();
					}
				}
			}

			System.out.println("Let's try again inserting the employee first into the EMPLOYEE relation");
			System.out.println("Inserting into EMPLOYEE relation...");
			String update2 = "INSERT INTO employee (Fname, Lname, Ssn, Sex, Salary, Super_ssn, Dno) VALUES ('Jack', 'Doe', '999999999', 'M', 30000, NULL, 1)";
			st.executeUpdate(update2);
			System.out.println("Jack Doe inserted into EMPLOYEE relation!!!\n\n");
			conn.commit();

			System.out.println("Trying again to insert the dependent in relation...");
			String update3 = "INSERT INTO dependent (Essn, Dependent_name, Sex, Bdate, Relationship) VALUES ('999999999', 'Jack Jr.', 'M', '2010-05-15', 'Son')";
			st.executeUpdate(update3);

			conn.commit();
			System.out.println("Jack Doe's dependent inserted into DEPENDENT relation!!!\n\n");

			// Show EMPLOYEE relation after inserting
			System.out.println("================= EMPLOYEE RELATION AFTER INSERTING =================\n");
			String afterEmployees = "SELECT * FROM employee";
			rs = st.executeQuery(afterEmployees);

			while (rs.next()) {
				String fname = rs.getString("Fname");
				String lname = rs.getString("Lname");
				String ssn = rs.getString("Ssn");
				String sex = rs.getString("Sex");
				Double salary = rs.getDouble("Salary");
				String supSsn = rs.getString("Super_ssn");
				int dno = rs.getInt("Dno");

				System.out.println("Employee name: " + fname + "\t last name: " + lname + "\t ssn: " + ssn + "\t sex: " + sex + "\t salary: " + salary + "\t super ssn: " + supSsn + "\t dno: " + dno + "\n");
			}

			// Show DEPENDENT relation after inserting
			System.out.println("================= DEPENDENT RELATION AFTER INSERTING =================\n");
			String afterDependents = "SELECT * FROM dependent";
			rs = st.executeQuery(afterDependents);

			while (rs.next()) {
				String essn = rs.getString("Essn");
				String dependentName = rs.getString("Dependent_name");
				String sex = rs.getString("Sex");
				String bdate = rs.getString("Bdate");
				String relationship = rs.getString("Relationship");

				System.out.println("Essn: " + essn + "\t dependent name: " + dependentName + "\t sex: " + sex + "\t birth date: " + bdate + "\t relationship: " + relationship + "\n");
			}

			resetInsertTransData();

		} catch (SQLException ex) {
			System.out.println("Something went wrong!\n" + ex.getMessage());
		} finally {
			closeConnection();
		}
	}

	private void resetInsertTransData() {
		try {
			openConnection();

			st = conn.createStatement();

			String deleteDependent = "DELETE FROM dependent WHERE Essn = '999999999' AND Dependent_name = 'Jack Jr.'";
			st.executeUpdate(deleteDependent);

			String deleteEmployee = "DELETE FROM employee WHERE Ssn = '999999999'";
			st.executeUpdate(deleteEmployee);

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			closeConnection();
		}
	}
	
	public void updateTransaction() {
	    // Try to update NumNights for a specific trip
	    try {
	        openConnection();

	        try {
	            conn.setAutoCommit(false);
	        } catch (SQLException e) {
	            System.out.println("Something went wrong when disabling autocommit");
	        }

	        st = conn.createStatement();

	        // Show before
	        System.out.println("================= NumNights FOR CUSTOMER ID: 10000025 BEFORE UPDATE ================= \n");
	        String beforeUpdate = "SELECT NumNights FROM hotel_trip_customer WHERE customerid = 10000025 AND TripTo = 'Hong Kong' AND DepartureDate = '2018-01-05' AND hotelid = 'h07'";

	        rs = st.executeQuery(beforeUpdate);

	        while (rs.next()) {
	            int NumNights = rs.getInt("NumNights");
	            System.out.println("NumNights: " + NumNights);
	        }

	        // Attempt to update NumNights
	        System.out.println("\n\nLet's try changing NumNights to 10 for customer ID 10000025...");
	        String update1 = "UPDATE hotel_trip_customer SET NumNights = 10 WHERE customerid = 10000025 AND TripTo = 'Hong Kong' AND DepartureDate = '2018-01-05' AND hotelid = 'h07' AND NumNights = 5";
	        st.executeUpdate(update1);
	        System.out.println("Successfully updated NumNights to 10!!\n\n");

	        savepoint1 = conn.setSavepoint("savepoint1");

	        try {
	            System.out.println("Let's try changing date of a trip for customer ID 10000025...");
	            String update2 = "UPDATE hotel_trip_customer SET DepartureDate = '2018-01-05' WHERE customerid = 10000025 AND TripTo = 'Hong Kong' AND DepartureDate = '2019-11-25' AND hotelid = 'h07'";
	            st.executeUpdate(update2);
	        } catch (SQLException ex) {
	            if (ex.getMessage().contains("Duplicate")) {
	                System.out.println("The customer with ID: 10000025 already has a trip to Hong Kong for that date and hotel, let's rollback to savepoint.");

	                try {
	                    conn.rollback(savepoint1);
	                    System.out.println("Rolled back!\n");
	                } catch (SQLException e) {
	                    System.out.println("Couldn't rollback to savepoint1!!");
	                    e.printStackTrace();
	                }
	            }
	        }

	        conn.commit();

	        // Show after
	        System.out.println("================= NumNights FOR CUSTOMER ID: 10000025 AFTER UPDATE ================= \n");
	        String afterUpdate = "SELECT NumNights FROM hotel_trip_customer WHERE customerid = 10000025 AND TripTo = 'Hong Kong' AND DepartureDate = '2018-01-05' AND hotelid = 'h07'";

	        rs = st.executeQuery(afterUpdate);

	        while (rs.next()) {
	            int NumNights = rs.getInt("NumNights");
	            System.out.println("NumNights: " + NumNights);
	        }

	        resetUpdateTransData();

	    } catch (SQLException ex) {
	        System.out.println("Something went wrong!");
	        ex.printStackTrace();

	    } finally {
	        closeConnection();
	    }
	}

	private void resetUpdateTransData() {
	    // Just for it to do the same every execution
	    try {
	        openConnection();

	        st = conn.createStatement();

	        String restoreNumNights = "UPDATE hotel_trip_customer SET NumNights = 5 WHERE customerid = 10000025 AND TripTo = 'Hong Kong' AND DepartureDate = '2018-01-05' AND hotelid = 'h12' AND NumNights = 10";
	        st.executeUpdate(restoreNumNights);

	    } catch (SQLException e) {
	        e.printStackTrace();

	    } finally {
	        closeConnection();
	    }
	}
}